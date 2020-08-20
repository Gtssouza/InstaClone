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
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmailLogin, campoSenhaLogin;
    private Button btnEntraLogin;
    private ProgressBar progressBarLog;
    private Usuario usuario;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verificaUsuarioLogado();
        setContentView(R.layout.activity_login);

        campoEmailLogin = findViewById(R.id.editTxtEmailLogin);
        campoSenhaLogin = findViewById(R.id.editTxtSenhaLogin);
        btnEntraLogin = findViewById(R.id.btnEntrar);
        progressBarLog = findViewById(R.id.progressBarLogin);

        progressBarLog.setVisibility(View.GONE);

        btnEntraLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmailLogin = campoEmailLogin.getText().toString();
                String txtSenhaLogin = campoSenhaLogin.getText().toString();
                if(!txtEmailLogin.isEmpty()){
                    if(!txtSenhaLogin.isEmpty()){

                        usuario = new Usuario();
                        usuario.setEmail(txtEmailLogin);
                        usuario.setSenha(txtSenhaLogin);
                        ValidarUsuario(usuario);

                    }else{
                        Toast.makeText(LoginActivity.this,"Preencha o campo senha",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,"Preencha o campo email",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void abrirCadastro(View view){
        Intent i = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(i);
    }

    public void verificaUsuarioLogado(){
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        if(auth.getCurrentUser() != null){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    public void ValidarUsuario(Usuario usuario){
        progressBarLog.setVisibility(View.VISIBLE);
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBarLog.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,"Sucesso ao logar usuário",Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }else {
                    progressBarLog.setVisibility(View.GONE);
                    String msg = "";
                    try{
                        throw task.getException();
                    }catch (Exception e){
                        msg = "Falha ao logar " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,"Erro" + msg,Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}