package com.example.instaclone.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.instaclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {

    public static Usuario getDadosUsuarioLogado(){
        FirebaseUser firebaseUser = getUserAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());
        usuario.setId(firebaseUser.getUid());

        if(firebaseUser.getPhotoUrl() == null){
            usuario.setCaminhoFoto("");
        }else {
            usuario.setCaminhoFoto(firebaseUser.getPhotoUrl().toString());
        }

        return usuario;
    }

    public static void atualizaNomeUser(String nome){
        try{
            FirebaseUser user = getUserAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil","Erro ao atualizar nome de perfil");
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static  FirebaseUser getUserAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAuth();
        return usuario.getCurrentUser();
    }

}
