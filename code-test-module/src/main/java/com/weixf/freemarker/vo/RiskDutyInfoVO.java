package com.weixf.freemarker.vo;

/**
 *
 *
 * @since 2022-10-11
 */
public class RiskDutyInfoVO {

    private String riskDutyName;
    private String insuranceAmount;
    private String standPrem;
    private String addPrem;
    private String quotaAmount;
    private String finalPrem;

    public String getRiskDutyName() {
        return riskDutyName;
    }

    public void setRiskDutyName(String riskDutyName) {
        this.riskDutyName = riskDutyName;
    }

    public String getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(String insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getStandPrem() {
        return standPrem;
    }

    public void setStandPrem(String standPrem) {
        this.standPrem = standPrem;
    }

    public String getAddPrem() {
        return addPrem;
    }

    public void setAddPrem(String addPrem) {
        this.addPrem = addPrem;
    }

    public String getQuotaAmount() {
        return quotaAmount;
    }

    public void setQuotaAmount(String quotaAmount) {
        this.quotaAmount = quotaAmount;
    }

    public String getFinalPrem() {
        return finalPrem;
    }

    public void setFinalPrem(String finalPrem) {
        this.finalPrem = finalPrem;
    }
}
