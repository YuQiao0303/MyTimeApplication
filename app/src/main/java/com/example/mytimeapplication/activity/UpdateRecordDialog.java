package com.example.mytimeapplication.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
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

import java.util.Calendar;

public class UpdateRecordDialog extends DialogFragment implements View.OnClickListener{

    private Record record;
    private EditText titleText;
    private Button modifyStartDate;
    private Button modifyStartTime;
    private Button modifyStopDate;
    private Button modifyStopTime;

    private int mYear;
    private int mMonth;
    private int mDay;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //设置标题
        //getDialog().setTitle("title");
        //获取要更改的record对象
        Bundle bundle = getArguments();
        record = (Record)bundle.getSerializable("record");
        View view=inflater.inflate(R.layout.fragment_update_record_dialog, container);

        titleText = view.findViewById(R.id.title_text);
        modifyStartDate = view.findViewById(R.id.modify_start_date);
        modifyStartTime = view.findViewById(R.id.modify_start_time);
        modifyStopDate = view.findViewById(R.id.modify_stop_date);
        modifyStopTime = view.findViewById(R.id.modify_stop_time);

        titleText.setText(record.getTitle());




        modifyStartDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
    @Override
    public void onClick(View v){
        Calendar calendar = Calendar.getInstance();
        switch (v.getId()){
            //更改开始日期
            case R.id.modify_start_date :{
                calendar.setTimeInMillis(record.getStartTime());
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MyApplication.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mYear = year;
                                mMonth = month;
                                mDay = dayOfMonth;
                                //修改开始日期
                                final String data =  (month+1) + "月-" + dayOfMonth + "日 ";
                            }
                        },
                        mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            }
        }
    }

}