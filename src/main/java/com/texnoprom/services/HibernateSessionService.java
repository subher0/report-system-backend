package com.texnoprom.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

@Service
public class HibernateSessionService {

  private SessionFactory sessionFactory;

  public HibernateSessionService() {
    sessionFactory = new Configuration().configure("hibernate.cfg.xml")
        .setProperty("hibernate.connection.url",
            "jdbc:postgresql://" + System.getenv("TEXNOPROM_DB_HOST") + "/reportsdb")
        .setProperty("hibernate.connection.username", System.getenv("TEXNOPROM_DB_LOGIN"))
        .setProperty("hibernate.connection.password", System.getenv("TEXNOPROM_DB_PASSWORD"))
        .buildSessionFactory();
  }

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}
