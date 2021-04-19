package com.infotran.springboot.shoppingmall.model;

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

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infotran.springboot.commonmodel.FoodTag;

@Entity
@Table(name = "foodTag_Product")
@Component
public class FoodTagProduct implements Serializable {
//	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="food_Product_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer foodProductId;
	
	
	@Column(name="fk_Product_id",insertable = false,updatable = false)
	private Integer fkproductid;
	
	@Column(name="fk_foodTag_id",insertable = false,updatable = false)
	private Integer fkFoodtagid;
	
	//////////////////////////////////////////////////
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false )
    @JoinColumn(name = "fk_Product_id")
    @JsonIgnore
    private Product fkProductid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_foodTag_id")
    @JsonIgnore
    private FoodTag fkfoodtagid;
    
    
    
    public FoodTagProduct() {
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	public Integer getFoodProductId() {
		return foodProductId;
	}

	public void setFoodProductId(Integer foodProductId) {
		this.foodProductId = foodProductId;
	}

	public Integer getFkproductid() {
		return fkproductid;
	}

	public void setFkproductid(Integer fkproductid) {
		this.fkproductid = fkproductid;
	}

	public Integer getFkFoodtagid() {
		return fkFoodtagid;
	}

	public void setFkFoodtagid(Integer fkFoodtagid) {
		this.fkFoodtagid = fkFoodtagid;
	}

	public Product getFkProductid() {
		return fkProductid;
	}

	public void setFkProductid(Product fkProductid) {
		this.fkProductid = fkProductid;
	}

	public FoodTag getFkfoodtagid() {
		return fkfoodtagid;
	}

	public void setFkfoodtagid(FoodTag fkfoodtagid) {
		this.fkfoodtagid = fkfoodtagid;
	}

	

	
	
}
