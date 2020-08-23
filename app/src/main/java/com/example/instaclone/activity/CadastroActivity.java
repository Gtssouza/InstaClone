package com.example.instaclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instaclone.R;
import com.example.instaclone.helper.ConfiguracaoFirebase;
import com.example.instaclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private EditText campoUser, campoEmail, campoSenha;
    private Button btnCadastro;
    private ProgressBar progressBarCadastro;
    private Usuario usuario;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoUser = findViewById(R.id.editTxtUserCadastro);
        campoEmail = findViewById(R.id.editTxtEmailCadastro);
        campoSenha = findViewById(R.id.editTxtSenhaCadastro);
        btnCadastro = findViewById(R.id.btnCadastrar);
        progressBarCadastro = findViewById(R.id.progressBarCadastrar);

        progressBarCadastro.setVisibility(View.GONE);
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtUser = campoUser.getText().toString();
                String txtEmail = campoEmail.getText().toString();
                String txtSenha = campoSenha.getText().toString();

                if(!txtUser.isEmpty()){
                    if(!txtEmail.isEmpty()){
                        if(!txtSenha.isEmpty()){

                            usuario = new Usuario();
                            usuario.setNome(txtUser);
                            usuario.setEmail(txtEmail);
                            usuario.setSenha(txtSenha);
                            cadastrarUsuario(usuario);

                        }else{
                            Toast.makeText(CadastroActivity.this,"Preencha o campo senha",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,"Preencha o campo email",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,"Preencha o campo nome",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cadastrarUsuario(Usuario user){
        progressBarCadastro.setVisibility(View.VISIBLE);
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    try{
                        progressBarCadastro.setVisibility(View.GONE);

                        //Salvar dados no Firebase


                        Toast.makeText(CadastroActivity.this,"Sucesso ao cadastrar usuário",Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(CadastroActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    progressBarCadastro.setVisibility(View.GONE);
                    String msg = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        msg = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        msg = "Por favor, digite um e-mail válido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        msg = "Esta conta já está cadastrada!";
                    }catch (Exception e){
                        msg = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,"Erro" + msg,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}