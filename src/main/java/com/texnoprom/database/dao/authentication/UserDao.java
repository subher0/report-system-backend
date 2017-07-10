package com.texnoprom.database.dao.authentication;

import com.texnoprom.database.entities.User;
import com.texnoprom.database.exceptions.DuplicateException;
import java.util.ArrayList;

public interface UserDao {
  User save(User user) throws DuplicateException;
  User findByLogin(String login);
  User findByUserId(Integer userId);
  ArrayList<User> findAll();
  void update(User user) throws DuplicateException;
  void delete(User user);
}
