package com.pkd.commscreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.google.android.material.snackbar.Snackbar;

public class peng_kritsar extends AppCompatActivity {

    private AppCompatRatingBar penilaian;
    private Button btRating;
    private TextView rate;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_peng_kritsar);

        penilaian = findViewById (R.id.penilaian);
        btRating = findViewById (R.id.submit);
        rate = findViewById (R.id.rate);

        penilaian.setOnRatingBarChangeListener (new RatingBar.OnRatingBarChangeListener () {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate.setText ("Rate : "+rating);
            }
        });

        btRating.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Email someone ...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                String addresses="kreaspeech@gmail.com";
                String subject="Hallo Commscreen";

                intent = new Intent(Intent.ACTION_SENDTO);
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