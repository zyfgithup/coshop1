package com.systop.amol.base.express.phone;

import com.systop.amol.base.express.model.ExpressCompany;
import com.systop.amol.base.express.service.ExpressCompanyManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * Created by Administrator on 2016/8/5.
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneExpressAction extends
        DefaultCrudAction<ExpressCompany, ExpressCompanyManager> {

    @Autowired
    UserManager userManager;
    /** 分页，页码 */
    private String pageNumber = "1";
    /** 每页显示条数 */
    private String pageCount = "10";

    private List<ExpressCompany> list = new ArrayList<ExpressCompany>();

    private ExpressCompany expressCompany;

    private Map<String,Object> result = new HashMap<String,Object>() {
    };
    public String index() {
        page = PageUtil.getPage(Integer.parseInt(pageNumber),Integer.parseInt(pageCount));
        List args = new ArrayList();
        StringBuffer sql = new StringBuffer(" from ExpressCompany c  where c.visible='1'  " );
        String userId = getRequest().getParameter("userId");
        if(StringUtils.isNotBlank(userId)){
            sql.append(" and c.user.id = ?  ");
            args.add(Integer.parseInt(userId));
        }
        sql.append(" order by c.createTime desc ");
        page = getManager().pageQuery(page, sql.toString(), args.toArray());
        list = page.getData();
        return INDEX;
    }
    public String removeCompany(){
        String id = getRequest().getParameter("comId");
        ExpressCompany ec = getManager().get(Integer.parseInt(id));
        ec.setVisible("0");
        getManager().update(ec);
        result.put("msg","删除成功");
        return  SUCCESS;
    }
    public String setMrAddress(){
        //设置默认信息的id
        String userId = getRequest().getParameter("userId");
        ExpressCompany zqMrCom = getManager().getRsByUserId(Integer.parseInt(userId));
        if(null!= zqMrCom){
            zqMrCom.setIfMrCompany("0");
            getManager().update(zqMrCom);
        }
        String comId = getRequest().getParameter("comId");
        ExpressCompany ec = getManager().get(Integer.parseInt(comId));
        ec.setIfMrCompany("1");
        getManager().update(ec);
        result.put("msg","设置成功");
        return  SUCCESS;
    }
    public String save(){
        /**
         * {"address":"浙江省衡州白领中路12号","bankName":"招商银行",
         * "cardNo":"621487827837283","createTime":"2016-08-05T10:12:06","id":1,
         * "name":"乾泰物流股份有限公司","phone":"13788922035","saxNo":"no232323"}
         * http://localhost:8080/coshop/phone/company/save.do?address=asfasf&bankName=afasfdsaf&cardNo=234234&name=公司&phone24234&saxNo=234234
         */
        try {
            ExpressCompany ec = null;
             ec = new ExpressCompany();
            ec.setCreateTime(new Date());
            String userId = getRequest().getParameter("userId");
            User user = userManager.get(Integer.parseInt(userId));
            ec.setUser(user);
            String address = getRequest().getParameter("address");
            ec.setAddress(address);
            String bankName = getRequest().getParameter("bankName");
            ec.setBankName(bankName);
            String cardNo = getRequest().getParameter("cardNo");
            ec.setCardNo(cardNo);
            String name = getRequest().getParameter("name");
            ec.setName(name);
            String phone = getRequest().getParameter("phone");
            ec.setPhone(phone);
            String saxNo = getRequest().getParameter("saxNo");
            ec.setSaxNo(saxNo);
            String ifMrAddr = getRequest().getParameter("ifMrCom");
            ec.setIfMrCompany(ifMrAddr);
            ec.setVisible("1");
            if(StringUtils.isNotBlank(ifMrAddr)){
                if(Integer.parseInt(ifMrAddr)==1){
                    ExpressCompany mrEx=getManager().getRsByUserId(Integer.parseInt(userId));
                    if(null!=mrEx){
                        mrEx.setIfMrCompany("0");
                        getManager().update(mrEx);
                    }
                }
            }
            ec.setIfMrCompany(ifMrAddr);
            getManager().save(ec);
            result.put("msg","success");
        } catch (Exception e) {
            result.put("msg","error");
            e.printStackTrace();
        }
        return SUCCESS;
    }
    public String update(){
        /**
         * {"address":"浙江省衡州白领中路12号","bankName":"招商银行",
         * "cardNo":"621487827837283","createTime":"2016-08-05T10:12:06","id":1,
         * "name":"乾泰物流股份有限公司","phone":"13788922035","saxNo":"no232323"}
         * http://localhost:8080/coshop/phone/company/save.do?address=asfasf&bankName=afasfdsaf&cardNo=234234&name=公司&phone24234&saxNo=234234
         */
        try {
            String comId = getRequest().getParameter("comId");
            ExpressCompany ec = getManager().get(Integer.parseInt(comId));
            ec.setCreateTime(new Date());
            String address = getRequest().getParameter("address");
            ec.setAddress(address);
            String bankName = getRequest().getParameter("bankName");
            ec.setBankName(bankName);
            String cardNo = getRequest().getParameter("cardNo");
            ec.setCardNo(cardNo);
            String name = getRequest().getParameter("name");
            ec.setName(name);
            String phone = getRequest().getParameter("phone");
            ec.setPhone(phone);
            String saxNo = getRequest().getParameter("saxNo");
            ec.setSaxNo(saxNo);
            String ifMrAddr = getRequest().getParameter("ifMrCom");
            ec.setIfMrCompany(ifMrAddr);
            ec.setVisible("1");
            if(StringUtils.isNotBlank(ifMrAddr)){
                if(Integer.parseInt(ifMrAddr)==1){
                    ExpressCompany mrEx=getManager().getRsByUserId(ec.getUser().getId());
                    if(null!=ec){
                        mrEx.setIfMrCompany("0");
                        getManager().update(mrEx);
                    }
                }

            }
            ec.setIfMrCompany(ifMrAddr);
            getManager().save(ec);
            result.put("msg","success");
        } catch (Exception e) {
            result.put("msg","error");
            e.printStackTrace();
        }
        return SUCCESS;
    }
    public String getDetailOfCom(){
        String comId = getRequest().getParameter("comId");
        expressCompany = getManager().get(Integer.parseInt(comId));
        return INPUT;
    }
    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<ExpressCompany> getList() {
        return list;
    }

    public void setList(List<ExpressCompany> list) {
        this.list = list;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public ExpressCompany getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(ExpressCompany expressCompany) {
        this.expressCompany = expressCompany;
    }
}
