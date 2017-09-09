package com.java.zu26.mainActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.java.zu26.R;
import com.java.zu26.category.CategoryActivity;
import com.java.zu26.favorite.FavoriteActivity;
import com.java.zu26.newsList.NewsListActivity;
import com.java.zu26.newsPage.NewsPageActivity;
import com.java.zu26.search.SearchActivity;
import com.java.zu26.util.SpeechUtils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button start = (Button) findViewById(R.id.button1);
//        startActivity(new Intent(this, SearchActivity.class));
//        startActivity(new Intent(this, CategoryActivity.class));
//        startActivity(new Intent(this, FavoriteActivity.class));

//        SpeechUtils speechUtils = SpeechUtils.getsSpeechUtils(this);
//        speechUtils.speak("这是一个晴朗的早晨，歌唱声伴着起床好阴");
        startActivity(new Intent(this, NewsListActivity.class));
    }
}
/*
private Button start, pause, resume, stop;
    private EditText content;
    private SpeechUtils speechUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.btn_start);
        pause = (Button) findViewById(R.id.btn_pause);
        resume = (Button) findViewById(R.id.btn_resume);
        stop = (Button) findViewById(R.id.btn_stop);
        content = (EditText) findViewById(R.id.et_content);

        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        resume.setOnClickListener(this);
        stop.setOnClickListener(this);

        speechUtils = SpeechUtils.getsSpeechUtils(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                if (!TextUtils.isEmpty(content.getText().toString())) {
                    speechUtils.speak(content.getText().toString());
                }
                break;
            case R.id.btn_pause:
                speechUtils.pause();
                break;
            case R.id.btn_resume:
                speechUtils.resume();
                break;
            case R.id.btn_stop:
                speechUtils.stop();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechUtils.stop();
        speechUtils.release();
    }
*/

