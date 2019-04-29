package com.example.mytimeapplication.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.example.mytimeapplication.MyApplication;
import com.example.mytimeapplication.R;
import com.example.mytimeapplication.bean.Record;
import com.example.mytimeapplication.constant.Constant;
import com.example.mytimeapplication.db.MyDatabaseHelper;

import java.util.Calendar;

public class UpdateRecordDialog extends DialogFragment implements View.OnClickListener{
    //控件
    private EditText titleText;
    private Button modifyStartDate;
    private Button modifyStartTime;
    private Button modifyStopDate;
    private Button modifyStopTime;
    //变量
    Calendar calendar;
    private Record record;
    private int itemPosition;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    //数据库
    private static SQLiteDatabase db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //设置标题
        //getDialog().setTitle("title");
        //获取要更改的record对象
        Bundle bundle = getArguments();
        record = (Record)bundle.getSerializable("record");
        itemPosition = (int) bundle.getInt("itemPosition");
        View view=inflater.inflate(R.layout.fragment_update_record_dialog, container);

        titleText = view.findViewById(R.id.title_text);
        modifyStartDate = view.findViewById(R.id.modify_start_date);
        modifyStartTime = view.findViewById(R.id.modify_start_time);
        modifyStopDate = view.findViewById(R.id.modify_stop_date);
        modifyStopTime = view.findViewById(R.id.modify_stop_time);

        titleText.setText(record.getTitle());


        modifyStartDate.setOnClickListener(this);
        modifyStartTime.setOnClickListener(this);
        modifyStopDate.setOnClickListener(this);
        modifyStopTime.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v){
        calendar = Calendar.getInstance();
        switch (v.getId()){
            //更改开始日期
            case R.id.modify_start_date :{
                onClickModifyStartDate();
                break;
            }
            //更改开始时间
            case R.id.modify_start_time :{
                onClickModifyStartTime();
                break;
            }
            //更改结束时间
            case R.id.modify_stop_time :{
                onClickModifyStopTime();
                break;
            }
        }
    }

    /**
     * 修改日期
     */
    private void onClickModifyStartDate(){
        calendar.setTimeInMillis(record.getStartTime());
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        //注意第一个参数得是Activity哦
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //获取新的日期
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        long newStartTime = calendar.getTimeInMillis();
                        //创建/获取数据库
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(MyApplication.getContext(), Constant.DB_NAME, null, 1);
                        db = dbHelper.getWritableDatabase();   //检测有没有该名字的数据库，若没有则创建，同时调用dbHelper 的 onCreate 方法；
                        //修改日期
                        dbHelper.updateStartTime(db,record.getId(),newStartTime);
                    }
                },
                mYear, mMonth, mDay);
        if(!getActivity().isFinishing())
        {
            datePickerDialog.show();
        }
    }

    /**
     * 修改开始时间
     */
    private void onClickModifyStartTime(){
        calendar.setTimeInMillis(record.getStartTime());
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        //注意第一个参数得是Activity哦
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        //获取新的时间
                        calendar.set(Calendar.HOUR_OF_DAY,hour);
                        calendar.set(Calendar.MINUTE,minute);
                        long newStartTime = calendar.getTimeInMillis();
                        //创建/获取数据库
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(MyApplication.getContext(), Constant.DB_NAME, null, 1);
                        db = dbHelper.getWritableDatabase();   //检测有没有该名字的数据库，若没有则创建，同时调用dbHelper 的 onCreate 方法；
                        //在数据库中修改日期
                        dbHelper.updateStartTime(db,record.getId(),newStartTime);
                        //
                        //在list中修改时间
                        record.setStartTime(newStartTime);
                        HistoryFragment.getList().remove(itemPosition);
                        HistoryFragment.getList().add(itemPosition,record);
                        HistoryFragment.getAdapter().notifyDataSetChanged();
                    }
                },
                mHour, mMinute,true);
        if(!getActivity().isFinishing())
            timePickerDialog.show();
    }

    /**
     * 修改结束时间
     */
    private void onClickModifyStopTime(){
        calendar.setTimeInMillis(record.getStopTime());
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        //注意第一个参数得是Activity哦
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        //获取新的时间
                        calendar.set(Calendar.HOUR_OF_DAY,hour);
                        calendar.set(Calendar.MINUTE,minute);
                        long newStopTime = calendar.getTimeInMillis();
                        //创建/获取数据库
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(MyApplication.getContext(), Constant.DB_NAME, null, 1);
                        db = dbHelper.getWritableDatabase();   //检测有没有该名字的数据库，若没有则创建，同时调用dbHelper 的 onCreate 方法；
                        //在数据库中修改日期
                        dbHelper.updateStopTime(db,record.getId(),newStopTime);
                        //
                        //在list中修改时间
                        record.setStopTime(newStopTime);
                        HistoryFragment.getList().remove(itemPosition);
                        HistoryFragment.getList().add(itemPosition,record);
                        HistoryFragment.getAdapter().notifyDataSetChanged();
                    }
                },
                mHour, mMinute,true);
        if(!getActivity().isFinishing())
            timePickerDialog.show();
    }

}