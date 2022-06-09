package com.example.gocook_;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ocrActivity extends AppCompatActivity {

    Bitmap image; // 사용되는 이미지
    private TessBaseAPI mTess; // Tess API reference
    String datapath = ""; // 언어데이터가 있는 경로

    Button btn_ocr; // 텍스트 추출 버튼

    private String imageFilePath; // 이미지 파일 경로
    private Uri p_Uri;

    static final int REQUEST_IMAGE_CAPTURE = 672;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_recipe);

        ImageView btn_back = findViewById(R.id.btn_back); // 뒤로 가기 버튼 선언

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MemberLoginActivity.class);
                startActivity(intent); // 첫 화면 호출
            }
        });

        btn_ocr = (Button)findViewById(R.id.ocr_button);

        //언어파일 경로 안드로이드 기기 내부에 있음
        datapath = getFilesDir() + "/tesseract/";

        //트레이닝데이터가 카피되어 있는지 체크
        checkFile(new File(datapath + "tessdata/"), "kor");

        String lang = "kor";
        mTess = new TessBaseAPI();  // Tesseract 라이브러리 생성
        mTess.init(datapath, lang);

        btn_ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTakePhotoIntent();

                Toast.makeText(getApplicationContext(), "hi There", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //사진을 찍어서 Intent형식으로 보낸다.
    private void sendTakePhotoIntent(){

        // 사진을 캡처하는 인텐트를 호출하는 함수입니다. + 여기에 문제가 있었음
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
        if(getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)){
            File photoFile = null;
            try{
                //
                photoFile = createImageFile();
            }catch (IOException ex){
                //
            }
            if(photoFile != null){
//                p_Uri = FileProvider.getUriForFile(this, "${applicationId}", photoFile);
                p_Uri = FileProvider.getUriForFile(this, "com.example.gocook_.fileprovider", photoFile);
                // 카메라 앱과 파일을 공유하기 위한 콘텐츠 URI 생성

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, p_Uri);
                // 인텐트를 내보낸다 목적한 곳으로 (p_Uri)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                // 완료시 원하는 작업이 시작된다 , onActivityResult() 실행
            }
        }else {
            //            Toast.makeText(getApplicationContext(), "not this way", Toast.LENGTH_SHORT).show();
        }
    }

    // 사진 파일 메타데이터 생성 및 저장
    private File createImageFile() throws IOException {
        // 이미지 파일 이름 만들기
        String timeStamp = new SimpleDateFormat(
                "yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(
//                Environment.DIRECTORY_PICTURES); // 갤러리로 저장
        File storageDir = getExternalFilesDir(
                Environment.DIRECTORY_PICTURES); // 갤러리로 저장
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",             /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath(); // 이미지 파일 경로 저장
        return image;
    }

    // sendTakePhotoIntent 완료 후 실행
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ((ImageView) findViewById(R.id.imageView)).setImageURI(p_Uri);
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
            ((ImageView)findViewById(R.id.imageView)).setImageBitmap(rotate(bitmap, exifDegree));


            BitmapDrawable d = (BitmapDrawable)
                    ((ImageView) findViewById(R.id.imageView)).getDrawable();

            image = d.getBitmap();


            String OCRresult = null;
            mTess.setImage(image);

            // 텍스트 추출
//                OCRresult = "hi" + mTess.getUTF8Text() + "hi";
            OCRresult = mTess.getUTF8Text();

            TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
            TextView ntView = (TextView) findViewById(R.id.ntView);
            TextView ctView = (TextView) findViewById(R.id.ctView);

            String[] time_values = getCookTime(OCRresult);
            OCRTextView.setText(OCRresult);

            if(time_values[0] == ""){
                Toast.makeText(getApplicationContext(), "요리 시간을 인식할 수 없습니다.", Toast.LENGTH_LONG).show();
            }else{
                // index 0 : nt, index 1 : ct
                ntView.setText(time_values[0]);
                ctView.setText(time_values[1]);
                Toast.makeText(getApplicationContext(), time_values[0], Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), time_values[1], Toast.LENGTH_LONG).show();

            }
            Toast.makeText(getApplicationContext(), OCRresult, Toast.LENGTH_LONG).show();

        }
    }

    //  ??
    private int exifOrientationToDegrees(int exifOrientation){
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    //장치에 파일 복사
    //디바이스 내부의 tess-two 언어 데이터를 받아와서 filepath에 저장한다
    private void copyFiles(String lang) {
        try{
            //언어 데이터를 저장할 공간!
            String filepath = datapath + "/tessdata/"+lang+".traineddata";

            //AssetManager에 액세스
            AssetManager assetManager = getAssets();

            //읽기,쓰기를 위한 열린 바이트 스트림
            InputStream instream = assetManager.open("tessdata/"+lang+".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //filepath에 의해 지정된 위치에 파일 복사
            byte[] buffer = new byte[1024];
            int read;

            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check file on the device
    //tess-two의 언어 파일이 제대로 설치 되었는지 확인하는과정
    private void checkFile(File dir, String lang) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles(lang);
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/"+lang+".traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles(lang);
            }
        }
    }

    // nt, ct 값 추출
    private String[] getCookTime(String recipe_text){
        //선언
        String nt;
        String ct;
        int nt_index;
        int ct_index;

        nt_index = recipe_text.indexOf("분간");

        // 못찾을 시 빈 문자열 선언
        if(nt_index == -1){
            nt="";
            ct="";
        }else{
            ct_index = recipe_text.indexOf("분간", nt_index+2); // ct 인덱스 찾기
            nt = recipe_text.substring(nt_index-2, nt_index);
            ct = recipe_text.substring(ct_index-2, ct_index);
        }
        return new String[]{nt, ct}; // 배열 형태로 반환 index
    }

}
