package com.pkd.commscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class peng_tentang extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_peng_tentang);
    }

    public void kebijakan(View view) {
        Intent intent = new Intent(peng_tentang.this, peng_kebijakan.class);
        startActivity(intent);
    }

    public void term(View view) {
        Intent intent = new Intent(peng_tentang.this, peng_term.class);
        startActivity(intent);
    }

    public void Syaratketentuan(View view) {
        Intent intent = new Intent(peng_tentang.this, peng_syarat.class);
        startActivity(intent);
    }

    public void faq(View view) {
        Intent intent = new Intent(peng_tentang.this, peng_faq.class);
        startActivity(intent);
    }

}