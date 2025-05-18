package com.employeemanagementsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employeemanagementsystem.entity.Employee;
import com.employeemanagementsystem.repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public List<Employee> getEmployees(){
		return employeeRepository.findAll();
	}
	
	public Optional<Employee> getEmployeeById(Long id) {
		return employeeRepository.findById(id);
	}
	
	public Employee createEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}
	
	public Employee updateEmployee(Long id,Employee employeeDetails) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee Not Found!"));
		employee.setName(employeeDetails.getName());
		employee.setPosition(employeeDetails.getPosition());
		employee.setSalary(employeeDetails.getSalary());
		return employee;
	}
	
	public void deleteEmployee(Long id){
		employeeRepository.deleteById(id);
	}
	
}
