package com.example.gocook_;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

// 레시피 읽기 화면
public class ReadFragment extends Fragment {

    Bitmap image; // 사용되는 이미지
    Button btn_ocr; // 텍스트 촬영 버튼
    Button btn_next; // 레시피 사용 버튼
    private String imageFilePath; // 이미지 파일 경로
    private Uri p_Uri;
    public String OCRresult = null; // OCR 결과 text data

    // 블루투스
    private String nt; // 면 끓이는 시간, 취향 맞춤으로 값 보내서 bt 통신에 쓸 값
    private String ct; // 요리 시간, 취향 맞춤으로 값 보내서 bt 통신에 쓸 값

    static final int REQUEST_IMAGE_CAPTURE = 672;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_read, container, false);

        btn_ocr = (Button) view.findViewById(R.id.ocr_button);
        btn_next = (Button) view.findViewById(R.id.next_button);


        btn_ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTakePhotoIntent();

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(OCRresult == null || OCRresult.isEmpty()){
                    Toast.makeText(getContext(),"레시피 부터 촬영해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getContext(), UserThingActivity.class);
                    intent.putExtra("nt", nt);
                    intent.putExtra("ct", ct);
                    startActivityForResult(intent, 0);
                }

            }
        });


        return view;
    }

    //사진을 찍어서 Intent형식으로 보낸다.
    private void sendTakePhotoIntent() {

        // 사진을 캡처하는 인텐트를 호출하는 함수입니다. + 여기에 문제가 있었음
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
        if (getContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            File photoFile = null;
            try {
                //
                photoFile = createImageFile();
            } catch (IOException ex) {
                //
            }
            if (photoFile != null) {
                p_Uri = FileProvider.getUriForFile(getContext(), "com.example.gocook_.fileprovider", photoFile);
                // 카메라 앱과 파일을 공유하기 위한 콘텐츠 URI 생성

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, p_Uri);
                // 인텐트를 내보낸다 목적한 곳으로 (p_Uri)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                // 완료시 원하는 작업이 시작된다 , onActivityResult() 실행
            }
        } else {
            //Toast.makeText(getApplicationContext(), "not this way", Toast.LENGTH_SHORT).show();
        }

    }

    // 사진 파일 메타데이터 생성 및 저장
    private File createImageFile() throws IOException {
        // 이미지 파일 이름 만들기
        //String timeStamp = new SimpleDateFormat(
        //        "yyyyMMdd_HHmmss").format(new Date());
        //String imageFileName = "recipe" + timeStamp + "_";
        String imageFileName = "recipe";
//        File storageDir = getExternalFilesDir(
//                Environment.DIRECTORY_PICTURES); // 갤러리로 저장
        File storageDir = getContext().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES); // 갤러리로 저장
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",             /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath(); // 이미지 파일 경로 저장
        System.out.println("이것이 이미지경로 ?");
        System.out.println(imageFilePath);
        return image;
    }


    // sendTakePhotoIntent 완료 후 실행
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ((ImageView) getView().findViewById(R.id.imageView)).setImageURI(p_Uri);
            ExifInterface exif = null;


            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation); // **
            } else {
                exifDegree = 0;
            }
            ((ImageView) getView().findViewById(R.id.imageView)).setImageBitmap(rotate(bitmap, exifDegree));

            BitmapDrawable d = (BitmapDrawable)
                    ((ImageView) getView().findViewById(R.id.imageView)).getDrawable();

            image = d.getBitmap();

            // Using Network Security 에러를 잡아줌
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            //OCR 실행할 객체 생성, 새로운 스레드
            ClovaOCRTask mClovaOCRTask = new ClovaOCRTask();
            // Naver Clova OCR 실행
            try {
                OCRresult = mClovaOCRTask.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("OCR 외부변수");
            System.out.println(OCRresult);

            TextView OCRTextView = (TextView) getView().findViewById(R.id.OCRTextView);
            TextView ntView = (TextView) getView().findViewById(R.id.ntView);
            TextView ctView = (TextView) getView().findViewById(R.id.ctView);

            String[] time_values = getCookTime(OCRresult);
            OCRTextView.setText(OCRresult);

            if (time_values[0] == "") {
                Toast.makeText(getContext(), "요리 시간을 인식할 수 없습니다.", Toast.LENGTH_LONG).show();
            } else {
                // index 0 : nt, index 1 : ct
                // 전역 변수에 값 저장
                nt = time_values[0];
                ct = time_values[1];
                // 값 띄우기
                ntView.setText(time_values[0]);
                ctView.setText(time_values[1]);
                //Toast.makeText(getContext(), time_values[0], Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(), time_values[1], Toast.LENGTH_LONG).show();

            }
            //Toast.makeText(getContext(), OCRresult, Toast.LENGTH_LONG).show();

        }


    }

    //  ??
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    // nt, ct 값 추출
    private String[] getCookTime(String recipe_text) {
        //선언
        String nt;
        String ct;
        int nt_index;
        int ct_index;

        nt_index = recipe_text.indexOf("분간");

        // 못찾을 시 빈 문자열 선언
        if (nt_index == -1) {
            nt = "";
            ct = "";
        } else {
            ct_index = recipe_text.indexOf("분간", nt_index + 2); // ct 인덱스 찾기

            nt = recipe_text.substring(nt_index - 2, nt_index);
            nt = nt.trim(); // 공백 제거
            ct = recipe_text.substring(ct_index - 2, ct_index);
            ct = ct.trim(); // 공백 제거
        }
        return new String[]{nt, ct}; // 배열 형태로 반환 index
    }

    public class ClovaOCRTask extends AsyncTask<String[], Void, String> {
        @Override
        protected String doInBackground(String[]... strings) {
            // 서버에 요청
            String mOCRresult = APIOCRActivity.main(imageFilePath);
            System.out.println("OCR 내부 변수");
            System.out.println(mOCRresult);
            return mOCRresult;
        }

        // doInBackground가 정상적으로 실행됐을때 실행되는 함수.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}