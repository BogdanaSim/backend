package com.hospital.backend.Services;

import com.hospital.backend.Models.Department;

public interface IDepartmentsService {
    Department save(Department department);

    void deleteById(Long id);


    Department findById(Long id);

    Department update(Department department);
}
