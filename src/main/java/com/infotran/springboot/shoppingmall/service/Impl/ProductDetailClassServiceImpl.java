package com.infotran.springboot.shoppingmall.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.shoppingmall.dao.ProductDetailClassRepository;
import com.infotran.springboot.shoppingmall.model.ProductDetailClass;
import com.infotran.springboot.shoppingmall.service.ProductDetailClassService;

@Service
public class ProductDetailClassServiceImpl implements ProductDetailClassService {

	@Autowired
	ProductDetailClassRepository prodetaildao;
	
	@Override
	public ProductDetailClass findById(Integer productdetailclassid) {
		Optional<ProductDetailClass> detail = prodetaildao.findById(productdetailclassid);
		ProductDetailClass productdetail = null;
		if (detail.isPresent()) {
			productdetail = detail.get();
		}else {
			throw new RuntimeException("Member(id=" + productdetailclassid + ")不存在");
		}
		return productdetail;
	}
	
	
}
