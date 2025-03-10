package com.example.employee.repository;

import com.example.employee.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT n FROM Employee n WHERE LOWER(n.employee_name) = LOWER(:name)" )
    Optional<Employee> findByName(@Param("name")String name);

}