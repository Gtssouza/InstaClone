package com.example.instaclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instaclone.R;
import com.example.instaclone.helper.ConfiguracaoFirebase;
import com.example.instaclone.helper.Permissao;
import com.example.instaclone.helper.UsuarioFirebase;
import com.example.instaclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    private CircleImageView imgPerfEdit;
    private EditText txtNomeEdit, txtEmailEdit;
    private Button btnAlterarNome;
    private TextView txtAlterarFoto;
    private StorageReference storageReference;
    private Usuario usuarioLogado;
    private static final int SELECAO_GALERIA=200;
    private StorageReference storageRef;
    private String identificadorUsuario;

    private String[] permissioesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            //Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        imgPerfEdit    = findViewById(R.id.circleImgEdit);
        txtNomeEdit    = findViewById(R.id.editTxtAlterarNome);
        txtEmailEdit   = findViewById(R.id.editTxtEmailEdit);
        btnAlterarNome = findViewById(R.id.btnAlterarEditPerfil);
        txtAlterarFoto = findViewById(R.id.btnAlterarFotoEdit);

        identificadorUsuario = UsuarioFirebase.getIdentificadorUser();

        storageRef = ConfiguracaoFirebase.getStorageReference();

        Permissao.validarPermissoes(permissioesNecessarias,this,1);

        final FirebaseUser usuario = UsuarioFirebase.getUserAtual();

        txtNomeEdit.setText(usuario.getDisplayName().toUpperCase());
        txtEmailEdit.setText(usuario.getEmail());

        Uri url = usuario.getPhotoUrl();

        if(url != null){
            Glide.with(EditarPerfilActivity.this).load(url).into(imgPerfEdit);
        }else{
            imgPerfEdit.setImageResource(R.drawable.avatar);
        }

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar perfil");
        setSupportActionBar(toolbar);

        btnAlterarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeAtualizado = txtNomeEdit.getText().toString();
                UsuarioFirebase.atualizaNomeUser(nomeAtualizado);
                usuarioLogado.setNome(nomeAtualizado);
                usuarioLogado.atualizar();

                Toast.makeText(EditarPerfilActivity.this, "Dados alterados com sucesso",Toast.LENGTH_SHORT).show();
            }
        });

        //Alterar foto do usuario
        txtAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try{
                switch (requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                        break;
                }

                if(imagem != null){
                    //Configura imagem na tela
                    imgPerfEdit.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                    byte[] dadosImagem = byteArrayOutputStream.toByteArray();
                    //Salvar imagem no firebase
                    final StorageReference imageRef = storageRef.child("imagens").child("perfil").child(identificadorUsuario + ".jpeg");
                    UploadTask uploadTask = imageRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                       Toast.makeText(EditarPerfilActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizarFotoUser(url);
                                }
                            });
                            Toast.makeText(EditarPerfilActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void atualizarFotoUser(Uri uri){
        //Atualiza foto no Perfil
        UsuarioFirebase.atualizaFotoUser(uri);

        //Atualiza foto no Firebase
        usuarioLogado.setCaminhoFoto(uri.toString());
        usuarioLogado.atualizar();

        Toast.makeText(EditarPerfilActivity.this, "Sua foto foi alterada",Toast.LENGTH_SHORT).show();
    }
}