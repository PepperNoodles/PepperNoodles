package com.infotran.springboot.shoppingmall.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.infotran.springboot.shoppingmall.model.Product;

public interface ShoppingMallService {
	
	public Product findById(Integer productId);
	
	public Product get(Integer productId);
	
	/*後台曾刪改查product用*/
	public Product save(Product product);
	
	public void delete(Product product);
	
	
	
	public void update(Product product);
	
	/*全.封面組*/
	List<Product> getAllProducts(int page, int size);
	
	public Map<String,Integer> getBtnFromAll(int page, int size);
	
	/*tag組+封面組*/
	public List<Product> getPagedProductsByTag(String aacountindex,int page, int size);
	
	public Map<String,Integer> getBtnFromTag(String aacountindex, int page, int size);
	
	public List<Product> getPagedProductsByTagAndPriceRange(String aacountindex,Integer startPrice, Integer endPrice,int page, int size);
	
	
	/*MainClass組*/
	public List<Product> findByProductByMainClass(String MainClassname,int page, int size);

	public List<Product> findByProductByMainClassAndPriceRange(String MainClassname,Integer startPrice, Integer endPrice,int page,int size);

	/*Detailclass組*/
	public List<Product> findProductByDetailClass(String DetailClassName,int page,int size);
	
	public List<Product> findProductByDetailClassAndPriceRange(String DetailClassName,Integer startPrice, Integer endPrice,int page,int size);

	Map<String, Integer> getBtnFromMainClass(String MainClassname, int page, int size);


	Map<String, Integer> getBtnFromTagAndPriceRange(String aacountindex, Integer startPrice, Integer endPrice, int page,int size);

	public List<Product> findProductByPriceBetween(Integer startPrice, Integer endPrice,int page, int size);

	Map<String, Integer> getBtnFromAllAndPriceBetween(Integer startPrice, Integer endPrice, int page, int size);

	Map<String, Integer> getBtnFromMainClassAndPriceRange(String MainClassname, Integer startPrice, Integer endPrice,
			int page, int size);

	Map<String, Integer> getBtnFromDetailClass(String DetailClassName, int page, int size);

	Map<String, Integer> getBtnFromDetailClassAndPriceRange(String DetailClassName, Integer startPrice,Integer endPrice, int page, int size);

	List<Product> getProductsByExactTag(String tagname, int page, int size);

	Map<String, Integer> getBtnFromExactTag(String tagname, int page, int size);

	List<Product> getProductsByExactTagAndPriceRange(String tagname, Integer startPrice, Integer endPrice, int page,int size);

	Map<String, Integer> getBtnFromExactTagAndPriceRange(String tagname, Integer startPrice, Integer endPrice, int page,int size);

	List<Product> getProductsBySearch(String input, int page, int size);

	Map<String, Integer> getBtnFromSearch(String input, int page, int size);

	List<Product> getProductsBySearchAndPriceRange(String input, Integer startPrice, Integer endPrice, int page,
			int size);

	Map<String, Integer> getBtnFromSearchAndPriceRange(String input, Integer startPrice, Integer endPrice, int page,
			int size);

}
