package com.hospital.backend.Services;

import com.hospital.backend.Exceptions.DepartmentNotFoundException;
import com.hospital.backend.Exceptions.UserNotFoundException;
import com.hospital.backend.Models.Department;
import com.hospital.backend.Repositories.DepartmentsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepartmentsService implements IDepartmentsService{

    private final DepartmentsRepository departmentsRepository;
    private static final Logger logger = LoggerFactory.getLogger(DepartmentsService.class);

    @Override
    public Department save(Department department) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Department findById(Long id) {
        logger.info("findById: " + id);
        return departmentsRepository.findById(id).orElseThrow(DepartmentNotFoundException::new);

    }

    @Override
    public Department update(Department department) {
        return null;
    }
}
