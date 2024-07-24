package com.generalnetdisk.service;

import java.util.List;

import com.generalnetdisk.entity.po.EmailCode;
import com.generalnetdisk.entity.query.EmailCodeQuery;
import com.generalnetdisk.entity.vo.PaginationResultVO;

/**
 * @Description: 邮箱验证码业务接口
 * @Date: 2024/07/24
 */
public interface EmailCodeService {

    /* 根据条件查询列表 */
    List<EmailCode> findListByParam(EmailCodeQuery query);

    /* 根据条件查询数量 */
    Integer findCountByParam(EmailCodeQuery query);

    /* 分页查询 */
    PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery query);

    /* 新增 */
    Integer add(EmailCode bean);

    /* 批量新增 */
    Integer addBatch(List<EmailCode> listBean);

    /* 批量新增或修改 */
    Integer addOrUpdateBatch(List<EmailCode> listBean);

    /* 根据EmailAndCode查询对象 */
    EmailCode getEmailCodeByEmailAndCode(String email, String code);

    /* 根据EmailAndCode更新 */
    Integer updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code);

    /* 根据EmailAndCode删除 */
    Integer deleteEmailCodeByEmailAndCode(String email, String code);

    void sendEmailCode(String email, Integer type);
}
