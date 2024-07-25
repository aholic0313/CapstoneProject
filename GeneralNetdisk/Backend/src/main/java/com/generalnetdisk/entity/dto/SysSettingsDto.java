package com.generalnetdisk.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingsDto implements Serializable {
    private String registerMailTitle = "邮箱验证码";

    private String registerMailContent = "您好，你的邮箱验证码是：%s,15分钟内有效";

    private Integer userInitUseSpace = 5;

    public String getRegisterMailTitle() {
        return registerMailTitle;
    }

    public void setRegisterMailTitle(String registerMailTitle) {
        this.registerMailTitle = registerMailTitle;
    }

    public String getRegisterMailContent() {
        return registerMailContent;
    }

    public void setRegisterMailContent(String registerMailContent) {
        this.registerMailContent = registerMailContent;
    }

    public Integer getUserInitUseSpace() {
        return userInitUseSpace;
    }

    public void setUserInitUseSpace(Integer userInitUseSpace) {
        this.userInitUseSpace = userInitUseSpace;
    }
}
