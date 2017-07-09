package com.texnoprom.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//TODO: assign data source and remove supressed warning
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("id")
  @Column(name = "user_id")
  private Integer userId;

  @Column(unique = true, nullable = false, name = "name")
  private String name;

  @JsonIgnore
  @Column(nullable = false, name = "password")
  private String password;

  public User() {
  }

  public User(String name, String email) {
    this.name = name;
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    this.password = encoder.encode(password);
  }

  public Integer getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public boolean matchPassword(String plainPassword) {
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(plainPassword, password);
  }

  public void setPassword(String password) {
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    this.password = encoder.encode(password);
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String toJSON() {
    String json;
    try {
      final ObjectMapper mapper = new ObjectMapper();
      json = mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      //TODO: Rewrite with adequate logger
      e.printStackTrace();
      json = "{}";
    }

    return json;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (!User.class.isAssignableFrom(obj.getClass())) {
      return false;
    }

    final User user = (User) obj;

    return this.name.equals(user.name) &&
        this.password.equals(user.password);
  }
}
