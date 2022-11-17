package com.jnu.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ExchangeText(View view){
        TextView LeftTextView;
        LeftTextView=findViewById(R.id.textView2);
        TextView RightTextView;
        RightTextView=findViewById(R.id.textView3);
        String temp=(String)RightTextView.getText();
        RightTextView.setText((String)LeftTextView.getText());
        LeftTextView.setText(temp);
    }
}