package com.example.mytimeapplication.activity;

import android.database.ContentObservable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;


import com.example.mytimeapplication.MyApplication;
import com.example.mytimeapplication.R;
import com.example.mytimeapplication.adapter.RecordAdapter;
import com.example.mytimeapplication.bean.Record;
import com.example.mytimeapplication.constant.Constant;
import com.example.mytimeapplication.db.MyDatabaseHelper;
import com.example.mytimeapplication.util.DateTimeUtil;


import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";

    //存储列表数据
    static List<Record> list = new ArrayList<>();
    static RecordAdapter adapter;

    //database
    private static MyDatabaseHelper dbHelper;
    private static SQLiteDatabase db;

    //当前选择的日期
    private static String currentDate;



    public static RecordAdapter getAdapter() {
        return adapter;
    }

    public static List<Record> getList(){return list;}

    public static Fragment newInstance(){
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        //创建/获取数据库
        dbHelper = new MyDatabaseHelper(MyApplication.getContext(), Constant.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();   //检测有没有该名字的数据库，若没有则创建，同时调用dbHelper 的 onCreate 方法；
        currentDate = DateTimeUtil.timestamp2ymd(System.currentTimeMillis());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,null);
        //time line
        RecyclerView recyclerView = view.findViewById(R.id.timeline_recycler_view);

        // recyclerview绑定适配器
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        adapter = new RecordAdapter(list,getActivity());
        recyclerView.setAdapter(adapter);


        //初始化数据
        getData();

        //Calendar View 选择日期
        final CalendarView calendarView = view.findViewById(R.id.calendar_view);

        currentDate = DateTimeUtil.timestamp2ymd(calendarView.getDate());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                currentDate = "" + year + "-";
                if(month<10) currentDate +="0";
                currentDate += ""+ (month+1) +"-";  //month 范围是0-11
                if(dayOfMonth<10) currentDate+="0";
                currentDate +=dayOfMonth;

                Log.d(TAG, "onSelectedDayChange: currentDate: " + currentDate);
                Log.d(TAG, "onSelectedDayChange: year month day : "+ year + "-" + month +"-"+dayOfMonth);
                getData();

            }
        });

        //隐藏日历
        final Button button = view.findViewById(R.id.show_hide_calendar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏
                if(calendarView.getVisibility() == View.VISIBLE)
                {
                    calendarView.setVisibility(View.GONE);
                    button.setText("显示日历");
                }
                //显示
                else if (calendarView.getVisibility() == View.GONE)
                {
                    calendarView.setVisibility((View.VISIBLE));
                    button.setText("隐藏日历");
                }
            }
        });

        return view;
    }

    /**
     * 用于初始化和刷新数据
     */
    public static void getData() {
        Log.d(TAG, "initData: ");
        list.clear();
        // 查询DB_RECORD_NAME表中所有所选日期的数据
        //Cursor cursor = db.query(Constant.DB_RECORD_TABLE_NAME, null, null, null, null, null, "startTime desc");
        Cursor cursor = db.rawQuery("select * from " + Constant.DB_RECORD_TABLE_NAME +" where startDate = ? order by startTime desc "  ,new String[] {currentDate});
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，将每条数据加入list，并打印到控制台
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                long startTime = cursor.getLong(cursor.getColumnIndex("startTime"));
                long stopTime = cursor.getLong(cursor.getColumnIndex("stopTime"));
                list.add(new Record(id,title, startTime, stopTime));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //更新listView
        adapter.notifyDataSetChanged();

    }


}
