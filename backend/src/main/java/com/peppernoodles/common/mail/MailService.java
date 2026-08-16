package com.peppernoodles.common.mail;

import com.peppernoodles.common.config.ApplicationProperties;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Transactional e-mail.
 *
 * <p>Replaces the legacy {@code SendEmail} and {@code RearSendEmail} classes,
 * which each opened their own {@code jakarta.mail} session against a Gmail
 * account whose app password was hardcoded in the source.
 *
 * <p>Sends are asynchronous and failures are logged rather than thrown: a user
 * must not see registration fail because the mail relay is briefly unavailable.
 */
@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;
    private final ApplicationProperties properties;

    public MailService(JavaMailSender mailSender, ApplicationProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    @Async
    public void sendEmailVerification(String to, String rawToken) {
        String link = "%s/auth/verify?token=%s".formatted(frontendBaseUrl(), encode(rawToken));
        send(
                to,
                "【胡椒MAP】請驗證您的電子信箱",
                """
                您好，

                感謝您註冊胡椒MAP！請點擊以下連結完成信箱驗證：

                %s

                此連結將於 %d 小時後失效。若您並未註冊本服務，請忽略這封信。

                — 胡椒MAP 團隊
                """
                        .formatted(link, properties.mail().verificationTtl().toHours()));
    }

    @Async
    public void sendPasswordReset(String to, String rawToken) {
        String link = "%s/auth/reset-password?token=%s".formatted(frontendBaseUrl(), encode(rawToken));
        send(
                to,
                "【胡椒MAP】重設密碼",
                """
                您好，

                我們收到重設密碼的請求。請點擊以下連結設定新密碼：

                %s

                此連結將於 %d 分鐘後失效，且僅能使用一次。
                若您並未提出此請求，請忽略這封信，您的密碼不會被變更。

                — 胡椒MAP 團隊
                """
                        .formatted(link, properties.mail().passwordResetTtl().toMinutes()));
    }

    @Async
    public void sendAccountSuspended(String to, String reason) {
        send(
                to,
                "【胡椒MAP】帳號已停權",
                """
                您好，

                您的帳號已被管理員停權，原因如下：

                %s

                若您認為這是誤判，請透過網站的「聯絡我們」與我們聯繫。

                — 胡椒MAP 團隊
                """
                        .formatted(reason == null || reason.isBlank() ? "（未提供原因）" : reason));
    }

    @Async
    public void sendAccountReinstated(String to) {
        send(
                to,
                "【胡椒MAP】帳號已恢復",
                """
                您好，

                您的帳號權限已恢復，現在可以正常登入使用。

                — 胡椒MAP 團隊
                """);
    }

    private void send(String to, String subject, String body) {
        var message = new SimpleMailMessage();
        message.setFrom(properties.mail().from());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try {
            mailSender.send(message);
            log.debug("Sent '{}' to {}", subject, to);
        } catch (MailException e) {
            // Never fail the caller's request because mail is down.
            log.error("Failed to send '{}' to {}", subject, to, e);
        }
    }

    private String frontendBaseUrl() {
        String base = properties.mail().frontendBaseUrl();
        return base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
