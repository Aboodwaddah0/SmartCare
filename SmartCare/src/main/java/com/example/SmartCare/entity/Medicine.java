package com.example.SmartCare.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {
    private String name;
    private String dose;
    private String frequency;
}
