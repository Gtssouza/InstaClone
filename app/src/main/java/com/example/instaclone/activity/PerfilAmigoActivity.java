package com.example.instaclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instaclone.R;
import com.example.instaclone.helper.ConfiguracaoFirebase;
import com.example.instaclone.helper.UsuarioFirebase;
import com.example.instaclone.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAmigoActivity extends AppCompatActivity {
    private Usuario usuarioSelecionado;
    private Button buttonAcaoPerfil;
    private CircleImageView circleImgPerfil;
    private DatabaseReference usuarioRef;
    private DatabaseReference usuarioAmigoRef;
    private DatabaseReference seguidoresRef;
    private ValueEventListener valueEventListener;
    private TextView txtNumPublicacoes, txtNumSeguidores, txtNumSeguindo;
   private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        //Configurações iniciais
        usuarioRef = ConfiguracaoFirebase.getDatabaseReference().child("usuarios");
        seguidoresRef = ConfiguracaoFirebase.getDatabaseReference().child("seguidores");
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUser();

        txtNumPublicacoes = findViewById(R.id.textViewPublicacoes);
        txtNumSeguidores =  findViewById(R.id.textViewSeguidores);
        txtNumSeguindo =    findViewById(R.id.textViewSeguindo);
        buttonAcaoPerfil =  findViewById(R.id.btnEditarPerfil);
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

        verificaSegueUsuarioAmigo();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void verificaSegueUsuarioAmigo(){
        DatabaseReference seguidorRef = seguidoresRef.child(idUsuarioLogado).child(usuarioSelecionado.getId());
        //Consulta apenas uma vez o banco de dados
        seguidorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //Já esta seguindo
                    Log.d("dadosUsuario", "Seguindo");
                    habilitarBotaoSeguir(true);
                }else{
                    //Ainda não esta seguindo
                    Log.d("dadosUsuario", "Seguir");
                    habilitarBotaoSeguir(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void habilitarBotaoSeguir(boolean segueUsuario){
        if(segueUsuario){
            buttonAcaoPerfil.setText("Seguindo");
        }else{
            buttonAcaoPerfil.setText("Seguir");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosPerfilAmigo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioAmigoRef.removeEventListener(valueEventListener);
    }

    private void recuperarDadosPerfilAmigo(){
        usuarioAmigoRef = usuarioRef.child(usuarioSelecionado.getId());
        valueEventListener = usuarioAmigoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                String postagens = String.valueOf(usuario.getPostagens());
                String seguidores = String.valueOf(usuario.getSeguidores());
                String seguindo = String.valueOf(usuario.getSeguindo());

                //Configura os valores recuperados
                txtNumPublicacoes.setText(postagens);
                txtNumSeguidores.setText(seguidores);
                txtNumSeguindo.setText(seguindo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}