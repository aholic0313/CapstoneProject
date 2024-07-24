package com.generalnetdisk.controller;

import java.util.List;
import com.generalnetdisk.entity.po.EmailCode;
import com.generalnetdisk.entity.query.EmailCodeQuery;
import com.generalnetdisk.entity.vo.ResponseVO;
import com.generalnetdisk.service.EmailCodeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import javax.annotation.Resource;
/**
 * @Description: 邮箱验证码业务控制层
 *
 * @Date: 2024/07/24
 */
@RestController
@RequestMapping("/emailCode")
public class EmailCodeController extends BaseController {

	@Resource
	private EmailCodeService emailCodeService;

	/* 根据条件分页查询 */
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(EmailCodeQuery query) {
		return getSuccessResponseVO(emailCodeService.findListByPage(query));
	}

	/* 新增 */
	@RequestMapping("add")
	public ResponseVO add(EmailCode bean) {
		emailCodeService.add(bean);
		return getSuccessResponseVO(null);
	}

	/* 批量新增 */
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<EmailCode> listBean) {
		emailCodeService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/* 批量新增或修改 */
	@RequestMapping("addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<EmailCode> listBean) {
		emailCodeService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/* 根据EmailAndCode查询对象 */
	@RequestMapping("getEmailCodeByEmailAndCode")
	public ResponseVO getEmailCodeByEmailAndCode(String email, String code) {
 		return getSuccessResponseVO(emailCodeService.getEmailCodeByEmailAndCode(email, code));
	}


	/* 根据EmailAndCode修改对象 */
	@RequestMapping("updateEmailCodeByEmailAndCode")
	public ResponseVO updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code) {
 		emailCodeService.updateEmailCodeByEmailAndCode (bean,email, code);
		return getSuccessResponseVO(null);
	}


	/* 根据EmailAndCode删除对象 */
	@RequestMapping("deleteEmailCodeByEmailAndCode")
	public ResponseVO deleteEmailCodeByEmailAndCode(String email, String code) {
 		emailCodeService.deleteEmailCodeByEmailAndCode(email, code);
		return getSuccessResponseVO(null);
	}


}
