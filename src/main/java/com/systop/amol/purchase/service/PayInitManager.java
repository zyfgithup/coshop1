package com.systop.amol.purchase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.amol.purchase.PurchaseConstants;
import com.systop.amol.purchase.model.PayAble;
import com.systop.amol.purchase.model.PayInit;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 应付初始化
 * 
 * @author WangHaiYan
 * 
 */
@Service
public class PayInitManager extends BaseGenericsManager<PayInit> {

	@Autowired
	private PayAbleManager payAbleManager;
	

	/**
   * 根据供应商名称获取对象
   * @param name
   * @return
   */
  public PayInit getPayInit(String name) {
    String hql = "from PayInit p where p.supplier.name = ? ";
    return (PayInit) getDao().findObject(hql, new Object[] { name });
  }

  

	@Transactional
	public void save(Integer userid,List<PayInit> paylist) {

		try {
			//删除及时应付
			//List<PayAble> plist=payAbleManager.query("from PayAble where user.id=?",userid);
			//for(PayAble pa:plist){
			//	payAbleManager.remove(pa);
			//}
			//List<PayInit> pilist=query("from PayInit where user.id=?",userid);
			//for(PayInit pa:pilist){
			//   remove(pa);
			//}
			//把旧的删除
			String hsql = "from PayInit c where c.user.id= ?  and c.status=0";
			List<PayInit> list = null;
			list = this.query(hsql, userid);
			for(PayInit pi:list){
			PayAble pa=new PayAble();
			pa.setAmount(pi.getAmount());
			pa.setSupplier(pi.getSupplier());
			pa.setUser(pi.getUser());
			payAbleManager.delIn(pa);
			this.remove(pi);
			}
			//添加新的
			for (PayInit od : paylist) {
				if(od.getStatus()==null){
					od.setStatus(0);
				}
				if(od.getStatus()==0){
      				this.save(od);
				PayAble pa=new PayAble();
				pa.setAmount(od.getAmount().doubleValue());
				pa.setSupplier(od.getSupplier());
				pa.setUser(od.getUser());
				payAbleManager.saveIn(pa);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("保存失败");
		}
	}

	public List<PayInit> getNoOver(User user,Integer supplierId){
		if(user.getSuperior()==null){
		return query("from PayInit where amount-payamount<>0 and user.id=?  and supplier.id=?",user.getId(),supplierId);
		}else{
			return query("from PayInit where amount-payamount<>0 and (user.id=? or user.id=?) and supplier.id=?",user.getId(),user.getSuperior().getId(),supplierId);
		}
	}
		
	@Transactional
	public void del(Integer id){
		PayInit pi=this.get(id);
			PayAble pa=new PayAble();
			pa.setAmount(pi.getAmount());
			pa.setSupplier(pi.getSupplier());
			pa.setUser(pi.getUser());
			payAbleManager.delIn(pa);
		if(pi!=null){
			this.remove(pi);
		}
	}
	@Transactional
	public void initstock(User user){
		List<PayInit> plist= query("from PayInit where  user.id=?  ",user.getId());
		for(PayInit pi:plist){
		  if(pi.getStatus()==null || pi.getStatus()!=1){
			  pi.setStatus(PurchaseConstants.PAYINIT_FINISH);
		  }
		  this.save(pi);
		}
		
	}

	
}
