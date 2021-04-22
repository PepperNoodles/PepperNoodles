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
import javax.persistence.Transient;

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
	
	
	@Column(name="fk_Product_id")
	@Transient
	private Integer fkProductid;
	
	@Column(name="fk_foodTag_id")
	@Transient
	private Integer fkfoodtagid;
	
	//////////////////////////////////////////////////
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_Product_id")
   @JsonIgnore
    private Product fkproductid;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_foodTag_id")
    @JsonIgnore
    private FoodTag fkFoodtagid;

    
    
    
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

	public Integer getFkProductid() {
		return fkProductid;
	}

	public void setFkProductid(Integer fkProductid) {
		this.fkProductid = fkProductid;
	}

	public Integer getFkfoodtagid() {
		return fkfoodtagid;
	}

	public void setFkfoodtagid(Integer fkfoodtagid) {
		this.fkfoodtagid = fkfoodtagid;
	}

	public Product getFkproductid() {
		return fkproductid;
	}

	public void setFkproductid(Product fkproductid) {
		this.fkproductid = fkproductid;
	}

	public FoodTag getFkFoodtagid() {
		return fkFoodtagid;
	}

	public void setFkFoodtagid(FoodTag fkFoodtagid) {
		this.fkFoodtagid = fkFoodtagid;
	}
	
}
