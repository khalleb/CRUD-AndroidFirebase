package com.khalleb.crud_androidfirebase.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Util {

    public static boolean statusInternet(Context context) {
        ConnectivityManager conexao = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo informacao = conexao.getActiveNetworkInfo();
        if (informacao != null && informacao.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean verificarCampos(Context context, String texto1, String texto2) {
        if (!texto1.isEmpty() && !texto2.isEmpty()) {
            if (statusInternet(context)) {
                return true;
            } else {
                Toast.makeText(context, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(context, "Preencher campos!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
