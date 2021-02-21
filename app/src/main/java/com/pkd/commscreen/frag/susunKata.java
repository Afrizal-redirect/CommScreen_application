package com.pkd.commscreen.frag;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkd.commscreen.R;
import com.pkd.commscreen.adapter.susun_kata;
import com.pkd.commscreen.model.data_kata;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link susunKata#newInstance} factory method to
 * create an instance of this fragment.
 */
public class susunKata extends Fragment {

    private FirebaseDatabase getDatabase;
    private DatabaseReference getRefenence;

    //susunkata
    private FirebaseAuth auth;
    private MediaPlayer audioPlayer;
    private boolean audioIsPlaying = false;
    RecyclerView rv;
    susun_kata susunkata;
    List<data_kata> datakata;
    LinearLayoutManager mManager, okesond;
    private Activity mActivity;
    Context context;

    boolean firstAttempt = true;

    ImageButton play_kata, hapussemuakata;


    public susunKata() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }


    // TODO: Rename and change types and number of parameters
    public static susunKata newInstance(String param1, String param2) {
        susunKata fragment = new susunKata ();
        Bundle args = new Bundle ();

        fragment.setArguments (args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        if (getArguments () != null) {
            // This callback will only be called when MyFragment is at least Started.
            OnBackPressedCallback callback = new OnBackPressedCallback (true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    // Handle the back button event
                }
            };
            requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

            // The callback can be enabled or disabled here or in handleOnBackPressed()
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_susun_kata, container, false);

        play_kata = view.findViewById (R.id.play_kata);
        hapussemuakata = view.findViewById (R.id.hapussemuakata);

        //btn hapus
        hapussemuakata.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                DatabaseReference reference;
                reference = FirebaseDatabase.getInstance().getReference();
                String getUserID = auth.getCurrentUser ().getUid ();
                reference.child("User").child (getUserID).child("Susun Kata").removeValue ();
            }
        });


        play_kata.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View v) {
                audioPlayer = new MediaPlayer();
                audioPlayer.setAudioAttributes (
                        new AudioAttributes
                                .Builder ()
                                .setContentType (AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build ());

                DatabaseReference ref;
                ref = FirebaseDatabase.getInstance().getReference();
                String getUserID = auth.getCurrentUser ().getUid ();
                ref.child("User").child (getUserID).child("Susun Kata").orderByKey ();

                for (data_kata song : datakata){
                    String oke = song.getSoundUrl ();
                   new initAudioPlayer ().execute (song.getSoundUrl ());
                   audioIsPlaying = true;
                }


            }
        });


        //susunkata
        rv=(RecyclerView)view.findViewById(R.id.datasusunkata);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager (mActivity));

        //ambilkata
        getsusunkataData();
        //susun ke recy
        datakata = new ArrayList<> ();
        mManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL,false);
        rv.setLayoutManager(mManager);

        rv.setHasFixedSize(true);

        return view;

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


    private void getsusunkataData() {
        //susunkata=new susun_kata(datakata,this);
        //rv.setAdapter(susunkata);

        DatabaseReference ref;
        DatabaseReference reference;
        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child("Susun Kata")
                .addValueEventListener(new ValueEventListener () {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        datakata = new ArrayList<> ();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                            data_kata Susunkata = snapshot.getValue(data_kata.class);

                            Susunkata.setKey(snapshot.getKey());
                            datakata.add(Susunkata);


                        }

                        susunkata = new susun_kata (datakata, mActivity);

                        rv.setAdapter(susunkata);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


}