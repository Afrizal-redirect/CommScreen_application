package com.pkd.commscreen.adapter;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pkd.commscreen.R;
import com.pkd.commscreen.model.data_kata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class list_kata_keluarga extends RecyclerView.Adapter<list_kata_keluarga.ViewHolder> {

    //musik variabel
    private static final int UPDATE_AUDIO_PROGRESS_BAR = 3;
    // Used to control audio (start, pause , stop etc).
    private MediaPlayer audioPlayer;

    // Record whether audio is playing or not.
    private boolean audioIsPlaying = false;

    //Deklarasi Variable
    private final ArrayList<data_kata> listkata;

    private FirebaseAuth auth;
    private SimpleDateFormat dateFormat;
    private String currentTimeStamp;

    private DatabaseReference ref;
    private final StorageReference reference;

    private Activity mActivity;

    RecyclerView verbaRec;

    //variable untuk firebase storage

    @NonNull
    @Override
    public list_kata_keluarga.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from (parent.getContext ()).inflate (R.layout.keluarga_lay, parent, false);
        return new ViewHolder (V);
    }

    //Membuat Konstruktor, untuk menerima input dari Database
    public list_kata_keluarga(ArrayList listKata, Activity mActivity) {
        this.listkata = listKata;
        this.mActivity = mActivity;
        ref = FirebaseDatabase.getInstance ().getReference ().child ("Kategori Keluarga");
        reference = FirebaseStorage.getInstance ().getReference ();

    }

    public void getImage(String data, final ImageView gambar_keluarga) {

        reference.child (data).getDownloadUrl ().addOnSuccessListener (new OnSuccessListener<Uri> () {
            @Override
            public void onSuccess(Uri uri) {
                if (mActivity == null) {
                    return;
                }
                Glide.with (mActivity).load (uri).into (gambar_keluarga);
            }
        }).addOnFailureListener (new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    @Override
    public void onBindViewHolder(@NonNull list_kata_keluarga.ViewHolder holder, int position) {
//Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu

        final String nama = listkata.get (position).getNamaGambar ();
        final String namasound = listkata.get (position).getSoundUrl ();
        final String gambar = "Gambar/"+nama+ ".png";


        // final String namasound = listkata.get (position).getSoundUrl ();

        getImage ("Gambar/" + nama + ".png", holder.gambar_keluarga);
        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.etnama.setText (listkata.get (position).getNamaGambar ());


        audioPlayer = new MediaPlayer();
        audioPlayer.setAudioAttributes (
                new AudioAttributes
                        .Builder ()
                        .setContentType (AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build ());

        holder.gambar_keluarga.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                //apply musik
                String varsound = listkata.get (position).getSoundUrl ();
                new initAudioPlayer ().execute (varsound);
                audioIsPlaying = true;

                //add to susun kata
                data_kata dk = new data_kata ();
                dk.setNamaGambar (nama);
                dk.setSoundUrl (namasound);
                dk.setGambar (gambar);

                FirebaseDatabase database = FirebaseDatabase.getInstance ();
                DatabaseReference getReference;
                String getUserID = auth.getCurrentUser ().getUid ();
                getReference = database.getReference (); //mendapatkan refrensi
                getReference.child ("User").child (getUserID).child ("Susun Kata").push ().setValue (dk);

            }
        });
    }

    private class initAudioPlayer extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean prepared = false;

            try {

                audioPlayer.setDataSource (strings[0]);

                audioPlayer.setOnCompletionListener (new MediaPlayer.OnCompletionListener () {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        audioPlayer.stop ();
                        audioPlayer.reset ();

                    }
                });
                audioPlayer.prepare ();
                prepared = true;

            } catch (Exception e) {
                e.printStackTrace ();
                prepared = false;
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean){
            super.onPostExecute (aBoolean);
            audioPlayer.start ();

        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute ();
        }

    }

    @Override
    public int getItemCount() {
        return listkata.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView etnama;

        ImageView gambar_keluarga;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            etnama = itemView.findViewById (R.id.etnama);

            gambar_keluarga = itemView.findViewById (R.id.gambar_keluarga);

        }
    }
}
