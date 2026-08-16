package com.peppernoodles.support;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Base class for tests that need a real database.
 *
 * <p>The container is started once for the whole suite and the schema comes from
 * the same {@code supabase/migrations} files the application runs against — so a
 * migration that would fail {@code ddl-auto: validate} fails the build here
 * rather than at boot.
 *
 * <p>Two migrations are skipped because they are Supabase-specific and have no
 * meaning in a plain Postgres container: the Storage bucket definitions (which
 * need the {@code storage} schema) and the RLS grants (which reference the
 * {@code anon} and {@code authenticated} roles).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Tag("integration")
public abstract class IntegrationTest {

    private static final Set<String> SUPABASE_ONLY_MIGRATIONS =
            Set.of("20260816120700_storage_buckets.sql", "20260816120800_row_level_security.sql");

    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
                    DockerImageName.parse("postgis/postgis:17-3.5")
                            .asCompatibleSubstituteFor("postgres"))
            .withDatabaseName("peppernoodles_test")
            .withUsername("test")
            .withPassword("test");

    static {
        POSTGRES.start();
        applyMigrations();
    }

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    private static void applyMigrations() {
        var dataSource = new org.springframework.jdbc.datasource.DriverManagerDataSource(
                POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);

        // The postgis image pre-installs PostGIS into `public`, so the migration's
        // `create extension … with schema extensions` would be a silent no-op and
        // `extensions.geography` would not resolve. Supabase puts it in
        // `extensions`; move it there so the container matches production.
        jdbc.execute("drop extension if exists postgis cascade");
        jdbc.execute("create schema if not exists extensions");

        for (Path migration : migrationFiles()) {
            try {
                jdbc.execute(Files.readString(migration, StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new IllegalStateException("Could not read migration " + migration, e);
            } catch (RuntimeException e) {
                throw new IllegalStateException("Migration failed: " + migration.getFileName(), e);
            }
        }
    }

    private static List<Path> migrationFiles() {
        Path dir = Path.of("..", "supabase", "migrations");
        try (Stream<Path> files = Files.list(dir)) {
            return files.filter(p -> p.toString().endsWith(".sql"))
                    .filter(p -> !SUPABASE_ONLY_MIGRATIONS.contains(p.getFileName().toString()))
                    .sorted(Comparator.comparing(p -> p.getFileName().toString()))
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Could not list " + dir.toAbsolutePath(), e);
        }
    }
}
