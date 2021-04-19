package com.infotran.springboot.shoppingmall.model;

import java.sql.Blob;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.infotran.springboot.commonmodel.Restaurant;

@Entity
@Table(name="Product")
@Component
@JsonIgnoreProperties(value = {"productImage"})
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Product_id")
	private Integer productId;
	
	@Column(name="ProductName")
	private String productName;
	
	@Column(name="ProductPrice")
	private Integer productPrice;
	
	@Column(name="Description")
	private String description;
	
	@Column(name="ProductImage")
	private Blob productImage;
	
	@Column(name="Quantity")
	private Integer quantity;

	/* 對應的餐廳 */
	@Column(name="fk_Restaurant_id",insertable = false,updatable = false)
	private Integer fkRestaurantId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_Restaurant_id")
	@JsonIgnore
	private Restaurant restaurant;
	
	/* 對應的子分類 */
	@Column(name="fk_ProductDetailClass_id",insertable = false,updatable = false)
	private Integer fkProductDetailClassId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_ProductDetailClass_id")
	@JsonIgnore 
	private ProductDetailClass productDetailClass;
	
	@Column(name="RealeasedDate")
	private Date realeasedDate;
	
	/* 所屬的tag */
	@OneToMany(mappedBy = "fkProductid",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<FoodTagProduct> productTags = new HashSet<FoodTagProduct>();
	
	/* 訂單編號多對多 */
	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<OrderDetail> orderList = new HashSet<OrderDetail>();
	
	@Transient
	private Integer totalpage;
	//
	public Product() {
	}
	
	public Integer getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(Integer totalpage) {
		this.totalpage = totalpage;
	}

	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Integer productPrice) {
		this.productPrice = productPrice;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Blob getProductImage() {
		return productImage;
	}
	public void setProductImage(Blob productImage) {
		this.productImage = productImage;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getFkRestaurantId() {
		return fkRestaurantId;
	}
	public void setFkRestaurantId(Integer fkRestaurantId) {
		this.fkRestaurantId = fkRestaurantId;
	}
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	public Integer getFkProductDetailClassId() {
		return fkProductDetailClassId;
	}
	public void setFkProductDetailClassId(Integer fkProductDetailClassId) {
		this.fkProductDetailClassId = fkProductDetailClassId;
	}
	public ProductDetailClass getProductDetailClass() {
		return productDetailClass;
	}
	public void setProductDetailClass(ProductDetailClass productDetailClass) {
		this.productDetailClass = productDetailClass;
	}
	public Date getRealeasedDate() {
		return realeasedDate;
	}
	public void setRealeasedDate(Date realeasedDate) {
		this.realeasedDate = realeasedDate;
	}
	public Set<FoodTagProduct> getProductTags() {
		return productTags;
	}
	public void setProductTags(Set<FoodTagProduct> productTags) {
		this.productTags = productTags;
	}
	public Set<OrderDetail> getOrderList() {
		return orderList;
	}
	public void setOrderList(Set<OrderDetail> orderList) {
		this.orderList = orderList;
	}
	
	
	
}
