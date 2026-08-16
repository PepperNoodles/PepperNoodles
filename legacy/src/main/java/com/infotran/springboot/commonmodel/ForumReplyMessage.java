package com.infotran.springboot.commonmodel;

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

import org.springframework.stereotype.Component;

@Entity
@Table(name="forumReplyMessage")
@Component
public class ForumReplyMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="forum_Reply_id ")
	private Integer forumReplyId;
	
	@Column(name="fk_ReplyNetizen_Account_ID")
	@Transient
	private Integer fkReplyNetizenAccountId;
	
	@Column(name="fk_ReplyForumMessage_ID")
	@Transient
	private Integer fkReplyForumMessageID ;
	
	@Column(name="Reply_Text ")
	private String replyText ;
	
	@Column(name="time")
	private Date time;
	
	@Column(name="likeAmount")
	private Integer likeAmount;
	
	@Column(name="fk_netizen_account_id")
	@Transient
	private Integer fkNetizenAccountId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_ReplyNetizen_Account_id")
	private UserAccount forumReplyuserAccount;
	
	//CascadeType要改嗎??
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_ReplyForumMessage_ID")
	private ForumMessageBox forumMessageBox;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_netizen_account_id")
	private UserAccount forumCommentUserAccount;


	public ForumReplyMessage() {
		super();
	}
	

}
