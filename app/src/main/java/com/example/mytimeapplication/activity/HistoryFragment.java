package com.example.mytimeapplication.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

    public static Fragment newInstance(){
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        //创建/获取数据库
        dbHelper = new MyDatabaseHelper(MyApplication.getContext(), Constant.DB_NAME, null, 1);
        dbHelper.getWritableDatabase();   //检测有没有该名字的数据库，若没有则创建，同时调用dbHelper 的 onCreate 方法；
        db = dbHelper.getWritableDatabase();

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
        adapter = new RecordAdapter(list);
        recyclerView.setAdapter(adapter);

        //初始化数据
        getData();

        //长按删除
        //调用适配器里的方法
        adapter.setOnLongClickLisenter(new RecordAdapter.onLongClickLisenter() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onLongClickLisenter(int i) {
                Toast.makeText(getContext(),"长按： "+i,Toast.LENGTH_SHORT).show();
            }
        });

        //

        return view;
    }
    public static void getData() {
        Log.d(TAG, "initData: ");
        list.clear();
        // 查询DB_RECORD_NAME表中所有的数据
        Cursor cursor = db.query(Constant.DB_RECORD_TABLE_NAME, null, null, null, null, null, "startTime desc");
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，将每条数据加入list，并打印到控制台
                String title = cursor.getString(cursor.getColumnIndex("title"));
                long startTime = cursor.getLong(cursor.getColumnIndex("startTime"));
                long stopTime = cursor.getLong(cursor.getColumnIndex("stopTime"));
                list.add(new Record(title, startTime, stopTime));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //更新listView
        adapter.notifyDataSetChanged();
    }
}
