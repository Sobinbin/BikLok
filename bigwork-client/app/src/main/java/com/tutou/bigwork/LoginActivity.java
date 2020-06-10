package com.tutou.bigwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tutou.bigwork.api.ApiService;
import com.tutou.bigwork.api.entity.LoginDto;
import com.tutou.bigwork.api.entity.RegisterDto;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tutou.bigwork.config.AuthConfig.TIKTOK_URI;
import static com.tutou.bigwork.config.AuthConfig.IS_LOGIN;
import static com.tutou.bigwork.config.AuthConfig.X_TIKTOK_TOKEN;

/**
 * @author Sobinbin,Songyang
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private Button btnGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.button_login);
        btnRegister = findViewById(R.id.button_register);
        btnGuest = findViewById(R.id.button_guest);

        MyListener listener = new MyListener();

        btnLogin.setOnClickListener(listener);
        btnRegister.setOnClickListener(listener);
        btnGuest.setOnClickListener(listener);
    }

    private class MyListener implements View.OnClickListener{

        private RequestBody requestBody;
        private String username;
        private String password;

        @Override
        public void onClick(View view){

            Map<String,String> map = new HashMap<>(2);
            username = etUsername.getText().toString().trim();
            password = etPassword.getText().toString().trim();
            map.put("username", username);
            map.put("password", password);
            requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"), new JSONObject(map).toString());

            switch (view.getId()) {
                // 登录
                case R.id.button_login:     login();    break;
                // 注册
                case R.id.button_register: register();  break;
                // 游客
                case R.id.button_guest:
                    Intent intent = new Intent(LoginActivity.this, VideoActivity.class);
                    startActivity(intent);
                default: break;
            }
        }

        private void login(){
            if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                Toast.makeText(LoginActivity.this,"密码或账号不能为空",Toast.LENGTH_SHORT).show();
            } else {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(TIKTOK_URI)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);
                apiService.login(requestBody).enqueue(new Callback<LoginDto>() {
                    @Override
                    public void onResponse(Call<LoginDto> call, Response<LoginDto> response) {

                        LoginDto loginDto = response.body();
                        if(loginDto==null){
                            Toast.makeText(LoginActivity.this,"后端未开启",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(loginDto.errno != 0){
                            Toast.makeText(LoginActivity.this,loginDto.errmsg,Toast.LENGTH_SHORT).show();
                        } else {

                            X_TIKTOK_TOKEN = loginDto.token;
                            IS_LOGIN = true;
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, VideoActivity.class);
                            startActivity(intent);
                        }

                    }
                    @Override
                    public void onFailure(Call<LoginDto> call, Throwable t) {
                        Log.d("retrofit", t.getMessage());
                    }
                });
            }
        }

        private void register(){
            if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                Toast.makeText(LoginActivity.this,"密码或账号不能为空",Toast.LENGTH_SHORT).show();
            } else {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(TIKTOK_URI)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);
                apiService.register(requestBody).enqueue(new Callback<RegisterDto>() {
                    @Override
                    public void onResponse(Call<RegisterDto> call, Response<RegisterDto> response) {

                        if(response.body()==null){
                            Toast.makeText(LoginActivity.this,"后端未开启",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(response.body().errno != 0){
                            Toast.makeText(LoginActivity.this,response.body().errmsg,Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<RegisterDto> call, Throwable t) {
                        Log.d("retrofit", t.getMessage());
                    }
                });
            }
        }

    }
}
