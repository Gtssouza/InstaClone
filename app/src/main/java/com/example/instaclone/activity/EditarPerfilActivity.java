package com.example.instaclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.instaclone.R;
import com.example.instaclone.helper.UsuarioFirebase;
import com.example.instaclone.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    private CircleImageView imgPerfEdit;
    private EditText txtNomeEdit, txtEmailEdit;
    private Button btnAlterarNome;
    private TextView txtAlterarFoto;
    private StorageReference storageReference;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        imgPerfEdit = findViewById(R.id.circleImgEdit);
        txtNomeEdit = findViewById(R.id.editTxtAlterarNome);
        txtEmailEdit = findViewById(R.id.editTxtEmailEdit);
        btnAlterarNome = findViewById(R.id.btnAlterarEditPerfil);
        txtAlterarFoto = findViewById(R.id.btnAlterarFotoEdit);

        final FirebaseUser usuario = UsuarioFirebase.getUserAtual();

        txtNomeEdit.setText(usuario.getDisplayName());
        txtEmailEdit.setText(usuario.getEmail());

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);
    }
}