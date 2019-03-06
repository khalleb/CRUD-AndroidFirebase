package com.khalleb.crud_androidfirebase.storage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.khalleb.crud_androidfirebase.R;
import com.khalleb.crud_androidfirebase.Util.DialogAlerta;
import com.khalleb.crud_androidfirebase.Util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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
                buttonDownload();
                break;
            case R.id.button_StorageDownload_Remover:
                buttonRemover();
                break;
        }
    }

    private void buttonDownload() {
        if (Util.statusInternet(getBaseContext())) {
            downloadImagemPorLink();
            //downloadImagemPorNome();
        } else {
            DialogAlerta alerta = new DialogAlerta("Sem conexão", "Verifique sua internet");
            alerta.show(getSupportFragmentManager(), "1");
        }
    }

    private void buttonRemover() {
        if (Util.statusInternet(getBaseContext())) {
            removerImagemPorLink();
            //removerImagemPorNome();
        } else {
            DialogAlerta alerta = new DialogAlerta("Sem conexão", "Verifique sua internet");
            alerta.show(getSupportFragmentManager(), "1");
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
                itemCompartilhar();
                break;

            case R.id.item_criar_pdf:
                try {
                    itemGerarPDF();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void itemCompartilhar() {
        if (imageView.getDrawable() != null) {
            compartilhar();
        } else {
            DialogAlerta dialogAlerta = new DialogAlerta("Compartilhamento", "Não possui imagem ainda para compartilhar.");
            dialogAlerta.show(getSupportFragmentManager(), "3");
        }
    }

    private void itemGerarPDF() throws IOException, DocumentException {
        if (imageView.getDrawable() != null) {
            geraPDF();
        } else {
            DialogAlerta dialogAlerta = new DialogAlerta("Erro ao gerar PDF", "Não existem imagem para gerar o PDF.");
            dialogAlerta.show(getSupportFragmentManager(), "3");
        }
    }

    //Baixar imagem pelo link
    private void downloadImagemPorLink() {
        this.progressBar.setVisibility(View.VISIBLE);

        String url = "https://firebasestorage.googleapis.com/v0/b/cursofirebaseandroidudemy.appspot.com/o/imagem%2FlulaLivre.jpg?alt=media&token=1b5f2e90-92fd-4017-aea2-be67d848cbd4";
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
        String url = "https://firebasestorage.googleapis.com/v0/b/cursofirebaseandroidudemy.appspot.com/o/imagem%2FlulaLivre.jpg?alt=media&token=1b5f2e90-92fd-4017-aea2-be67d848cbd4";
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);

        reference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    imageView.setImageDrawable(null);
                    Toast.makeText(getBaseContext(), "Sucesso em remover a imagem", Toast.LENGTH_SHORT).show();
                } else {
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
                if (task.isSuccessful()) {
                    imageView.setImageDrawable(null);
                    Toast.makeText(getBaseContext(), "Sucesso em remover a imagem", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Erro ao remover a imagem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void compartilhar() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("imagem/jpeg");
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "CursoFirebase", null);

        Uri uri = Uri.parse(path);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Compartilhar"));


    }


    private void geraPDF() throws IOException, DocumentException {
        File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String nome_Arquivo = diretorio.getPath() + "/" + "FirebaseCurso" + System.currentTimeMillis() + ".pdf";

        File pdf = new File(nome_Arquivo);
        OutputStream outputStream = new FileOutputStream(pdf);

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setBoxSize("firebase", new Rectangle(36, 54, 559, 788));

        document.open();
        Font font = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Paragraph paragraph = new Paragraph("Curso Firebase Módulo II", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        Paragraph paragraph_2 = new Paragraph("Jone Arce", font);
        paragraph_2.setAlignment(Element.ALIGN_LEFT);

        ListItem item = new ListItem();

        item.add(paragraph);
        item.add(paragraph_2);

        document.add(item);

        try {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

            Bitmap bitmap = drawable.getBitmap();

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


            Image image = Image.getInstance(bytes.toByteArray());

            image.scaleAbsolute(100f, 100f);

            image.setAlignment(Element.ALIGN_CENTER);

            image.setRotationDegrees(10f);
            document.add(image);


        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();

        visualizarPDF(pdf);

    }

    private void visualizarPDF(File pdf) {
        PackageManager packageManager = getPackageManager();
        Intent itent = new Intent(Intent.ACTION_VIEW);
        itent.setType("application/pdf");
        List<ResolveInfo> lista = packageManager.queryIntentActivities(itent, PackageManager.MATCH_DEFAULT_ONLY);

        if (lista.size() > 0) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(getBaseContext(), "com.khalleb.crud_androidfirebase", pdf);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
        } else {
            DialogAlerta dialogAlerta = new DialogAlerta("Erro ao Abrir PDF", "Não foi detectado nenhum leitor de PDF no seu Dispositivo.");
            dialogAlerta.show(getSupportFragmentManager(), "3");


        }

    }
}
