package com.texnoprom.database.dao;

import com.texnoprom.database.entities.User;
import java.util.ArrayList;

public interface UserDao {
  User save(User user);
  User findByLogin(String login);
  User findByUserId(Integer userId);
  ArrayList<User> findAll();
  void update(User user);
  void delete(User user);
}
