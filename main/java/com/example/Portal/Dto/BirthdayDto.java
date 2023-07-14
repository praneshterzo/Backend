package com.example.Portal.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirthdayDto {
    private String name;
    private String designation;
    private Date Dateofbirth;
}
