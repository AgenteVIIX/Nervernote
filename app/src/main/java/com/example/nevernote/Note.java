package com.example.nevernote;

import java.io.Serializable;

public class Note implements Serializable {
    private String nome, desc;
    private int id, id_book;

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getDesc() { return desc; }

    public void setDesc(String desc) { this.desc = desc; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getId_book() { return id_book; }

    public void setId_book(int id_book) { this.id_book = id_book; }

    @Override
    public String toString() {
        return "Nota: " + getNome() + "\n"+
                "Descrição: " + getDesc();
    }
}
