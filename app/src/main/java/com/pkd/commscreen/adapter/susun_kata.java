package com.pkd.commscreen.adapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pkd.commscreen.R;
import com.pkd.commscreen.model.data_kata;

import java.util.ArrayList;
import java.util.List;

public class susun_kata  extends RecyclerView.Adapter<susun_kata.ViewHolder> {
    private List<data_kata>datakata;
    private Activity mActivity;
    private DatabaseReference ref;
    private StorageReference reference;
    private Button play_kata;
    MediaPlayer audioPlayer;
    private boolean audioIsPlaying = false;
    ImageButton hapuskata, hapussemuakata;
    JcPlayerView jcPlayerView;
    ListView listView;
    private RecyclerView verbaRec;
    private static FirebaseAuth auth;
    Toast toast;

    ArrayList<JcAudio> jcAudios = new ArrayList<> ();

    public susun_kata(List<data_kata> datakata, Activity mActivity) {
        this.datakata = datakata;
        this.mActivity = mActivity;

        ref = FirebaseDatabase.getInstance().getReference();
        ref = FirebaseDatabase.getInstance ().getReference ().child ("Susun Kata");
        reference = FirebaseStorage.getInstance ().getReference ();

    }




    //Deklarasi objek dari Interfece

    @NonNull
    @Override
    public susun_kata.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.susun_lay,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull susun_kata.ViewHolder holder, int position) {

        final String url;
        final data_kata datasusun = datakata.get (position);
        Glide.with(mActivity).load(datakata.get(position).getGambar ()).into(holder.gambarSusun);
       // holder.namaGambar.setText (datakata.get (position).getNamaGambar ());

        final String nama = datakata.get (position).getNamaGambar ();
        final String namasound = datakata.get (position).getSoundUrl ();
        final String gambar = "Gambar/"+nama+ ".png";

        getImage ("Gambar/" + nama + ".png", holder.gambarSusun);
        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.namaGambar.setText (datakata.get (position).getNamaGambar ());
        holder.namasounds.setText (datakata.get (position).getSoundUrl ());

        final data_kata dataKata = datakata.get (position);
        url = dataKata.getSoundUrl ();



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



    //Membuat Interfece
    public interface dataListener{
        void onDeleteData(data_kata data, int position);
    }


    public void getImage(String data, final ImageView gambar_img) {

        reference.child (data).getDownloadUrl ().addOnSuccessListener (new OnSuccessListener<Uri> () {
            @Override
            public void onSuccess(Uri uri) {
                if (mActivity == null) {
                    return;
                }
                Glide.with (mActivity).load (uri).into (gambar_img);
            }
        }).addOnFailureListener (new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return datakata.size ();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView gambarSusun;
        private TextView namaGambar;
        private TextView namasounds;
        private ImageButton play_kata;
        private final LinearLayout ListItem ;


        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            gambarSusun=(ImageView) itemView.findViewById(R.id.gambar_img);
            ListItem = itemView.findViewById (R.id.list_item);
            namaGambar=(TextView)itemView.findViewById(R.id.etnama);
            namasounds=(TextView)itemView.findViewById(R.id.etsound);
            play_kata=(ImageButton)itemView.findViewById (R.id.play_kata);
            hapussemuakata = itemView.findViewById (R.id.hapussemuakata);




        }
    }

}
