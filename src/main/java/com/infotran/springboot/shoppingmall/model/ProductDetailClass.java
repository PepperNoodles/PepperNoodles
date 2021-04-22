package com.infotran.springboot.shoppingmall.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="ProductDetailClass")
@Component

public class ProductDetailClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="DetailClass_id")
	private Integer DetailClassId;
	
	private String DetailClassName;
	
	/*對應主類別(多對一)*/
	@Column(name="fk_ProductMainClass_id",insertable = false,updatable = false)
	private Integer FkProductMainClassId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_ProductMainClass_id")
	@JsonIgnore
	private ProductMainClass productMainClass;
	
	/*對應產品*/
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "productDetailClass",cascade =CascadeType.ALL)
	@JsonIgnore
	private Set<Product> products =new HashSet<Product>();

	public Integer getDetailClassId() {
		return DetailClassId;
	}

	public void setDetailClassId(Integer detailClassId) {
		DetailClassId = detailClassId;
	}

	public String getDetailClassName() {
		return DetailClassName;
	}

	public void setDetailClassName(String detailClassName) {
		DetailClassName = detailClassName;
	}

	public Integer getFkProductMainClassId() {
		return FkProductMainClassId;
	}

	public void setFkProductMainClassId(Integer fkProductMainClassId) {
		FkProductMainClassId = fkProductMainClassId;
	}

	public ProductMainClass getProductMainClass() {
		return productMainClass;
	}

	public void setProductMainClass(ProductMainClass productMainClass) {
		this.productMainClass = productMainClass;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	
	
}
