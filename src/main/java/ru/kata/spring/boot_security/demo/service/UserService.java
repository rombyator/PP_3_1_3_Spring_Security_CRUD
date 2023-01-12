package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public List<User> getAll();

    public void add(User user);

    public User getById(long id);

    public void update(User user);

    public void delete(long id);

    public boolean isUserWithRoleExists(Role role);
}
