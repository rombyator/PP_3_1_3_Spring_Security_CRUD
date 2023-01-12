package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    @Transactional
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    public User getById(long id) {
        return userRepo.getById(id);
    }

    @Override
    @Transactional
    public void update(User user) {
        var dbUser = loadUserByUsername(user.getUsername());
        var rawPassword = user.getPassword();
        var encodedPassword = dbUser.getPassword();

        if (rawPassword != null && !passwordEncoder.matches(rawPassword, encodedPassword)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepo.save(user);
    }

    @Override
    @Transactional
    public void delete(long id) {
        userRepo.deleteById(id);
    }

    @Override
    public boolean isUserWithRoleExists(Role role) {
        return userRepo.existsWithRole(role.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByName(username)
                       .orElseThrow(
                           () -> new UsernameNotFoundException(
                               String.format("No user with username: %s", username)
                           )
                       );
    }
}
