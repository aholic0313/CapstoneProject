package com.generalnetdisk.entity.query;

import java.util.Date;
/**
 * @Description: 用户信息
 *
 * @Date: 2024/07/24
 */
public class UserInfoQuery extends BaseQueryParam {
	/* 用户ID查询对象 */
	private String userId;

	private String userIdFuzzy;

	/* 昵称查询对象 */
	private String nickName;

	private String nickNameFuzzy;

	/* 邮箱查询对象 */
	private String email;

	private String emailFuzzy;

	/* qqOpenId查询对象 */
	private String qqOpenId;

	private String qqOpenIdFuzzy;

	/* qq头像查询对象 */
	private String qqAvatar;

	private String qqAvatarFuzzy;

	/* 密码查询对象 */
	private String password;

	private String passwordFuzzy;

	/* 加入时间查询对象 */
	private Date joinTime;

	private String joinTimeStart;

	private String joinTimeEnd;

	/* 最后登录时间查询对象 */
	private Date lastLoginTime;

	private String lastLoginTimeStart;

	private String lastLoginTimeEnd;

	/* 0:禁用 1:启用查询对象 */
	private Integer status;

	/* 使用空间单位 byte查询对象 */
	private Long userSpace;

	/* 总空间查询对象 */
	private Long totalSpace;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setQqOpenId(String qqOpenId) {
		this.qqOpenId = qqOpenId;
	}

	public String getQqOpenId() {
		return qqOpenId;
	}

	public void setQqAvatar(String qqAvatar) {
		this.qqAvatar = qqAvatar;
	}

	public String getQqAvatar() {
		return qqAvatar;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setUserSpace(Long userSpace) {
		this.userSpace = userSpace;
	}

	public Long getUserSpace() {
		return userSpace;
	}

	public void setTotalSpace(Long totalSpace) {
		this.totalSpace = totalSpace;
	}

	public Long getTotalSpace() {
		return totalSpace;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}

	public void setNickNameFuzzy(String nickNameFuzzy) {
		this.nickNameFuzzy = nickNameFuzzy;
	}

	public String getNickNameFuzzy() {
		return nickNameFuzzy;
	}

	public void setEmailFuzzy(String emailFuzzy) {
		this.emailFuzzy = emailFuzzy;
	}

	public String getEmailFuzzy() {
		return emailFuzzy;
	}

	public void setQqOpenIdFuzzy(String qqOpenIdFuzzy) {
		this.qqOpenIdFuzzy = qqOpenIdFuzzy;
	}

	public String getQqOpenIdFuzzy() {
		return qqOpenIdFuzzy;
	}

	public void setQqAvatarFuzzy(String qqAvatarFuzzy) {
		this.qqAvatarFuzzy = qqAvatarFuzzy;
	}

	public String getQqAvatarFuzzy() {
		return qqAvatarFuzzy;
	}

	public void setPasswordFuzzy(String passwordFuzzy) {
		this.passwordFuzzy = passwordFuzzy;
	}

	public String getPasswordFuzzy() {
		return passwordFuzzy;
	}

	public void setJoinTimeStart(String joinTimeStart) {
		this.joinTimeStart = joinTimeStart;
	}

	public String getJoinTimeStart() {
		return joinTimeStart;
	}

	public void setJoinTimeEnd(String joinTimeEnd) {
		this.joinTimeEnd = joinTimeEnd;
	}

	public String getJoinTimeEnd() {
		return joinTimeEnd;
	}

	public void setLastLoginTimeStart(String lastLoginTimeStart) {
		this.lastLoginTimeStart = lastLoginTimeStart;
	}

	public String getLastLoginTimeStart() {
		return lastLoginTimeStart;
	}

	public void setLastLoginTimeEnd(String lastLoginTimeEnd) {
		this.lastLoginTimeEnd = lastLoginTimeEnd;
	}

	public String getLastLoginTimeEnd() {
		return lastLoginTimeEnd;
	}

}