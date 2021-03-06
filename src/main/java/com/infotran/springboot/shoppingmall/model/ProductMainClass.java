package com.infotran.springboot.shoppingmall.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="ProductMainClass")
@Component
public class ProductMainClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ProductMainClass_id")
	private Integer productMainClassId;
	
	@Column(name="MainClassName")
	private String mainClassName;
	
	/*對應子類別*/
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "productMainClass",cascade =CascadeType.ALL)
	@JsonIgnore 
	private List<ProductDetailClass> productDetailClass = new ArrayList<ProductDetailClass>();

	public ProductMainClass() {
	}

	public Integer getProductMainClassId() {
		return productMainClassId;
	}

	public void setProductMainClassId(Integer productMainClassId) {
		this.productMainClassId = productMainClassId;
	}

	public String getMainClassName() {
		return mainClassName;
	}

	public void setMainClassName(String mainClassName) {
		this.mainClassName = mainClassName;
	}

	public List<ProductDetailClass> getProductDetailClass() {
		return productDetailClass;
	}

	public void setProductDetailClass(List<ProductDetailClass> productDetailClass) {
		this.productDetailClass = productDetailClass;
	}   
	
}
