package com.systop.amol.app.index.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.systop.amol.app.push.model.PushMessage;
import com.systop.common.modules.security.user.model.User;

/**
 * 首页信息
 * @author 王会璞
 *
 */
public class Index implements Serializable {

	private List<PushMessage> photoCarouselImgList;
	private List<Map<String, Object>> productSorts;
	private List<User> recommendMerchant;
	
	public List<PushMessage> getPhotoCarouselImgList() {
		return photoCarouselImgList;
	}
	public void setPhotoCarouselImgList(List<PushMessage> photoCarouselImgList) {
		this.photoCarouselImgList = photoCarouselImgList;
	}
	public List<Map<String, Object>> getProductSorts() {
		return productSorts;
	}
	public void setProductSorts(List<Map<String, Object>> productSorts) {
		this.productSorts = productSorts;
	}
	public List<User> getRecommendMerchant() {
		return recommendMerchant;
	}
	public void setRecommendMerchant(List<User> recommendMerchant) {
		this.recommendMerchant = recommendMerchant;
	}
	
	
}
