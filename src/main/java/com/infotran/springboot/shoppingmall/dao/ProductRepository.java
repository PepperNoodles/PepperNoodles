package com.infotran.springboot.shoppingmall.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.infotran.springboot.shoppingmall.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	/*單純價格間距*/
	List<Product> findByPriceBetween(Integer startPrice, Integer endPrice);
	
	/*根據興趣推薦給使用者適合的產品*/
	@Query(value="SELECT Product"
				+"FROM FoodTag ft INNER JOIN"
				+"foodtag_user fu ON ft.FoodTag_id = fu.fk_foodTag_id INNER JOIN"
				+"FoodTag_Product fp ON ft.FoodTag_id = fp.fk_foodTag_id INNER JOIN"
				+"Product p ON fp.fk_Product_id = p.Product_id INNER JOIN"
				+"UserAccount ua ON fu.fk_userAccount_id = ua.account_id"
				+"where ua.acoount_index = ?1")
	public List<Product> findByTag(String accountindex,Pageable pageable);
	
	/*根據興趣推薦給使用者適合的產品/價格間距  */
	@Query(value="SELECT Product"
				+"FROM FoodTag ft INNER JOIN"
				+"foodtag_user ftu ON ft.FoodTag_id = ftu.fk_foodTag_id INNER JOIN"
				+"FoodTag_Product ftp ON ft.FoodTag_id = ftp.fk_foodTag_id INNER JOIN"
				+"Product p ON ftp.fk_Product_id = p.Product_id INNER JOIN"
				+"UserAccount ua ON ftu.fk_userAccount_id = ua.account_id"
				+"where ua.acoount_index = ?1 and (p.ProductPrice BETWEEN ?2 AND ?3)")
	public List<Product> findByTag(String accountindex,Integer startPrice, Integer endPrice,Pageable pageable);
	
	/*主分類搜尋/可以sortby productname*/
	@Query(value="SELECT Product FROM ProductMainClass pm INNER JOIN"
				+"ProductDetailClass pd ON pm.ProductMainClass_id = pd.fk_ProductMainClass_id INNER JOIN"
				+"Product p ON pd.DetailClass_id = p.fk_ProductDetailClass_id"
				+"where pm.MainClassName = ?1")
	List<Product> findByProductMainClass(String MainClassName,Pageable pageable);
	
	
	/*主分類搜尋和價格間距/可以sortby productname*/
	@Query(value="SELECT Product FROM ProductMainClass pm INNER JOIN"
				+"ProductDetailClass pd ON pm.ProductMainClass_id = pd.fk_ProductMainClass_id INNER JOIN"
				+"Product p ON pd.DetailClass_id = p.fk_ProductDetailClass_id"
				+"where pm.MainClassName = ?1 and (p.ProductPrice BETWEEN ?2 AND ?3) ")
	List<Product> findByProductMainClass(String MainClassName,Integer startPrice, Integer endPrice,Pageable pageable);
	
	
	/*子分類搜尋/可以sortby productname*/
	@Query(value="SELECT Product FROM ProductMainClass pm INNER JOIN"
				+"ProductDetailClass pd ON pm.ProductMainClass_id = pd.fk_ProductMainClass_id INNER JOIN"
				+"Product p ON pd.DetailClass_id = p.fk_ProductDetailClass_id"
				+"where pd.DetailClassName = ?1")
	List<Product> findByProductDetailClass(String DetailClassName,Pageable pageable);
	
	/*子分類搜尋和價格間距/可以sortby productname*/
	@Query(value="SELECT Product FROM ProductMainClass pm INNER JOIN"
				+"ProductDetailClass pd ON pm.ProductMainClass_id = pd.fk_ProductMainClass_id INNER JOIN"
				+"Product p ON pd.DetailClass_id = p.fk_ProductDetailClass_id"
				+"where pd.DetailClassName = ?1 and (p.ProductPrice BETWEEN ?2 AND ?3)")
	List<Product> findByProductDetailClass(String DetailClassName,Integer startPrice, Integer endPrice,Pageable pageable);
	
}
