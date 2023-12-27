package net.hbusstu.wastesorting;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends BaseActivity {
    protected static final int RESULT_SPEECH = 1;
    public ImageView exam;
    public SearchView search;
    public ImageView knowledge;
    public ImageView settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        Intent intent = getIntent();
        String str = intent.getStringExtra("str");
        exam.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExamActivity.class);
                intent.putExtra("str",str);
                startActivity(intent);
                finish();
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(MainActivity.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("garbageID",query);
                    intent.putExtra("str",str);
                    startActivity(intent);
                    finish();
                }
                return true;
            }

            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    Toast.makeText(MainActivity.this, "请输入查找内容!！", Toast.LENGTH_SHORT).show();
                } else {

                }
                return true;
            }
        });
        knowledge.setOnClickListener(new View.OnClickListener() {
            //@Override

            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KnowledgeActivity.class);
                intent.putExtra("str",str);
                startActivity(intent);
                finish();

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("str",str);
                startActivity(intent);
                finish();
            }
        });

    }


    private void findViews() {
        search=(SearchView) findViewById(R.id.Search);
        knowledge=(ImageView) findViewById(R.id.baike);
        settings=(ImageView) findViewById(R.id.settings);
        exam=(ImageView) findViewById(R.id.exam);
    }
}


