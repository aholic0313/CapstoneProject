package com.generalnetdisk.controller;

import java.io.IOException;
import java.util.List;

import com.generalnetdisk.entity.constants.Constants;
import com.generalnetdisk.entity.dto.CreateImageCode;
import com.generalnetdisk.entity.po.UserInfo;
import com.generalnetdisk.entity.query.UserInfoQuery;
import com.generalnetdisk.entity.vo.ResponseVO;
import com.generalnetdisk.service.UserInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description: 用户信息业务控制层
 * @Date: 2024/07/22
 */
@RestController("userInfoController")
public class AccountController extends BaseController {

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session, Integer type) throws
            IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        if (type == null || type == 0) {
            session.setAttribute(Constants.CHECK_CODE_KEY, code);
        } else {
            session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL, code);
        }
        vCode.write(response.getOutputStream());
    }
}
