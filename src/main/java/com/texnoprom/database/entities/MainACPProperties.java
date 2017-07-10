package com.texnoprom.database.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "main_acp_properties")
public class MainACPProperties {
  @Id
  @JsonProperty("id")
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "address", nullable = false, length = 1024)
  private String address;

  @Column(name = "equipment_type", nullable = false)
  private String equipmentType;

  @Column(name = "manufacturer_id", nullable = false)
  private String manufacturerId;

  @Column(name = "year_of_issue", nullable = false)
  private Integer yearOfIssue;

  public MainACPProperties(String address, String equipmentType, String manufacturerId,
      Integer yearOfIssue) {
    this.address = address;
    this.equipmentType = equipmentType;
    this.manufacturerId = manufacturerId;
    this.yearOfIssue = yearOfIssue;
  }

  public MainACPProperties() {
  }

  public Long getId() {
    return id;
  }

  public String getAddress() {
    return address;
  }

  public String getEquipmentType() {
    return equipmentType;
  }

  public String getManufacturerId() {
    return manufacturerId;
  }

  public Integer getYearOfIssue() {
    return yearOfIssue;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setEquipmentType(String equipmentType) {
    this.equipmentType = equipmentType;
  }

  public void setManufacturerId(String manufacturerId) {
    this.manufacturerId = manufacturerId;
  }

  public void setYearOfIssue(Integer yearOfIssue) {
    this.yearOfIssue = yearOfIssue;
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

}
