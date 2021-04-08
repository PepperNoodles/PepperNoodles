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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

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
	@Column(name="fk_ProductMainClass_id")
	@Transient
	private Integer FkProductMainClassId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_ProductMainClass_id")
	private ProductMainClass productMainClass;
	
	/*對應產品*/
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "productDetailClass",cascade =CascadeType.ALL)
	private List<Product> products =new ArrayList<Product>();

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

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	
	
}
