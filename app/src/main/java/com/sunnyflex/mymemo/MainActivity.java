package com.sunnyflex.mymemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private boolean isFirstInput = true;
    private ImageButton optionBtn;
    private ImageButton micBtn;
    private Button loadBtn;
    private Button saveBtn;
    private Button deleteBtn;
    private EditText editText;
    //저장할 파일명(하나만 지원)
    private String fileName = "MyMemo.txt";
    //시간저장을 위한 변수
    private long backPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // **변수 객체 연결**
        optionBtn = findViewById(R.id.option_btn);
        micBtn = findViewById(R.id.mic_btn);
        loadBtn = findViewById(R.id.load_btn);
        saveBtn = findViewById(R.id.save_btn);
        deleteBtn = findViewById(R.id.delete_btn);
        editText = findViewById(R.id.memo_editText);

        // **버튼 이벤트 콜백**
        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionBtnClick(view);
            }
        });
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadBtnClick(view);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBtnClick(view);
            }
        });
        deleteBtn.setOnClickListener(deleteBtnListener);
        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                micBtnClick(view);
            }
        });
    }

    // *mic버튼 클릭시 이벤트 정의*
    private void micBtnClick(View view) {
        Snackbar.make(view,"Replace with your own action", BaseTransientBottomBar.LENGTH_LONG)
                .setAction("Action", null).show();

        //voiceTask class 실행
        VoiceTask.execute();
    }

    // *옵션버튼클릭시 이벤트 정리*
    private void optionBtnClick(View view) {

    }

    // *파일불러오기 클릭시 이벤트 정리*
    private void loadBtnClick(View view) {
        //파일 로드 시 이용할 파일 입력 스트림
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = openFileInput(fileName);

            //바이트 단위로 스트림 데이터를 읽음
            byte[] data = new byte[fileInputStream.available()];
            while (fileInputStream.read(data) != -1) {}
            //현재 에디트 박스에 읽은 바이트 데이터를 세팅
            editText.setText(new String(data));
            //화면 아래 간단한 알림 메시지 출력
            Toast.makeText(getApplicationContext(), "로드성공", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //파일 읽기 성공 여부 상관없이 반드시 input 스트림 닫기
        try {
            if (fileInputStream != null){
                fileInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // *파일저장하기 클릭시 이벤트 정리*
    private void saveBtnClick(View view) {

        //파일 저장시 사용할 파일 출력 스트림 변수 선언
        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);
            //에디트 박스에 저장된 스트링 데이터를 스트림에 저장
            fileOutputStream.write(editText.getText().toString().getBytes());
            //output 스트림 닫기
            fileOutputStream.close();

            Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    // *저장된 파일 삭제시 이벤트 정리*
    View.OnClickListener deleteBtnListener = new View.OnClickListener(){
        public void onClick(View view) {

            //해당파일을 디스크에성 삭제 true리턴시 삭제 성공
            boolean deleteBtn = deleteFile(fileName);

            if (deleteBtn) {
                Toast.makeText(getApplicationContext(), "메모삭제완료", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "메모삭제실패", Toast.LENGTH_SHORT).show();
            }
        }
    };


    //****음성인식관련 class 정의****
    public class VoiceTask extends AsyncTask<String, Integer, String> {
        String str = null;

        @Override
        protected String doInBackground(String... params) {

            try {
                getVoice();
            }catch (Exception e){

            }
            return str;
        }

        @Override
        protected void onPostExecute(String result) {
            try {

            } catch (Exception e){
                Log.d("onActivityResult", "getImageURL exception");
            }
        }
    }

    // *안드로이드 음성 입력 voice를 한글로 띄우기*
    private void getVoice() {

        Intent intent = new Intent();
        intent.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        String language = "ko-KR";

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        startActivityForResult(intent, 2);

    }

    @Override
    // *activity가 호출이 끝나고 나서 호출되는 결과에 대한 정의*
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String str = results.get(0);
            Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();

            EditText editText = findViewById(R.id.memo_editText);
            if (isFirstInput){
                editText.setText(str);
                isFirstInput = false;
            }else{
                editText.append(str);
            }
        }
    }

    //백버튼 두번 연속 입력으로 종료 처리
    @Override
    public void onBackPressed() {

        if(System.currentTimeMillis() - backPressTime >= 2000) {
            backPressTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "백(Back) 버튼을 한 번 더 누르면 종료", Toast.LENGTH_LONG).show();
        }
        else if(System.currentTimeMillis() - backPressTime < 2000)
            finish();
    }


}
