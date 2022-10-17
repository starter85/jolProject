package com.example.gocook_;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;                                         // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;                                     // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd, mEtPwdck, mEtName, mEtPhonenb;           // 회원가입 입력필드
    private Button mBtnIdck, mBtnRegister;                                      // 회원가입 버튼
    private RadioButton male, female;                                           // 라디오 버튼
    private RadioGroup radioGroup;
    int i = 0;
    private String id = "";
    private boolean checkId = false;
    private boolean checkTotal = false;
    // private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("GoCook");

//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    i = (int)snapshot.getChildrenCount();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtPwdck = findViewById(R.id.et_pwdck);
        mEtName = findViewById(R.id.et_name);
        mEtPhonenb = findViewById(R.id.et_phonenb);
        male = findViewById(R.id.rbtn_man);
        female = findViewById(R.id.rbtn_woman);
        mBtnIdck = findViewById(R.id.btn_idck);

        String check_em = mEtEmail.getText().toString();

        // 중복확인
        mBtnIdck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                id = mEtEmail.getText().toString();
                mDatabaseRef.child("userAccount").orderByChild("emailId").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(getApplicationContext(), "존재하는 아이디 입니다.",Toast.LENGTH_LONG).show();
                        }
                        else if(mEtEmail.getText().toString() == null || id.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.",Toast.LENGTH_LONG).show();
                        }
                        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches()){
                            Toast.makeText(RegisterActivity.this,"이메일 형식이 아닙니다",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(mEtEmail.getText().toString().substring(mEtEmail.getText().toString().indexOf('.')+1).length() <2) {       // 공백 데이터 방지 - 아이디
                            Toast.makeText(RegisterActivity.this, "잘못된 이메일 주소 입니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            checkId =true;
                            Toast.makeText(getApplicationContext(), "사용 가능합니다.",Toast.LENGTH_LONG).show(); }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        mBtnRegister = findViewById(R.id.btn_register);

        // GOCOOK 시작하기 버튼 이벤트
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strPwdck = mEtPwdck.getText().toString();
                String strName = mEtName.getText().toString();
                String strPhonenb = mEtPhonenb.getText().toString();
                String strGender1 = male.getText().toString();
                String strGender2 = female.getText().toString();

//                mBtnRegister.setEnabled(!strEmail.isEmpty() && !strPwd.isEmpty() &&
//                        !strPwdck.isEmpty() && !strName.isEmpty() && !strPhonenb.isEmpty() &&
//                        !male.isChecked() && !female.isChecked());


                if(strEmail.equals("")) {       // 공백 데이터 방지 - 아이디
                    Toast.makeText(RegisterActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(strEmail.substring(strEmail.indexOf('.')+1).length() <2) {       // 공백 데이터 방지 - 아이디
                    Toast.makeText(RegisterActivity.this, "잘못된 이메일 주소 입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!checkId) {       // 중복확인 검사 유무
                    Toast.makeText(RegisterActivity.this, "중복확인이 되지않았습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(strPwd.equals("")) {       // 공백 데이터 방지 - 비밀번호
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(strPwd.length()<6){
                    Toast.makeText(RegisterActivity.this, "비밀번호 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (strPwdck.equals("")){
                    Toast.makeText(RegisterActivity.this, "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!strPwd.equals(strPwdck)){
                    Log.d(TAG, strPwd + " , " + strPwdck);
                    Toast.makeText(RegisterActivity.this, "비밀번호와 비밀번호 확인이 다릅니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(strName.equals("")) {        // 공백 데이터 방지 - 이름
                    Toast.makeText(RegisterActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(strPhonenb.equals("")) {       // 공백 데이터 방지 - 비밀번호
                    Toast.makeText(RegisterActivity.this, "휴대폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(strPhonenb.length()!=11){
                    Toast.makeText(RegisterActivity.this, "올바른 휴대폰 번호가 아닙니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!male.isChecked() && !female.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "성별에 체크해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    checkTotal = true;
                }

                if(checkTotal) {
                    final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                    mDialog.show();

                    // Firebase Auth 진행
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                mDialog.dismiss();
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                UserAccount account = new UserAccount();
                                // account.setIdToken(firebaseUser.getUid());
                                account.setEmailId(firebaseUser.getEmail());
                                account.setPassword(strPwd);
                                account.setName(strName);
                                account.setPhone_number(strPhonenb);
                                // mDatabaseRef.child(String.valueOf(i+1)).setValue(account);
                                if (male.isChecked()) {
                                   account.setGender(strGender1);
                                   //mDatabaseRef.child(String.valueOf(i+1)).setValue(account);
                                } else if (female.isChecked()){
                                    account.setGender(strGender2);
                                    //mDatabaseRef.child(String.valueOf(i+1)).setValue(account);
                                }
                                // setValue : database에 insert
                                mDatabaseRef.child("userAccount").child(firebaseUser.getUid()).setValue(account);

                                Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MemberLoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                                return;
                            }
                        }
                    });

                    // 비밀번호 오류 시
                } else {
                    Toast.makeText(RegisterActivity.this, "모든 정보가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });
    }
}
