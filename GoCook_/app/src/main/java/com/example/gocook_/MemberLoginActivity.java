package com.example.gocook_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemberLoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;         // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터베이스

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
        setContentView(R.layout.member_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("GoCook");


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

                // 아이디와 패스워드 공백 시 리턴 처리
                if(check_id == null || check_id.isEmpty() && check_pw == null || check_pw.isEmpty()){
                    Toast.makeText(getApplicationContext(), "아이디와 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
//                else if(check_pw == null || check_pw.isEmpty()){
//                    Toast.makeText(getApplicationContext(), "Enter New Password", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if(check_id.equals(sup_id) && check_pw.equals(sup_pw)){
                    Toast myToast = Toast.makeText(getApplicationContext() ,"000님 환영합니다", Toast.LENGTH_SHORT);
                    myToast.show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    // Intent intent = new Intent(getApplicationContext(), ocrActivity.class);
                    startActivity(intent); // ocr 화면 호출
                }else{
                    mFirebaseAuth.signInWithEmailAndPassword(check_id, check_pw).addOnCompleteListener(MemberLoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // 로그인 성공!
                                Intent intent = new Intent(MemberLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MemberLoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // 회원가입 로그인 버튼 클릭 이벤트
        mJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent); // 회원 가입 화면 호출
            }
        });
        // 회원가입 로그인 버튼 클릭 이벤트
        mJoinBtnicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
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