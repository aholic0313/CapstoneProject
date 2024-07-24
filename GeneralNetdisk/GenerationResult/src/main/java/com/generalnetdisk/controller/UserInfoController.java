package com.generalnetdisk.controller;

import java.util.List;
import com.generalnetdisk.entity.po.UserInfo;
import com.generalnetdisk.entity.query.UserInfoQuery;
import com.generalnetdisk.entity.vo.ResponseVO;
import com.generalnetdisk.service.UserInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import javax.annotation.Resource;
/**
 * @Description: 用户信息业务控制层
 *
 * @Date: 2024/07/24
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController {

	@Resource
	private UserInfoService userInfoService;

	/* 根据条件分页查询 */
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(UserInfoQuery query) {
		return getSuccessResponseVO(userInfoService.findListByPage(query));
	}

	/* 新增 */
	@RequestMapping("add")
	public ResponseVO add(UserInfo bean) {
		userInfoService.add(bean);
		return getSuccessResponseVO(null);
	}

	/* 批量新增 */
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<UserInfo> listBean) {
		userInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/* 批量新增或修改 */
	@RequestMapping("addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<UserInfo> listBean) {
		userInfoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/* 根据UserId查询对象 */
	@RequestMapping("getUserInfoByUserId")
	public ResponseVO getUserInfoByUserId(String userId) {
 		return getSuccessResponseVO(userInfoService.getUserInfoByUserId(userId));
	}


	/* 根据UserId修改对象 */
	@RequestMapping("updateUserInfoByUserId")
	public ResponseVO updateUserInfoByUserId(UserInfo bean, String userId) {
 		userInfoService.updateUserInfoByUserId (bean,userId);
		return getSuccessResponseVO(null);
	}


	/* 根据UserId删除对象 */
	@RequestMapping("deleteUserInfoByUserId")
	public ResponseVO deleteUserInfoByUserId(String userId) {
 		userInfoService.deleteUserInfoByUserId(userId);
		return getSuccessResponseVO(null);
	}


	/* 根据Email查询对象 */
	@RequestMapping("getUserInfoByEmail")
	public ResponseVO getUserInfoByEmail(String email) {
 		return getSuccessResponseVO(userInfoService.getUserInfoByEmail(email));
	}


	/* 根据Email修改对象 */
	@RequestMapping("updateUserInfoByEmail")
	public ResponseVO updateUserInfoByEmail(UserInfo bean, String email) {
 		userInfoService.updateUserInfoByEmail (bean,email);
		return getSuccessResponseVO(null);
	}


	/* 根据Email删除对象 */
	@RequestMapping("deleteUserInfoByEmail")
	public ResponseVO deleteUserInfoByEmail(String email) {
 		userInfoService.deleteUserInfoByEmail(email);
		return getSuccessResponseVO(null);
	}


	/* 根据QqOpenId查询对象 */
	@RequestMapping("getUserInfoByQqOpenId")
	public ResponseVO getUserInfoByQqOpenId(String qqOpenId) {
 		return getSuccessResponseVO(userInfoService.getUserInfoByQqOpenId(qqOpenId));
	}


	/* 根据QqOpenId修改对象 */
	@RequestMapping("updateUserInfoByQqOpenId")
	public ResponseVO updateUserInfoByQqOpenId(UserInfo bean, String qqOpenId) {
 		userInfoService.updateUserInfoByQqOpenId (bean,qqOpenId);
		return getSuccessResponseVO(null);
	}


	/* 根据QqOpenId删除对象 */
	@RequestMapping("deleteUserInfoByQqOpenId")
	public ResponseVO deleteUserInfoByQqOpenId(String qqOpenId) {
 		userInfoService.deleteUserInfoByQqOpenId(qqOpenId);
		return getSuccessResponseVO(null);
	}


	/* 根据NickName查询对象 */
	@RequestMapping("getUserInfoByNickName")
	public ResponseVO getUserInfoByNickName(String nickName) {
 		return getSuccessResponseVO(userInfoService.getUserInfoByNickName(nickName));
	}


	/* 根据NickName修改对象 */
	@RequestMapping("updateUserInfoByNickName")
	public ResponseVO updateUserInfoByNickName(UserInfo bean, String nickName) {
 		userInfoService.updateUserInfoByNickName (bean,nickName);
		return getSuccessResponseVO(null);
	}


	/* 根据NickName删除对象 */
	@RequestMapping("deleteUserInfoByNickName")
	public ResponseVO deleteUserInfoByNickName(String nickName) {
 		userInfoService.deleteUserInfoByNickName(nickName);
		return getSuccessResponseVO(null);
	}


}
