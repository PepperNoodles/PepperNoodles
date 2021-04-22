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

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "foodtag_user")
@Component
public class FoodTagUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="food_UserAccount_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer FooTagUserId;
	
	@Column(name="fk_userAccount_id")
	@Transient
	private Integer fkuserid;

	@Column(name="fk_foodTag_id")
	@Transient
	private Integer fkfoodtagid;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY/*, optional = false*/)
    @JoinColumn(name = "fk_userAccount_id"/*,insertable = false,updatable = false*/)
	private UserAccount  fkuser;
		
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY/*, optional = false*/)
    @JoinColumn(name = "fk_foodTag_id"/*,insertable = false,updatable = false*/)
    private FoodTag  fkfoodtag;
	

	public FoodTagUser() {
	}

	public Integer getFooTagUserId() {
		return FooTagUserId;
	}


	public void setFooTagUserId(Integer fooTagUserId) {
		FooTagUserId = fooTagUserId;
	}



	


	public UserAccount getFkuser() {
		return fkuser;
	}

	public void setFkuser(UserAccount fkuser) {
		this.fkuser = fkuser;
	}

	public FoodTag getFkfoodtag() {
		return fkfoodtag;
	}

	public void setFkfoodtag(FoodTag fkfoodtag) {
		this.fkfoodtag = fkfoodtag;
	}

	public Integer getFkuserid() {
		return fkuserid;
	}

	public void setFkuserid(Integer fkuserid) {
		this.fkuserid = fkuserid;
	}

	public Integer getFkfoodtagid() {
		return fkfoodtagid;
	}

	public void setFkfoodtagid(Integer fkfoodtagid) {
		this.fkfoodtagid = fkfoodtagid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FooTagUserId == null) ? 0 : FooTagUserId.hashCode());
		result = prime * result + ((fkfoodtag == null) ? 0 : fkfoodtag.hashCode());
		result = prime * result + ((fkuser == null) ? 0 : fkuser.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoodTagUser other = (FoodTagUser) obj;
		if (FooTagUserId == null) {
			if (other.FooTagUserId != null)
				return false;
		} else if (!FooTagUserId.equals(other.FooTagUserId))
			return false;
		if (fkfoodtag == null) {
			if (other.fkfoodtag != null)
				return false;
		} else if (!fkfoodtag.equals(other.fkfoodtag))
			return false;
		if (fkuser == null) {
			if (other.fkuser != null)
				return false;
		} else if (!fkuser.equals(other.fkuser))
			return false;
		return true;
	}
	

}