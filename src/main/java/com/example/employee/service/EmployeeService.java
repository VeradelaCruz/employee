package com.example.employee.service;

import com.example.employee.dto.EmployeeAndDepartmentDTO;
import com.example.employee.common.Department;
import com.example.employee.models.Employee;
import com.example.employee.repository.EmployeeRepository;
import jakarta.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RestTemplate apiConsumir;


    //Show all employees
    public List<Employee> showAllEmployees(){
        return employeeRepository.findAll();
    }

    //Show employee by id
    public Employee showEmployeeById(Long employee_id){
        return employeeRepository.findById(employee_id)
                .orElseThrow(()-> new NoResultException("Employee not founded."));

    }

    //Show employee by name:
    public Employee showEmployeeByName(String employee_name){
        return employeeRepository.findByName(employee_name)
                .orElseThrow(()-> new NoResultException("Employee not founded."));

    }

    //Delete employee by id
    public Employee deleteEmployee(Long employee_id){
        Employee employee = employeeRepository.findById(employee_id).
                orElseThrow(()->new NoResultException("Employee not founded"));
        employeeRepository.deleteById(employee_id);
        return employee;
    }


    //Microservicios:
    //Create Employee
    public Employee addEmployee(EmployeeAndDepartmentDTO employeeAndDepartmentDTO) {
        // Llamar al microservicio de Department
        Department department = apiConsumir.getForObject(
                "http://localhost:8081/department/getDepartmentBy/" + employeeAndDepartmentDTO.getDepartmentId(),
                Department.class);

        // Verificar si el departamento no existe
        if (department == null) {
            throw new RuntimeException("Department not found.");
        }

        // Crear el empleado
        Employee employee = new Employee();
        employee.setEmployee_name(employeeAndDepartmentDTO.getEmployee_name());
        employee.setEmail(employeeAndDepartmentDTO.getEmail());
        employee.setSalary(employeeAndDepartmentDTO.getSalary());
        employee.setDepartmentId(employeeAndDepartmentDTO.getDepartmentId()); // Guardar solo el ID

        return employeeRepository.save(employee);
    }


    //Show employee and department:
    public String showDepartmentEmployee(Long departmentId,Long employee_id){
            // Obtener la información del departamento
            Department department = apiConsumir.getForObject("http://localhost:8081/department/getDepartmentById/" + departmentId, Department.class);

            // Obtener el nombre del departamento
            String departmentName = department.getDepartmentName();

            // Obtener el empleado desde la base de datos
            Employee employee = employeeRepository.findById(employee_id)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            // Retornar el nombre del empleado y el nombre del departamento
            return employee.getEmployee_name() + "'s department is " + departmentName;
    }

    //Buscar departamento por id de empleado


}


