package top.bootz.old_security.api.bean;

// TODO: Auto-generated Javadoc
/**
 * UserCacheInfo.
 */
public class UserCacheInfo {
	
	/** The org id. */
	private String orgId;
	
	/** The user id. */
	private String userId;
	
	/** The consumer user id. */
	private String consumerUserId;
	
	/** The role id. */
	private String roleId;
	
	/** The permission json. */
	private String permissionJson;
	
	/** The fourd user id. */
	private String fourdUserId;

	/**
	 * constructor.
	 *
	 * @param orgId String
	 * @param userId String
	 * @param consumerUserId the consumer user id
	 * @param roleId String
	 * @param permissionJson String
	 * @param fourdUserId the fourd user id
	 */
	public UserCacheInfo(String orgId, String userId, String consumerUserId, String roleId, String permissionJson, String fourdUserId) {
		this.orgId = orgId;
		this.userId = userId;
		this.consumerUserId = consumerUserId;
		this.roleId = roleId;
		this.permissionJson = permissionJson;
		this.fourdUserId = fourdUserId;
	}

	/**
	 * getOrgId.
	 *
	 * @return String
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * setOrgId.
	 *
	 * @param orgId String
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * getUserId.
	 *
	 * @return String
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * setUserId.
	 *
	 * @param userId String
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the consumer user id.
	 *
	 * @return the consumer user id
	 */
	public String getConsumerUserId() {
		return consumerUserId;
	}

	/**
	 * Sets the consumer user id.
	 *
	 * @param consumerUserId the new consumer user id
	 */
	public void setConsumerUserId(String consumerUserId) {
		this.consumerUserId = consumerUserId;
	}

	/**
	 * getRoleId.
	 *
	 * @return String
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * setRoleId.
	 *
	 * @param roleId String
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * getPermissionJson.
	 *
	 * @return String
	 */
	public String getPermissionJson() {
		return permissionJson;
	}

	/**
	 * setPermissionJson.
	 *
	 * @param permissionJson String
	 */
	public void setPermissionJson(String permissionJson) {
		this.permissionJson = permissionJson;
	}

	/**
	 * Gets the fourd user id.
	 *
	 * @return the fourd user id
	 */
	public String getFourdUserId() {
		return fourdUserId;
	}

	/**
	 * Sets the fourd user id.
	 *
	 * @param fourdUserId the new fourd user id
	 */
	public void setFourdUserId(String fourdUserId) {
		this.fourdUserId = fourdUserId;
	}
}
