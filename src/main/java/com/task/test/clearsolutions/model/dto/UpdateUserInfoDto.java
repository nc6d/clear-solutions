package com.task.test.clearsolutions.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class UpdateUserInfoDto {
    @Nullable
    private Long id;

    @Nullable
    @Pattern(regexp = "^([\\w\\s]){2,50}$", message = "Input valid name(2..50 chars including whitespaces)")
    private String firstName;

    @Nullable
    @Pattern(regexp = "^([\\w\\s]){2,50}$", message = "Input valid name(2..50 chars including whitespaces)")
    private String lastName;

    @Nullable
    @Pattern(regexp = "^\\w{1,70}@\\w{1,20}\\.\\w{1,10}$",
            message = "Input email 2..100 chars, should include exactly one @ and one dot")
    private String email;

    @Nullable
    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty("birthDate")
    private LocalDate birthDate;

    @Nullable
    private String address;

    @Nullable
    @Pattern(regexp = "\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})")
    private String phoneNumber;
}
