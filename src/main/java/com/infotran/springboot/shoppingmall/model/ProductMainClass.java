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

@Entity
@Table(name="ProductMainClass")
@Component
public class ProductMainClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ProductMainClass_id")
	private Integer ProductMainClassId;
	
	private String MainClassName;
	
	/*對應子類別*/
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "productMainClass",cascade =CascadeType.ALL)
	private List<ProductDetailClass> productDetailClass = new ArrayList<ProductDetailClass>();   
	
}
