package net.hbusstu.wastesorting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import net.hbusstu.wastesorting.RegisterPart.UserService;


public class LoginActivity extends BaseActivity {
    private EditText et_account;
    private EditText et_password;
    private Button login;
    private CheckBox rememberPass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        et_account = findViewById(R.id.account);
        et_password = findViewById(R.id.password);
        rememberPass=(CheckBox) findViewById(R.id.rememberPass);
        register=(Button) findViewById(R.id.register);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            et_account.setText(account);
            et_password.setText(password);
            rememberPass.setChecked(true);
        }
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = et_account.getText().toString();
                String password = et_password.getText().toString();
                System.out.println(account);
                System.out.println(password);
                Log.i("TAG",account+"_"+password);
                UserService uService=new UserService(LoginActivity.this);
                boolean flag=uService.login(account, password);
                if (flag) {
                    editor= pref.edit();
                    if(rememberPass.isChecked()){
                        editor.putBoolean("remember_password", true);
                        editor.putString("account",account);
                        editor.putString("password",password);
                    }else{
                        editor.clear();
                    }
                    editor.apply();
                    Log.i("TAG","登录成功");
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("str",account);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.i("TAG","登录失败");
                    Toast.makeText(LoginActivity.this, "密码或者用户名输入错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

//    private class addDefaultAccount() {
//        File file=new File("/com.example.MySchool/password.txt");
//
//        if(!file.exist()){
//
//        }
//
//    }

}
