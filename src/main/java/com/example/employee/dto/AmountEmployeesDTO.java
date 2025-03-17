package com.example.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class AmountEmployeesDTO {
    private String departmentName;
    private Long amountEmployees;
}
