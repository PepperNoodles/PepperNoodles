package com.peppernoodles.auth.api.dto;

import com.peppernoodles.user.domain.UserProfile.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * 會員註冊.
 *
 * <p>The legacy form validated only in JavaScript on the client, so the server
 * accepted anything a direct POST sent it.
 */
public record RegisterRequest(
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank
                @Size(min = 8, max = 100, message = "密碼長度需介於 8 到 100 個字元")
                @Pattern(
                        regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                        message = "密碼需同時包含英文字母與數字")
                String password,
        @NotBlank @Size(max = 50) String realName,
        @Size(max = 50) String nickname,
        @Pattern(regexp = "^09\\d{8}$", message = "請輸入有效的台灣手機號碼") String phone,
        @Past(message = "生日必須是過去的日期") LocalDate birthDate,
        Gender gender,
        @Size(max = 200) String location,
        /** Interest tags picked during registration. */
        List<Long> foodTagIds,
        /** reCAPTCHA response; required when reCAPTCHA is enabled. */
        String recaptchaToken) {}
