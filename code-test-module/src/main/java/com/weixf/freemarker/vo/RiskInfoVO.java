package com.weixf.freemarker.vo;

import java.util.List;

/**
 *
 *
 * @since 2022-10-11
 */
public class RiskInfoVO {

    private String riskName;
    private String uwIdea;
    private List<RiskDutyInfoVO> riskDutyInfoVOList;
    private String payEndYear;

    public String getRiskName() {
        return riskName;
    }

    public void setRiskName(String riskName) {
        this.riskName = riskName;
    }

    public String getUwIdea() {
        return uwIdea;
    }

    public void setUwIdea(String uwIdea) {
        this.uwIdea = uwIdea;
    }

    public List<RiskDutyInfoVO> getRiskDutyInfoVOList() {
        return riskDutyInfoVOList;
    }

    public void setRiskDutyInfoVOList(List<RiskDutyInfoVO> riskDutyInfoVOList) {
        this.riskDutyInfoVOList = riskDutyInfoVOList;
    }

    public String getPayEndYear() {
        return payEndYear;
    }

    public void setPayEndYear(String payEndYear) {
        this.payEndYear = payEndYear;
    }
}
