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

public class list_kat_pakaian extends RecyclerView.Adapter<list_kat_pakaian.ViewHolder> {

    //musik variabel
    private static final int UPDATE_AUDIO_PROGRESS_BAR = 3;
    // Used to control audio (start, pause , stop etc).
    private MediaPlayer audioPlayer;

    Activity mActivity;
    // Record whether audio is playing or not.
    private boolean audioIsPlaying = false;
    private FirebaseAuth auth;

    //Deklarasi Variable
    private final ArrayList<data_kata> listkata;

    private SimpleDateFormat dateFormat;
    private String currentTimeStamp;

    private DatabaseReference ref;
    private final StorageReference reference;

    private Activity perasaan;

    @NonNull
    @Override
    public list_kat_pakaian.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from (parent.getContext ()).inflate (R.layout.pakaian_lay, parent, false);
        return new ViewHolder (V);
    }

    @Override
    public void onBindViewHolder(@NonNull list_kat_pakaian.ViewHolder holder, int position) {

        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu

        final String nama = listkata.get (position).getNamaGambar ();
        final String namasound = listkata.get (position).getSoundUrl ();
        final String gambar = "Gambar/"+nama+ ".png";


        // final String namasound = listkata.get (position).getSoundUrl ();

        getImage ("Gambar/" + nama + ".png", holder.gambar_verba);
        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.etnama.setText (listkata.get (position).getNamaGambar ());


        audioPlayer = new MediaPlayer();
        audioPlayer.setAudioAttributes (
                new AudioAttributes
                        .Builder ()
                        .setContentType (AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build ());

        holder.gambar_verba.setOnClickListener (new View.OnClickListener () {
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

    //Membuat Konstruktor, untuk menerima input dari Database
    public list_kat_pakaian(ArrayList listKata, Activity mActivity) {
        this.listkata = listKata;
        this.mActivity = mActivity;

        ref = FirebaseDatabase.getInstance ().getReference ().child ("Kategori Pakaian");
        reference = FirebaseStorage.getInstance ().getReference ();

    }

    public void getImage(String data, final ImageView gambar_pakaian) {

        reference.child (data).getDownloadUrl ().addOnSuccessListener (new OnSuccessListener<Uri> () {
            @Override
            public void onSuccess(Uri uri) {
                if (mActivity == null) {
                    return;
                }
                Glide.with (mActivity).load (uri).into (gambar_pakaian);
            }
        }).addOnFailureListener (new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return listkata.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView etnama;

        ImageView gambar_verba;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            etnama = itemView.findViewById (R.id.etnama);

            gambar_verba = itemView.findViewById (R.id.gambar_pakaian);
        }
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
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute (aBoolean);
            audioPlayer.start ();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

    }
}
