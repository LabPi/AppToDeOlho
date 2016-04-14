package com.jp.todeolho;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.jp.adapter.Interaction;
import com.jp.adapter.RVAdapter;
import com.jp.beans.Post;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Interaction {

    //Componentes
    private RecyclerView recyclerView = null;

    //Variaveis
    private ArrayList<Post> posts = null;
    private RVAdapter adapter = null;
    private ImageView imgUsuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        imgUsuario = (ImageView) findViewById(R.id.imgUsuario);
        assert imgUsuario != null;
        imgUsuario.setOnClickListener(this);



        posts = new ArrayList<>();
        posts.add(new Post("Ponte Sobre o Rio Tocantins", "Ponte que fará a ligação entre os estados do Maranhão e Tocantins, facilitando o trânsito entre a população e dessa forma diminuindo o tempo de viagem", R.mipmap.img01, false));
        posts.add(new Post("Quadra Polispostiva", "Construção de quadra polispostiva da quadra 906 sul em Palmas-TO", R.mipmap.img02, true));
        posts.add(new Post("Escola Infantil", "Construção de escola infantil de tempo integral na cidade de Araguiana-TO ", R.mipmap.img03, false));

        //ReciclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //Fixa o tamanho
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        //Instancia LinearLayoutManager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        //Seta o layoutManager
        recyclerView.setLayoutManager(llm);
        //Instancia adapter passando o array
        adapter = new RVAdapter(posts, this);
        //Seta o adapter
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Instancia adapter passando o array
        adapter = new RVAdapter(posts, this);
        //Seta o adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.imgUsuario)
        {
            startActivity(new Intent(MainActivity.this, PerfilActivity.class));
        }
    }

    @Override
    public void atualiza() {
        onResume();
    }
}