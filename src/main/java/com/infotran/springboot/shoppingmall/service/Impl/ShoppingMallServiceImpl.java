package com.infotran.springboot.shoppingmall.service.Impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.infotran.springboot.shoppingmall.dao.ProductRepository;
import com.infotran.springboot.shoppingmall.model.Product;
import com.infotran.springboot.shoppingmall.service.ShoppingMallService;

@Service
public class ShoppingMallServiceImpl implements ShoppingMallService {

	@Autowired
	public ProductRepository productrepository;
	
	public List<Product> getAllProducts(int page, int size) {
        Page<Product> pageResult = productrepository.findAll(
                PageRequest.of(page,  // 查詢的頁數，從0起算
                               size, // 查詢的每頁筆數(目前應該為6)
                               Sort.by("RealeasedDate").descending())); // 依RealeasedDateE欄位降冪(新到舊)排序
        pageResult.getNumberOfElements(); // 本頁筆數
        pageResult.getSize();             // 每頁筆數 
        pageResult.getTotalElements();    // 全部筆數
        pageResult.getTotalPages();       // 全部頁數
        List<Product> productlist =  pageResult.getContent();
        return productlist;
    }

	@Override
	public Product save(Product product) {
		return productrepository.save(product);
	}

	@Override
	public void delete(Product product) {
		productrepository.delete(product);
	}

	@Override
	public void update(Product product) {
		productrepository.save(product);
	}


	@Override
	public List<Product> getPagedProductsByTag(int page, int size) {
		
		return null;
	}


	@Override
	public List<Product> GetPagedProductsByTagAndPriceRange(int page, int size) {
		return null;
	}


	@Override
	public List<Product> findByProductByMainClass(int page, int size) {
		return null;
	}


	@Override
	public List<Product> findByProductByMainClassAndPriceRange(int page, int size) {
		return null;
	}


	@Override
	public List<Product> findProductByDetailClass(int page, int size) {
		return null;
	}


	@Override
	public List<Product> findProductByDetailClassAndPriceRange(int page, int size) {
		return null;
	}
}
