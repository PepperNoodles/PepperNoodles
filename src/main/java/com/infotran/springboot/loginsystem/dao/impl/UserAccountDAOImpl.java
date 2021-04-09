package com.infotran.springboot.loginsystem.dao.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.loginsystem.dao.UserAccountDAOInterface;

@Repository
public class UserAccountDAOImpl implements UserAccountDAOInterface {

	@Autowired
	private SessionFactory sessionfactory;

	@SuppressWarnings("unchecked")
	@Override
	public UserAccount SelectByAccount(UserAccount user) {
		Session session = sessionfactory.getCurrentSession();
		String hql = "FROM UserAccount u Where u.Account=:Account";
		List<UserAccount> userlist = session.createQuery(hql).setParameter("Account", user.getAccountIndex()).getResultList();
		UserAccount useraccount=null;
		if(!userlist.isEmpty()) {
			useraccount = userlist.get(0);
		}
		return useraccount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserAccount> SelectAll() {
		Session session = sessionfactory.getCurrentSession();
		String hql = "FROM UserAccount";
		List<UserAccount> userlist = session.createQuery(hql).getResultList();
		return userlist;
	}

	@Override
	public void DeleteAccount(UserAccount user) {
		Session session = sessionfactory.getCurrentSession();
		session.delete(user);
	}

	@Override
	// 註冊會員(新增)
	public int RegistureUserAccount(UserAccount user) {
		int flag = 0;
		boolean exist = isExist(user);
		if (exist) {//true為已存在，帳號重複
			return -1;
		}
		Session session = sessionfactory.getCurrentSession();
		try {
			session.save(user);
			flag = 1;
		} catch (Exception e) {
			e.printStackTrace();
			flag = -2;
		}
		return flag;
	}
	
	
	

	@Override
	public UserAccount Update(int id, String name, String password) {
		return null;
	}

	@Override
	public Set<UserAccount> selectfriend(UserAccount user) {
		return null;
	}

	@Override
	public Set<UserAccount> selectuser(UserAccount user) {
		return null;
	}

	@Override
	public boolean isExist(UserAccount user) {
		boolean isexist = false;
		Session session = sessionfactory.getCurrentSession();
		String hql = "FROM UserAccount u WHERE u.Account=:account";
		try {
			session.createQuery(hql).setParameter("account", user.getAccountIndex()).getSingleResult();
			isexist = true;			
		} catch (NoResultException ex) {
			;
		}
		return isexist;
	}

	@Override
	// 判斷會員帳號是否重複
	public String checkUsserAccount(UserAccount user) {
//		System.out.println("1");
		String userAccountName = "";
		Session session = sessionfactory.getCurrentSession();
		String hql = "FROM UserAccount u where u.Account=:account";
		try {
			UserAccount user2 = (UserAccount) session.createQuery(hql).setParameter("account", user.getAccountIndex()).getSingleResult();
			userAccountName = user2.getAccountIndex();
		} catch (NoResultException ex) {
			return userAccountName;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			userAccountName = "Error: 資料庫異常，請檢查資料庫";
		}
		return userAccountName;
	}

	@Override
	public void evictAccount(UserAccount user) {
		Session session = sessionfactory.getCurrentSession();
		session.evict(user);		
	}

	
//	public UserAccount checkLogin(UserAccount users) {//查詢
//		Session session = sessionfactory.getCurrentSession();
//		String hql = "from Account Where username=:user and userpwd=:pwd";
//		Query<UserAccount> query = session.createQuery(hql, UserAccount.class);
//		query.setParameter("user", users.getAccount());
//		query.setParameter("pwd", users.getPassword());
//		UserAccount account = (UserAccount)query.uniqueResult();
//		if (account!=null)
//			return account;
//		return null;
//	}

}
