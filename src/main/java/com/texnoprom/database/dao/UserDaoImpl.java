package com.texnoprom.database.dao;

import com.texnoprom.database.entities.User;
import com.texnoprom.services.HibernateSessionService;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unchecked")
@Repository
public class UserDaoImpl implements UserDao {
  private final HibernateSessionService hibernateSessionService;

  @Autowired
  public UserDaoImpl(HibernateSessionService hibernateSessionService) {
    this.hibernateSessionService = hibernateSessionService;
  }

  @Override
  public User save(User user){
    try (final Session session = hibernateSessionService.getSessionFactory().openSession()) {
      final Transaction transaction = session.beginTransaction();
      user.setUserId((Integer) session.save(user));
      transaction.commit();
      return user;
    }
  }

  @Override
  public User findByLogin(String login) {
    try (final Session session = hibernateSessionService.getSessionFactory().openSession()) {
      final Transaction transaction = session.beginTransaction();
      final Query query = session.createQuery("from User user where user.login = :login");
      query.setParameter("login", login);
      final User user = (User) query.uniqueResult();
      transaction.commit();
      return user;
    }
  }

  @Override
  public User findByUserId(Integer userId) {
    try(final Session session = hibernateSessionService.getSessionFactory().openSession()) {
      final Transaction transaction = session.beginTransaction();
      final Query query = session.createQuery("from User user where user.userId = :userId");
      query.setParameter("userId", userId);
      final User user = (User) query.uniqueResult();
      transaction.commit();
      return user;
    }
  }

  @Override
  public ArrayList<User> findAll() {
    try (final Session session = hibernateSessionService.getSessionFactory().openSession()) {
      final Transaction transaction = session.beginTransaction();
      final ArrayList<User> users = (ArrayList<User>) session.createQuery("from User user").list();
      transaction.commit();
      return users;
    }
  }

  @Override
  public void update(User user) {
    try (final Session session = hibernateSessionService.getSessionFactory().openSession()) {
      final Transaction transaction = session.beginTransaction();
      session.update(user);
      transaction.commit();
    }
  }

  @Override
  public void delete(User user) {
    try (final Session session = hibernateSessionService.getSessionFactory().openSession()) {
      final Transaction transaction = session.beginTransaction();
      session.remove(user);
      transaction.commit();
      session.close();
    }
  }
}
