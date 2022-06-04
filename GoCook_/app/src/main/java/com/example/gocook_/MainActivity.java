package com.example.gocook_;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mEmailView;            // 아이디 입력 칸
    private EditText mPwdView;              // 비밀번호 입력 칸
    private Button mLoginBtn;               // 로그인 버튼

    private TextView mJoinBtn;              // 회원가입 버튼
    private ImageView mJoinBtnicon;         // 회원가입 아이콘

    private TextView mManagerLoginBtn;      // 관리자 로그인 버튼
    private ImageView mManagerLoginBtnicon; // 관리자 로그인 아이콘



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            mEmailView = (EditText) findViewById(R.id.login_email);                 // 아이디 입력 칸
            mPwdView = (EditText) findViewById(R.id.login_pwd);                     // 비밀번호 입력 칸
            mLoginBtn = (Button) findViewById(R.id.btn_login);                      // 로그인 버튼

            mJoinBtn = (TextView) findViewById(R.id.btn_join);                      // 회원가입 버튼
            mJoinBtnicon = (ImageView) findViewById(R.id.icon_join);                // 회원가입 아이콘

            mManagerLoginBtn = (TextView) findViewById(R.id.btn_mlogin);            // 관리자 로그인 버튼
            mManagerLoginBtnicon = (ImageView) findViewById(R.id.icon_mlogin);      // 관리자 로그인 아이콘


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check_id = mEmailView.getText().toString();
                String check_pw = mPwdView.getText().toString();
                String sup_id = "111";
                String sup_pw = "111";

                if(check_id.equals(sup_id) && check_pw.equals(sup_pw)){
                    Toast myToast = Toast.makeText(getApplicationContext() ,"hi", Toast.LENGTH_SHORT);
                    myToast.show();
                    Intent intent = new Intent(getApplicationContext(), ocrActivity.class);
                    startActivity(intent); // ocr 화면 호출
                }else{
                    Toast myToast = Toast.makeText(getApplicationContext() ,"bye", Toast.LENGTH_SHORT);
                    myToast.show();

                }
            }
        });


        // 회원가입 로그인 버튼 클릭 이벤트
        mJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent); // 회원 가입 화면 호출
            }
        });
        // 회원가입 로그인 버튼 클릭 이벤트
        mJoinBtnicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent); // 회원 가입 화면 호출
            }
        });


        mManagerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerLoginActivity.class);
                startActivity(intent); // 관리자 로그인 화면 호출
            }
        });

        mManagerLoginBtnicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerLoginActivity.class);
                startActivity(intent); // 관리자 로그인 화면 호출
            }
        });
    }
}