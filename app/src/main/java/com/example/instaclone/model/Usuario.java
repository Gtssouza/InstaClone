package com.example.instaclone.model;

import com.example.instaclone.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String caminhoFoto;

    public Usuario() {
    }

    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference usuario = firebaseRef.child("usuarios").child(getId());
        usuario.setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
