package com.example.ushiama.gsapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ushiama.gsapp.R.id;

/**
 * Created by ushiama on 2017/01/21.
 */

    public class ButtonPartActivity extends Activity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_web);

            Button button = (Button) findViewById(id.button);
            // ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ボタンがクリックされた時に呼び出されます
                    finish();
                    //Button button = (Button) v;
                    //Toast.makeText(ButtonPartActivity.this, "onClick()",
                     //       Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
