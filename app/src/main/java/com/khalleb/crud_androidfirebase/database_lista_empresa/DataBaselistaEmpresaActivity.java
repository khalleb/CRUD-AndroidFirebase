package com.khalleb.crud_androidfirebase.database_lista_empresa;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khalleb.crud_androidfirebase.R;
import com.khalleb.crud_androidfirebase.database.Gerente;

import java.util.ArrayList;
import java.util.List;

public class DataBaselistaEmpresaActivity extends AppCompatActivity implements RecyclerView_ListaEmpresa.ClickEmpresa {

    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private RecyclerView_ListaEmpresa recyclerView_listaEmpresa;
    private List<Empresa> empresas = new ArrayList<>();

    private ChildEventListener childEventListener;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_baselista_empresa_activity);

        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView_DataBase_Empresa_Lista);
        this.database = FirebaseDatabase.getInstance();

        iniciarRecyclerView();

    }

    private void iniciarRecyclerView() {
       /* Empresa empresa1 = new Empresa("DataRey", "0");
        Empresa empresa2 = new Empresa("LG", "1");
        this.empresas.add(empresa1);
        this.empresas.add(empresa2);*/


        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView_listaEmpresa = new RecyclerView_ListaEmpresa(getBaseContext(), empresas, this);
        this.recyclerView.setAdapter(this.recyclerView_listaEmpresa);
    }

    @Override
    public void clickEmpresa(Empresa empresa) {

    }


    private void ouvinte() {
        this.reference = database.getReference().child("BD").child("Empresas");

        this.childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                Empresa empresa = dataSnapshot.getValue(Empresa.class);
                empresa.setId(key);
                empresas.add(empresa);
                recyclerView_listaEmpresa.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("Ouviente", dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.i("Ouviente", dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        this.reference.addChildEventListener(this.childEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ouvinte();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.childEventListener != null) {
            this.reference.removeEventListener(this.childEventListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.childEventListener != null) {
            this.reference.removeEventListener(this.childEventListener);
        }
    }
}
