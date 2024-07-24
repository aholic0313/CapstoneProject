package com.generalnetdisk.service.impl;

import java.util.Date;
import java.util.List;

import com.generalnetdisk.entity.constants.Constants;
import com.generalnetdisk.entity.po.EmailCode;
import com.generalnetdisk.entity.po.UserInfo;
import com.generalnetdisk.entity.query.EmailCodeQuery;
import com.generalnetdisk.entity.query.UserInfoQuery;
import com.generalnetdisk.entity.vo.PaginationResultVO;
import com.generalnetdisk.exception.BusinessException;
import com.generalnetdisk.mappers.UserInfoMapper;
import com.generalnetdisk.service.EmailCodeService;
import com.generalnetdisk.entity.query.SimplePage;
import com.generalnetdisk.enums.PageSize;
import com.generalnetdisk.mappers.EmailCodeMapper;
import com.generalnetdisk.service.UserInfoService;
import com.generalnetdisk.utils.StringTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description: 邮箱验证码业务接口
 * @Date: 2024/07/24
 */
@Service("emailCodeService")
public class EmailCodeServiceImpl implements EmailCodeService {

    @Resource
    private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    /* 根据条件查询列表 */
    @Override
    public List<EmailCode> findListByParam(EmailCodeQuery query) {
        return this.emailCodeMapper.selectList(query);
    }

    /* 根据条件查询数量 */
    @Override
    public Integer findCountByParam(EmailCodeQuery query) {
        return this.emailCodeMapper.selectCount(query);
    }

    /* 分页查询 */
    @Override
    public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<EmailCode> list = this.findListByParam(query);
        PaginationResultVO<EmailCode> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /* 新增 */
    @Override
    public Integer add(EmailCode bean) {
        return this.emailCodeMapper.insert(bean);
    }

    /* 批量新增 */
    @Override
    public Integer addBatch(List<EmailCode> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertBatch(listBean);
    }

    /* 批量新增或修改 */
    @Override
    public Integer addOrUpdateBatch(List<EmailCode> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertOrUpdateBatch(listBean);
    }

    /* 根据email, code查询对象 */
    @Override
    public EmailCode getEmailCodeByEmailAndCode(String email, String code) {
        return this.emailCodeMapper.selectByEmailAndCode(email, code);
    }

    /* 根据email, code更新 */
    @Override
    public Integer updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code) {
        return this.emailCodeMapper.updateByEmailAndCode(bean, email, code);
    }

    /* 根据email, code删除 */
    @Override
    public Integer deleteEmailCodeByEmailAndCode(String email, String code) {
        return this.emailCodeMapper.deleteByEmailAndCode(email, code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) {
        if (type == Constants.ZERO) {
            UserInfo userInfo = userInfoMapper.selectByEmail(email);
            if (null != userInfo) {
                throw new BusinessException("该邮箱已注册");
            }
        }

        String code = StringTools.getRandomNumber(Constants.LENGTH_5);

        //TODO 发送验证码

        //将之前的验证码 置为无效
        emailCodeMapper.disableEmailCode(email);

        EmailCode emailCode = new EmailCode();
        emailCode.setCode(code);
        emailCode.setEmail(email);
        emailCode.setStatus(Constants.ZERO);
        emailCode.setCreateTime(new Date());
        emailCodeMapper.insert(emailCode);
    }
}
