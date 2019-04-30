package com.example.mytimeapplication.activity;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

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
    private Button modifyTitleOK;
    private Button modifyDuration;
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
        modifyTitleOK = view.findViewById(R.id.modify_text_ok);
        modifyDuration = view.findViewById(R.id.modify_duration);
        titleText.setText(record.getTitle());


        modifyStartDate.setOnClickListener(this);
        modifyStartTime.setOnClickListener(this);
        modifyStopDate.setOnClickListener(this);
        modifyStopTime.setOnClickListener(this);
        modifyTitleOK.setOnClickListener(this);
        modifyDuration.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v){
        calendar = Calendar.getInstance();
        switch (v.getId()){
            //更改开始日期
            case R.id.modify_start_date :{
                modifyDate(Constant.START);
                break;
            }
            //更改开始时间
            case R.id.modify_start_time :{
                modifyTime(Constant.START);
                break;
            }
            //更改结束日期
            case R.id.modify_stop_date :{
                modifyDate(Constant.STOP);
                break;
            }
            //更改结束时间
            case R.id.modify_stop_time :{
                modifyTime(Constant.STOP);
                break;
            }
            //确定修改标题
            case R.id.modify_text_ok:{
                modifyTitle();
            }

        }
    }

    /**
     * 修改日期
     */
    private void modifyDate(final int startOrStop){
        //获得修改前的日期
        if(startOrStop == Constant.START)
            calendar.setTimeInMillis(record.getStartTime());
        else if (startOrStop == Constant.STOP)
            calendar.setTimeInMillis(record.getStopTime());

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        //弹出DatePickerDialog
        //注意第一个参数得是Activity哦
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //获取新的日期
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        long newTime = calendar.getTimeInMillis();
                        //创建/获取数据库
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(MyApplication.getContext(), Constant.DB_NAME, null, 1);
                        db = dbHelper.getWritableDatabase();   //检测有没有该名字的数据库，若没有则创建，同时调用dbHelper 的 onCreate 方法；
                        //修改日期
                        if(startOrStop == Constant.START)
                            dbHelper.updateStartTime(db,record.getId(),newTime);
                        else
                            dbHelper.updateStopTime(db,record.getId(),newTime);
                        //更新ui
                        HistoryFragment.getData();
                    }
                },
                mYear, mMonth, mDay);
        if(!getActivity().isFinishing())
        {
            datePickerDialog.show();
        }
    }

    /**
     * 修改时间
     */
    private void modifyTime(final int startOrStop){
        //获得修改前的时间
        if(startOrStop == Constant.START)
            calendar.setTimeInMillis(record.getStartTime());
        else if (startOrStop == Constant.STOP)
            calendar.setTimeInMillis(record.getStopTime());

        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        //弹出timePickerDialog
        //注意第一个参数得是Activity哦
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        //获取新的时间
                        calendar.set(Calendar.HOUR_OF_DAY,hour);
                        calendar.set(Calendar.MINUTE,minute);
                        long newTime = calendar.getTimeInMillis();
                        //创建/获取数据库
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(MyApplication.getContext(), Constant.DB_NAME, null, 1);
                        db = dbHelper.getWritableDatabase();   //检测有没有该名字的数据库，若没有则创建，同时调用dbHelper 的 onCreate 方法；
                        //在数据库中修改日期
                        if(startOrStop == Constant.START)
                        {
                            dbHelper.updateStartTime(db,record.getId(),newTime);
                            record.setStartTime(newTime);
                        }
                        else
                        {
                            dbHelper.updateStopTime(db,record.getId(),newTime);
                            record.setStopTime((newTime));
                        }

                        //在list中修改时间
                        HistoryFragment.getData();
                    }
                },
                mHour, mMinute,true);
        if(!getActivity().isFinishing())
            timePickerDialog.show();
    }
    private void modifyTitle(){
        //获取新的标题
        String newTitle = titleText.getText().toString();

        //创建/获取数据库
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(MyApplication.getContext(), Constant.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();   //检测有没有该名字的数据库，若没有则创建，同时调用dbHelper 的 onCreate 方法；
        //在数据库中修改标题
        dbHelper.updateTitle(db,record.getId(),newTitle);
        //在list中更新标题
        record.setTitle(newTitle);
        HistoryFragment.getList().remove(itemPosition);
        HistoryFragment.getList().add(itemPosition,record);
        HistoryFragment.getAdapter().notifyDataSetChanged();
    }
}