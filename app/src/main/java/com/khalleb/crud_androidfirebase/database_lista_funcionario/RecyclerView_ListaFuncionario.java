package com.khalleb.crud_androidfirebase.database_lista_funcionario;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.khalleb.crud_androidfirebase.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerView_ListaFuncionario extends RecyclerView.Adapter<RecyclerView_ListaFuncionario.ViewHolder> {

    private Context context;
    private List<Funcionario> funcionarios;
    private RecyclerView_ListaFuncionario.ClickFuncionario clickFuncionario;


    public RecyclerView_ListaFuncionario(Context context, List<Funcionario> funcionarios, ClickFuncionario clickFuncionario) {
        this.context = context;
        this.funcionarios = funcionarios;
        this.clickFuncionario = clickFuncionario;
    }

    public interface ClickFuncionario {
        void clickFuncionario(Funcionario funcionario);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.database_lista_funcionario_conteudo_recyclerview, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Funcionario funcionario = funcionarios.get(i);
        viewHolder.textView_Nome.setText(funcionario.getNome());
        viewHolder.textView_Idade.setText(funcionario.getIdade() + "");
        viewHolder.progressBar.setVisibility(View.VISIBLE);

        Picasso.with(context).load(funcionario.getUrlImagem()).into(viewHolder.imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onError() {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
        });
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFuncionario.clickFuncionario(funcionario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return funcionarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView_Nome;
        TextView textView_Idade;
        ImageView imageView;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView_ListaFuncionario_Item_CardView);
            textView_Nome = (TextView) itemView.findViewById(R.id.textView_ListaFuncionario_Nome);
            textView_Idade = (TextView) itemView.findViewById(R.id.textView_ListaFuncionario_Idade);
            imageView = (ImageView) itemView.findViewById(R.id.imageView_ListaFuncionario_Item);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar_ListaFuncionario_Item);

        }
    }
}