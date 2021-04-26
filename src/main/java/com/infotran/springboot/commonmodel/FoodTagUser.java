package com.infotran.springboot.commonmodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "foodtag_user")
public class FoodTagUser implements Serializable {
// 
 private static final long serialVersionUID = 1L;
 
 @Id
 @Column(name="food_UserAccount_id")
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer FooTagUserId;
 
 @Column(name="fk_foodTag_id",insertable = false,updatable = false)
 private Integer fkfoodTagid;
 
 @Column(name="fk_userAccount_id",insertable = false,updatable = false)
 private Integer fkuserAccountid;
 
 
 @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_userAccount_id")
 @JsonBackReference
 private UserAccount fkuserid;
 
 
 @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_foodTag_id")
 @JsonBackReference
 private FoodTag fkfoodtagid;
 

 public FoodTagUser() {
 }


 public Integer getFooTagUserId() {
  return FooTagUserId;
 }


 public void setFooTagUserId(Integer fooTagUserId) {
  FooTagUserId = fooTagUserId;
 }


 public Integer getFkfoodTagid() {
  return fkfoodTagid;
 }


 public void setFkfoodTagid(Integer fkfoodTagid) {
  this.fkfoodTagid = fkfoodTagid;
 }


 public Integer getFkuserAccountid() {
  return fkuserAccountid;
 }


 public void setFkuserAccountid(Integer fkuserAccountid) {
  this.fkuserAccountid = fkuserAccountid;
 }


 public UserAccount getFkuserid() {
  return fkuserid;
 }


 public void setFkuserid(UserAccount fkuserid) {
  this.fkuserid = fkuserid;
 }


 public FoodTag getFkfoodtagid() {
  return fkfoodtagid;
 }


 public void setFkfoodtagid(FoodTag fkfoodtagid) {
  this.fkfoodtagid = fkfoodtagid;
 }


 public static long getSerialversionuid() {
  return serialVersionUID;
 }


 

 
 

}

