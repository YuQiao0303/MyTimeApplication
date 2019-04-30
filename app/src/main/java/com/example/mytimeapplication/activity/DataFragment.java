package com.example.mytimeapplication.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mytimeapplication.MyApplication;
import com.example.mytimeapplication.R;
import com.example.mytimeapplication.constant.Constant;
import com.example.mytimeapplication.db.MyDatabaseHelper;


public class DataFragment extends Fragment {
    private static final String TAG = "DataFragment";

    //widgets
    EditText dataText;
    Button resetData;
    Button exportData;
    Button addData;

    //database
    private static MyDatabaseHelper dbHelper;
    SQLiteDatabase db;

    public static Fragment newInstance(){
        DataFragment fragment = new DataFragment();
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
        View view = inflater.inflate(R.layout.fragment_data,null);

        dataText =view.findViewById(R.id.data_text);
        resetData = view.findViewById(R.id.reset_data);
        exportData = view.findViewById(R.id.export_data);
        addData = view.findViewById(R.id.add_data);

        //导入
        resetData.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空数据库
                dbHelper.deleteAll(db);
                String data = dataText.getText().toString();
                if(data.equals("")||data == null) {
                    HistoryFragment.getData();
                    return;
                }
                //将读取的文本进行分割,得到每条记录
                String[] records = data.split("\n");
                for(String record : records)
                {
                    //分割得到每个属性
                    String[] columeValues = record.split("\\|");
                    for(int i=0;i<columeValues.length;i++)
                    {
                        Log.d(TAG, "onClick: "+ i +" = " + columeValues[i]);
                    }
                    String title = columeValues[0];
                    long startTime =Long.parseLong(columeValues[1]);
                    long stopTime =Long.parseLong(columeValues[2]);
                    dbHelper.addRecordInDB(db,title,startTime,stopTime);

                }
                HistoryFragment.getData();
            }
        }));

        //导出
        exportData.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空文本框
                dataText.setText("");
                // 查询DB_RECORD_NAME表中所有数据
                Cursor cursor = db.query(Constant.DB_RECORD_TABLE_NAME, null, null, null, null, null, "startTime desc");
                if (cursor.moveToFirst()) {
                    do {
                        // 遍历Cursor对象，将每条数据加入list，并打印到控制台
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        long startTime = cursor.getLong(cursor.getColumnIndex("startTime"));
                        long stopTime = cursor.getLong(cursor.getColumnIndex("stopTime"));
                        String startDate = cursor.getString(cursor.getColumnIndex("startDate"));

                        if(title.equals("") || title == null)
                            dataText.append(" |");
                        else
                            dataText.append(title + "|");
                        dataText.append(startTime +"|");
                        dataText.append(stopTime +"|");
//                        dataText.append(startDate +"|");
                        dataText.append("\n");

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }));
        return view;
    }
}
