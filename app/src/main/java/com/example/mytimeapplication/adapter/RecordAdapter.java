package com.example.mytimeapplication.adapter;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mytimeapplication.MainActivity;
import com.example.mytimeapplication.MyApplication;
import com.example.mytimeapplication.R;
import com.example.mytimeapplication.activity.HistoryFragment;
import com.example.mytimeapplication.activity.UpdateRecordDialog;
import com.example.mytimeapplication.bean.Record;
import com.example.mytimeapplication.constant.Constant;
import com.example.mytimeapplication.db.MyDatabaseHelper;
import com.example.mytimeapplication.util.DateTimeUtil;
import com.example.mytimeapplication.view.PopupWindowList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wen on 2017/6/14.
 */

public class RecordAdapter extends RecyclerView.Adapter{
    private static final String TAG = "TimeAdapter";

    //context
    private Context context;
    //数据
    private List<Record> list;
    public RecordAdapter(List<Record> list,Context context) {
        this.list = list;
        this.context = context;
    }

    //长按弹框 popup window list
    private PopupWindowList mPopupWindowList;

    //database
    private static MyDatabaseHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RecyclerView.ViewHolder mHolder =holder;
        final int itemPsition = position;
        ((ViewHolder) mHolder).setPosition(position);
        //长按的监听事件：弹框
        mHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int layoutPosition = mHolder.getLayoutPosition();
                showPopWindows(v,itemPsition);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private Record record;
        private TextView startTimeText;
        private TextView stopTimeText;
        private TextView titleText;
        private TextView durationText;

        public ViewHolder(View itemView) {
            super(itemView);
            startTimeText = itemView.findViewById(R.id.start_time);
            stopTimeText = itemView.findViewById(R.id.stop_time);
            titleText = itemView.findViewById(R.id.title);
            durationText = itemView.findViewById(R.id.duration);
        }
        //显示对应的实体类的内容
        public void setPosition(int position) {

            record = list.get(position);

            titleText.setText(record.getTitle());
            startTimeText.setText(DateTimeUtil.timestamp2hms(record.getStartTime()));
            stopTimeText.setText(DateTimeUtil.timestamp2hms(record.getStopTime()));
            durationText.setText(DateTimeUtil.formateDuration(record.getStopTime()-record.getStartTime()));
        }
    }

    /**
     * 长按弹框方法的实现
     * @param view  itemView，被长按的条目
     * @param itemPosition   被长按的条目的position
     */
    private void showPopWindows(View view , final int itemPosition){
        final View itemView = view;

        List<String> dataList = new ArrayList<>();
        dataList.add("修改");
        dataList.add("删除");
        dataList.add("多选");


        if (mPopupWindowList == null){
            mPopupWindowList = new PopupWindowList(view.getContext());
        }
        mPopupWindowList.setAnchorView(view);
        mPopupWindowList.setItemData(dataList);
        mPopupWindowList.setModal(true);
        mPopupWindowList.show();
        mPopupWindowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "click position="+position);
                switch (position){
                    //修改
                    case (0):{
                        onClickUpdate(itemView,itemPosition);
                        break;
                    }
                    case (1):{
                        onClickDelete(itemView,itemPosition);
                        break;
                    }
                    case (2):{
                        Toast.makeText(MyApplication.getContext(),"多选2",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                mPopupWindowList.hide();
            }
        });
    }

    /**
     * 删除条目的实现
     * @param itemView
     * @param itemPosition
     */
    private void onClickDelete(View itemView,final int itemPosition){
        //获取实体类对象
        final Record record = list.get(itemPosition);
        //创建/获取数据库
        dbHelper = new MyDatabaseHelper(MyApplication.getContext(), Constant.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();   //检测有没有该名字的数据库，若没有则创建，同时调用dbHelper 的 onCreate 方法；
        //在数据库中删除
        dbHelper.deleteById(db,record.getId());
        //在list中删除
        list.remove(itemPosition);
        //更新ui
        HistoryFragment.getAdapter().notifyDataSetChanged();
        //弹出snackBar
        Log.d(TAG, "onItemClick: 删除");
        Snackbar.make(itemView, "您删除了一条数据", Snackbar.LENGTH_SHORT)
                .setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //重新保存数据到数据库
                        dbHelper.addRecordInDB(db,record.getTitle(),record.getStartTime(),record.getStopTime());
                        //重新保存数据到list
                        list.add(itemPosition,record);
                        //更新ui
                        HistoryFragment.getAdapter().notifyDataSetChanged();
                    }
                })
                .show();
    }

    /**
     * 修改
     * @param itemView
     * @param itemPosition
     */
    private void onClickUpdate(View itemView,final int itemPosition){
        //获取实体类对象
        final Record record = list.get(itemPosition);
        //创建/获取数据库
        dbHelper = new MyDatabaseHelper(MyApplication.getContext(), Constant.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();   //检测有没有该名字的数据库，若没有则创建，同时调用dbHelper 的 onCreate 方法；
        //实例化一个UpdateRecordDialog对象
        UpdateRecordDialog dialog = new UpdateRecordDialog();
        //给UpdateRecordDialog 传参
        Bundle bundle = new Bundle();
        bundle.putSerializable("record",record);
        bundle.putInt("itemPosition",itemPosition);
        dialog.setArguments(bundle);
        //显示UpdateRecordDialog
        dialog.show(((MainActivity)context).getFragmentManager(),"tag");
    }
}
