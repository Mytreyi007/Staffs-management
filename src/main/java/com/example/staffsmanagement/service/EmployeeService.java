package com.example.staffsmanagement.service;

import com.example.staffsmanagement.dto.EmployeeDTO;
import com.example.staffsmanagement.entity.Employee;
import com.example.staffsmanagement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    public List<EmployeeDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EmployeeDTO getById(Long id) {
        Employee emp = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return toDTO(emp);
    }

    public EmployeeDTO create(EmployeeDTO dto) {
        return toDTO(repo.save(toEntity(dto)));
    }

    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        Employee emp = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        emp.setName(dto.getName());
        emp.setEmail(dto.getEmail());
        emp.setDepartment(dto.getDepartment());
        return toDTO(repo.save(emp));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        repo.deleteById(id);
    }

    // --- Mappers ---
    private EmployeeDTO toDTO(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setEmail(e.getEmail());
        dto.setDepartment(e.getDepartment());
        return dto;
    }

    private Employee toEntity(EmployeeDTO dto) {
        return new Employee(dto.getName(), dto.getEmail(), dto.getDepartment());
    }
}
