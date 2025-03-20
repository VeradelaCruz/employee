package com.example.employee.service;

import com.example.employee.dto.AmountEmployeesDTO;
import com.example.employee.dto.EmployeeAndDepartmentDTO;
import com.example.employee.common.Department;
import com.example.employee.dto.EmployeeByDepartmentDTO;
import com.example.employee.models.Employee;
import com.example.employee.repository.EmployeeRepository;
import jakarta.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Optional;

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
            Department department = apiConsumir.getForObject(
                    "http://localhost:8081/department/getDepartmentBy/" + departmentId, Department.class);

            // Obtener el nombre del departamento
            if(department==null){
                throw new RuntimeException("Department not found.");
            }

            // Obtener el empleado desde la base de datos
            Employee employee = employeeRepository.findById(employee_id)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            // Retornar el nombre del empleado y el nombre del departamento
            return employee.getEmployee_name() + "'s department is " + department.getDepartmentName();
    }



    //Show employees by department:
    public EmployeeByDepartmentDTO showEmployeesByDepartment(Long departmentId) {
        // 1. Obtener el departamento desde el microservicio de department
        Department department = apiConsumir.getForObject(
                "http://localhost:8081/department/getDepartmentBy/" + departmentId, Department.class);

        if (department == null) {
            throw new RuntimeException("Department with ID " + departmentId + " not found.");
        }

        // 2. Obtener la lista de empleados asociados con el departamento
        List<Employee> employees = employeeRepository.findEmployeesByDepartmentId(departmentId);

        // 3. Devolver el DTO con el nombre del departamento y la lista de empleados
        return new EmployeeByDepartmentDTO(department.getDepartmentName(), employees);
    }


    //Show amount of employees on each department:

    public AmountEmployeesDTO amountOfEmployeesByDepartment(Long departmentId){
        //1.Obtener el departamento desde el microservicio de department:
        Department department= apiConsumir.getForObject(
                "http://localhost:8081/department/getDepartmentBy/" + departmentId, Department.class);

        if(department==null){
            throw new RuntimeException("Department with ID "+ departmentId + "not found.");
        }

        //2. Obtener la lista de la cantidad de empleados que tiene cada departamento:
        Long amountEmployee= employeeRepository.countEmployeesByDepartment(departmentId);

        //3. Devolver el DTO con el departmaneto y la cantidad de empleados:
        return new AmountEmployeesDTO(department.getDepartmentName(), amountEmployee);


    }

    //Change employee's department:
    public Employee changeDepartment(Long employee_id, Long departmentId){
        //1.Obtener el departamento desde el microservicio de department:
        Department department= apiConsumir.getForObject("http://localhost:8081/department/getDepartmentBy/" + departmentId, Department.class);

        if (department==null){
            throw new RuntimeException("Department with ID "+ departmentId + " not found.");
        }
        //2. Otener el empleado que se quiere actualizar:
        Employee employee= employeeRepository.findById(employee_id)
                .orElseThrow(()->new RuntimeException("Employee not found."));

        //3. Modificar su departamento:
        employee.setDepartmentId(departmentId);
        return employeeRepository.save(employee);
    }

}