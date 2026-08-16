package com.peppernoodles.common.storage;

import com.peppernoodles.common.config.ApplicationProperties;
import com.peppernoodles.common.error.ApiExceptions.UpstreamException;
import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

/**
 * Uploads to and deletes from Supabase Storage.
 *
 * <p>Every image the legacy schema kept as a {@code java.sql.Blob} lives here
 * instead; tables store only the returned object path. That change removes the
 * legacy pattern of streaming megabytes of image bytes out of Postgres through
 * a controller on every page render.
 *
 * <p>Uploads go through the backend rather than straight from the browser so
 * that ownership, size, and content type are checked before anything is written,
 * and so the service key never reaches a client.
 */
@Service
public class StorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of("image/jpeg", "image/png", "image/webp", "image/avif");
    private static final long MAX_BYTES = 15L * 1024 * 1024;

    private final ApplicationProperties properties;
    private final RestClient restClient;

    public StorageService(ApplicationProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.create();
    }

    /**
     * Stores {@code file} in {@code bucket} under a freshly generated key.
     *
     * @return the object path to persist on the owning row
     */
    public String upload(StorageBucket bucket, MultipartFile file) {
        validate(file);
        String objectPath = generateKey(file.getOriginalFilename());

        requireConfigured();
        try {
            restClient
                    .post()
                    .uri("%s/storage/v1/object/%s/%s".formatted(baseUrl(), bucket.bucketName(), objectPath))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.storage().serviceKey())
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .body(file.getBytes())
                    .retrieve()
                    .toBodilessEntity();
        } catch (IOException e) {
            throw new UpstreamException("Could not read the uploaded file.", e);
        } catch (Exception e) {
            log.error("Upload to bucket {} failed", bucket.bucketName(), e);
            throw new UpstreamException("Could not store the uploaded image.", e);
        }

        log.debug("Uploaded {} to {}", objectPath, bucket.bucketName());
        return objectPath;
    }

    /** Deletes an object, ignoring the case where it is already gone. */
    public void delete(StorageBucket bucket, String objectPath) {
        if (!StringUtils.hasText(objectPath) || !properties.storage().isConfigured()) {
            return;
        }
        try {
            restClient
                    .delete()
                    .uri("%s/storage/v1/object/%s/%s".formatted(baseUrl(), bucket.bucketName(), objectPath))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.storage().serviceKey())
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            // A failed cleanup must not fail the user's request; the object is orphaned at worst.
            log.warn("Could not delete {} from {}", objectPath, bucket.bucketName(), e);
        }
    }

    /** The browser-reachable URL for a stored object, or null when there is none. */
    public String publicUrl(StorageBucket bucket, String objectPath) {
        if (!StringUtils.hasText(objectPath)) {
            return null;
        }
        return "%s/storage/v1/object/public/%s/%s".formatted(baseUrl(), bucket.bucketName(), objectPath);
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("請選擇要上傳的圖片。");
        }
        if (file.getSize() > MAX_BYTES) {
            throw new ValidationException("圖片大小不可超過 15MB。");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new ValidationException("僅支援 JPEG、PNG、WebP 或 AVIF 格式的圖片。");
        }
    }

    private void requireConfigured() {
        if (!properties.storage().isConfigured()) {
            throw new UpstreamException(
                    "Image storage is not configured. Set SUPABASE_SERVICE_KEY in .env "
                            + "(see `supabase status` for the value).",
                    null);
        }
    }

    /**
     * A random key with the original extension preserved. The client's filename
     * is never used as a path: it is attacker-controlled and could contain
     * traversal sequences or collide with another user's object.
     */
    private static String generateKey(String originalFilename) {
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String suffix = StringUtils.hasText(extension) ? "." + extension.toLowerCase(Locale.ROOT) : "";
        return UUID.randomUUID() + suffix;
    }

    private String baseUrl() {
        String url = properties.storage().url();
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
