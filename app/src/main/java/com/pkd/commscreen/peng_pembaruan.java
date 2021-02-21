package com.pkd.commscreen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class peng_pembaruan extends AppCompatActivity {

    private ProgressBar progressBar;
    TextView txt_pembaruan;
    Button btn_pembaruan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_peng_pembaruan);

        txt_pembaruan = findViewById (R.id.txtpembaruan);
        btn_pembaruan = findViewById (R.id.btn_pembaruan);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
    }


}