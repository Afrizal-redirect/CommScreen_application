package com.pkd.commscreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pkd.commscreen.model.data_kata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class peng_tambah_gambar_user extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private EditText nama_gambar;
    private TextView mRecordLabel;
    private ImageView ImageContainer;
    private FirebaseAuth auth;
    private Button btn_simpan, select_image, berkas;

    private SimpleDateFormat dateFormat;
    private String currentTimeStamp;

    //deklarasi record
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean checkPermission = false;
    private static String fileName = null;

    private Button recordButton;
    private Button stoprecord;
    private Button stopplaying;

    private boolean uploadsound;

    private String Ya, Kembali;
    private MediaRecorder recorder = null;
    Uri uri;
    String namaGambar, sound, soundUrl;


    private Button playButton = null;
    private MediaPlayer player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    //Deklarasi Variable StorageReference
    private StorageReference reference;

    //Kode permintaan untuk memilih metode pengambilan gamabr
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    private static final int UPLOAD_RECORD = 4;

    private int RC_SIGN_IN = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish ();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_peng_tambah_gambar_user);

        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir ().getAbsolutePath ();

        fileName += "/records.mp3";

        ActivityCompat.requestPermissions (this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        //Mendapatkan Referensi dari Firebase Storage
        reference = FirebaseStorage.getInstance ().getReference ();

        mRecordLabel = findViewById (R.id.mrecordlabel);
        nama_gambar = findViewById (R.id.etnama);
        ImageContainer = findViewById (R.id.imageContainer);
        select_image = findViewById (R.id.select_image);
        select_image.setOnClickListener (this);
        btn_simpan = findViewById (R.id.btn_simpan);
        btn_simpan.setOnClickListener (this);
        berkas = findViewById (R.id.btn_upload);
        berkas.setOnClickListener (this);
        recordButton = findViewById (R.id.btn_record);
        recordButton.setOnClickListener (this);
        playButton = findViewById (R.id.btn_play);
        playButton.setOnClickListener (this);
        stoprecord = findViewById (R.id.stoprecord);
        stoprecord.setOnClickListener (this);
        stopplaying = findViewById (R.id.stopplay);
        stopplaying.setOnClickListener (this);
        btn_simpan.setOnClickListener (this);
        progressBar = findViewById (R.id.progress);
        progressBar.setVisibility (View.GONE);

        auth = FirebaseAuth.getInstance (); //Mendapakan Instance Firebase Autentifikasi

        //inialisasi edit teks
        nama_gambar = findViewById (R.id.etnama);

        berkas.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (validatePermision ()) {
                    pickSong ();
                }
            }
        });

        if (auth.getCurrentUser () == null) {
            defaultUI ();
        } else {
            updateUI ();
        }


    }

    private void pickSong() {

        Intent intent_upload = new Intent ();
        intent_upload.setType ("audio/*");
        intent_upload.setAction (Intent.ACTION_GET_CONTENT);
        startActivityForResult (intent_upload, 3);

    }


    private void stopRecording() {
        recorder.stop ();
        recorder.release ();
        recorder = null;
        recordButton.setEnabled (true);
        stoprecord.setEnabled (true);
        playButton.setEnabled (true);
        stopplaying.setEnabled (false);
        mRecordLabel.setText ("Berhenti Merekam");
    }

    private void startRecording() {
        recordButton.setEnabled (true);
        stoprecord.setEnabled (true);
        playButton.setEnabled (false);
        stopplaying.setEnabled (false);

        recorder = new MediaRecorder ();
        recorder.setAudioSource (MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat (MediaRecorder.OutputFormat.DEFAULT);
        recorder.setOutputFile (fileName);
        recorder.setAudioEncoder (MediaRecorder.AudioEncoder.AMR_WB);

        try {
            recorder.prepare ();
        } catch (IOException e) {
            Log.e (LOG_TAG, "prepare() failed");
        }

        recorder.start ();
        mRecordLabel.setText ("Mulai Merekam");

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording ();
        } else {
            stopRecording ();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying ();
        } else {
            stopPlaying ();
        }
    }

    private void startPlaying() {
        recordButton.setEnabled (false);
        stoprecord.setEnabled (false);
        playButton.setEnabled (true);
        stopplaying.setEnabled (true);
        player = new MediaPlayer ();
        try {
            player.setDataSource (fileName);
            player.prepare ();
            player.start ();
        } catch (IOException e) {
            Log.e (LOG_TAG, "prepare() failed");
        }
        mRecordLabel.setText ("Memutar Rekaman");
    }

    private void stopPlaying() {
        recordButton.setEnabled (true);
        stoprecord.setEnabled (false);
        playButton.setEnabled (true);
        stopplaying.setEnabled (true);
        player.release ();
        player = null;
        mRecordLabel.setText ("Berhenti Merekam");
    }

    class RecordButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartRecording = true;

        OnClickListener clicker = v -> {
            onRecord (mStartRecording);
            if (mStartRecording) {
                setText ("Stop recording");
            } else {
                setText ("Start recording");
            }
            mStartRecording = !mStartRecording;
        };

        public RecordButton(Context ctx) {
            super (ctx);
            setText ("Start recording");
            setOnClickListener (clicker);
        }
    }

    class PlayButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = v -> {
            onPlay (mStartPlaying);
            if (mStartPlaying) {
                setText ("Stop playing");
            } else {
                setText ("Start playing");
            }
            mStartPlaying = !mStartPlaying;
        };

        public PlayButton(Context ctx) {
            super (ctx);
            setText ("Start playing");
            setOnClickListener (clicker);
        }
    }

    private void defaultUI() {
        select_image.setEnabled (true);
        btn_simpan.setEnabled (true);
        nama_gambar.setEnabled (true);
    }

    private void updateUI() {
        select_image.setEnabled (true);
        btn_simpan.setEnabled (true);
        nama_gambar.setEnabled (true);
        progressBar.setVisibility (View.GONE);

    }

    private boolean validatePermision() {
        Dexter.withActivity (peng_tambah_gambar_user.this)
                .withPermission (Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener (new PermissionListener () {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        checkPermission = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        checkPermission = false;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest ();
                    }

                }).check ();

        return checkPermission;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()) {
            case R.id.btn_record:
                startRecording ();
                break;
            case R.id.btn_play:
                startPlaying ();
                break;
            case R.id.stoprecord:
                stopRecording ();
                break;
            case R.id.stopplay:
                stopPlaying ();
                break;
            case R.id.select_image:
                getImage ();
                break;
            case R.id.btn_simpan:

                //mendapatkan userID dari pengguna
                String getUserID = auth.getCurrentUser ().getUid ();
                //mendapatkan instance dari database
                FirebaseDatabase database = FirebaseDatabase.getInstance ();
                DatabaseReference getReference;

                //menyimpan data yang diinputkan kedalam variabel
                String NamaGambar = nama_gambar.getText ().toString ();
                getReference = database.getReference (); //mendapatkan refrensi

                ImageContainer.setDrawingCacheEnabled (true);
                ImageContainer.buildDrawingCache ();
                Bitmap bitmap = ((BitmapDrawable) ImageContainer.getDrawable ()).getBitmap ();
                ByteArrayOutputStream stream = new ByteArrayOutputStream ();

                //Mengkompress bitmap menjadi JPG dengan kualitas gambar 100%
                bitmap.compress (Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray ();

                //Lokasi lengkap dimana gambar akan disimpan
                String namaFile = NamaGambar;
                String pathImage = "Gambar/" + namaFile + ".png";
                UploadTask uploadTask = reference.child (pathImage).putBytes (bytes);
                namaGambar = nama_gambar.getText ().toString ();
                data_kata mahasiswa = new data_kata (namaGambar, sound, soundUrl);
                getReference.child ("User").child (getUserID).child ("Kategori User").push ().setValue (mahasiswa).addOnSuccessListener (this, new OnSuccessListener () {
                    @Override
                    public void onSuccess(Object o) {
                        nama_gambar.setText ("");
                        Toast.makeText (peng_tambah_gambar_user.this, "Data Berhasil Tersimpan", Toast.LENGTH_SHORT).show ();
                        Intent intent = new Intent (peng_tambah_gambar_user.this, pengaturan.class);
                        startActivity (intent);
                    }
                });
                break;
        }
    }

    private void getImage() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        //Method ini digunakan untuk mengambil gambar dari Kamera
        CharSequence[] menu = {"Kamera", "Galeri"};
        AlertDialog.Builder dialog = new AlertDialog.Builder (this)
                .setTitle ("Tambah Gambar")
                .setItems (menu, new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //Mengambil gambar dari Kemara ponsel
                                Intent imageIntentCamera = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult (imageIntentCamera, REQUEST_CODE_CAMERA);
                                break;

                            case 1:
                                //Mengambil gambar dari galeri
                                Intent imageIntentGallery = new Intent (Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult (imageIntentGallery, REQUEST_CODE_GALLERY);
                                break;
                        }
                    }
                });
        dialog.create ();
        dialog.show ();
    }

    private boolean isEmpty(String s) {
        return TextUtils.isEmpty (s);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    ImageContainer.setVisibility (View.VISIBLE);
                    Bitmap bitmap = (Bitmap) data.getExtras ().get ("data");
                    ImageContainer.setImageBitmap (bitmap);
                }
                break;

            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    ImageContainer.setVisibility (View.VISIBLE);
                    Uri uri = data.getData ();
                    ImageContainer.setImageURI (uri);
                }
                break;
        }

        //upload berkas
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {

                uri = data.getData ();

                Cursor mcursor = getApplicationContext ().getContentResolver ()
                        .query (uri, null, null, null, null);

                int indexedname = ((Cursor) mcursor).getColumnIndex (OpenableColumns.DISPLAY_NAME);
                mcursor.moveToFirst ();
                sound = mcursor.getString (indexedname);
                mcursor.close ();

                uploadtofirebase ();

            }
        }

        //upload sound
        if (requestCode == 4) {
            if (resultCode == RESULT_OK) {
                String namasound = nama_gambar.getText ().toString ();
                //upload sound
                String soundFile = namasound + ".mp3";
                String pathsound = "audio/" + soundFile;

                StorageReference filepath = reference.child (pathsound);

                Uri uri = Uri.fromFile (new File (fileName));
                filepath.putFile (uri).addOnSuccessListener (new OnSuccessListener<UploadTask.TaskSnapshot> () {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });

            }
        }

        super.onActivityResult (requestCode, resultCode, data);
    }

    private void uploadtofirebase() {

        StorageReference storageReference = FirebaseStorage.getInstance ().getReference ()
                .child ("Audio").child (uri.getLastPathSegment ());
        mRecordLabel.setText ("Berhasil Upload Sound");

        storageReference.putFile (uri).addOnSuccessListener (new OnSuccessListener<UploadTask.TaskSnapshot> () {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage ().getDownloadUrl ();
                while (!uriTask.isComplete ()) ;
                Uri urlSong = uriTask.getResult ();
                soundUrl = urlSong.toString ();

            }
        });

    }


    @Override
    public void onStop() {
        super.onStop ();
        if (recorder != null) {
            recorder.release ();
            recorder = null;
        }

        if (player != null) {
            player.release ();
            player = null;
        }
    }
}