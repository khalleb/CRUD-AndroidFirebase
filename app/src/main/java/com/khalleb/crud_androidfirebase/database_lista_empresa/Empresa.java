package com.khalleb.crud_androidfirebase.database_lista_empresa;

public class Empresa {

    private String nome;
    private String id;

    public Empresa() {
    }

    public Empresa(String nome, String id) {
        this.nome = nome;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
