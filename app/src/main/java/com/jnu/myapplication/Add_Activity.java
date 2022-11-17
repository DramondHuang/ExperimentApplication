package com.jnu.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Objects;

public class Add_Activity extends AppCompatActivity {
    Intent intent;
    EditText editName;
    int mresultcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        intent=getIntent();
        mresultcode =1;
        editName=(EditText)findViewById(R.id.editTextName);
        if(Objects.equals(intent.getStringExtra("Action"), "Edit")) {
            String name = intent.getStringExtra("name");
            editName.setText(name);
            mresultcode =2;
        }
    }

    public void OK(View view){
        intent.putExtra("book_name", editName.getText().toString());// 把返回数据存入Intent
        Add_Activity.this.setResult(mresultcode, intent);// 设置回传数据。resultCode这个值在主窗口将用来区分回传数据的来源，以做不同的处理
        Add_Activity.this.finish();// 关闭子窗口ChildActivity
    }

    public void Cancel(View view){
        Add_Activity.this.finish();// 关闭子窗口ChildActivity
    }
}