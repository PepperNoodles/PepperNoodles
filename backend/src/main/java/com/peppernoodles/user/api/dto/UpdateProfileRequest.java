package com.peppernoodles.user.api.dto;

import com.peppernoodles.user.domain.UserProfile.Gender;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record UpdateProfileRequest(
        @Size(max = 50) String realName,
        @Size(max = 50) String nickname,
        @Pattern(regexp = "^09\\d{8}$", message = "請輸入有效的台灣手機號碼") String phone,
        @Past(message = "生日必須是過去的日期") LocalDate birthDate,
        Gender gender,
        @Size(max = 200) String location,
        List<Long> foodTagIds) {}
