package com.example.instaclone.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.instaclone.R;
import com.example.instaclone.activity.EditarPerfilActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {

    private ProgressBar progressBarPerfil;
    private TextView txtNumPublicacoes, txtNumSeguidores, txtNumSeguindo;
    private Button btnEditarPerfil;
    public GridView gridViewPublicacoes;
    private CircleImageView imgPerfil;

    public PerfilFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_perfil, container, false);

        progressBarPerfil = view.findViewById(R.id.progressBarPefil);
        txtNumPublicacoes = view.findViewById(R.id.textViewPublicacoes);
        txtNumSeguidores = view.findViewById(R.id.textViewSeguidores);
        txtNumSeguindo = view.findViewById(R.id.textViewSeguindo);
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
        gridViewPublicacoes = view.findViewById(R.id.gridViewPerfil);
        imgPerfil = view.findViewById(R.id.circleImgPerfil);

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(i);
            }
        });

        return view;

    }
}