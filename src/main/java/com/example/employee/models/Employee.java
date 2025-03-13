package com.example.employee.models;

import com.example.employee.common.Department;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employee_id;

    @NotBlank(message = "Name can not be empty.")
    private String employee_name;

    @Email
    private String email;

    @NotNull(message = "Salary can not be null.")
    private Double salary;


    public Employee(String employeeName, String email, Double salary) {
    }

    @Column(nullable = false)
    private Long departmentId;
}