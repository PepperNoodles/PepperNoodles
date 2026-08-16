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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "likefeeds")
public class Like implements Serializable {
	 private static final long serialVersionUID = 1L;
	 
	 @Id
	 @Column(name="like_Id")
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer likeId;
	 
	 @Column(name="fk_userAccount_id", insertable=false, updatable=false)
	 private Integer fkUserAccountId;
	 
	 @Column(name="fk_messageBox_id",insertable=false, updatable=false)
	 private Integer fkMessageBoxId;
	 
	 
	 @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "fk_userAccount_id")
	 @JsonIgnore
	 private UserAccount fkUAId;
	 
	 
	 @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "fk_messageBox_id")
	 @JsonIgnore
	 private MessageBox fkMSNId;
	 

	 public Like() {
	 }


	public Integer getLikeId() {
		return likeId;
	}


	public void setLikeId(Integer likeId) {
		this.likeId = likeId;
	}


	public Integer getFkUserAccountId() {
		return fkUserAccountId;
	}


	public void setFkUserAccountId(Integer fkUserAccountId) {
		this.fkUserAccountId = fkUserAccountId;
	}


	public Integer getFkMessageBoxId() {
		return fkMessageBoxId;
	}


	public void setFkMessageBoxId(Integer fkMessageBoxId) {
		this.fkMessageBoxId = fkMessageBoxId;
	}


	public UserAccount getFkUAId() {
		return fkUAId;
	}


	public void setFkUAId(UserAccount fkUAId) {
		this.fkUAId = fkUAId;
	}


	public MessageBox getFkMSNId() {
		return fkMSNId;
	}


	public void setFkMSNId(MessageBox fkMSNId) {
		this.fkMSNId = fkMSNId;
	}

	 
}
