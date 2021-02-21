package com.pkd.commscreen.tampilData;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkd.commscreen.R;
import com.pkd.commscreen.adapter.list_kat_verba;
import com.pkd.commscreen.model.data_kata;

import java.util.ArrayList;

public class verba extends Fragment {

    private static DatabaseReference reference;
    private static ArrayList<data_kata> listkata;
    list_kat_verba adapter ;
    Context context;
    MediaPlayer mediaPlayer;
    private Activity mActivity;
    private static FirebaseAuth auth;
    private RecyclerView verbaRec;
    private GridLayoutManager mManager;
    ImageButton imagebuton, image_user;


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

        View view = inflater.inflate (R.layout.fragment_verba, container, false);
        auth = FirebaseAuth.getInstance ();
        verbaRec = view.findViewById(R.id.data_verba);
        imagebuton = view.findViewById (R.id.gambar_verba);

        verbaRec.setHasFixedSize (true);
        GetData ();
        listkata = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager (mActivity,5, GridLayoutManager.VERTICAL,false);
        verbaRec.setLayoutManager(gridLayoutManager);
        verbaRec.setHasFixedSize(true);

        // Inflate the layout for this fragment
        return view;
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

    private void GetData() {
        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child("Kategori Verba")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        if (mActivity == null) {
                            return;
                        }
                        listkata = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            data_kata mahasiswa = snapshot.getValue(data_kata.class);
                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            mahasiswa.setKey(snapshot.getKey());
                            listkata.add(mahasiswa);

                        }
                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new list_kat_verba (listkata, mActivity);
                        //Memasang Adapter pada RecyclerView

                        verbaRec.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

}


