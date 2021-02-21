package com.pkd.commscreen;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkd.commscreen.adapter.list_kat_sajak;
import com.pkd.commscreen.adapter.list_menu_utama;
import com.pkd.commscreen.frag.susunKata;
import com.pkd.commscreen.model.data_kata;
import com.pkd.commscreen.tampilData.Makanan;
import com.pkd.commscreen.tampilData.Minuman;
import com.pkd.commscreen.tampilData.alatMakan;
import com.pkd.commscreen.tampilData.alatMandi;
import com.pkd.commscreen.tampilData.angka;
import com.pkd.commscreen.tampilData.buah;
import com.pkd.commscreen.tampilData.hewan;
import com.pkd.commscreen.tampilData.keluarga;
import com.pkd.commscreen.tampilData.pakaian;
import com.pkd.commscreen.tampilData.perasaan;
import com.pkd.commscreen.tampilData.sayuran;
import com.pkd.commscreen.tampilData.transportasi;
import com.pkd.commscreen.tampilData.tubuh;
import com.pkd.commscreen.tampilData.ungkapan;
import com.pkd.commscreen.tampilData.user;
import com.pkd.commscreen.tampilData.verba;
import com.pkd.commscreen.tampilData.warna;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    private static DatabaseReference reference;
    private static ArrayList<data_kata> listmenu, listsajak;
    list_menu_utama adapter;
    list_kat_sajak adapter1;
    Context context;
    MediaPlayer mediaPlayer;

    private static FirebaseAuth auth;
    private RecyclerView verbaRec, sajakrec;
    private GridLayoutManager mManager;
    ImageButton imagebuton;
    TextView txtsusun;

    private final String susun = null;

    private String KEY_NAME ="NAMA";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_home);

        getMenu();

        getSasjak();

        if (verbaRec == null){
            txtsusun.setText ("Susun Disini");
        } else {

            FragmentManager fm = getSupportFragmentManager ();
            FragmentTransaction ft = fm.beginTransaction ();
            ft.replace (R.id.susunkata, new susunKata ());
            ft.commit ();
        }

    }

    private void getSasjak() {

        sajakrec = findViewById (R.id.data_sajak);

        imagebuton = findViewById (R.id.gambar_sajak);
        sajakrec.setHasFixedSize (true);

        GetDatasajak ();
        listsajak = new ArrayList<> ();
        GridLayoutManager gridLayoutManager = new GridLayoutManager (context, 2, GridLayoutManager.HORIZONTAL, false);
        sajakrec.setLayoutManager (gridLayoutManager);
        sajakrec.setHasFixedSize (true);
    }

    private void GetDatasajak() {
        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child("Kategori Sajak")
                .addValueEventListener(new ValueEventListener () {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList

                        listsajak = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            data_kata mahasiswa = snapshot.getValue(data_kata.class);
                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            mahasiswa.setKey(snapshot.getKey());
                            listsajak.add(mahasiswa);

                        }
                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter1 = new list_kat_sajak (listsajak, Home.this);
                        //Memasang Adapter pada RecyclerView

                        sajakrec.setAdapter(adapter1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void getMenu() {
        auth = FirebaseAuth.getInstance ();
        verbaRec = findViewById(R.id.data_menu);

        imagebuton = findViewById (R.id.gambar_menu);
        verbaRec.setHasFixedSize (true);

        GetDatamenu ();
        listmenu = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager (context,3, GridLayoutManager.HORIZONTAL,false);
        verbaRec.setLayoutManager(gridLayoutManager);
        verbaRec.setHasFixedSize(true);
    }

    private void GetDatamenu() {
        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child("Kategori Menu")
                .addValueEventListener(new ValueEventListener () {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList

                        listmenu = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            data_kata mahasiswa = snapshot.getValue(data_kata.class);
                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            mahasiswa.setKey(snapshot.getKey());
                            listmenu.add(mahasiswa);

                        }
                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new list_menu_utama (listmenu, Home.this);
                        //Memasang Adapter pada RecyclerView

                        verbaRec.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //cek toolbar menu histori dan pengaturan
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }


    public void histori(MenuItem item) {
        Intent intent = new Intent (Home.this, histori.class);
        startActivity(intent);
    }

    public void pengaturan(MenuItem item) {
        Intent intent = new Intent(Home.this, pengaturan.class);
        startActivity(intent);
    }

    public void ketik(MenuItem item) {
        Intent intent = new Intent(Home.this, ketik.class);
        startActivity(intent);
    }

    public void _verba(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new verba ())
                .addToBackStack(null)
                .commit();


    }

    public void susunsini(View view) {
        FragmentManager fm = getSupportFragmentManager ();
        FragmentTransaction ft = fm.beginTransaction ();
        ft.replace (R.id.susunkata, new susunKata ());
        ft.commit ();
    }

    public void _perasaan(View view) {
          getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new perasaan ())
                .addToBackStack(null)
                .commit();
    }

    public void _keluarga(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new keluarga ())
                .addToBackStack(null)
                .commit();
    }

    public void _pakaian(View view) {

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new pakaian ())
                .addToBackStack(null)
                .commit();

    }

    public void _makanan(View view) {
        getSupportFragmentManager ()
                .beginTransaction ()
                .add (R.id.container, new Makanan ())
                .addToBackStack (null)
                .commit ();
    }

    public void _minum(View view) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new Minuman ())
                    .addToBackStack(null)
                    .commit();
    }

    public void _tubuh(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new tubuh ())
                .addToBackStack(null)
                .commit();
    }

    public void _warna(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new warna ())
                .addToBackStack(null)
                .commit();
    }

    public void _angka(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new angka ())
                .addToBackStack(null)
                .commit();
    }

    public void _alatmakan(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new alatMakan ())
                .addToBackStack(null)
                .commit();
    }

    public void _alatmandi(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new alatMandi ())
                .addToBackStack(null)
                .commit();
    }

    public void _buah(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new buah ())
                .addToBackStack(null)
                .commit();
    }

    public void _hewan(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new hewan ())
                .addToBackStack(null)
                .commit();
    }

    public void _transportasi(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new transportasi ())
                .addToBackStack(null)
                .commit();
    }

    public void _sayuran(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new sayuran ())
                .addToBackStack(null)
                .commit();
    }

    public void _ungkapan(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ungkapan ())
                .addToBackStack(null)
                .commit();
    }


    public void _user(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new user ())
                .addToBackStack(null)
                .commit();
    }
}