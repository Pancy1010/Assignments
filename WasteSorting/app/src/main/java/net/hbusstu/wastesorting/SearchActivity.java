package net.hbusstu.wastesorting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import net.hbusstu.wastesorting.SearchPart.GarbageGSON;
import net.hbusstu.wastesorting.SearchPart.GarbageSearch;
import net.hbusstu.wastesorting.SearchPart.HttpUtil;
import net.hbusstu.wastesorting.SearchPart.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends BaseActivity{
    private ScrollView garbageLayout;
    private LinearLayout newslayout;
    public ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        garbageLayout=(ScrollView) findViewById(R.id.garbagelayout);
        newslayout=(LinearLayout) findViewById(R.id.news_layout);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        back=(ImageView) findViewById(R.id.back_main);
        String str =getIntent().getStringExtra("str");
        back.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                intent.putExtra("str",str);
                startActivity(intent);
                finish();
            }
        });
//          String garbageString=prefs.getString("garbageSearch",null);
//          if(garbageString!=null){
//              GarbageSearch garbageSearch=Utility.handleGarbageSearchResponse(garbageString);
//              showGarbageInfo(garbageSearch);
//          }else{
              String garbageID = getIntent().getStringExtra("garbageID");
              garbageLayout.setVisibility(View.VISIBLE);
              requestGarbage(garbageID);
    }

    public void requestGarbage(final String garbageID){
        String garbageUrl="https://api.tianapi.com/lajifenlei/index?key=882f80503756018b20d77c21d92057bc&word="+garbageID;
        HttpUtil.sendOkHttpRequest(garbageUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final GarbageSearch garbageSearch= Utility.handleGarbageSearchResponse(responseText);
                Log.d(getClass().getSimpleName(), "========="+garbageSearch.msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(getClass().getSimpleName(), "========="+garbageSearch.msg);
                        if(garbageSearch!=null&&"success".equals(garbageSearch.msg) ) {
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(SearchActivity.this).edit();
                            editor.putString("garbageSearch",responseText);
                            editor.apply();
                            showGarbageInfo(garbageSearch);
                        }else {
                            Toast.makeText(SearchActivity.this,"获取垃圾信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this,"获取垃圾信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
    private void showGarbageInfo(GarbageSearch garbageSearch){
        newslayout.removeAllViews();
        for(GarbageGSON.News news : garbageSearch.Newslist){
            View view = LayoutInflater.from(this).inflate(R.layout.news_items,newslayout,false);
            TextView nameText=(TextView)view.findViewById(R.id.name);
            TextView typeText=(TextView)view.findViewById(R.id.type);
            //TextView aipreText=(TextView)view.findViewById(R.id.aipre);
            TextView explainText=(TextView)view.findViewById(R.id.explain);
            TextView containText=(TextView)view.findViewById(R.id.contain);
            TextView tipText=(TextView)view.findViewById(R.id.tip);
            nameText.setText("垃圾名称： "+news.Garbagename);
            if(0==news.type) {
                typeText.setText("垃圾类型： 可回收垃圾");
            }
            if(1==news.type) {
                typeText.setText("垃圾类型： 不回收垃圾");
            }
            if(2==news.type) {
                typeText.setText("垃圾类型： 湿垃圾（厨余垃圾）");
            }
            if(3==news.type) {
                typeText.setText("垃圾类型： 干垃圾");
            }
            //typeText.setText("垃圾类型： "+news.type+"");
            //aipreText.setText(news.aipre+"");
            explainText.setText("类型解释： "+news.explain);
            containText.setText("类型样例： "+news.contain);
            tipText.setText("分类注意： "+news.tip+"\n");
            newslayout.addView(view);
        }
        garbageLayout.setVisibility(View.VISIBLE);
    }
}