package net.hbusstu.wastesorting;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends BaseActivity {
    public LinearLayout Offline;
    public TextView username;
    public LinearLayout edition;
    public ImageView main;
    public ImageView knowledge;
    public LinearLayout useragrement;
    public LinearLayout comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Offline=(LinearLayout)findViewById(R.id.ForceOffine);
        Offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.garbagesorting.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });
        String str =getIntent().getStringExtra("str");
        username=(TextView)findViewById(R.id.username);
        username.setText(str);
        edition=(LinearLayout)findViewById(R.id.edition);
        edition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "已经是当前最新版本", Toast.LENGTH_SHORT).show();
            }
        });
        useragrement=(LinearLayout)findViewById(R.id.agreement);
        useragrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AgreementActivity.class);
                intent.putExtra("str",str);
                startActivity(intent);
                finish();
            }
        });
        comment=(LinearLayout)findViewById(R.id.evaluate);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + "垃圾分类"));
                startActivity(intent);
                finish();
            }
        });
        main=(ImageView)findViewById(R.id.Main);
        main.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.putExtra("str",str);
                startActivity(intent);
                finish();
            }
        });
        knowledge=(ImageView)findViewById(R.id.baike);
        knowledge.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, KnowledgeActivity.class);
                intent.putExtra("str",str);
                startActivity(intent);
                finish();
            }
        });
    }
}