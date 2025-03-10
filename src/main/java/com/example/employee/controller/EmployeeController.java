package com.example.employee.controller;

import com.example.employee.models.Department;
import com.example.employee.models.Employee;
import com.example.employee.service.EmployeeService;
import jakarta.persistence.NoResultException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/getAllEmployees")
    public List<Employee> getAllEmployees() {
        return employeeService.showAllEmployees();
    }

    @GetMapping("/getEmployee/{employee_id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long employee_id) {
        try {
            Employee employee = employeeService.showEmployeeById(employee_id);
            return ResponseEntity.ok(employee);
        } catch (NoResultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");

        }
    }

    @GetMapping("/getEmployeeName/{employeeName}")
    public ResponseEntity<?> getEmployeeByName(@PathVariable String employeeName) {
        try {
            Employee employee = employeeService.showEmployeeByName(employeeName);
            return ResponseEntity.ok(employee);
        } catch (NoResultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }

    @DeleteMapping("/deleteEmployee/{employee_id}")
    public ResponseEntity<?> removeEmployee(@PathVariable Long employee_id) {
        try {
            employeeService.deleteEmployee(employee_id);
            return ResponseEntity.ok("Employee deleted.");
        } catch (NoResultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This employee does not exist.");
        }
    }

    @PostMapping("/addEmployee")
    public ResponseEntity<String> createEmployee(@Valid @RequestBody Employee employee) {
        Employee savedEmployee = employeeService.addEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Employee created: " + savedEmployee.getEmployee_name());
    }

    @GetMapping("/departmentAndEmployee/{departmentId}/{employee_id}/")
    public ResponseEntity<?> getDepartmentAndEmployee(@PathVariable Long departmentId, Long employee_id) {
        try {
            // Llamar al servicio para obtener la información del empleado y departamento
            String result = employeeService.showDepartmentEmployee(departmentId,employee_id);

            // Devolver la respuesta con código 200 OK y el resultado
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            // En caso de error (por ejemplo, si no se encuentra el empleado), devolver 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }
    @PutMapping("/asignDepartment")
    public ResponseEntity<?> changeDepartment (@RequestBody Long employee_id, Department departmentId){
        Employee employeeUpdated= employeeService.updateDepartment(employee_id, departmentId);
        return ResponseEntity.ok((
                "Department updated successfully for " + employeeUpdated.getEmployee_name() +
                        ". New department: " + employeeUpdated.getDepartment().getDepartmentName()
        ));
    }
}
