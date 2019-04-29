package com.example.mytimeapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mytimeapplication.MyApplication;
import com.example.mytimeapplication.constant.Constant;
import com.example.mytimeapplication.util.DateTimeUtil;


public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "MyDatabaseHelper";
    private SQLiteDatabase db;
    //建表的SQL语句
    public static final String CREATE_TABLE_RECORD = "create table "+ Constant.DB_RECORD_TABLE_NAME +" (" +
            "id integer primary key autoincrement, " +
            "title text," +
            "startTime integer," +
            "stopTime integer ," +
            "startDate text) ";


    //删表语句
    public static final String DROP_TABLE_RECORD ="drop table if exists "+ Constant.DB_RECORD_TABLE_NAME;
    private Context mContext;
    //构造方法
    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }


    /**
     * 该函数在数据库创建时执行
     * 尚未创建数据库是，调用以下方法就会创建数据库：dbHelper.getWritableDatabase();
     * 在onCreate中执行建表操作
     * 也就是创建数据库的同时，建表
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        //建表
        db.execSQL(CREATE_TABLE_RECORD);  //
        Log.d(TAG, "onCreate: 建表成功！");
    }

    /**
     * 该函数在版本号比已存在的数据库更大时调用
     * 可以在里面重新建表
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_RECORD);
        onCreate(db);
    }

    public long addRecordInDB(SQLiteDatabase db, String title, long startTime, long stopTime){
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("startTime", startTime);
        values.put("stopTime",stopTime);
        values.put("startDate",DateTimeUtil.timestamp2ymd(startTime));
        long result = db.insert(Constant.DB_RECORD_TABLE_NAME,null,values);
        values.clear();
        if(result != -1)
            Log.d(TAG, "addRecordInDB: 加入数据库成功！");
        return result;
    }

    public long deleteById(SQLiteDatabase db, int id){
        long result = db.delete(Constant.DB_RECORD_TABLE_NAME,"id = ?",new String[] {(""+ id)});
        return result;
    }
}
