package com.generalnetdisk.service.impl;

import java.util.List;
import com.generalnetdisk.entity.po.UserInfo;
import com.generalnetdisk.entity.query.UserInfoQuery;
import com.generalnetdisk.entity.vo.PaginationResultVO;
import com.generalnetdisk.service.UserInfoService;
import com.generalnetdisk.entity.query.SimplePage;
import com.generalnetdisk.enums.PageSize;
import com.generalnetdisk.mappers.UserInfoMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
 * @Description: 用户信息业务接口
 *
 * @Date: 2024/07/24
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	@Resource
	private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;

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

		SimplePage page = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<UserInfo> list = this.findListByParam(query);
		PaginationResultVO<UserInfo> result = new PaginationResultVO(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);
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
		if (listBean == null || listBean.isEmpty()){
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}

	/* 批量新增或修改 */
	@Override
	public Integer addOrUpdateBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()){
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
	public Integer updateUserInfoByUserId(UserInfo bean,String userId) {
		return this.userInfoMapper.updateByUserId(bean,userId);
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
	public Integer updateUserInfoByEmail(UserInfo bean,String email) {
		return this.userInfoMapper.updateByEmail(bean,email);
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
	public Integer updateUserInfoByQqOpenId(UserInfo bean,String qqOpenId) {
		return this.userInfoMapper.updateByQqOpenId(bean,qqOpenId);
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
	public Integer updateUserInfoByNickName(UserInfo bean,String nickName) {
		return this.userInfoMapper.updateByNickName(bean,nickName);
	}

	/* 根据nickName删除 */
	@Override
	public Integer deleteUserInfoByNickName(String nickName) {
		return this.userInfoMapper.deleteByNickName(nickName);
	}

}
