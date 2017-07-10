package com.texnoprom.database.dao.documents;

import com.texnoprom.database.entities.MainACPProperties;

public interface MainACPPropertiesDao {
  MainACPProperties save(MainACPProperties properties);
  MainACPProperties findById(long id);
}
