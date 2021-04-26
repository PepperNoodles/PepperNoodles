package com.infotran.springboot.shoppingmall.service.Impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.infotran.springboot.shoppingmall.dao.ProductRepository;
import com.infotran.springboot.shoppingmall.model.Product;
import com.infotran.springboot.shoppingmall.service.ShoppingMallService;

@Service
public class ShoppingMallServiceImpl implements ShoppingMallService {

	@Autowired
	public ProductRepository productrepository;


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
	public List<Product> findProductByPriceBetween(Integer startPrice, Integer endPrice,int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult= productrepository.findPageByProductPriceBetween(startPrice,endPrice,pageable);
		List<Product> productlist =pageResult.getContent();
		return productlist;
	}
	
	@Override
	public List<Product> getAllProducts(int page, int size) {
		Page<Product> pageResult = productrepository.findAll(PageRequest.of(page, // 查詢的頁數，從0起算
				size, // 查詢的每頁筆數(目前每頁為6)
				Sort.by("quantity").descending())); // 依RealeasedDateE欄位降冪(新到舊)排序
		pageResult.getNumberOfElements(); // 本頁筆數
		pageResult.getSize(); // 每頁筆數
		pageResult.getTotalElements(); // 全部筆數
		pageResult.getTotalPages(); // 全部頁數
		List<Product> productlist = pageResult.getContent();
		return productlist;
	}
	
	@Override
	public Map<String,Integer> getBtnFromAll(int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findAll(pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}
	
	@Override
	public Map<String,Integer> getBtnFromAllAndPriceBetween(Integer startPrice, Integer endPrice,int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult= productrepository.findPageByProductPriceBetween(startPrice, endPrice, pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<Product> getProductsBySearch(String input, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findBySearch(input, pageable);
		List<Product> productListByTag = pageResult.getContent();
		return productListByTag;
	}

	@Override
	public Map<String,Integer> getBtnFromSearch(String input, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findBySearch(input, pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}
	
	@Override
	public List<Product> getProductsBySearchAndPriceRange(String input, Integer startPrice, Integer endPrice,int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "productPrice");
		Page<Product> pageResult = productrepository.findBySearchAndPriceBetween(input,startPrice, endPrice,pageable);
		List<Product> productListByTagAndPriceRange = pageResult.getContent();
		return productListByTagAndPriceRange;
	}
	
	@Override
	public Map<String,Integer> getBtnFromSearchAndPriceRange(String input,Integer startPrice, Integer endPrice, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findBySearchAndPriceBetween(input,startPrice, endPrice,pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<Product> getProductsByExactTag(String tagname, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findByExactLikeTag(tagname, pageable);
		List<Product> productListByTag = pageResult.getContent();
		return productListByTag;
	}
	
	@Override
	public Map<String,Integer> getBtnFromExactTag(String tagname, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findByExactLikeTag(tagname, pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}
	
	@Override
	public List<Product> getProductsByExactTagAndPriceRange(String tagname, Integer startPrice, Integer endPrice,int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "productPrice");
		Page<Product> pageResult = productrepository.findByExactLikeTagAndPriceBetween(tagname, startPrice, endPrice, pageable);
		List<Product> productListByTagAndPriceRange = pageResult.getContent();
		return productListByTagAndPriceRange;
	}
	
	@Override
	public Map<String,Integer> getBtnFromExactTagAndPriceRange(String tagname,Integer startPrice, Integer endPrice, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findByExactLikeTagAndPriceBetween(tagname,startPrice, endPrice, pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public List<Product> getPagedProductsByTag(String aacountindex, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findByTag(aacountindex, pageable);
			pageResult.getNumberOfElements(); // 本頁筆數
			pageResult.getSize(); // 每頁筆數
			pageResult.getTotalElements(); // 全	部筆數
			pageResult.getTotalPages(); // 全部頁數
		List<Product> productListByTag = pageResult.getContent();
		return productListByTag;
	}
	
	//取得Tag總共幾頁
	@Override
	public Map<String,Integer> getBtnFromTag(String aacountindex, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findByTag(aacountindex, pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}
	
	@Override
	public List<Product> getPagedProductsByTagAndPriceRange(String aacountindex, Integer startPrice, Integer endPrice,
			int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "productPrice");
		Page<Product> pageResult = productrepository.findByTag(aacountindex, startPrice, endPrice, pageable);
		List<Product> productListByTagAndPriceRange = pageResult.getContent();
		// test
		return productListByTagAndPriceRange;
	}
	
	@Override
	public Map<String,Integer> getBtnFromTagAndPriceRange(String aacountindex,Integer startPrice, Integer endPrice, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findByTag(aacountindex,startPrice, endPrice, pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public List<Product> findByProductByMainClass(String MainClassname, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "productPrice");
		Page<Product> pageResult = productrepository.findByProductMainClass(MainClassname, pageable);
		List<Product> productListByMainClass = pageResult.getContent();
		// test
		return productListByMainClass;
	}
	
	@Override
	public Map<String,Integer> getBtnFromMainClass(String MainClassname, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findByProductMainClass(MainClassname, pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}
	

	@Override
	public List<Product> findByProductByMainClassAndPriceRange(String MainClassname, Integer startPrice,
			Integer endPrice, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "productPrice");
		Page<Product> pageResult = productrepository.findByProductMainClass(MainClassname, startPrice, endPrice,
				pageable);
		List<Product> productListByMainClassAndPriceRange = pageResult.getContent();
		// test
		return productListByMainClassAndPriceRange;
	}
	
	@Override
	public Map<String,Integer> getBtnFromMainClassAndPriceRange(String MainClassname,Integer startPrice,Integer endPrice,int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult =productrepository.findByProductMainClass(MainClassname, startPrice, endPrice, pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public List<Product> findProductByDetailClass(String DetailClassName, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "productPrice");
		Page<Product> pageResult = productrepository.findByProductDetailClass(DetailClassName, pageable);
		List<Product> productListByDetailClass = pageResult.getContent();
		// test
		return productListByDetailClass;
	}
	
	@Override
	public Map<String,Integer> getBtnFromDetailClass(String DetailClassName, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findByProductDetailClass(DetailClassName, pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}
	

	@Override
	public List<Product> findProductByDetailClassAndPriceRange(String DetailClassName, Integer startPrice,
			Integer endPrice, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "productPrice");
		Page<Product> pageResult = productrepository.findByProductDetailClass(DetailClassName, startPrice, endPrice,
				pageable);
		List<Product> productListByDetailClassAndPriceRange = pageResult.getContent();
		// test
		return productListByDetailClassAndPriceRange;
	}
	
	
	@Override
	public Map<String,Integer> getBtnFromDetailClassAndPriceRange(String DetailClassName,Integer startPrice,Integer endPrice, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "quantity");
		Page<Product> pageResult = productrepository.findByProductDetailClass(DetailClassName, startPrice, endPrice, pageable);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("TotalPages", pageResult.getTotalPages());
		return map;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Product get(Integer productId) {
		Optional<Product> optional = productrepository.findById(productId);
		Product product = null;
		if (optional.isPresent()) {
			product = optional.get();
		} else {
			throw new RuntimeException("Member(id=" + productId + ")不存在");
		}
		return product;
	}

	@Override
	public Product findById(Integer productId) {
		Optional<Product> op = productrepository.findById(productId);
		Product product = null;
		if (op.isPresent()) {
			product = op.get();
		} else {
			throw new RuntimeException("Member(id=" + productId + ")不存在");
		}
		return product;
	}
	
	
}
