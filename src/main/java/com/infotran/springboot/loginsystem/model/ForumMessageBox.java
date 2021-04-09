package com.infotran.springboot.loginsystem.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="forumMessageBox")
public class ForumMessageBox {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="forumMessage_id ")
	private Integer forumMessageId;
	
	@Column(name="fk_netizen_account_id")
	@Transient
	private Integer fkNetizenAccount;
	
	@Column(name="text")
	private String text;
	
	@Column(name="time")
	private Date time;
	
	@Column(name="likeAmount")
	private Integer likeAmount;
	
	@Column(name="score")
	private Integer score ;
	
	@Column(name="fk_forum_id")
	@Transient
	private Integer fkForumId ;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_netizen_account_id")
	private UserAccount forumNetizenAccount;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_forum_id")
	private Forum forum;
	
	//replyMessageBox
	@OneToOne(mappedBy ="forumMessageBox",cascade = CascadeType.ALL)
	private ForumMessageBox replyMessage;

	
	
	public ForumMessageBox() {
		super();
	}


	
	

}
