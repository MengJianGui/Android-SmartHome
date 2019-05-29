package com.example.administrator.smarthome1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("DefaultLocale")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_password; // 声明一个文本视图对象
    private EditText et_password; // 声明一个编辑框对象
    private Button btn_forget; // 声明一个按钮控件对象
    private Button btn_login;
    private Button btn_exit;
    private CheckBox ck_remember; // 声明一个复选框对象

    private int mRequestCode = 0; // 跳转页面时的请求代码
    private boolean isRemember; // 是否记住密码
    private String password="111111"; // 默认密码

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null){
            password = savedInstanceState.getString("password");
        }
        tv_password = findViewById(R.id.tv_password);
        et_password = findViewById(R.id.et_password);
        btn_forget = findViewById(R.id.btn_forget);
        btn_login=findViewById(R.id.btn_login);
        btn_exit=findViewById(R.id.btn_exit);
        ck_remember = findViewById(R.id.ck_remember);
        // 给rg_login设置单选监听器
        ck_remember.setOnCheckedChangeListener(new CheckListener());
        // 给et_phone添加文本变更监听器
        et_password.addTextChangedListener(new HideTextWatcher(et_password));
        btn_forget.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isRemember = sharedPreferences.getBoolean("remember_password",false);
        password = sharedPreferences.getString("password","111111");
        if (isRemember){
            et_password.setText(password);
            ck_remember.setChecked(true);
        }
    }


    // 定义是否记住密码的勾选监听器
    private class CheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.ck_remember) {
                isRemember = isChecked;
            }
        }
    }
    // 定义编辑框的文本变化监听器
    private class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;
        private CharSequence mStr;

        HideTextWatcher(EditText v) {
            super();
            mView = v;
            mMaxLength = com.example.administrator.smarthome1.util.ViewUtil.getMaxLength(v);
        }

        // 在编辑框的输入文本变化前触发
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        // 在编辑框的输入文本变化时触发
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mStr = s;
        }

        // 在编辑框的输入文本变化后触发
        public void afterTextChanged(Editable s) {
            if (mStr == null || mStr.length() == 0)
                return;
            // 手机号码输入达到11位，或者密码/验证码输入达到6位，都关闭输入法软键盘
            if (mStr.length() == 6 && mMaxLength == 6) {
                com.example.administrator.smarthome1.util.ViewUtil.hideOneInputMethod(MainActivity.this, mView);
            }
        }
    }

    // 从后一个页面携带参数返回当前页面时触发
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mRequestCode && data != null) {
            // 用户密码已改为新密码，故更新密码变量
            password = data.getStringExtra("new_password");
        }
    }

    // 从修改密码页面返回登录页面，要清空密码的输入框
    @Override
    protected void onRestart() {
        et_password.setText("");
        super.onRestart();
    }
    @Override
    public void onClick(View v){
        if(v.getId()==R.id.btn_forget){
            Intent intent = new Intent(this, LoginForgetActivity.class);
            startActivityForResult(intent, mRequestCode);
        }else if(v.getId()==R.id.btn_login){
            if(!et_password.getText().toString().equals(password)){
                Toast.makeText(this,"请输入正确的密码",Toast.LENGTH_SHORT).show();
            }else{//密码校验通过
                editor = sharedPreferences.edit();
                if (ck_remember.isChecked()){
                    editor.putBoolean("remember_password",true);
                    editor.putString("password",password);
                }else {
                    editor.clear();
                }
                editor.apply();
                loginSuccess();//提示用户登录成功，并转到下一activity
            }
        }else if(v.getId()==R.id.btn_exit){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
    // 校验通过，登录成功
    private void loginSuccess() {
        String desc = "恭喜您登录成功";
        // 弹出提醒对话框，提示用户登录成功
        Toast.makeText(this,desc,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, TabFragmentActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("password",password);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        password = savedInstanceState.getString("password");
    }
}

