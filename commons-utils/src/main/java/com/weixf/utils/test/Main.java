package com.weixf.utils.test;

import java.util.Base64;

/**
 *
 *
 * @since 2022-06-22
 */
public class Main {

    public static void main(String[] args) throws Exception {

        String password = "ABCDEF";
        String md5Key = "123456";
        // 核保
        // String content = "{\"body\":{\"channelType\":\"JT\",\"expandInfo\":{\"operTraceSessionId\":\"cb91a643e3b063f47950bc638a9985ca\"},\"holderInfo\":{\"holderAddress\":\"珠江逸景家园410号\",\"holderAnnualIncome\":\"10\",\"holderAreaId\":\"110101\",\"holderBirthday\":\"1994-06-20\",\"holderCardBeginDate\":\"1972-07-01\",\"holderCardEndDate\":\"2023-07-01\",\"holderCardNo\":\"440512199406208844\",\"holderCardTremType\":0,\"holderCardType\":1,\"holderCityId\":\"110100\",\"holderEmail\":\"123@qq.com\",\"holderJobCode\":\"2020202\",\"holderJobLevel\":\"\",\"holderMobile\":\"13000062395\",\"holderName\":\"核保试试\",\"holderProvinceId\":\"110000\",\"holderSex\":2,\"holderType\":1,\"isInsured\":1},\"orderTotalPayPrice\":3477,\"orderTotalPrice\":3477,\"policyList\":[{\"insuredInfoList\":[{\"benefitType\":1,\"insuredAddress\":\"珠江逸景家园410号\",\"insuredAnnualIncome\":\"10\",\"insuredAreaId\":\"110101\",\"insuredBirthday\":\"1994-06-20\",\"insuredCardBeginDate\":\"1972-07-01\",\"insuredCardEndDate\":\"2023-07-01\",\"insuredCardNo\":\"440512199406208844\",\"insuredCardTremType\":0,\"insuredCardType\":1,\"insuredCityId\":\"110100\",\"insuredEmail\":\"123@qq.com\",\"insuredHeight\":\"175\",\"insuredJobCode\":\"2020202\",\"insuredJobLevel\":\"\",\"insuredMedical\":1,\"insuredMobile\":\"13000059651\",\"insuredName\":\"核保试试\",\"insuredNum\":1,\"insuredPrice\":3477,\"insuredProvinceId\":\"110000\",\"insuredRelation\":0,\"insuredSex\":2,\"insuredType\":1,\"insuredWeight\":\"50\"}],\"policyInfo\":{\"antiMoneyLaunder\":0,\"bankNo\":\"103\",\"cardId\":\"6200596392975802192\",\"insuredNum\":1,\"policyId\":\"16566623955482100120000\",\"policyPayPrice\":3477,\"policyPrice\":3477},\"productSchemeList\":[{\"mainInsuranceFlag\":1,\"productCode\":\"B27D01\",\"productSchemeInfo\":{\"insurancePeriod\":\"10\",\"insurancePeriodType\":\"Y\",\"paymentType\":\"Y\",\"policyBeginDate\":\"2022-07-02 00:00:00\",\"policyEndDate\":\"2032-07-02 00:00:00\",\"productName\":\"华贵大麦2022定期寿险勿动\",\"schemeCode\":\"2026710686996082\",\"schemeCoverage\":100000000,\"schemeName\":\"100万\",\"schemePremium\":3477},\"renewalInfo\":{\"paymentFrequency\":\"1\",\"paymentFrequencyType\":\"M\",\"paymentPeriod\":\"5\",\"paymentPeriodType\":\"Y\"},\"riskList\":[{\"deductibleAmount\":0,\"dutyCoverage\":100000000,\"riskExplain\":\"100万\",\"riskName\":\"身故及全残\",\"riskType\":\"0\"}],\"waitPeriodDays\":90}],\"renewInfo\":{}}],\"subChannel\":\"JDJR\"},\"header\":{\"comId\":\"470002\",\"licenseTag\":\"jintou\",\"requestType\":\"P01\",\"sendTime\":1656662395554,\"uuid\":\"1656662395548210015\",\"version\":\"1.2.1.0\"}}";
        // 签单
        // String content = "{\"header\":{\"comId\":\"470002\",\"licenseTag\":\"jintou\",\"requestType\":\"P02\",\"sendTime\":1655695783647,\"uuid\":\"1655695783959130048\",\"version\":\"1.2.1.0\"},\"body\":{\"orderTotalPayPrice\":116640,\"orderTotalPrice\":116640,\"payment\":{\"payAccountId\":\"\",\"payAmount\":116640,\"payBankNo\":\"\",\"payId\":\"18650062206070933140874803295\",\"payTime\":\"2022-06-28 09:33:15\",\"payType\":1},\"policyList\":[{\"productSchemeList\":[{\"productCode\":\"012B2700\"}]}],\"proposalList\":[{\"policyId\":\"16556957839591300480000\",\"policyPayPrice\":116640,\"policyPrice\":116640,\"proposalNo\":\"120077000000120\"}]}}";
        // 退保试算
        // String content = "{\"header\":{\"version\":\"1.2.2.5\",\"requestType\":\"P101\",\"uuid\":\"1655695783959130048\",\"comId\":\"470002\",\"sendTime\":1656399353282},\"body\":{\"policyId\":\"16556957839591300480000\",\"policyNo\":\"888050001638466\",\"applyTime\":\"2022-06-28 11:11:11\"}}";
        // 续期信息查询
        // String content = "{\"header\":{\"version\":\"1.2.2.5\",\"requestType\":\"P21\",\"uuid\":\"1655695783959130048\",\"comId\":\"470002\",\"sendTime\":1656399353282},\"body\":{\"orderId\":\"1589524506153\",\"policyNo\":\"888050001638466\",\"renewalPeriod\":\"202207\"}}";
        // 退保
        // String content = "{\"header\":{\"version\":\"1.2.2.5\",\"requestType\":\"P102\",\"uuid\":\"1655695783959130048\",\"comId\":\"470002\",\"sendTime\":1656399353282},\"body\":{\"policyId\":\"16556957839591300480000\",\"policyNo\":\"888050001638466\",\"surrenderAmount\":116640,\"surrenderChannel\":\"0\",\"accountingDate \":\"2022-06-28 16:43:00\",\"surrenderApplyId\":\"123456789\",\"bankCardNo\":\"6227002190466716191\",\"bankAccountName\":\"王洪涛\",\"bankNo\":\"105\",\"surrenderLockType\":\"0\",\"antiMoneyLaunder\":0}}";
        // 人员要素查询
        // String content = "{\"header\":{\"version\":\"1.2.2.5\",\"requestType\":\"P142\",\"uuid\":\"1655695783959130048\",\"comId\":\"470002\",\"sendTime\":1655695783647},\"body\":{\"policyNo\":\"120077000000120\",\"policyId\":\"16556957839591300480000\",\"businessType\":1}}";

        // 回访同步
        String content = "{\"body\":{\"returnTime\":\"2022-07-21 14:11:48\",\"policyNo\":\"888050001638518\",\"orderId\":\"1640425963071131067\",\"returnType\":1},\"header\":{\"sendTime\":1658393400259,\"comId\":\"470002\",\"requestType\":\"P08\",\"uuid\":\"1640425963071131067\",\"version\":\"1.2.1.0\"}}";

        String base64String = Base64.getEncoder().encodeToString(content.getBytes());
        System.out.println(base64String);

        String aesString = AesUtil.encrypt(base64String, password);
        String signString = Md5Util.Md5(base64String + md5Key);
        System.out.println("参数：" + aesString);
        System.out.println("签名：" + signString);
        // d90774b81a859d37bad08516bc4970b7
        // d90774b81a859d37bad08516bc4970b7
        // Map<String, Object> paramsMap = new HashMap<>();
        // paramsMap.put("data", aesString);
        // paramsMap.put("sign", signString);
        // String params = JSON.toJSONString(paramsMap);
        // System.out.println("请求参数：" + params);
        // 核保
        // String url = "https://dscapitest.huaguilife.cn/dsc/thirdSepc/i/5055/tjjtInsure";
        // 签单
        // String url = "https://dscapitest.huaguilife.cn/dsc/thirdSepc/i/5055/tjjtSign";
        // 退保试算
        // String url = "https://dscapitest.huaguilife.cn/dsc/thirdSepc/i/5055/tjjtEdorTrial";
        // 退保
        // String url = "https://dscapitest.huaguilife.cn/dsc/thirdSepc/i/5055/tjjtEdorCT";
        // 续期扣费结果通知
        // String url = "https://dscapitest.huaguilife.cn/dsc/thirdSepc/i/5055/tjjtRePayResultNotice";
        // 续期申请
        // String url = "https://dscapitest.huaguilife.cn/dsc/thirdSepc/i/5055/tjjtReApply";
        // 续期查询
        // String url = "https://dscapitest.huaguilife.cn/dsc/thirdSepc/i/5055/tjjtQueryReInfo";
        // 撤单
        // String url = "https://dscapitest.huaguilife.cn/dsc/thirdSepc/i/5055/tjjtRemove";
        // 人员要素查询
        // String url = "https://dscapitest.huaguilife.cn/dsc/tjjt/o/5055/tjjtQueryCerInfo";

        // long start = System.currentTimeMillis();
        // // 发请求
        // String result = HttpUtils.postFromJson(url, params);
        // long end = System.currentTimeMillis();
        // System.out.println("消耗时间：" + (end - start) + "ms");
        //
        //
        // JSONObject jsonObject = JSON.parseObject(result);
        // System.out.println("响应参数：" + jsonObject);
        // if (jsonObject != null) {
        //     String data = (String) jsonObject.get("data");
        //     String decrypt = AesUtil.decrypt(data, password);
        //     byte[] decode = Base64.getDecoder().decode(decrypt);
        //     String out = new String(decode);
        //     System.out.println("解密后：" + out);
        // }
    }
}
