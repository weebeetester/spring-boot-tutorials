package com.websystique.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdationDto {
    private Long id;
    private String name;
    private String major;
}
