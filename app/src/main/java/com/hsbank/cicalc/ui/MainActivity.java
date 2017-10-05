package com.hsbank.cicalc.ui;

import android.content.Context;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.hsbank.cicalc.R;
import com.hsbank.cicalc.common.Constants;
import com.hsbank.cicalc.utils.AppUtils;
import com.hsbank.cicalc.utils.RateUtil;
import com.hsbank.cicalc.utils.T;
import com.umeng.analytics.MobclickAgent;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context context_;

    @Bind(R.id.startDate) EditText strStartDate;
    @Bind(R.id.oughtDate) EditText strOughtDate;
    @Bind(R.id.actualDate) EditText strActualDate;
    @Bind(R.id.amount) EditText amount;
    @Bind(R.id.annualRate) EditText annualRate;
    @Bind(R.id.punishDayRate) EditText punishDayRate;
    @Bind(R.id.txtCalcResult) TextView txtCalcResult;
    @Bind(R.id.btnCalc) Button btnCalc;
    Calendar calendar;
    Date startDate, oughtDate, actualDate;
    Double loanAmount;
    Double apr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context_ = this;
        ButterKnife.bind(this);
        initViews();
        initValues();

//        MobclickAgent.updateOnlineConfig(context_);
//        /** 设置是否对日志信息进行加密, 默认false(不加密). */
//        AnalyticsConfig.enableEncrypt(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initViews() {
        btnCalc.setFocusable(true);
        btnCalc.requestFocus();
        btnCalc.setFocusableInTouchMode(true);
        calendar = Calendar.getInstance();
    }

    private void initValues() {
        annualRate.setText(String.valueOf(Constants.APR));
    }

    @OnFocusChange(R.id.startDate)
    void pickStartDate(boolean focused) {
        if (focused) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, DateSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }

    @OnFocusChange(R.id.oughtDate)
    void pickOughtDate(boolean focused) {
        if (focused) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, DateSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }

    @OnFocusChange(R.id.actualDate)
    void pickActualDate(boolean focused) {
        if (focused) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, DateSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }

    @OnClick(R.id.btnCalc)
    void calc() {
        if (TextUtils.isEmpty(strStartDate.getText())) {
            T.showShort(context_, R.string.msg_start_date_empty);
            return;
        }
        if (TextUtils.isEmpty(strOughtDate.getText())) {
            T.showShort(context_, R.string.msg_ought_date_empty);
            return;
        }
        if (TextUtils.isEmpty(strActualDate.getText())) {
            T.showShort(context_, R.string.msg_actual_date_empty);
            return;
        }
        if (startDate.getTime() > oughtDate.getTime()) {
            T.showShort(context_, R.string.msg_ought_less_than_start);
            return;
        }
        if (startDate.getTime() > actualDate.getTime()) {
            T.showShort(context_, R.string.msg_actual_less_than_start);
            return;
        }
        if (TextUtils.isEmpty(amount.getText())) {
            T.showShort(context_, R.string.msg_loan_amount_empty);
            return;
        }
        if (TextUtils.isEmpty(annualRate.getText())) {
            T.showShort(context_, R.string.msg_annual_rate_empty);
            return;
        }
        loanAmount = Double.valueOf(amount.getText().toString().trim());
        apr = Double.valueOf(annualRate.getText().toString().trim());
        if (loanAmount <= 0) {
            T.showShort(context_, R.string.msg_loan_amount_error);
            return;
        }
        calcRate();

    }

    /**
     * 计算实际利息
     */
    private void calcRate() {
        Double rate;
        //实际还息日期小于应还日期，正常计息
        if (actualDate.getTime() <= oughtDate.getTime()) {
            rate = RateUtil.normalInterests(loanAmount, startDate, actualDate, apr);
        } else {
            rate = RateUtil.overdueInterests(loanAmount, startDate, oughtDate, actualDate, apr);
        }
        if (null != rate) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context_);
            builder.setTitle(R.string.calcResult);
            builder.setMessage(getString(R.string.txtCalcResult, rate));
            AlertDialog ad = builder.create();
            ad.show();
        }
    }

    /**
     * @description 日期设置匿名类
     */
    DatePickerDialog.OnDateSetListener DateSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // 每次保存设置的日期
            calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);

            String str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            System.out.println("set is " + str);

            if (strStartDate.isFocused()) {
                strStartDate.setText(str);
                startDate = calendar.getTime();
                btnCalc.requestFocus();
            }
            if (strOughtDate.isFocused()) {
                strOughtDate.setText(str);
                oughtDate = calendar.getTime();
                btnCalc.requestFocus();
            }
            if (strActualDate.isFocused()) {
                strActualDate.setText(str);
                actualDate = calendar.getTime();
                btnCalc.requestFocus();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context_);
            builder.setTitle(getString(R.string.str_app_version, AppUtils.getVersionName(context_)));
            builder.setMessage(R.string.str_tech_support);
            builder.setPositiveButton(R.string.str_confirm, null);
            AlertDialog ad = builder.create();
            ad.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
