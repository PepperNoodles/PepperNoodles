package com.infotran.springboot.shoppingmall.service;

import java.util.List;
import com.infotran.springboot.shoppingmall.model.Product;

public interface ShoppingMallService {
	
	/*後台曾刪改查product用*/
	public Product save(Product product);
	
	public void delete(Product product);
	
	public void update(Product product);
	
	/*全.封面組*/
	List<Product> getAllProducts(int page, int size);
	
	/*tag組+封面組*/
	public List<Product> getPagedProductsByTag(int page, int size);
	
	public List<Product> GetPagedProductsByTagAndPriceRange(int page, int size);

	/*MainClass組*/
	public List<Product> findByProductByMainClass(int page, int size);

	public List<Product> findByProductByMainClassAndPriceRange(int page,int size);

	/*Detailclass組*/
	public List<Product> findProductByDetailClass(int page,int size);
	
	public List<Product> findProductByDetailClassAndPriceRange(int page,int size);

}
