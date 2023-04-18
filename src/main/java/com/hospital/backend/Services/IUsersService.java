package com.hospital.backend.Services;

import com.hospital.backend.Models.User;

public interface IUsersService {
    User findByEmail(String email);
    User save(User users);
    void deleteByEmail(String email);
    User findById(Long id);
    User update(User users);
    void deleteById(Long id);
}
