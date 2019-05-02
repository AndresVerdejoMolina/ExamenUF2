package com.example.examenuf2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AgregarIncidencia extends AppCompatActivity {

    private FirebaseStorage mFirebase = FirebaseStorage.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    Button subir;
    TextView descripcion, aula;
    ImageButton foto;
    static Uri uri;

    String urlDescargaFoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_incidencia);

        subir = findViewById(R.id.subir);

        descripcion = findViewById(R.id.descripcion);
        aula = findViewById(R.id.aula);

        foto = findViewById(R.id.addFoto);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarFoto();
            }
        });

        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarIncidencia(descripcion.getText().toString(), aula.getText().toString(), uri);
            }
        });
    }

    private void seleccionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //Abro la petición de Intent pero esperando una respuesta
        //porque en seleccionar un imagen quiero volver a mi App y
        //quiero conocer que imagen se ha seleccionado, esta info la
        //recibimos en el metodo onActivityResult
        startActivityForResult(intent, 20);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;

        if (requestCode == 10 && resultCode == RESULT_OK) {
            uri = data.getData();

        }
    }
    private void agregarIncidencia (String descripcion, String aula, Uri uri){
        UUID uuid = UUID.randomUUID();
        final ProgressDialog progressDialog = new ProgressDialog(AgregarIncidencia.this);
        if(descripcion.isEmpty() && aula.isEmpty()){
            Toast.makeText(AgregarIncidencia.this,
                    "Campos vacios",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Subiendo audio...");
        progressDialog.show();

        final StorageReference ref = mFirebase.getReference().child("fotos_incidencias/" + descripcion + "-" + aula + "(" + uuid + ")");

        UploadTask uploadTask;
        uploadTask = ref.putFile(uri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            urlDescargaFoto = String.valueOf(uri);
                        }
                    });

                }

            }
        });

        database.getReference().push().setValue(new Incidencia(descripcion, urlDescargaFoto, aula));
    }
}
