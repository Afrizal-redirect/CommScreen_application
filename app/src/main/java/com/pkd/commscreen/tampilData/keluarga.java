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
import com.pkd.commscreen.adapter.list_kata_keluarga;
import com.pkd.commscreen.model.data_kata;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link keluarga#newInstance} factory method to
 * create an instance of this fragment.
 */
public class keluarga extends Fragment {

    private static DatabaseReference reference;
    private static ArrayList<data_kata> listkata;
    list_kata_keluarga adapter;
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

    public keluarga() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment keluarga.
     */
    // TODO: Rename and change types and number of parameters
    public static keluarga newInstance(String param1, String param2) {
        keluarga fragment = new keluarga ();
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
        View view = inflater.inflate (R.layout.fragment_keluarga, container, false);
        auth = FirebaseAuth.getInstance ();
        verbaRec = view.findViewById(R.id.data_keluarga);

        imagebuton = view.findViewById (R.id.gambar_keluarga);
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
        reference.child("Admin").child("Kategori Keluarga")
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
                        adapter = new list_kata_keluarga (listkata, mActivity);
                        //Memasang Adapter pada RecyclerView

                        verbaRec.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

}