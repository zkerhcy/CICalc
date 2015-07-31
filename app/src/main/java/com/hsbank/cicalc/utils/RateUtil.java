package com.hsbank.cicalc.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 利率工具类
 * Created by chenzhao on 15-7-31.
 */
public class RateUtil {
    public static final String TAG = RateUtil.class.getSimpleName();

    /**
     * 正常时间还款利息（应还日期前还款，不计复利）
     *
     * @param loanAmount 借款金额
     * @param startDate  借款日期
     * @param actualDate 实际还款日期
     * @param apr        年利率(%)
     * @return
     */
    public static Double normalInterests(Double loanAmount, Date startDate, Date actualDate, double apr) {
        int days;
        double interests = 0.00;
        try {
            days = DateUtils.getIntervalDays(startDate, actualDate) + 1;
            interests = loanAmount * days * ((apr / 100) / 360);//应还利息=借款金额×天数×正常日利率
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Calc normal interests error" + e);
        }
        return keepTwoDecimalPlaces(interests);
    }

    /**
     * 逾期还款利息（逾期天数计日复利）
     *
     * @param loanAmount 借款金额
     * @param startDate  借款日期
     * @param oughtDate  应还款日期
     * @param actualDate 实际还款日期
     * @param apr        年利率(%)
     * @return
     */
    public static Double overdueInterests(Double loanAmount, Date startDate, Date oughtDate, Date actualDate, Double apr) {
        int overdueDays;//逾期天数
        double interests, overdueInterests = loanAmount;
        double pdr = 1.5 * (apr / 100) / 360;//罚息日利率
        overdueDays = DateUtils.getIntervalDays(oughtDate, actualDate);
        interests = normalInterests(loanAmount, startDate, oughtDate, apr); //正常利息部分
        for (int i = 0; i < overdueDays; i++) {
            overdueInterests *= (1 + pdr);
        }
        overdueInterests = overdueInterests - loanAmount;//逾期部分利息
        interests += overdueInterests;
        return keepTwoDecimalPlaces(interests);
    }

    public static Double keepTwoDecimalPlaces(double f) {
        DecimalFormat df = new DecimalFormat(".##");
        f = Double.valueOf(df.format(f));
        return f;
    }
}
