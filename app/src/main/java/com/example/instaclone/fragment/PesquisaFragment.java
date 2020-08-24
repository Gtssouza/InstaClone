package com.example.instaclone.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.instaclone.R;
import com.example.instaclone.helper.ConfiguracaoFirebase;
import com.example.instaclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PesquisaFragment extends Fragment {

    private SearchView searchViewPesquisa;
    private RecyclerView recyclerViewPesquisa;
    private List<Usuario> listaUsers = new ArrayList<>();
    private DatabaseReference usuariosRef;

    public PesquisaFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

       searchViewPesquisa = view.findViewById(R.id.searchViewPesquisas);
       recyclerViewPesquisa = view.findViewById(R.id.recyclerViewPesquisas);
       usuariosRef = ConfiguracaoFirebase.getDatabaseReference().child("usuarios");

       //Configura searchView
        searchViewPesquisa.setQueryHint("Buscar usuários");
        searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String txtDigitado = searchViewPesquisa.getQuery().toString().toUpperCase();
                pesquisarUsuarios(txtDigitado);
                return true;
            }
        });

       return view;
    }

    private void pesquisarUsuarios(String txtPesquisado){

        listaUsers.clear();
        //Pesquisar usuarios caso tenha texto na pesquisa
        if(txtPesquisado.length() > 0){
            Query query = usuariosRef.orderByChild("nome").startAt(txtPesquisado).endAt(txtPesquisado + "\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()){
                        listaUsers.add(ds.getValue(Usuario.class));
                    }

                    int total = listaUsers.size();
                    Log.i("totalUsuarios", "total:" + total);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}