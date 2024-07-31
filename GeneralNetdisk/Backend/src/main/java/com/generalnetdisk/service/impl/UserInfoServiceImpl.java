package com.generalnetdisk.service.impl;

import java.util.Date;
import java.util.List;

import com.generalnetdisk.component.RedisComponent;
import com.generalnetdisk.entity.config.AppConfig;
import com.generalnetdisk.entity.constants.Constants;
import com.generalnetdisk.entity.dto.SessionWebUserDto;
import com.generalnetdisk.entity.dto.SysSettingsDto;
import com.generalnetdisk.entity.dto.UserSpaceDto;
import com.generalnetdisk.entity.po.UserInfo;
import com.generalnetdisk.entity.query.UserInfoQuery;
import com.generalnetdisk.entity.vo.PaginationResultVO;
import com.generalnetdisk.enums.UserStatusEnum;
import com.generalnetdisk.exception.BusinessException;
import com.generalnetdisk.service.EmailCodeService;
import com.generalnetdisk.service.UserInfoService;
import com.generalnetdisk.entity.query.SimplePage;
import com.generalnetdisk.enums.PageSize;
import com.generalnetdisk.mappers.UserInfoMapper;
import com.generalnetdisk.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description: 用户信息业务接口
 * @Date: 2024/07/22
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private EmailCodeService emailCodeService;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private AppConfig appConfig;

    /* 根据条件查询列表 */
    @Override
    public List<UserInfo> findListByParam(UserInfoQuery query) {
        return this.userInfoMapper.selectList(query);
    }

    /* 根据条件查询数量 */
    @Override
    public Integer findCountByParam(UserInfoQuery query) {
        return this.userInfoMapper.selectCount(query);
    }

    /* 分页查询 */
    @Override
    public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<UserInfo> list = this.findListByParam(query);
        PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /* 新增 */
    @Override
    public Integer add(UserInfo bean) {
        return this.userInfoMapper.insert(bean);
    }

    /* 批量新增 */
    @Override
    public Integer addBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoMapper.insertBatch(listBean);
    }

    /* 批量新增或修改 */
    @Override
    public Integer addOrUpdateBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoMapper.insertOrUpdateBatch(listBean);
    }

    /* 根据userId查询对象 */
    @Override
    public UserInfo getUserInfoByUserId(String userId) {
        return this.userInfoMapper.selectByUserId(userId);
    }

    /* 根据userId更新 */
    @Override
    public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
        return this.userInfoMapper.updateByUserId(bean, userId);
    }

    /* 根据userId删除 */
    @Override
    public Integer deleteUserInfoByUserId(String userId) {
        return this.userInfoMapper.deleteByUserId(userId);
    }

    /* 根据email查询对象 */
    @Override
    public UserInfo getUserInfoByEmail(String email) {
        return this.userInfoMapper.selectByEmail(email);
    }

    /* 根据email更新 */
    @Override
    public Integer updateUserInfoByEmail(UserInfo bean, String email) {
        return this.userInfoMapper.updateByEmail(bean, email);
    }

    /* 根据email删除 */
    @Override
    public Integer deleteUserInfoByEmail(String email) {
        return this.userInfoMapper.deleteByEmail(email);
    }

    /* 根据qqOpenId查询对象 */
    @Override
    public UserInfo getUserInfoByQqOpenId(String qqOpenId) {
        return this.userInfoMapper.selectByQqOpenId(qqOpenId);
    }

    /* 根据qqOpenId更新 */
    @Override
    public Integer updateUserInfoByQqOpenId(UserInfo bean, String qqOpenId) {
        return this.userInfoMapper.updateByQqOpenId(bean, qqOpenId);
    }

    /* 根据qqOpenId删除 */
    @Override
    public Integer deleteUserInfoByQqOpenId(String qqOpenId) {
        return this.userInfoMapper.deleteByQqOpenId(qqOpenId);
    }

    /* 根据nickName查询对象 */
    @Override
    public UserInfo getUserInfoByNickName(String nickName) {
        return this.userInfoMapper.selectByNickName(nickName);
    }

    /* 根据nickName更新 */
    @Override
    public Integer updateUserInfoByNickName(UserInfo bean, String nickName) {
        return this.userInfoMapper.updateByNickName(bean, nickName);
    }

    /* 根据nickName删除 */
    @Override
    public Integer deleteUserInfoByNickName(String nickName) {
        return this.userInfoMapper.deleteByNickName(nickName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String email, String nickName, String password, String emailCode) {
        UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
        if (null != userInfo) {
            throw new BusinessException("当前邮箱已存在");
        }
        UserInfo nickNameUser = this.userInfoMapper.selectByNickName(nickName);
        if (null != nickNameUser) {
            throw new BusinessException("当前昵称已被使用");
        }

        //校验邮箱验证码
        emailCodeService.checkCode(email, emailCode);

        String userId = StringTools.getRandomNumber(Constants.LENGTH_10);
        userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setNickName(nickName);
        userInfo.setEmail(email);
        userInfo.setPassword(StringTools.encodeByMd5(password));
        userInfo.setJoinTime(new Date());
        userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
        userInfo.setUserSpace(0L);
        SysSettingsDto sysSettingsDto = redisComponent.getSysSettingDto();
        userInfo.setTotalSpace(sysSettingsDto.getUserInitUseSpace() * Constants.MB);
        this.userInfoMapper.insert(userInfo);
    }

    @Override
    public SessionWebUserDto login(String email, String password) {
        UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
        if (null == userInfo || !userInfo.getPassword().equals(password)) {
            throw new BusinessException("账号或密码错误");
        }

        if (UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
            throw new BusinessException("账号已被禁用");
        }
        UserInfo updateInfo = new UserInfo();
        updateInfo.setLastLoginTime(new Date());

        this.userInfoMapper.updateByUserId(updateInfo, userInfo.getUserId());

        SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
        sessionWebUserDto.setNickName(userInfo.getNickName());
        sessionWebUserDto.setUserId(userInfo.getUserId());
        if (ArrayUtils.contains(appConfig.getAdminEmail().split(","), email)) {
            sessionWebUserDto.setAdmin(true);
        } else {
            sessionWebUserDto.setAdmin(false);
        }
        //用户空间
        UserSpaceDto userSpaceDto = new UserSpaceDto();
        //userSpaceDto.setUseSpace();
        // TODO 查询当前用户已经上传文件总大小
        userSpaceDto.setTotalSpace(userSpaceDto.getTotalSpace());
        redisComponent.saveUserSpaceUse(userInfo.getUserId(), userSpaceDto);
        return sessionWebUserDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(String email, String password, String emailCode) {
        UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
        if (null == userInfo) {
            throw new BusinessException("账号不存在");
        }
        emailCodeService.checkCode(email, emailCode);
        UserInfo updateInfo = new UserInfo();
        updateInfo.setPassword(StringTools.encodeByMd5(password));
        this.userInfoMapper.updateByEmail(updateInfo, email);
    }
}
