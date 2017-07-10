package com.texnoprom.services;

import com.texnoprom.database.dao.documents.MainACPPropertiesDao;
import com.texnoprom.database.entities.MainACPProperties;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ACPService {
  private final MainACPPropertiesDao mainACPPropertiesDao;

  @Autowired
  public ACPService(MainACPPropertiesDao mainACPPropertiesDao) {
    this.mainACPPropertiesDao = mainACPPropertiesDao;
  }

  public MainACPProperties getACPPropertiesById(long id) {
    try {
      return mainACPPropertiesDao.findById(id);
    } catch (HibernateException ex) {
      return null;
    }
  }
}
