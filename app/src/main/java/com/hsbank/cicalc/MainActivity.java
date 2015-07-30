package com.hsbank.cicalc;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.Button;
import com.hsbank.cicalc.utils.DateUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;


public class MainActivity extends ActionBarActivity {

    @Bind(R.id.startDate) EditText strStartDate;
    @Bind(R.id.oughtDate) EditText strOughtDate;
    @Bind(R.id.actualDate)EditText strActualDate;
    @Bind(R.id.amount) EditText amount;
    @Bind(R.id.annualRate) EditText annualRate;
    @Bind(R.id.punishDayRate) EditText punishDayRate;
    @Bind(R.id.txtCalcResult) TextView txtCalcResult;
    @Bind(R.id.btnCalc) Button btnCalc;
    Calendar calendar;
    Date startDate,oughtDate,actualDate;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        btnCalc.setFocusable(true);
        btnCalc.requestFocus();
        btnCalc.setFocusableInTouchMode(true);
        calendar = Calendar.getInstance();
    }

    @OnFocusChange(R.id.startDate)
    void pickStartDate(boolean focused) {
        if (focused) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this, DateSet, calendar
                    .get(Calendar.YEAR), calendar
                    .get(Calendar.MONTH), calendar
                    .get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }

    @OnFocusChange(R.id.oughtDate)
    void pickOughtDate(boolean focused) {
        if (focused) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this, DateSet, calendar
                    .get(Calendar.YEAR), calendar
                    .get(Calendar.MONTH), calendar
                    .get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }

    @OnFocusChange(R.id.actualDate)
    void pickActualDate(boolean focused) {
        if (focused) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this, DateSet, calendar
                    .get(Calendar.YEAR), calendar
                    .get(Calendar.MONTH), calendar
                    .get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }

    @OnClick(R.id.btnCalc)
    void calc() {
        Toast.makeText(this, String.valueOf(DateUtil.getIntervalDays(startDate,oughtDate)), Toast.LENGTH_LONG).show();   
    }

    /**
     * @description 日期设置匿名类
     */
    DatePickerDialog.OnDateSetListener DateSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // 每次保存设置的日期
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
