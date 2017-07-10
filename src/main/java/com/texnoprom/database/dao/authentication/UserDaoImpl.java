package com.texnoprom.database.dao.authentication;

import com.texnoprom.database.entities.User;
import com.texnoprom.database.exceptions.DuplicateException;
import com.texnoprom.database.exceptions.ErrorMessages;
import com.texnoprom.services.HibernateSessionService;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//FIXME: Crash when adding same user
@SuppressWarnings("unchecked")
@Repository
public class UserDaoImpl implements UserDao {
  private final HibernateSessionService hibernateSessionService;

  @Autowired
  public UserDaoImpl(HibernateSessionService hibernateSessionService) {
    this.hibernateSessionService = hibernateSessionService;
  }

  @Override
  public User save(User user) throws DuplicateException {
    try (final Session session = hibernateSessionService.getSessionFactory().openSession()) {
      final Transaction transaction = session.beginTransaction();
      user.setUserId((Integer) session.save(user));
      transaction.commit();
      return user;
    } catch (ConstraintViolationException ex) {
      throw new DuplicateException(ErrorMessages.DUBLICATE_ERROR, ex);
    }
  }

  @Override
  public User findByLogin(String login) {
    try (final Session session = hibernateSessionService.getSessionFactory().openSession()) {
      final Transaction transaction = session.beginTransaction();
      final Query query = session.createQuery("from User user where user.name = :name");
      query.setParameter("name", login);
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
  public void update(User user) throws DuplicateException {
    try (final Session session = hibernateSessionService.getSessionFactory().openSession()) {
      final Transaction transaction = session.beginTransaction();
      session.update(user);
      transaction.commit();
    } catch (ConstraintViolationException ex) {
      throw new DuplicateException(ErrorMessages.DUBLICATE_ERROR, ex);
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
