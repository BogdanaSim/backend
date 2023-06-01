package com.hospital.backend.Services;

import com.hospital.backend.Exceptions.*;
import com.hospital.backend.Models.Department;
import com.hospital.backend.Models.RoleStaff;
import com.hospital.backend.Models.User;
import com.hospital.backend.Repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersService implements IUsersService{

    private final UsersRepository usersRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User save(User user) {
        logger.info("save: " + user.getEmail());
        usersRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistingException();
        });
        usersRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistingException();
        });
        return usersRepository.save(user);
    }

    @Override
    public void deleteByEmail(String email) {
        logger.info("deleteByEmail: " + email);
        usersRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        usersRepository.deleteByEmail(email);
    }

    @Override
    public User findById(Long id) {
        logger.info("findById: " + id);
        return usersRepository.findById(id).orElseThrow(UserNotFoundException::new);

    }

    @Override
    public User update(User user) {
        logger.info("update: " + user.getId());
        usersRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        return usersRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("deleteById: " + id);
        usersRepository.findById(id).orElseThrow(UserNotFoundException::new);
        usersRepository.deleteById(id);
    }

    public List<User> getAllUsers(){
        return usersRepository.findAll();
    }

    public List<User> getAllUsersWithoutShifts(){
        List<User> allUsers = this.usersRepository.findAll();
        List<User> users = new ArrayList<>();
        for(User user:allUsers){
            user.setShifts(new ArrayList<>());
            users.add(user);
        }
        return users;
    }

    public List<User> getUsersWithRoleWithoutShifts(String role){
        RoleStaff roleStaff = RoleStaff.valueOf(role);
        List<User> allUsers = this.usersRepository.findByRoleStaff(roleStaff);
        List<User> users = new ArrayList<>();
        for(User user:allUsers){
            user.setShifts(new ArrayList<>());
            users.add(user);
        }
        return users;
    }

    public List<User> getUserByRoleAndDepartment(String role, Long idDepartment){
        RoleStaff roleStaff = RoleStaff.valueOf(role);
        Department department = new Department();
        department.setId(idDepartment);
        return usersRepository.findByRoleStaffAndDepartment(roleStaff,department);
    }
}
