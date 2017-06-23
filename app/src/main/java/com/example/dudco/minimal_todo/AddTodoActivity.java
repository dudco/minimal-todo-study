package com.example.dudco.minimal_todo;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddTodoActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    SwitchCompat mSwitch;
    LinearLayout dateSet;
    Toolbar toolbar;

    EditText dateEdit;
    EditText timeEdit;
    TextView reminderDateText;
    EditText titleEdit;

    Date mUserReminderDate;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        mUserReminderDate = new Date();
        toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);

        dateEdit = (EditText) findViewById(R.id.newTodoDateEditText);
        timeEdit = (EditText) findViewById(R.id.newTodoTimeEditText);

        titleEdit = (EditText) findViewById(R.id.userToDoEditText);
        fab = (FloatingActionButton) findViewById(R.id.add_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra("date", mUserReminderDate.getTime());
                intent.putExtra("title", titleEdit.getText().toString());

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                if(mUserReminderDate != null){
                    cal.setTime(mUserReminderDate);
                }
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddTodoActivity.this,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                if(mUserReminderDate != null){
                    cal.setTime(mUserReminderDate);
                }

                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AddTodoActivity.this,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        cal.get(Calendar.SECOND),
                        false
                );
                tpd.show(getFragmentManager(), "TimepickerDialog");
            }
        });

        final Drawable cross = ContextCompat.getDrawable(this, R.drawable.ic_clear_white_24dp);
        if(cross !=null){
            cross.setColorFilter(ContextCompat.getColor(this, R.color.icons), PorterDuff.Mode.SRC_ATOP);
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(cross);
        }

        dateSet = (LinearLayout) findViewById(R.id.date_set);
        mSwitch = (SwitchCompat) findViewById(R.id.toDoHasDateSwitchCompat);
        reminderDateText = (TextView) findViewById(R.id.newToDoDateTimeReminderTextView);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                animateDateSet(b);

            }
        });

        dateSet.setVisibility((mSwitch.isChecked() ? View.VISIBLE : View.INVISIBLE));
    }

    private void setTime(int hour, int minute){
        Calendar cal = Calendar.getInstance();

        if(mUserReminderDate != null){
            cal.setTime(mUserReminderDate);
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        cal.set(year, month, day, hour, minute);
        mUserReminderDate = cal.getTime();
        setTime2Edit(mUserReminderDate);
        setReminderDateText(mUserReminderDate);
    }

    private void setDate(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        if(mUserReminderDate != null){
            cal.setTime(mUserReminderDate);
        }

        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);

        cal.set(year, month, day, hour, minute);
        mUserReminderDate = cal.getTime();
        setDate2Edit(mUserReminderDate);
        setReminderDateText(mUserReminderDate);
    }

    private void setDate2Edit(Date date){
        String dateFormat = "d MMM, yyyy";
        dateEdit.setText(formatDate(dateFormat, date));
        Log.d("dudco", date.getTime()+"");
    }

    private void setTime2Edit(Date date){
        String dateFormat = "h:mm a";
        Calendar cal = Calendar.getInstance();
        timeEdit.setText(formatDate(dateFormat, date));
        Log.d("dudco", formatDate("yyyy-MMM-dd h:mm a", date));
    }

    private void setReminderDateText(Date date){
        String dateFormat = "d MMM, yyyy, h:mm a";
        reminderDateText.setText("Reminder set for " + formatDate(dateFormat, date));
    }

    static public String formatDate(String format, Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                setResult(RESULT_CANCELED);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void animateDateSet(boolean isChecked){
        if(isChecked){
            setDate2Edit(new Date());
            setTime2Edit(new Date());
            setReminderDateText(new Date());
//            reminderDateText.setText("Reminder set for" + getDate(new Date()) + ", " + getTime(new Date()));

            dateSet.animate().alpha(1.0f).setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    dateSet.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }else{
            dateSet.animate().alpha(0.0f).setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    dateSet.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String sDate = +dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        setDate(year,monthOfYear, dayOfMonth);

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = hourOfDay+":"+minute+":"+second;
        setTime(hourOfDay, minute);
    }
}
