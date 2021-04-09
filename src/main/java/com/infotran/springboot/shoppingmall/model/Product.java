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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.infotran.springboot.common.model.FoodTag;


@Entity
@Table(name="Product")
@Component
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Product_id")
	private Integer ProductId;
	
	private String ProductName;
	
	private Integer ProductPrice;
	
	private String Description;
	
	private Blob ProductImage;
	
	private Integer Quantity;

	/* 對應的餐廳 */
	@Column(name="fk_Restaurant_id")
	@Transient
	private Integer FkRestaurantId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_Restaurant_id")
	private Restaurant restaurant;
	
	/* 對應的子分類 */
	@Column(name="fk_ProductDetailClass_id")
	@Transient
	private Integer FkProductDetailClassId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_ProductDetailClass_id")
	private ProductDetailClass productDetailClass;
	
	/*ReleasedDate*/
	private Date RealeasedDate;
	
	/* 所屬的tag */
	@ManyToMany(mappedBy = "Product")
	private Set<FoodTag> productTags = new HashSet<FoodTag>();
	
	/* 訂單編號多對多 */
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(name="OrderDetail",joinColumns = {
			@JoinColumn(name="fk_Product_id",referencedColumnName = "Product_id")},inverseJoinColumns = {
			@JoinColumn(name="fk_Order_id",referencedColumnName = "Order_id")})
	private Set<OrderList> OrderList = new HashSet<OrderList>();

	public Integer getProductId() {
		return ProductId;
	}

	public void setProductId(Integer productId) {
		ProductId = productId;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public Integer getProductPrice() {
		return ProductPrice;
	}

	public void setProductPrice(Integer productPrice) {
		ProductPrice = productPrice;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Blob getProductImage() {
		return ProductImage;
	}

	public void setProductImage(Blob productImage) {
		ProductImage = productImage;
	}

	public Integer getQuantity() {
		return Quantity;
	}

	public void setQuantity(Integer quantity) {
		Quantity = quantity;
	}

	public Integer getFkRestaurantId() {
		return FkRestaurantId;
	}

	public void setFkRestaurantId(Integer fkRestaurantId) {
		FkRestaurantId = fkRestaurantId;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public Integer getFkProductDetailClassId() {
		return FkProductDetailClassId;
	}

	public void setFkProductDetailClassId(Integer fkProductDetailClassId) {
		FkProductDetailClassId = fkProductDetailClassId;
	}

	public ProductDetailClass getProductDetailClass() {
		return productDetailClass;
	}

	public void setProductDetailClass(ProductDetailClass productDetailClass) {
		this.productDetailClass = productDetailClass;
	}

	public Set<FoodTag> getProductTags() {
		return productTags;
	}

	public void setProductTags(Set<FoodTag> productTags) {
		this.productTags = productTags;
	}

	public Set<OrderList> getOrder() {
		return OrderList;
	}

	public void setOrder(Set<OrderList> order) {
		this.OrderList = order;
	}

	public Date getRealeasedDate() {
		return RealeasedDate;
	}

	public void setRealeasedDate(Date realeasedDate) {
		RealeasedDate = realeasedDate;
	}

	public Set<OrderList> getOrderList() {
		return OrderList;
	}

	public void setOrderList(Set<OrderList> orderList) {
		OrderList = orderList;
	}
	
}
