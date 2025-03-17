package com.example.employee.dto;

import com.example.employee.models.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeByDepartmentDTO {
    private String departmentName;

    private List<Employee> employees;

}
