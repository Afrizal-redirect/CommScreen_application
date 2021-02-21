package com.pkd.commscreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class pengaturan extends AppCompatActivity {
    Button btnHub;
    Intent intent;
    private FirebaseAuth auth;

    String usernameKey, passwordKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_pengaturan);

        btnHub = findViewById (R.id.hubkami);

        btnHub.setOnClickListener (new View.OnClickListener () {
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

    public void tampilan(View view) {
        Intent intent = new Intent(pengaturan.this, peng_tampilan.class);
        startActivity(intent);
    }

    public void tentangkami(View view) {
        Intent intent = new Intent(pengaturan.this, peng_tentang.class);
        startActivity(intent);
    }

    public void hapushistori(View view) {
        Intent intent = new Intent(pengaturan.this, peng_hapus_histori.class);
        startActivity(intent);
    }

    public void tambahgambar(View view) {

        auth = FirebaseAuth.getInstance(); //Mendapakan Instance Firebase Autentifikasi

        if(auth.getCurrentUser() != null){
            Intent intent = new Intent(pengaturan.this, peng_tambah_gambar_user.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(pengaturan.this, peng_tambah_gambar.class);
            startActivity(intent);
        }




    }


    public void hubkami(View view) {
        Intent intent = new Intent(pengaturan.this, peng_hub_kami.class);
        startActivity(intent);
    }

    public void kritsar(View view) {
        Intent intent = new Intent(pengaturan.this, peng_kritsar.class);
        startActivity(intent);
    }

    public void pembaruan(View view) {
        Intent intent = new Intent(pengaturan.this, peng_pembaruan.class);
        startActivity(intent);
    }

    public void logout(View view) {
        switch (view.getId()){
            case R.id.btn_logout:
                // Statement program untuk logout/keluar
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener () {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Keluar();
                            }
                        });
                break;
        }
    }

    private void Keluar() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.app_name)
                .setMessage("Logut Akun dari Aplikasi ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(pengaturan.this, login.class);
                        startActivity(intent);
                        Toast.makeText (pengaturan.this, "Logout berhasil", Toast.LENGTH_SHORT).show ();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
    }

