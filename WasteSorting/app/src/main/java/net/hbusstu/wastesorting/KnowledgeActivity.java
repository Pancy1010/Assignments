package net.hbusstu.wastesorting;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;

import net.hbusstu.wastesorting.KnowledgePart.FirstFragment;
import net.hbusstu.wastesorting.KnowledgePart.ForthFragment;
import net.hbusstu.wastesorting.KnowledgePart.SecondFragment;
import net.hbusstu.wastesorting.KnowledgePart.ThirdFragment;


import java.util.ArrayList;
import java.util.List;

public class KnowledgeActivity extends BaseActivity {
    public ImageView main;
    public ImageView settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        main=(ImageView)findViewById(R.id.Main);
        settings=(ImageView)findViewById(R.id.settings);
        //init ViewPager
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FirstFragment());
        fragments.add(new SecondFragment());
        fragments.add(new ThirdFragment());
        fragments.add(new ForthFragment());
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        String str =getIntent().getStringExtra("str");
        main.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(KnowledgeActivity.this, MainActivity.class);
                intent.putExtra("str",str);
                startActivity(intent);
                finish();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(KnowledgeActivity.this, SettingsActivity.class);
                intent.putExtra("str",str);
                startActivity(intent);
                finish();
            }
        });
    }

    //内部类：定义viewpager的适配器
    public class PagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments;
        public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments=fragments;
        }

        @Override
        public int getCount() {

            return fragments.size();//有几个页面
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);//显示第几个页面
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) return "可回收垃圾";
            if(position == 1) return "有害垃圾";
            if(position == 2) return "湿垃圾";
            if(position == 3) return "干垃圾";
            return "数据错误";
        }
    };
}

