package com.example.gocook_;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ManagerLoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;         // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd;          // 로그인 입력필드
    private Button btn_login;                   // 로그인 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_login); //xml , java 소스 연결

        mFirebaseAuth = FirebaseAuth.getInstance();
        // mDatabaseRef = FirebaseDatabase.getInstance().getReference("manager");

        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);

        Button btn_login = findViewById(R.id.btn_login); // 로그인 버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 로그인 요청
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                String id = "manager";
                String pw = "manager";

                boolean a = strEmail.equals(id);
                boolean b = strPwd.equals(pw);

                if (strEmail.equals(id) && strPwd.equals(pw)) {
                    Toast.makeText(getApplicationContext() ,"관리자 모드", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext() ,"다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
