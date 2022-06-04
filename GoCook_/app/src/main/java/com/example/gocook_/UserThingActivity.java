package com.example.gocook_;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserThingActivity extends AppCompatActivity {

    TextView mNoodleWeight,mSouceWeight; // 면 용량

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_thing); //xml , java 소스 연결

        mNoodleWeight = findViewById(R.id.noodleWeight); //면 용량
        mSouceWeight = findViewById(R.id.souceWight); // 소스 용량

        DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("humidity "); // 나중에 humidity 대신 무게값을 가져올 것, 아두이노에서 변경먼저 해야함.
        DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference("humidity2 ");

        // 파이어베이스에 있는 값 불러오는 이벤트 리스너
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mNoodleWeightValue = snapshot.getValue(String.class);    // 실시간으로 확인한 면 무게값을 변수에 넣는다.
                mNoodleWeight.setText(mNoodleWeightValue+"g");  // 무게값 텍스트 뷰에 보여주기
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //실패
                mNoodleWeight.setText("-1");    // 에러가 났을 시 무게값에 -1 표시
                Log.e("e",error.toException().toString());


            }
        });
        // 파이어베이스에 있는 소스 무게값 불러오는 이벤트 리스너
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mSouceWeightValue= snapshot.getValue(String.class);    // 실시간으로 확인한 소스 무게값을 변수에 넣는다.
                mSouceWeight.setText(mSouceWeightValue+"g");  // 무게값 텍스트 뷰에 보여주기
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mSouceWeight.setText("-1");    // 에러가 났을 시 무게값에 -1 표시
                Log.e("e",error.toException().toString());
            }
        });
    }
}
