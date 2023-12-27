package net.hbusstu.wastesorting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class AgreementActivity extends BaseActivity {
    public RelativeLayout touch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        touch=(RelativeLayout) findViewById(R.id.touch);
            String str =getIntent().getStringExtra("str");
        touch.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(AgreementActivity.this, SettingsActivity.class);
                intent.putExtra("str",str);
                startActivity(intent);
                finish();
            }
        });
    }
}