package com.texnoprom.services;

import com.texnoprom.database.dao.authentication.UserDaoImpl;
import com.texnoprom.database.entities.User;
import com.texnoprom.database.exceptions.DuplicateException;
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

  public User addUser(User user) throws DuplicateException {
      return userDao.save(user);
  }

  @Transactional
  public User getUser(String login) {
    try {
      return userDao.findByLogin(login);
    } catch (HibernateException ex) {
      return null;
    }
  }

  public User getUser(int userId) {
    try {
      return userDao.findByUserId(userId);
    } catch (HibernateException ex) {
      return null;
    }
  }

  public Iterable<User> getAllUsers() {
    try {
      return userDao.findAll();
    } catch (HibernateException ex) {
      return null;
    }
  }

  public void updateUser(User user) throws DuplicateException {
    try {
      userDao.update(user);
    } catch (HibernateException ignore) {
    }
  }

  public void deleteUser(User user) {
    try {
      userDao.delete(user);
    } catch (HibernateException ignore) {
    }
  }
}
