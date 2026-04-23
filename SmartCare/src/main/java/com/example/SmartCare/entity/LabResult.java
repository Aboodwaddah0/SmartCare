package com.example.SmartCare.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabResult {

    private String testName;
    private String result;
    private String normalRange;
}
