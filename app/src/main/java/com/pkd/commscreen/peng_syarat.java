package com.pkd.commscreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class peng_syarat extends AppCompatActivity {
    Button kirim;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_peng_syarat);

        kirim =findViewById (R.id.kirim);

        kirim.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String addresses="kreaspeech@gmail.com";
                String subject="Hallo Commscreen";

                intent = new Intent (Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }
}