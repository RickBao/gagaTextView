package com.nick.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nick.gagatext.gagaText;

public class MainActivity extends AppCompatActivity {
    private gagaText mGagaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGagaText = (gagaText) findViewById(R.id.superTextView);
        mGagaText.setDynamicText("天青色等烟雨，而我在等你");
        mGagaText.setOnDynamicListener(new gagaText.OnDynamicListener() {
            @Override
            public void onChange(int position) {
                Log.e("nick", " onChange:" + position);
            }

            @Override
            public void onCompile() {
                Log.e("nick", "onCompile");
            }
        });
    }

    private void clickByDynamicStyle(gagaText.DynamicStyle style) {
        mGagaText.setDynamicStyle(style);
        mGagaText.start();
    }

    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                clickByDynamicStyle(gagaText.DynamicStyle.NORMAL);
                break;
            case R.id.btn2:
                clickByDynamicStyle(gagaText.DynamicStyle.TYPEWRITING);
                break;
            case R.id.btn3:
                clickByDynamicStyle(gagaText.DynamicStyle.CHANGE_COLOR);
                break;
        }
    }
}
