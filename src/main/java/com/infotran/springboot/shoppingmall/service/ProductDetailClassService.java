package com.infotran.springboot.shoppingmall.service;

import org.springframework.stereotype.Service;

import com.infotran.springboot.shoppingmall.model.ProductDetailClass;

@Service
public interface ProductDetailClassService {

	ProductDetailClass findById(Integer productdetailclassid);

}
