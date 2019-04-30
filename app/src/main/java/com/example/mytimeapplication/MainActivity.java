package com.example.mytimeapplication;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.mytimeapplication.activity.DataFragment;
import com.example.mytimeapplication.activity.HistoryFragment;
import com.example.mytimeapplication.activity.NowFragment;
import com.example.mytimeapplication.adapter.MyFragmentAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String []sTitle = new String[]{"Now","History","Data"};
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }
    private void initView(){
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tab_layout);

        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[1]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[2]));

        mTabLayout.setupWithViewPager(mViewPager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(NowFragment.newInstance());
        fragments.add(HistoryFragment.newInstance());
        fragments.add(DataFragment.newInstance());


        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),fragments, Arrays.asList(sTitle));
        mViewPager.setAdapter(adapter);
    }
}
