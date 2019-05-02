package com.example.examenuf2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.examenuf2.R.id.action_addSong;

public class MainActivity extends AppCompatActivity implements FragmentIncidencia.OnFragmentInteractionListener {

    private StorageReference mStorage;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    public ChildEventListener childEvent;

    private boolean initialStage = true;
    private boolean playPause;

    private ProgressDialog progressDialog;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(MainActivity.this);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        mStorage = FirebaseStorage.getInstance().getReference();
        childEvent = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);

                    guardarIncidencia(incidencia);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        obtenerIncidenciasBBDD();
        cargarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action, menu);
        return true;
    }

    public void guardarIncidencia(Incidencia incidencia){
        FragmentIncidencia fragment = (FragmentIncidencia) getSupportFragmentManager().findFragmentByTag("IncidenciaFragment");
        fragment.addIncidencia(incidencia);
    }

    private void obtenerIncidenciasBBDD() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("incidencias").addChildEventListener(childEvent);
    }

    private void cargarLista() {
        FragmentIncidencia fragment = FragmentIncidencia.newInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("incidencias").removeEventListener(childEvent);

        mDatabase.child("incidencias").addChildEventListener(childEvent);
        cargar_fragment(fragment, "IncidenciasFragment");

    }

    private void cargar_fragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.incidencias_container, fragment, tag).commit();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onBackPressed() {
        cargarLista();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_addSong:
                startActivity(new Intent(getApplicationContext(), AgregarIncidencia.class));
                break;
            case R.id.action_playSong:
                if(!playPause) {
                    if (initialStage) {
                        new Player().execute("https://firebasestorage.googleapis.com/v0/b/examenuf2-e75c8.appspot.com/o/cancion%2FAdele%20-%20Someone%20Like%20You%20(Lyrics).mp3?alt=media&token=877247ed-4282-49f5-96b5-0ad87d61f566");
                    } else {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                        }
                    }
                    playPause = true;
                }else{
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    playPause = false;
                }
                break;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mediaPlayer != null)
            mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    class Player extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            Boolean prepared = false;
            try {

                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        initialStage = true;
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                prepared = true;

            }catch (Exception e){
                Log.e("AudioStreaming", e.getMessage());
                prepared = false;
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(progressDialog.isShowing()){
                progressDialog.cancel();
            }

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Cargando cancion...");
            progressDialog.show();

        }
    }
}
