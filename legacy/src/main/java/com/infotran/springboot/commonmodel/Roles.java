package com.infotran.springboot.commonmodel;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "roles")
@Component
public class Roles {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Integer roleId; 
	
	@Column(name = "roleName")
	private String roleName;


	
//	@OneToMany(fetch = FetchType.LAZY,mappedBy = "fkfoodtag",cascade = CascadeType.ALL)
//	 private Set<RolesUser> RolesUser = new HashSet<RolesUser>();
	
	@ManyToMany(mappedBy = "roles")
	private Set<UserAccount> userAccounts = new HashSet<UserAccount>();

	
	public Roles() {
		super();
	}

	
	public Integer getRoleId() {
		return roleId;
	}


	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
