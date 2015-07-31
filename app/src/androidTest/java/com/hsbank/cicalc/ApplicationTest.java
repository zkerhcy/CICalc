package com.hsbank.cicalc;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.hsbank.cicalc.common.Constants;
import com.hsbank.cicalc.utils.DateUtils;
import com.hsbank.cicalc.utils.RateUtil;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public static final String TAG = ApplicationTest.class.getSimpleName();
    public ApplicationTest() {
        super(Application.class);
    }

    public void testNormalInterests() throws Exception {
        Double result1 = RateUtil.normalInterests(10000d, DateUtils.StringToDate("2015-07-21"), DateUtils.StringToDate("2015-07-30"), Constants.APR);
        Double result2 = RateUtil.overdueInterests(10000d, DateUtils.StringToDate("2015-07-21"), DateUtils.StringToDate("2015-07-25"), DateUtils.StringToDate("2015-07-30"), Constants.APR);
        Log.i(TAG, "normalInterest:" + String.valueOf(result1));
        Log.i(TAG, "overdueInterests:" + String.valueOf(result2));
    }
}