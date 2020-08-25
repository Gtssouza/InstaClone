package com.example.instaclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.instaclone.R;
import com.example.instaclone.helper.UsuarioFirebase;
import com.example.instaclone.model.Usuario;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAmigoActivity extends AppCompatActivity {
    private Usuario usuarioSelecionado;
    private Button buttonAcaoPerfil;
    private CircleImageView circleImgPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        buttonAcaoPerfil = findViewById(R.id.btnEditarPerfil);
        buttonAcaoPerfil.setText("Seguir");

        circleImgPerfil = findViewById(R.id.circleImgPerfil);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);



        //Recupera usuario selecionado
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");
            getSupportActionBar().setTitle(usuarioSelecionado.getNome());
        }

        String caminhoFoto = usuarioSelecionado.getCaminhoFoto();

        if(caminhoFoto != null){
            Uri url = Uri.parse(caminhoFoto);
            Glide.with(PerfilAmigoActivity.this).load(url).into(circleImgPerfil);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}