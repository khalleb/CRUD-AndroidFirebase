package com.khalleb.crud_androidfirebase.storage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khalleb.crud_androidfirebase.R;
import com.khalleb.crud_androidfirebase.Util.DialogAlerta;
import com.khalleb.crud_androidfirebase.Util.DialogProgress;
import com.khalleb.crud_androidfirebase.Util.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class StorageUploadActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private Button button_enviar;
    private Uri uri_imagem;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_upload_activity);

        this.imageView = (ImageView) findViewById(R.id.imageView_StorageUpload);
        this.button_enviar = (Button) findViewById(R.id.button_StorageUpload_Enviar);

        this.button_enviar.setOnClickListener(this);

        firebaseStorage = FirebaseStorage.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_StorageUpload_Enviar:
                buttonUpload();
                break;
        }
    }

    private void buttonUpload(){
        if(Util.statusInternet(getBaseContext())){
            if(imageView.getDrawable() != null){
                //uploadImagem();
                uploadImagemSegundaOpcao();
            }else{
                Toast.makeText(getBaseContext(), "Não existe imagem para fazer upload.", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getBaseContext(), "Sem acesso a internet. Verifique!.", Toast.LENGTH_LONG).show();
        }
    }
    private void uploadImagem() {

        final DialogProgress dialogProgress = new DialogProgress();
        dialogProgress.show(getSupportFragmentManager(), "Trabalhando...");
        StorageReference reference = firebaseStorage.getReference().child("upload").child("imagens");
        StorageReference nomeImagem = reference.child("FirebaseUpload" + System.currentTimeMillis() + ".jpg");

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        UploadTask uploadTask = nomeImagem.putBytes(bytes.toByteArray());
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    dialogProgress.dismiss();
                    Toast.makeText(getBaseContext(), "Upload feito com sucesso!", Toast.LENGTH_LONG).show();
                } else {
                    dialogProgress.dismiss();
                    Toast.makeText(getBaseContext(), "Erro no  upload!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void uploadImagemSegundaOpcao() {
        final DialogProgress dialogProgress = new DialogProgress();
        dialogProgress.show(getSupportFragmentManager(), "Trabalhando...");
        StorageReference reference = firebaseStorage.getReference().child("upload").child("imagens");
        final StorageReference nomeImagem = reference.child("FirebaseUpload" + System.currentTimeMillis() + ".jpg");

        Glide.with(getBaseContext()).asBitmap().load(uri_imagem).apply(new RequestOptions().override(1024, 768)).
                listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(getBaseContext(), "Erro ao transformar a imagem", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes.toByteArray());
                        try {
                            bytes.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        UploadTask uploadTask = nomeImagem.putStream(inputStream);
                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                return nomeImagem.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    dialogProgress.dismiss();
                                    Uri uri = task.getResult();
                                    String url_imagem = uri.toString();
                                    Toast.makeText(getBaseContext(), "Upload feito com sucesso!", Toast.LENGTH_LONG).show();
                                } else {
                                    dialogProgress.dismiss();
                                    Toast.makeText(getBaseContext(), "Erro ao realizar upload.", Toast.LENGTH_LONG).show();
                                }
                        }
                    });
                    return false;
                }
        }).submit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_storage_upload, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_galeria:
                obterImagemGaleria();
                break;

            case R.id.item_camera:
                itemCamera();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void itemCamera(){
        if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            DialogAlerta dialogAlerta = new DialogAlerta("Permissão necessária","Permissão da câmera negada.");
            dialogAlerta.show(getSupportFragmentManager(),"1");
        }else{
            obterImagemPelaCamera();
        }
    }


    private void obterImagemGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Escolha a imagem"), 0);

    }

    private void obterImagemPelaCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String nomeImagem = diretorio.getPath() + "/" + "CursoImagem" + System.currentTimeMillis() + ".jpg";
        File file = new File(nomeImagem);

        String autorizacao = "com.khalleb.crud_androidfirebase";
        this.uri_imagem = FileProvider.getUriForFile(getBaseContext(), autorizacao, file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_imagem);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                if (data != null) {
                    uri_imagem = data.getData();
                    Glide.with(getBaseContext()).asBitmap().load(uri_imagem).listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            Toast.makeText(getBaseContext(), "Erro ao carregar imagem", Toast.LENGTH_LONG).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imageView);
                } else {
                    Toast.makeText(getBaseContext(), "Falhar ao selecionar a imagem", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == 1) { //Câmera
                if (this.uri_imagem != null) {
                    Glide.with(getBaseContext()).asBitmap().load(uri_imagem).listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            Toast.makeText(getBaseContext(), "Erro ao carregar imagem", Toast.LENGTH_LONG).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imageView);
                } else {
                    Toast.makeText(getBaseContext(), "Falhar ao capturar imagem.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}


