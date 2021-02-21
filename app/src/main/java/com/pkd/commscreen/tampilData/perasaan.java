package com.pkd.commscreen.tampilData;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
import com.pkd.commscreen.adapter.lis_kat_perasaan;
import com.pkd.commscreen.model.data_kata;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link perasaan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class perasaan extends Fragment {

    private static DatabaseReference reference;
    private static ArrayList<data_kata> listkata;
    lis_kat_perasaan adapter;
    Context context;
    MediaPlayer mediaPlayer;
    private Activity mActivity;
    private static FirebaseAuth auth;
    private RecyclerView verbaRec;
    private GridLayoutManager mManager;
    ImageButton imagebuton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public perasaan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment perasaab.
     */
    // TODO: Rename and change types and number of parameters
    public static perasaan newInstance(String param1, String param2) {
        perasaan fragment = new perasaan ();
        Bundle args = new Bundle ();
        args.putString (ARG_PARAM1, param1);
        args.putString (ARG_PARAM2, param2);
        fragment.setArguments (args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        if (getArguments () != null) {
            mParam1 = getArguments ().getString (ARG_PARAM1);
            mParam2 = getArguments ().getString (ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_perasaab, container, false);
        auth = FirebaseAuth.getInstance ();
        verbaRec = view.findViewById(R.id.data_perasaan);

        imagebuton = view.findViewById (R.id.gambar_perasaan);
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
        reference.child("Admin").child("Kategori Perasaan")
                .addValueEventListener(new ValueEventListener () {
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
                        adapter = new lis_kat_perasaan(listkata, mActivity);
                        //Memasang Adapter pada RecyclerView

                        verbaRec.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }



}