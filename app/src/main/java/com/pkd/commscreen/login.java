package com.pkd.commscreen;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;

public class login extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private Button logingoogle,loginoke, log;
    private FirebaseAuth auth;
    private EditText username_key, password_Key;

    //Membuat Kode Permintaan
    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        progressBar = findViewById (R.id.progress);
        progressBar.setVisibility (View.GONE);

        username_key = findViewById (R.id.usernameKey);
        password_Key = findViewById (R.id.passwordKey);
        loginoke = findViewById (R.id.loginoke);
        loginoke.setOnClickListener (this);
        log = findViewById (R.id.log);
        log.setOnClickListener (this);
        logingoogle = findViewById (R.id.logingoogle);
        logingoogle.setOnClickListener (this);

        auth = FirebaseAuth.getInstance ();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        // RC_SIGN_IN adalah kode permintaan yang Anda berikan ke startActivityForResult, saat memulai masuknya arus.
        if (requestCode == RC_SIGN_IN) {

            //Berhasil masuk
            if (resultCode == RESULT_OK) {
                Toast.makeText (login.this, "Login Berhasil", Toast.LENGTH_SHORT).show ();
                Intent intent = new Intent (login.this, wellcome.class);
                startActivity (intent);

            } else {
                progressBar.setVisibility (View.GONE);
                Toast.makeText (login.this, "Login Dibatalkan", Toast.LENGTH_SHORT).show ();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId ()) {
            case R.id.logingoogle:
                // Statement program untuk login/masuk
                startActivityForResult (AuthUI.getInstance ()
                                .createSignInIntentBuilder ()

                                //Memilih Provider atau Method masuk yang akan kita gunakan
                                .setAvailableProviders (Collections.singletonList (new AuthUI.IdpConfig.GoogleBuilder ().build ()))
                                .setIsSmartLockEnabled (false)
                                .build (),
                        RC_SIGN_IN);
                progressBar.setVisibility (View.VISIBLE);
                break;

            case R.id.loginoke:
                String usernameKey = username_key.getText().toString();
                String passwordKey = password_Key.getText().toString();

                if (usernameKey.equals("kreaspeech@gmail.com") && passwordKey.equals("1q2w3e4r5t6y*")){
                    //jika login berhasil
                    Toast.makeText(getApplicationContext(), "LOGIN SUKSES ADMIN",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(login.this, Home.class);
                    startActivity(intent);
                    finish();
                }else {
                    //jika login gagal
                    AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                    builder.setMessage("Username atau Password Anda Belum Terdaftar, Silahkan Login Dengan Akun Google")
                            .setNegativeButton("Retry", null).create().show();
                }
                break;
                
            case R.id.log :
                Exit();
                break;

        }
    }

    private void Exit() {
        finish ();
    }
}