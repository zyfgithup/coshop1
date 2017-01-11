package com.systop.amol.base.customer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.systop.amol.base.customer.model.Customer;
import com.systop.core.service.BaseGenericsManager;

/**
 * 客户Service
 * 
 * @author 王会璞
 */
@Service
public class CustomerManager extends BaseGenericsManager<Customer> {
	
  /**
   * 根据经销商ID和身份证号
   * @param Id  经销商ID
   * @param idCard 身份证号
   * @return
   */
	public Customer getCustomerInfo(Integer Id, String idCard) {
		String hql ="from Customer c where c.idCard=? and (c.owner.id = ? or c.owner.superior.id=?)";
		Customer customer= (Customer) getDao().findObject(hql,new Object[] {idCard,Id,Id});
		return customer;
	}
	
	/***
	 * 根据身份证号  和   经销商id得到客户信息
	 * @param userId  经销商id
	 * @param idCard  客户身份证号 
	 */
	public Customer searchCustomer(Integer userId, String idCard){
		String hql = "from Customer c where c.owner.id = ? and c.idCard = ?";
		return (Customer) getDao().findObject(hql,userId,idCard);
	}

	/**
	 * 根据身份证号得到客户信息
	 * @param idCard  客户身份证号 
	 * @return
	 */
	public Customer searchCustomer(String idCard) {
		String hql = "from Customer c where c.idCard = ?";
		return (Customer) getDao().findObject(hql,idCard);
	}
	
	/**
	 * 根据经销商Id得到从属与该分销商的所有客户
	 * @param Id
	 * @return
	 */
	public List<Customer> getCustomerByAgent(Integer Id){
		List<Customer> customers = query("from Customer c where c.agent.id = ?", Id);
		return customers;
	}
}

