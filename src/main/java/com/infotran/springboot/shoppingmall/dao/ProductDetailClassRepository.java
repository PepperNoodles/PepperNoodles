package com.infotran.springboot.shoppingmall.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.shoppingmall.model.ProductDetailClass;


public interface ProductDetailClassRepository extends JpaRepository<ProductDetailClass, Integer> {

	
	
	
}
