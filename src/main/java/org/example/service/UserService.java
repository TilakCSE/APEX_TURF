package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;
import java.sql.SQLException;

public class UserService {
    private final UserDao userDao = new UserDao();
    private final PasswordService passwordService = new PasswordService();

    public void register(User user) throws SQLException, IllegalArgumentException {
        if (userDao.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        // Hash the password before saving
        String hashedPassword = passwordService.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userDao.create(user);
    }

    public User login(String email, String password) throws SQLException {
        User user = userDao.findByEmail(email);
        if (user != null && passwordService.checkPassword(password, user.getPassword())) {
            return user; // Login successful
        }
        return null; // Login failed
    }
}