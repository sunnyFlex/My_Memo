package com.sunnyflex.mymemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageButton optionBtn;
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

        //변수 객체 연결
        optionBtn = findViewById(R.id.option_btn);
        loadBtn = findViewById(R.id.load_btn);
        saveBtn = findViewById(R.id.save_btn);
        deleteBtn = findViewById(R.id.delete_btn);
        editText = findViewById(R.id.memo_editText);

        //버튼 이벤트 콜백
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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBtnClick(view);
            }
        });

    }

    private void optionBtnClick(View view) {
    }

    //파일불러오기 클릭시 이벤트 정리
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
    //파일저장하기 클릭시 이벤트 정리
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

        } catch (IOException e) {
            e.printStackTrace();

        }


    }

    private void deleteBtnClick(View view) {
    }
}