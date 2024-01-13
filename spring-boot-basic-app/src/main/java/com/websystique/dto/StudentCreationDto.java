package com.websystique.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreationDto {
    private String name;
    private String major;
}
