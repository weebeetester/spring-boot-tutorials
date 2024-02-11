package com.websystique.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreationDto {

    @NotEmpty
    @Size(min = 2, message = "name must have at least 2 characters")
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 2, message = "major must have at least 2 characters")
    private String major;

}
