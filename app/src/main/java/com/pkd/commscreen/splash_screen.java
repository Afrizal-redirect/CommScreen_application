package com.pkd.commscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class splash_screen extends AppCompatActivity {
    private int waktu_loading=2000;

    //4000=4 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView (R.layout.activity_splash_screen);
        new Handler ().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent home=new Intent (splash_screen.this, login.class);
                startActivity(home);
                finish();

            }
        },waktu_loading);
    }
}