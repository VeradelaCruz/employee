package com.example.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class EmployeeAndDepartmentDTO {

        private Long employee_id;

        @NotBlank(message = "Name can not be empty.")
        private String employee_name;

        @Email
        private String email;

        @NotNull(message = "Salary can not be null.")
        private Double salary;

        private Long departmentId;

}
