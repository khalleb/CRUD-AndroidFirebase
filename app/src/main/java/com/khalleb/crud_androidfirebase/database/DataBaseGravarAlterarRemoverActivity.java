package com.khalleb.crud_androidfirebase.database;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khalleb.crud_androidfirebase.R;

import java.util.HashMap;
import java.util.Map;

public class DataBaseGravarAlterarRemoverActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nomePasta;
    private EditText nome;
    private EditText idade;
    private Button salvar;
    private Button alterar;
    private Button remover;

    private FirebaseDatabase firebaseDatabase;
    private boolean firebaseOffLine = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_base_gravar_alterar_remover_activity);

        this.nomePasta = (EditText) findViewById(R.id.editText_dataBase_gravar_alterar_remover_nome_pasta);
        this.nome = (EditText) findViewById(R.id.editText_dataBase_gravar_alterar_remover_nome);
        this.idade = (EditText) findViewById(R.id.editText_dataBase_gravar_alterar_remover_idade);
        this.salvar = (Button) findViewById(R.id.button_dataBase_gravar_alterar_remover_salvar);
        this.alterar = (Button) findViewById(R.id.button_dataBase_gravar_alterar_remover_alterar);
        this.remover = (Button) findViewById(R.id.button_dataBase_gravar_alterar_remover_remover);

        this.salvar.setOnClickListener(this);
        this.alterar.setOnClickListener(this);
        this.remover.setOnClickListener(this);

        this.firebaseDatabase = FirebaseDatabase.getInstance();
        ativarFirebaseOffLine();
    }

    private void ativarFirebaseOffLine() {
        try {
            if (!this.firebaseOffLine) {
                //Rodando OffLine
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                this.firebaseOffLine = true;
            } else {
                //Firebase já estiver funcionando OffLine
            }
        } catch (Exception erro) {
            Toast.makeText(getBaseContext(), erro.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_dataBase_gravar_alterar_remover_salvar:
                salvarDados();
                break;
            case R.id.button_dataBase_gravar_alterar_remover_alterar:
                alterarDados();
                break;
            case R.id.button_dataBase_gravar_alterar_remover_remover:
                removerDados();
                break;
        }
    }

    private void salvarDados() {
        /*DatabaseReference reference = this.firebaseDatabase.getReference().child("BD").child("Gerentes");
        Map<String,Object> valor = new HashMap<>();
        valor.put("nome", "Maria");
        valor.put("idade", 28);
        valor.put("fumante", false);
        reference.child("10").setValue(valor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getBaseContext(), "Sucesso ao salvar!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getBaseContext(), "Poblema ao salvar!", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

       /* DatabaseReference reference = this.firebaseDatabase.getReference().child("BD").child("Gerentes");
        Gerente gerente = new Gerente("Antonio", 52, false);
        reference.child("9").setValue(gerente).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getBaseContext(), "Sucesso ao salvar!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getBaseContext(), "Poblema ao salvar!", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

      /*  DatabaseReference reference = this.firebaseDatabase.getReference().child("BD").child("Gerentes");
        Gerente gerente = new Gerente("Antonio", 52, false);
        reference.push().setValue(gerente).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getBaseContext(), "Sucesso ao salvar!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getBaseContext(), "Poblema ao salvar!", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        String nome = this.nome.getText().toString();
        String idadeString = this.idade.getText().toString();
        int idade = Integer.parseInt(idadeString);

        DatabaseReference reference = this.firebaseDatabase.getReference().child("BD").child("Gerentes");
        Gerente gerente = new Gerente(nome, idade, false);
        reference.push().setValue(gerente).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Sucesso ao salvar!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Problema ao salvar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void alterarDados() {

        String nomePasta = this.nomePasta.getText().toString();
        String nome = this.nome.getText().toString();
        String idadeString = this.idade.getText().toString();
        int idade = Integer.parseInt(idadeString);

        DatabaseReference reference = this.firebaseDatabase.getReference().child("BD").child("Gerentes");
        Gerente gerente = new Gerente(nome, idade, true);

        Map<String, Object> atualizacao = new HashMap<>();
        atualizacao.put(nomePasta, gerente);
        reference.updateChildren(atualizacao).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Sucesso ao alterar!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Problema ao alterar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removerDados() {
        String nomePasta = this.nomePasta.getText().toString();


        DatabaseReference reference = this.firebaseDatabase.getReference().child("BD").child("Gerentes");
        reference.child(nomePasta).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Sucesso ao remover!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Problema ao remover!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
