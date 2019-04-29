package com.example.mytimeapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mytimeapplication.MyApplication;
import com.example.mytimeapplication.R;
import com.example.mytimeapplication.bean.Record;
import com.example.mytimeapplication.util.DateTimeUtil;

import java.util.List;

/**
 * Created by wen on 2017/6/14.
 */

public class RecordAdapter extends RecyclerView.Adapter{
    private static final String TAG = "TimeAdapter";


    private List<Record> list;
    public RecordAdapter(List<Record> list) {
        this.list = list;
    }


    //长按接口
    public interface onLongClickLisenter
    {
        void onLongClickLisenter(int position);
    }
    //声明接口
    private onLongClickLisenter onLongClickLisenter;

    public void setOnLongClickLisenter(RecordAdapter.onLongClickLisenter onLongClickLisenter) {
        this.onLongClickLisenter = onLongClickLisenter;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RecyclerView.ViewHolder mHolder =holder;
        ((ViewHolder) mHolder).setPosition(position);
        //长按
        mHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                int layoutPosition = mHolder.getLayoutPosition();
                Toast.makeText(MyApplication.getContext(),"直接在onLongClick里面写，长按"+ layoutPosition,Toast.LENGTH_SHORT).show();
                if (onLongClickLisenter!=null)
                {
                    onLongClickLisenter.onLongClickLisenter(layoutPosition);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //长按弹出contextMenu
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView rightTxt;

        private LinearLayout text;

        private Record record;
        private ImageView dotImage;
        private TextView startTimeText;
        private TextView stopTimeText;
        private TextView titleText;
        private TextView durationText;




        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            startTimeText = itemView.findViewById(R.id.start_time);
            stopTimeText = itemView.findViewById(R.id.stop_time);
            titleText = itemView.findViewById(R.id.title);
            durationText = itemView.findViewById(R.id.duration);
            dotImage = itemView.findViewById(R.id.dot_image);

        }
        public void setPosition(int position) {

            record = list.get(position);

            titleText.setText(record.getTitle());
            startTimeText.setText(DateTimeUtil.timestamp2hms(record.getStartTime()));
            stopTimeText.setText(DateTimeUtil.timestamp2hms(record.getStopTime()));
            durationText.setText(DateTimeUtil.formateDuration(record.getStopTime()-record.getStartTime()));
        }

    }
}
