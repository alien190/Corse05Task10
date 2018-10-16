package com.example.alien.corse05task10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mBtnIncrease;
    private Button mBtnDecrease;
    private CustomCounter mCustomCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnIncrease = findViewById(R.id.btnIncrease);
        mBtnIncrease.setOnClickListener(this::onClick);
        mBtnDecrease = findViewById(R.id.btnDecrease);
        mBtnDecrease.setOnClickListener(this::onClick);
        mCustomCounter = findViewById(R.id.customCounter);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnIncrease: {
                mCustomCounter.increase();
                break;
            }
            case R.id.btnDecrease: {
                mCustomCounter.decrease();
                break;
            }
        }
    }
}
