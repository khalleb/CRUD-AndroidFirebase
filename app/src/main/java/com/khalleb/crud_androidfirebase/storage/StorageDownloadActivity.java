package com.khalleb.crud_androidfirebase.storage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.khalleb.crud_androidfirebase.R;

public class StorageDownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private ProgressBar progressBar;
    private Button btn_download;
    private Button btn_remover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_download_activity);

        this.imageView = (ImageView) findViewById(R.id.imageView_StorageDownload);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar_StorageDownload);
        this.btn_download = (Button) findViewById(R.id.button_StorageDownload_Download);
        this.btn_remover = (Button) findViewById(R.id.button_StorageDownload_Remover);

        this.btn_download.setOnClickListener(this);
        this.btn_remover.setOnClickListener(this);

        this.progressBar.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_StorageDownload_Download:
                downloadImagemPorLink();
                //downloadImagemPorNome();
                break;

            case R.id.button_StorageDownload_Remover:
                removerImagemPorLink();
                //removerImagemPorNome();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_storage_download, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_compartilhar:
                break;

            case R.id.item_criar_pdf:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Baixar imagem pelo link
    private void downloadImagemPorLink() {
        this.progressBar.setVisibility(View.VISIBLE);

        String url = "https://firebasestorage.googleapis.com/v0/b/cursofirebaseandroidudemy.appspot.com/o/imagem%2FlulaLivre.jpg?alt=media&token=3dd7f41e-5d6d-4834-817e-dc0a9b4ceca2";
     /*   Picasso.with(getBaseContext()).load(url).into(this.imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });*/

        Glide.with(getBaseContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(imageView);

    }

    //Baixar imagem pelo nome
    private void downloadImagemPorNome() {
        this.progressBar.setVisibility(View.VISIBLE);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("imagem").child("lulaLivre.jpg");

        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String url = task.getResult().toString();
                    Glide.with(getBaseContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(imageView);

                }
            }
        });
    }

    private void removerImagemPorLink() {
        String url = "https://firebasestorage.googleapis.com/v0/b/cursofirebaseandroidudemy.appspot.com/o/imagem%2FlulaLivre.jpg?alt=media&token=3dd7f41e-5d6d-4834-817e-dc0a9b4ceca2";
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);

        reference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    imageView.setImageDrawable(null);
                    Toast.makeText(getBaseContext(), "Sucesso em remover a imagem", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getBaseContext(), "Erro ao remover a imagem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removerImagemPorNome() {
        String nome = "lulaLivre.jpg";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("imagem").child(nome);

        reference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    imageView.setImageDrawable(null);
                    Toast.makeText(getBaseContext(), "Sucesso em remover a imagem", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getBaseContext(), "Erro ao remover a imagem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
