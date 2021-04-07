package com.infotran.springboot.loginsystem.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "userAccount",uniqueConstraints = { @UniqueConstraint(columnNames = "Account") })
@Component
public class UserAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "accoount_index")
	private Integer accoount_index;
	
	private String Account;

	private String Password;

	@Column(name = "fk_accountDetail_id")
	@Transient
	private Integer detailid;
//
//	@Transient
//	private Integer fk_levelDetail_id;
//
//	// =============================================================
//
//	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinTable(name = "FriendList", joinColumns = {
//			@JoinColumn(name = "fk_UserAccount", referencedColumnName = "Account") }, inverseJoinColumns = {
//					@JoinColumn(name = "fk_FriendAccount", referencedColumnName = "Account") })
//	private Set<UserAccount> friends = new HashSet<UserAccount>();
//
//	@ManyToMany(mappedBy = "friends")
//	private Set<UserAccount> friendsinmap = new HashSet<UserAccount>();
//
//	// =============================================================
//	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_accountDetail_id")
	private UserDetail useraccountdetail;
//
//	// =============================================================
//	@ManyToMany(mappedBy = "user")
//	private Set<TagList> usertags = new HashSet<TagList>();
//	
//	// =============================================================
//
//	@OneToMany(fetch = FetchType.LAZY,mappedBy = "netizen",cascade = CascadeType.ALL)
//	private List<MessageBox> netizens = new ArrayList<MessageBox>();
//	
//	@OneToMany(fetch = FetchType.LAZY,mappedBy = "msguser",cascade = CascadeType.ALL)
//	private List<MessageBox> msnbox = new ArrayList<MessageBox>();
	
	// =============================================================
	
	@Transient
	private String code;
	
	@Transient
	MultipartFile productImage;


	public UserAccount() {
	}

	public UserAccount(String Account, String Password) {
		this.Account = Account;
		this.Password = Password;
	}

	// =============================================================
	
	public MultipartFile getProductImage() {
		return productImage;
	}

	public void setProductImage(MultipartFile productImage) {
		this.productImage = productImage;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getAccoount_index() {
		return accoount_index;
	}

	public void setAccoount_index(Integer accoount_index) {
		this.accoount_index = accoount_index;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public Integer getDetailid() {
		return detailid;
	}

	public void setDetailid(Integer detailid) {
		this.detailid = detailid;
	}
//
//	public Integer getFk_levelDetail_id() {
//		return fk_levelDetail_id;
//	}
//
//	public void setFk_levelDetail_id(Integer fk_levelDetail_id) {
//		this.fk_levelDetail_id = fk_levelDetail_id;
//	}
//
//	public Set<UserAccount> getFriends() {
//		return friends;
//	}
//
//	public void setFriends(Set<UserAccount> friends) {
//		this.friends = friends;
//	}
//
	public UserDetail getUseraccountdetail() {
		return useraccountdetail;
	}

	public void setUseraccountdetail(UserDetail useraccountdetail) {
		this.useraccountdetail = useraccountdetail;
	}
//
//	public Set<TagList> getUsertags() {
//		return usertags;
//	}
//
//	public void setUsertags(Set<TagList> usertags) {
//		this.usertags = usertags;
//	}
//
//	public Set<UserAccount> getFriendsinmap() {
//		return friendsinmap;
//	}
//
//	public void setFriendsinmap(Set<UserAccount> friendsinmap) {
//		this.friendsinmap = friendsinmap;
//	}
//
//	public List<MessageBox> getNetizens() {
//		return netizens;
//	}
//
//	public void setNetizens(List<MessageBox> netizens) {
//		this.netizens = netizens;
//	}
//
//	public List<MessageBox> getMsnbox() {
//		return msnbox;
//	}
//
//	public void setMsnbox(List<MessageBox> msnbox) {
//		this.msnbox = msnbox;
//	}

}
