package com.generalnetdisk.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.generalnetdisk.enums.DateTimeEnum;
import com.generalnetdisk.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 邮箱验证码
 *
 * @Date: 2024/07/24
 */
public class EmailCode implements Serializable {
	/* 邮箱 */
	private String email;

	/* 编号 */
	private String code;

	/* 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/* 0:未使用 1:已使用 */
	private Integer status;

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "邮箱:" + (email == null ? "空" : email)+ " | "  + "编号:" + (code == null ? "空" : code)+ " | "  + "创建时间:" + (createTime == null ? "空" : DateUtils.format(createTime, DateTimeEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+ " | "  + "0:未使用 1:已使用:" + (status == null ? "空" : status)+ " | " ;
	}
}