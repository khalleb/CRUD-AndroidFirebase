package com.khalleb.crud_androidfirebase.database_lista_empresa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khalleb.crud_androidfirebase.R;

import java.util.List;

public class RecyclerView_ListaEmpresa extends RecyclerView.Adapter<RecyclerView_ListaEmpresa.ViewHolder> {

    private Context context;
    private List<Empresa> empresas;
    private ClickEmpresa clickEmpresa;

    public RecyclerView_ListaEmpresa(Context context, List<Empresa> empresas, ClickEmpresa clickEmpresa) {
        this.context = context;
        this.empresas = empresas;
        this.clickEmpresa = clickEmpresa;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.database_lista_empresa_conteudo_recyclerviwwer, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Empresa empresa = empresas.get(i);
        viewHolder.nome.setText(empresa.getNome());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEmpresa.clickEmpresa(empresa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    public interface ClickEmpresa {
        void clickEmpresa(Empresa empresa);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView nome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView_listaEmpresa_item_cardview);
            nome = (TextView) itemView.findViewById(R.id.textView_ListaEmpresa_item_nome);
        }
    }
}


