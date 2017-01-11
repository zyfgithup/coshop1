package com.systop.amol.base.yyy.phone;

import com.systop.amol.base.yyy.model.YyyEvent;
import com.systop.amol.base.yyy.model.YyyRecords;
import com.systop.amol.base.yyy.service.YyyAwardManager;
import com.systop.amol.base.yyy.service.YyyManager;
import com.systop.amol.base.yyy.service.YyyRecordsManager;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.JsonCrudAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class PhoneYyyAction extends JsonCrudAction<YyyEvent,YyyManager> {
	@Autowired
	private UserManager userManager;
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private YyyRecordsManager yyyRecordsManager;
	@Autowired
	private YyyAwardManager yyyAwardManager;
	private YyyEvent yyyEvent;
	private String pageNumber = "1";
	private Map<String, Object> message = new HashMap<String,Object>();

	private String pageCount = "10";
	private List<YyyRecords> list = new ArrayList<YyyRecords>();
	public String getYyyZjRecords() {
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		String userId = getRequest().getParameter("userId");
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from YyyRecords rs where rs.isZj='1' and  rs.user.id=?");
		args.add(Integer.parseInt(userId));
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		list = page.getData();
		for (YyyRecords record : list) {
		}
		return "index";
	}
	// 返回用户摇一摇记录
	public String getYyyRecords() {
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		String userId = getRequest().getParameter("userId");
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from YyyRecords rs where  rs.user.id=?");
		args.add(Integer.parseInt(userId));
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		list = page.getData();
		for (YyyRecords record : list) {
		}
		return "index";
	}
	/*public static int[] getRandom(int n,int t){
		List numbers=new ArrayList();
		int[] rtnumbers=new int[t];    for(int i=0;i<n;i++){
			//初始化数组
			numbers.add(i+1);    }
		for(int j=0;j<t;j++){
			int raNum=(int)(Math.random()*numbers.size());
			rtnumbers[j]=(int) numbers.get(raNum);
			numbers.remove(raNum);
			}
		return rtnumbers;
		}*/
	/*// app摇一摇事件接口
	public String ctrYyyGl() {
		String msg = "";
		String userId = getRequest().getParameter("userId");
		String yyId = getRequest().getParameter("yyId");
		YyyRecords  yyyRecords=yyyRecordsManager.getObjectById(Integer.parseInt(yyId),Integer.parseInt(userId));
		if(null!=yyyRecords){
			msg="您已中奖,谢谢参与。";
		}else{
		YyyEvent yyyEvent = getManager().getObjectById(Integer.parseInt(yyId));
		int count=yyyAwardManager.getCntOfAward(yyyEvent.getId());
		if(count==0){
			msg="该活动还未设置奖项，稍后参与";
		}else{
		int[] randomNum=this.getRandom(count,1);
		System.out.println("------您抽到的数字是-------"+randomNum[0]);
		List<Map<String,Object>> adList=yyyAwardManager.getAwardNames(Integer.parseInt(yyId));
		System.out.println("----adList.get(randomNum[0]-1).getJxName()--------------"+adList.get(randomNum[0]-1));
		System.out.println("----adList.get(randomNum[0]-1).getJxName()--------------"+adList.get(randomNum[0]-1).get("jxName"));
		System.out.println("---------adList.get(randomNum[0]-1).get(id))----------"+adList.get(randomNum[0]-1).get("id"));
		YyyAward yyyAward=yyyAwardManager.getYyyAwardByYyyeventId(Integer.parseInt(String.valueOf(adList.get(randomNum[0]-1).get("id"))));
		int[] userNums=this.getRandom(100, yyyAward.getZjProbability());
		User user = userManager.get(Integer.parseInt(userId));
		if (null == user.getIntegral() || user.getIntegral() < yyyEvent.getMinusIntegal()) {
			msg = "您的积分不足，或是您还没有积分";
		} else if (null!=yyyAward.getHaveZjTimes()&&yyyAward.getHaveZjTimes() >= yyyAward.getZjTimes()) {
			msg = "该奖项已经抽奖结束,请继续参与活动";
		} else {
			String isZj = "";
			// 给用户减积分
			user.setIntegral(user.getIntegral() - yyyEvent.getMinusIntegal());
			userManager.update(user);
			User regionUser = userManager.get(yyyEvent.getCreateUser().getId());
			Integer regionUserIntegral = regionUser.getIntegral();
			Integer regionUserDhIntegral = regionUser.getDhIntegral();
			if (null != regionUserDhIntegral) {
				regionUser.setDhIntegral(regionUser.getDhIntegral() + yyyEvent.getMinusIntegal());
			} else {
				regionUser.setDhIntegral(yyyEvent.getMinusIntegal());
			}
			if (null != regionUserIntegral) {
				regionUser.setIntegral(regionUser.getIntegral() + yyyEvent.getMinusIntegal());
			} else {
				regionUser.setIntegral(yyyEvent.getMinusIntegal());
			}
			userManager.update(regionUser);
			int luckNum = yyyAward.getLuckyNum();
			for (int no: userNums) {
				if (no == luckNum) {
					if(null!=yyyAward.getHaveZjTimes()){
						yyyAward.setHaveZjTimes(yyyAward.getHaveZjTimes() + 1);
					}else{
						yyyAward.setHaveZjTimes(1);
					}
					msg = "恭喜您中了"+yyyAward.getJxName()+"，奖品是"+yyyAward.getProName();
					isZj = "1";
					yyyAwardManager.update(yyyAward);
					break;
				} else {
					msg = "您没有中奖，谢谢参与";
					isZj = "0";
				}
			}
			YyyRecords records = new YyyRecords();
			records.setCreateTime(new Date());
			records.setIsZj(isZj);
			if(isZj.equals("1"))
			{
				records.setFlag("0");
			}
			String strs="";
			for (int no: userNums) {
				strs=strs+","+no;
			}
			records.setUser(user);
			records.setYyyAward(yyyAward);
			records.setYyyEvent(yyyEvent);
			records.setRegion(yyyEvent.getRegion());
			records.setUserNo(strs);
			yyyRecordsManager.save(records);
		}
		}
		}
		message.put("msg", msg);
		return "complete";
	}*/
	public String index() {
		page = PageUtil.getPage(Integer.valueOf(pageNumber), Integer.valueOf(pageCount));
		String regionId = getRequest().getParameter("regionId");
		String userId = getRequest().getParameter("userId");
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(
				"from YyyEvent rs where rs.region.id=? and rs.visible='1' and rs.isUsed='1'");
		args.add(Integer.parseInt(regionId));
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		list = page.getData();
		return "index";
	}

	public String yyyDetail() {
		String yyId = getRequest().getParameter("yyId");
		yyyEvent = getManager().getObjectById(Integer.parseInt(yyId));
		return "detail";
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public YyyEvent getYyyEvent() {
		return yyyEvent;
	}

	public void setYyyEvent(YyyEvent yyyEvent) {
		this.yyyEvent = yyyEvent;
	}

	public Map<String, Object> getMessage() {
		return message;
	}

	public void setMessage(Map<String, Object> message) {
		this.message = message;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
