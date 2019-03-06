package com.khalleb.crud_androidfirebase.database;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khalleb.crud_androidfirebase.R;
import com.khalleb.crud_androidfirebase.Util.DialogAlerta;

import java.util.ArrayList;
import java.util.List;

public class DataBaseLerDadosActivity extends AppCompatActivity {

    private TextView tvNome;
    private TextView tvIdade;
    private TextView tvFumante;
    private TextView tvNome2;
    private TextView tvIdade2;
    private TextView tvFumante2;

    private FirebaseDatabase database;
    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_base_ler_dados_activity);

        this.tvNome = (TextView) findViewById(R.id.textView_dataBase_lerDados_nome);
        this.tvIdade = (TextView) findViewById(R.id.textView_dataBase_lerDados_idade);
        this.tvFumante = (TextView) findViewById(R.id.textView_dataBase_lerDados_fumante);
        this.tvNome2 = (TextView) findViewById(R.id.textView_dataBase_lerDados_nome_2);
        this.tvIdade2 = (TextView) findViewById(R.id.textView_dataBase_lerDados_idade_2);
        this.tvFumante2 = (TextView) findViewById(R.id.textView_dataBase_lerDados_fumante_2);

        database = FirebaseDatabase.getInstance();

        ouvinte1();
    }


    private void ouvinte1() {
        DatabaseReference reference = database.getReference().child("BD").child("Gerentes").child("0");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Gerente> gerentes = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Gerente gerente = data.getValue(Gerente.class);
                    gerentes.add(gerente);
                }


                tvNome.setText(gerentes.get(0).getNome());
                tvIdade.setText(gerentes.get(0).getIdade() + "");
                tvFumante.setText(gerentes.get(0).isFumante() + "");


            /*    List<String> nomes = new ArrayList<>();
                List<Integer> idades = new ArrayList<>();
                List<Boolean> fumantes = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String nome = data.child("nome").getValue(String.class);
                    Integer idade = data.child("idade").getValue(Integer.class);
                    Boolean fumante = data.child("fumante").getValue(Boolean.class);
                    nomes.add(nome);
                    idades.add(idade);
                    fumantes.add(fumante);
                }

                tvNome.setText(nomes.get(0));
                tvIdade.setText(idades.get(0) + "");
                tvFumante.setText(fumantes.get(0) + "");

                tvNome2.setText(nomes.get(1));
                tvIdade2.setText(idades.get(1) + "");
                tvFumante2.setText(fumantes.get(1) + "");
*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
