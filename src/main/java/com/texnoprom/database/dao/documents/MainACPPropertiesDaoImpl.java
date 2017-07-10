package com.texnoprom.database.dao.documents;

import com.texnoprom.database.entities.MainACPProperties;
import com.texnoprom.services.HibernateSessionService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MainACPPropertiesDaoImpl implements MainACPPropertiesDao {
  private final HibernateSessionService hibernateSessionService;

  @Autowired
  public MainACPPropertiesDaoImpl(HibernateSessionService hibernateSessionService) {
    this.hibernateSessionService = hibernateSessionService;
  }

  @Override
  public MainACPProperties save(MainACPProperties properties) {
    try (final Session session = hibernateSessionService.getSessionFactory().openSession()) {
      final Transaction transaction = session.beginTransaction();
      properties.setId((Long) session.save(properties));
      transaction.commit();
      return properties;
    }
  }

  @Override
  public MainACPProperties findById(long id) {
    try(final Session session = hibernateSessionService.getSessionFactory().openSession()) {
      final Transaction transaction = session.beginTransaction();
      final Query query = session.createQuery("from MainACPProperties props where props.id = :id");
      query.setParameter("id", id);
      final MainACPProperties properties = (MainACPProperties) query.uniqueResult();
      transaction.commit();
      return properties;
    }
  }
}
