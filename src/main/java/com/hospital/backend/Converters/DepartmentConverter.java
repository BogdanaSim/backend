package com.hospital.backend.Converters;


import com.hospital.backend.DTOs.DayDTO;
import com.hospital.backend.DTOs.DepartmentDTO;
import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.Department;
import com.hospital.backend.Models.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentConverter implements IConverter<Department, DepartmentDTO> {
    @Override
    public Department convertDtoToModel(DepartmentDTO departmentDTO) {
        Department department = new Department();
        if(departmentDTO.getId()!=null)
            department.setId(departmentDTO.getId());
        department.setName(departmentDTO.getName());
        return department;
    }

    @Override
    public DepartmentDTO convertModelToDto(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        if(department.getId() == null)
            return departmentDTO;
        departmentDTO.setId(department.getId());
        departmentDTO.setName(department.getName());

        return departmentDTO;
    }

    @Override
    public List<DepartmentDTO> convertModelListToDtoList(List<Department> departments) {
        return departments
                .stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toList());
    }
}
