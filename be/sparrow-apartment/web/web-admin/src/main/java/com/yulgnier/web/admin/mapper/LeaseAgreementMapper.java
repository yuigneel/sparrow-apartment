package com.yulgnier.web.admin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yulgnier.model.entity.LeaseAgreement;
import com.yulgnier.web.admin.vo.agreement.AgreementQueryVo;
import com.yulgnier.web.admin.vo.agreement.AgreementVo;
import org.apache.ibatis.annotations.Param;

/**
* @author liubo
* @description 针对表【lease_agreement(租约信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.atguigu.lease.model.LeaseAgreement
*/
public interface LeaseAgreementMapper extends BaseMapper<LeaseAgreement> {
    // 分页查询租约列表 (@Param("queryVo")：指定参数别名，供Mapper XML中通过queryVo获取参数值)
    IPage<AgreementVo> pageAgreementVo(Page<AgreementVo> agreementVoPage, @Param("queryVo") AgreementQueryVo queryVo);
    /*
     * 如果 Mapper 接口方法是多参数且没有用 @Param 注解，
     * MyBatis 会默认把参数封装成一个 Map，键是 arg0、arg1... 或 param1、param2...，
     * 这时候在 XML 里写 #{参数名} 是识别不到的，必须用 #{arg0} 或 #{param1} 才行。
     * 但如果加了 @Param("a")，就能直接用 #{a}，
     * 这不仅是 "起别名方便"，更是避免参数名识别错误的关键。
     */
}




