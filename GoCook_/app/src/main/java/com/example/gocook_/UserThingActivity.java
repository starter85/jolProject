package com.example.gocook_;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class UserThingActivity extends AppCompatActivity {

    private String sendToDevice; // 블루투스로 아두이노에 보낼 값 담긴 변수
    private String nt; // 블루투스로 아두이노에 보낼 면 끓이는 시간 값
    private String ct; // 블루투스로 아두이노에 보낼 요리하는 시간 값

    private Button noodle1, noodle2, noodle3; // 면 양 버튼
    private Button sauce1, sauce2; // 소스 양 버튼
    private EditText user_noodleweight; // 사용자 면 양 출력 부분
    private EditText user_sauceweight; // 사용자 소스 양 출력 부분
    private String num_user_sauceweight; // 계산
    private Integer result1; // 계산
    private double result2; // 계산

    private BluetoothSPP bt;

    // 면 용량, 소스용량, 면끓이는 시간, 요리하는시간
    private TextView mNoodleWeight, mSouceWeight, mUser_thing_nt, mUser_thing_ct; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_thing); //xml , java 소스 연결

        noodle1 = (Button) findViewById(R.id.noodle_one); // 1인
        noodle2 = (Button) findViewById(R.id.noodle_two); // 2인
        noodle3 = (Button) findViewById(R.id.noodle_three); // 3인

        sauce1 = (Button) findViewById(R.id.sauce_one); // 150 x 인
        sauce2 = (Button) findViewById(R.id.sauce_two); // 150 x 인 + 30%

        user_noodleweight = (EditText) findViewById(R.id.user_noodleweight); // 사용자 면 양
        user_sauceweight = (EditText) findViewById(R.id.user_sauceweight); // 사용자 소스 양

        noodle1.setOnClickListener(new View.OnClickListener() { // 1인 버튼 클릭 시
            @Override
            public void onClick(View view) {
                user_noodleweight = (EditText) findViewById(R.id.user_noodleweight);
                user_noodleweight.setText("80g");
            }
        });

        noodle2.setOnClickListener(new View.OnClickListener() { // 2인 버튼 클릭 시
            @Override
            public void onClick(View view) {
                user_noodleweight = (EditText) findViewById(R.id.user_noodleweight);
                user_noodleweight.setText("160g");
            }
        });

        noodle3.setOnClickListener(new View.OnClickListener() { // 3인 버튼 클릭 시
            @Override
            public void onClick(View view) {
                user_noodleweight = (EditText) findViewById(R.id.user_noodleweight);
                user_noodleweight.setText("240g");
            }
        });

        sauce1.setOnClickListener(new View.OnClickListener() { // 소스 기본 버튼
            @Override
            public void onClick(View view) {
                num_user_sauceweight = user_noodleweight.getText().toString().replace("g", "");
                result1 = Integer.parseInt(num_user_sauceweight) / 80 * 150;
                user_sauceweight.setText(result1+"g");
            }
        });

        sauce2.setOnClickListener(new View.OnClickListener() { // 소스 많게 버튼
            @Override
            public void onClick(View view) {
                num_user_sauceweight = user_noodleweight.getText().toString().replace("g", "");
                result1 = Integer.parseInt(num_user_sauceweight) / 80 * 150;
                result2 = Integer.parseInt(String.valueOf(result1)) * 0.3;
                user_sauceweight.setText(result1+result2+"g");
            }
        });


        // 블루투스 객체 생성 후 미리 선언한 변수에 넣음
        bt = new BluetoothSPP(this); //Initializing

        // OCR 로 읽은 값 받는 코드
        Intent inIntent = getIntent();
        this.nt = inIntent.getStringExtra("nt"); // ocr 로 읽은 nt 값 받기, ReadFragment.java에서 옴
        this.ct = inIntent.getStringExtra("ct"); // ocr 로 읽은 ct 값 받기, ReadFragment.java에서 옴

        mUser_thing_nt = findViewById(R.id.user_thing_nt);
        mUser_thing_ct = findViewById(R.id.user_thing_ct);
        mUser_thing_nt.setText(nt);
        mUser_thing_ct.setText(ct);

        // 실시간 무게값 ESP32 - firebase - android 통신
        mNoodleWeight = findViewById(R.id.noodleWeight); //면 용량
        mSouceWeight = findViewById(R.id.souceWight); // 소스 용량

        DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("humidity "); // 나중에 humidity 대신 무게값을 가져올 것, 아두이노에서 변경먼저 해야함.
        DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference("humidity2 ");

        // 파이어베이스에 있는 값 불러오는 이벤트 리스너
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mNoodleWeightValue = snapshot.getValue(String.class);    // 실시간으로 확인한 면 무게값을 변수에 넣는다.
                mNoodleWeight.setText(mNoodleWeightValue + "g");  // 무게값 텍스트 뷰에 보여주기
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //실패
                mNoodleWeight.setText("-1");    // 에러가 났을 시 무게값에 -1 표시
                Log.e("e", error.toException().toString());


            }
        });
        // 파이어베이스에 있는 소스 무게값 불러오는 이벤트 리스너
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mSouceWeightValue = snapshot.getValue(String.class);    // 실시간으로 확인한 소스 무게값을 변수에 넣는다.
                mSouceWeight.setText(mSouceWeightValue + "g");  // 무게값 텍스트 뷰에 보여주기
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mSouceWeight.setText("-1");    // 에러가 났을 시 무게값에 -1 표시
                Log.e("e", error.toException().toString());
            }
        });
        // 실시간 무게값 ESP32 - firebase - android 통신 끝

        // 블루투스 코드
        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가라면
            // 사용불가라고 토스트 띄워줌
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            // 화면 종료
            finish();
        }


        // 블루투스 데이터를 받았는지 감지하는 리스너
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            //데이터 수신되면
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); // 토스트로 데이터 띄움
            }
        });
        // 블루투스가 잘 연결이 되었는지 감지하는 리스너
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            // 블루투스 연결해제
            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            // 블루투스 연결 실패
            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });
        // 블루투스 연결하는 기능 버튼 가져와서 이용하기
        Button btnConnect = findViewById(R.id.btnConnect); //연결시도
        // 블루투스 버튼 클릭하면
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) { // 현재 버튼의 상태에 따라 연결이 되어있으면 끊고, 반대면 연결
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

    }// oncreate() 끝

    // 블루투스 코드
    // 앱 중단시 (액티비티 나가거나, 특정 사유로 중단시)
    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    // 앱이 시작하면
    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { // 앱의 상태를 보고 블루투스 사용 가능하면
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 새로운 액티비티 띄워줌, 거기에 현재 가능한 블루투스 정보 intent로 넘겨
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        }
        else {
            if (!bt.isServiceAvailable()) { // 블루투스 사용 불가
                // setupService() 실행하도록
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기끼리
                // 셋팅 후 연결되면 setup()으로
                setup();
            }
        }
    }
    // 블루투스 사용 - 데이터 전송
    public void setup() {
        Button cooking_btn = findViewById(R.id.cooking_btn); // 요리하기 버튼
        cooking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "잠시 후 작동됩니다.", Toast.LENGTH_SHORT).show();
                sendToDevice = "nt " + nt + " ct " + ct + "e";
                bt.send(sendToDevice, true); // e : 문자열의 끝지점
            }
        });
    }


    // 새로운 액티비티 (현재 액티비티의 반환 액티비티?)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 아까 응답의 코드에 따라 연결 가능한 디바이스와 연결 시도 후 ok 뜨면 데이터 전송
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) { // 연결시도
            if (resultCode == Activity.RESULT_OK) // 연결됨
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) { // 연결 가능
            if (resultCode == Activity.RESULT_OK) { // 연결됨
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else { // 사용불가
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
