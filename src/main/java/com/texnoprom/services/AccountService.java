package com.texnoprom.services;

import com.texnoprom.database.dao.UserDaoImpl;
import com.texnoprom.database.entities.User;
import javax.transaction.Transactional;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//TODO: use adequate logger
@Service
public class AccountService {
  final UserDaoImpl userDao;

  @Autowired
  public AccountService(UserDaoImpl userDao) {
    this.userDao = userDao;
  }

  public User addUser(User user) {
      return userDao.save(user);
  }

  @Transactional
  public User getUser(String login) {
    try {
      return userDao.findByLogin(login);
    } catch (HibernateException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public User getUser(int userId) {
    try {
      return userDao.findByUserId(userId);
    } catch (HibernateException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public Iterable<User> getAllUsers() {
    try {
      return userDao.findAll();
    } catch (HibernateException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public void updateUser(User user) {
    try {
      userDao.update(user);
    } catch (HibernateException ex) {
      ex.printStackTrace();
    }
  }

  public void deleteUser(User user) {
    try {
      userDao.delete(user);
    } catch (HibernateException ex) {
      ex.printStackTrace();
    }
  }
}
