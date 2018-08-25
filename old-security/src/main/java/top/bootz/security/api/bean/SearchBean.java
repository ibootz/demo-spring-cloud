package top.bootz.security.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SearchBean
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchBean {

	@JsonProperty("status")
	private String status;

	@JsonProperty("searchKey")
	private String searchKey;
	
	@JsonProperty("userID")
	private String userID;
	
	@JsonProperty("orgID")
	private String orgID;

	@JsonProperty("offset")
	private int offSet;

	@JsonProperty("limit")
	private int pageSize;

	@JsonProperty("orderby")
	private String orderByField;

	@JsonProperty("direction")
	private String orderType;
	
	@JsonProperty("searchStartTime")
	private String searchStartTime;
	
	@JsonProperty("searchEndTime")
	private String searchEndTime;
	
	@JsonProperty("mine")
	private String mine;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public int getOffSet() {
		return offSet;
	}

	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderByField() {
		return orderByField;
	}

	public void setOrderByField(String orderByField) {
		this.orderByField = orderByField;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getSearchStartTime() {
		return searchStartTime;
	}

	public void setSearchStartTime(String searchStartTime) {
		this.searchStartTime = searchStartTime;
	}

	public String getSearchEndTime() {
		return searchEndTime;
	}

	public void setSearchEndTime(String searchEndTime) {
		this.searchEndTime = searchEndTime;
	}

	public String getMine() {
		return mine;
	}

	public void setMine(String mine) {
		this.mine = mine;
	}

}
