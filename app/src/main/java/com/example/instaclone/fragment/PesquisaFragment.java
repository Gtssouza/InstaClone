package com.example.instaclone.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.instaclone.R;

public class PesquisaFragment extends Fragment {

    private SearchView searchViewPesquisa;
    private RecyclerView recyclerViewPesquisa;

    public PesquisaFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

       searchViewPesquisa = view.findViewById(R.id.searchViewPesquisa);
       recyclerViewPesquisa = view.findViewById(R.id.recyclerViewPesquisa);

       //Configura searchView
        searchViewPesquisa.setQueryHint("Buscar usuários");
        searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("onQueryTextSubmit","texto digitado" + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("onQueryTextChange","texto digitado" + newText);
                return true;
            }
        });

       return view;
    }
}