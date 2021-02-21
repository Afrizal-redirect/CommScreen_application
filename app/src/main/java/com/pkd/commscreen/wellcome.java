package com.pkd.commscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class wellcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_wellcome);
    }

    public void setuju(View view) {
        Intent intent = new Intent(wellcome.this, Home.class);
        startActivity(intent);
    }
}