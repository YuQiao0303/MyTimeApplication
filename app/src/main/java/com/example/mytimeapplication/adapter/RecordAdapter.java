package com.example.mytimeapplication.adapter;

import android.app.NotificationManager;
import android.content.DialogInterface;
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
import com.example.mytimeapplication.bean.Record;
import com.example.mytimeapplication.util.DateTimeUtil;
import com.example.mytimeapplication.view.PopupWindowList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wen on 2017/6/14.
 */

public class RecordAdapter extends RecyclerView.Adapter{
    private static final String TAG = "TimeAdapter";

    //数据
    private List<Record> list;
    public RecordAdapter(List<Record> list) {
        this.list = list;
    }

    //长按弹框 popup window list
    private PopupWindowList mPopupWindowList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RecyclerView.ViewHolder mHolder =holder;
        ((ViewHolder) mHolder).setPosition(position);
        //长按的监听事件：弹框
        mHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int layoutPosition = mHolder.getLayoutPosition();
                showPopWindows(v);
//                Toast.makeText(MyApplication.getContext(),"长按"+ layoutPosition,Toast.LENGTH_LONG).show();
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
     * @param view
     */
    private void showPopWindows(View view){
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
                        Toast.makeText(MyApplication.getContext(),"修改0",Toast.LENGTH_LONG).show();
                        break;
                    }
                    case (1):{
                        //删除
                        Log.d(TAG, "onItemClick: 删除");
                        Snackbar.make(itemView, "Data deleted", Snackbar.LENGTH_SHORT)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(MyApplication.getContext(), "Data restored",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();

//                        AlertDialog.Builder dialog = new AlertDialog.Builder (MyApplication.getContext());
//                        dialog.setTitle("删除");
//                        dialog.setMessage("确认要删除这条数据吗");
//                        dialog.setCancelable(false);
//                        dialog.setPositiveButton("是的", new DialogInterface.
//                                OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {//关闭通知
//
//                            }});
//                        dialog.setNegativeButton("取消", new DialogInterface.
//                                OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        });
//                        dialog.show();
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
}
