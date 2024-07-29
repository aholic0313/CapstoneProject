package com.generalnetdisk.controller;

import com.generalnetdisk.entity.constants.Constants;
import com.generalnetdisk.entity.dto.SessionWebUserDto;
import com.generalnetdisk.entity.vo.ResponseVO;
import com.generalnetdisk.enums.ResponseCodeEnum;
import com.generalnetdisk.exception.BusinessException;
import com.generalnetdisk.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
    protected static final String STATUS_SUCCESS = "success";
    protected static final String STATUS_ERROR = "error";

    protected <T> ResponseVO getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO();
        responseVO.setStatus(STATUS_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMessage());
        responseVO.setData(t);
        return responseVO;
    }

    protected <T> ResponseVO getBussinessErrorResponseVO(BusinessException e, T t) {
        ResponseVO<T> responseVO = new ResponseVO();
        responseVO.setStatus(STATUS_ERROR);
        if (e.getCode() == null) {
            responseVO.setCode(ResponseCodeEnum.CODE_400.getCode());
        } else {
            responseVO.setCode(e.getCode());
        }
        responseVO.setInfo(e.getMessage());
        responseVO.setData(t);
        return responseVO;
    }

    protected <T> ResponseVO getServerErrprResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO();
        responseVO.setStatus(STATUS_ERROR);
        responseVO.setCode(ResponseCodeEnum.CODE_500.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_500.getMessage());
        responseVO.setData(t);
        return responseVO;
    }

    protected void readFile(HttpServletResponse response, String filePath) {
        if (!StringTools.pathIsOk(filePath)) {
            return;
        }
        OutputStream out = null;
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            in = new FileInputStream(file);
            byte[] byteData = new byte[1024];
            out = response.getOutputStream();
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            logger.error("读取文件异常", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("读取文件异常", e);
                }
            }
        }
    }

    protected SessionWebUserDto getUserInfoFromSession(HttpSession session) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        return sessionWebUserDto;
    }
}
