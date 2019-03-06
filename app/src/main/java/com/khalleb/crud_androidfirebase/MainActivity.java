package com.khalleb.crud_androidfirebase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.khalleb.crud_androidfirebase.Util.Permissao;
import com.khalleb.crud_androidfirebase.storage.StorageDownloadActivity;
import com.khalleb.crud_androidfirebase.storage.StorageUploadActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardView_Download;
    private CardView cardView_Upload;
    private CardView cardView_DataBase_LerDados;
    private CardView cardView_DataBase_GravarAlterarExcluir;
    private CardView cardView_Empresa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.cardView_Download = (CardView) findViewById(R.id.cardView_dataBase_download);
        this.cardView_Upload = (CardView) findViewById(R.id.cardView_dataBase_upload);
        this.cardView_DataBase_LerDados = (CardView) findViewById(R.id.cardView_dataBase_ler_dados);
        this.cardView_DataBase_GravarAlterarExcluir = (CardView) findViewById(R.id.cardView_dataBase_ler_gravarAlterarExcluir);
        this.cardView_Empresa = (CardView) findViewById(R.id.cardView_empresa);

        this.cardView_Download.setOnClickListener(this);
        this.cardView_Upload.setOnClickListener(this);
        this.cardView_DataBase_LerDados.setOnClickListener(this);
        this.cardView_DataBase_GravarAlterarExcluir.setOnClickListener(this);
        this.cardView_Empresa.setOnClickListener(this);

        permissao();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardView_dataBase_download:
                Intent intent = new Intent(getBaseContext(), StorageDownloadActivity.class);
                startActivity(intent);
                break;

            case R.id.cardView_dataBase_upload:
                startActivity(new Intent(getBaseContext(), StorageUploadActivity.class));

                break;

            case R.id.cardView_dataBase_ler_dados:
                break;


            case R.id.cardView_dataBase_ler_gravarAlterarExcluir:
                break;


            case R.id.cardView_empresa:
                break;

        }
    }

    private void permissao(){
        String permissoes [] = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,

        };

        Permissao.permissao(this, 0, permissoes);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int resultado : grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "Aceite as permissões, por favor!", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }
    }
}
