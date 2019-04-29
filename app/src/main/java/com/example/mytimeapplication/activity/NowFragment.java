package com.example.mytimeapplication.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytimeapplication.MyApplication;
import com.example.mytimeapplication.R;
import com.example.mytimeapplication.constant.Constant;
import com.example.mytimeapplication.db.MyDatabaseHelper;
import com.example.mytimeapplication.util.DateTimeUtil;


public class NowFragment extends Fragment {
    private static final String TAG = "NowFragment";

    //widgets
    ImageView startStopImg;
    TextView startTimeText;
    TextView durationText;
    EditText titleText;

    //database
    private static MyDatabaseHelper dbHelper;
    SQLiteDatabase db;

    //variables
    boolean isDoingSomething = false;
    long startTime;
    long stopTime;
    long duration = 0;

    Handler mHandler = new Handler();
    Runnable task = new Runnable() {
        @Override
        public void run() {
            //每隔1s循环执行run方法
            mHandler.postDelayed(this, 1000);
            //更新ui
            duration += 1000;
            durationText.setText("" +DateTimeUtil.formateDuration(duration));
        }
    };


    public static Fragment newInstance(){
        NowFragment fragment = new NowFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //创建/获取数据库
        dbHelper = new MyDatabaseHelper(MyApplication.getContext(), Constant.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();   //检测有没有该名字的数据库，若没有则创建，同时调用dbHelper 的 onCreate 方法；


        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now,null);

        titleText =view.findViewById(R.id.title);
        startStopImg = view.findViewById(R.id.start_stop);
        startTimeText = view.findViewById(R.id.start_time);
        durationText = view.findViewById(R.id.duration);

        startStopImg.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDoingSomething){
                    //to stop
                    startStopImg.setImageResource(R.drawable.play);
                    stopTime = System.currentTimeMillis();
                    String title = titleText.getText().toString();
                    Log.d(TAG, "onClick: stop time is "+ stopTime);
                    Log.d(TAG, "onClick: last for " + (stopTime-startTime)/1000 + "s");
                    duration = 0;
                    mHandler.removeCallbacks(task);//停止定时更新ui

                    //加入数据库
                    dbHelper.addRecordInDB(db,title,startTime,stopTime);
                    Toast.makeText(MyApplication.getContext(),"加入数据库成功",Toast.LENGTH_LONG);
                    HistoryFragment.getData();

                }
                else{
                    //to start
                    startStopImg.setImageResource(R.drawable.stop);
                    startTime = System.currentTimeMillis();
                    startTimeText.setText(DateTimeUtil.timestamp2whole(startTime));
                    mHandler.post(task);//定时更新ui
                    Log.d(TAG, "onClick: start time is "+ startTime);
                }
                isDoingSomething = !isDoingSomething;
            }
        }));
        return view;
    }
}
