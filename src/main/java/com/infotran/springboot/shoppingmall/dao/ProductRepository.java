package com.infotran.springboot.shoppingmall.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.shoppingmall.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query(value= "SELECT distinct p "
				+ "FROM Product p,FoodTagProduct ftp INNER JOIN "
				+ "FoodTag ft ON ftp.fkFoodtagid = ft.foodTagIid INNER JOIN "
				+ "Product p ON ftp.fkproductid = p.productId "
				+ "where ft.foodTagName like %:input% or p.productName like %:input% and p.status ='上架中'")
	Page<Product> findBySearch(String input,Pageable pageable);
	
	
	@Query(value= "SELECT distinct p "
			+ "FROM Product p,FoodTagProduct ftp INNER JOIN "
			+ "FoodTag ft ON ftp.fkFoodtagid = ft.foodTagIid INNER JOIN "
			+ "Product p ON ftp.fkproductid = p.productId "
			+ "where (ft.foodTagName like %:input% or p.productName like %:input%) "
			+ "and (p.productPrice BETWEEN :startPrice AND :endPrice) and p.status ='上架中'")
	Page<Product> findBySearchAndPriceBetween(String input,Integer startPrice, Integer endPrice,Pageable pageable);
	
	
	
	@Query(value= "SELECT distinct p "
				+ "FROM Product p,FoodTagProduct ftp INNER JOIN "
				+ "FoodTag ft ON ftp.fkFoodtagid = ft.foodTagIid INNER JOIN "
				+ "Product p ON ftp.fkproductid = p.productId "
				+ "where ft.foodTagName = ?1 and p.status ='上架中'")
	Page<Product> findByExactLikeTag(String tagname,Pageable pageable);
	
	@Query(value= "SELECT distinct p "
				+ "FROM Product p,FoodTagProduct ftp INNER JOIN "
				+ "FoodTag ft ON ftp.fkFoodtagid = ft.foodTagIid INNER JOIN "
				+ "Product p ON ftp.fkproductid = p.productId "
				+ "where ft.foodTagName = ?1 and (p.productPrice BETWEEN ?2 AND ?3) and p.status ='上架中'")
	Page<Product> findByExactLikeTagAndPriceBetween(String tagname,Integer startPrice, Integer endPrice,Pageable pageable);
	
	/*模糊查詢*/
	List<Product> findByProductNameIgnoreCaseContaining(String ProductName);
	
	
	List<Product> findByProductPriceBetween(Integer startPrice, Integer endPrice);
	
	
	@Query(value = "select p from Product p where p.productPrice BETWEEN ?1 AND ?2 and p.status ='上架中'")
	Page<Product> findPageByProductPriceBetween(Integer startPrice, Integer endPrice,Pageable pageable);
	
	
	/*根據興趣推薦給使用者適合的產品*/
	@Query(value="SELECT distinct p "
				+"FROM Product p, FoodTag ft INNER JOIN "
				+"FoodTagUser fu ON ft.foodTagIid = fu.fkfoodtagid INNER JOIN "
				+"FoodTagProduct fp ON ft.foodTagIid = fp.fkFoodtagid INNER JOIN "
				+"Product p ON fp.fkproductid = p.productId INNER JOIN "
				+"UserAccount ua ON fu.fkuserid = ua.accountId "
				+"where ua.accountIndex = ?1 and p.status ='上架中'")
	public Page<Product> findByTag(String accountindex,Pageable pageable);
	
	
	/*根據興趣推薦給使用者適合的產品/價格間距  */
	@Query(value="SELECT distinct p "
				+"FROM Product p, FoodTag ft INNER JOIN "
				+"FoodTagUser fu ON ft.foodTagIid = fu.fkfoodtagid INNER JOIN "
				+"FoodTagProduct fp ON ft.foodTagIid = fp.fkFoodtagid INNER JOIN "
				+"Product p ON fp.fkproductid = p.productId INNER JOIN "
				+"UserAccount ua ON fu.fkuserid = ua.accountId "
				+"where ua.accountIndex = ?1 and (p.productPrice BETWEEN ?2 AND ?3) and p.status ='上架中'")
	public Page<Product> findByTag(String accountindex,Integer startPrice, Integer endPrice,Pageable pageable);
	
	/*主分類搜尋/可以sortby productname*/
	@Query(value="SELECT distinct p "
				+"FROM Product p, ProductMainClass as pm INNER JOIN "
				+"ProductDetailClass pd ON pm.productMainClassId = pd.FkProductMainClassId INNER JOIN "
				+"Product p ON pd.DetailClassId = p.fkProductDetailClassId "
				+"where pm.mainClassName = ?1 and p.status ='上架中'")
	Page<Product> findByProductMainClass(String mainClassName,Pageable pageable);
	
	
	/*主分類搜尋和價格間距/可以sortby productname*/
	@Query(value="SELECT distinct p "
				+"FROM Product p, ProductMainClass as pm INNER JOIN "
				+"ProductDetailClass pd ON pm.productMainClassId = pd.FkProductMainClassId INNER JOIN "
				+"Product p ON pd.DetailClassId = p.fkProductDetailClassId "
				+"where pm.mainClassName = ?1 and (p.productPrice BETWEEN ?2 AND ?3) and p.status ='上架中'")
	Page<Product> findByProductMainClass(String mainClassName,Integer startPrice, Integer endPrice,Pageable pageable);
	
	
	/*子分類搜尋/可以sortby productname*/
	@Query(value="SELECT distinct p FROM Product p, ProductMainClass as pm INNER JOIN "
				+"ProductDetailClass pd ON pm.productMainClassId = pd.FkProductMainClassId INNER JOIN "
				+"Product p ON pd.DetailClassId = p.fkProductDetailClassId "
				+"where pd.DetailClassName = ?1 and p.status ='上架中'")
	Page<Product> findByProductDetailClass(String DetailClassName,Pageable pageable);
	
	/*子分類搜尋和價格間距/可以sortby productname*/
	@Query(value="SELECT distinct p FROM Product p, ProductMainClass as pm INNER JOIN "
				+"ProductDetailClass pd ON pm.productMainClassId = pd.FkProductMainClassId INNER JOIN "
				+"Product p ON pd.DetailClassId = p.fkProductDetailClassId "
				+"where pd.DetailClassName = ?1 and (p.productPrice BETWEEN ?2 AND ?3) and p.status ='上架中'")
	Page<Product> findByProductDetailClass(String DetailClassName,Integer startPrice, Integer endPrice,Pageable pageable);
	
	
	Optional<Product> findById(Integer product);
	
	@Query(value="select p.productId from Product p")
	public Set<Integer> findIdByProduct();
	
	@Query(value="select p from Product p where convert(varchar,p.updateDate,111) >= ?1 and p.status ='下架中'")
	public List<Product> findUpdatedProductByDate(String startDate);
	
	@Query(value="select p from Product p where convert(nvarchar,p.realeasedDate,111) =?1 and p.status = '上架中'")
	public List<Product> findNewProductByReleasedDate(String todayDate);
	
	
}
