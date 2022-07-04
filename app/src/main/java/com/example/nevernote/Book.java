package com.example.nevernote;

import java.io.Serializable;

public class Book implements Serializable {
    private String nome, desc;
    private int id, id_user;

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getDesc() { return desc; }

    public void setDesc(String desc) { this.desc = desc; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getId_user() { return id_user; }

    public void setId_user(int id_user) { this.id_user = id_user; }

    @Override
    public String toString() {
        return "Caderno: " + getNome() + "\n"+
                "Descrição: "+ getDesc();
    }
}
