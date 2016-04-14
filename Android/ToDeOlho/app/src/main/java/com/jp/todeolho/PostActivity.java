package com.jp.todeolho;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    //Componentes
    private ProgressBar progressCurtidas = null, progressDoacoes = null;
    private Button btnDenunciar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_post);

        setTitle("Descrição");

        btnDenunciar = (Button) findViewById(R.id.btnDenuncia);
        btnDenunciar.setOnClickListener(this);

        progressCurtidas = (ProgressBar) findViewById(R.id.progressBar);
        progressCurtidas.setMax(100);
        progressCurtidas.setProgress(40);
        progressCurtidas.getProgressDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        progressDoacoes = (ProgressBar) findViewById(R.id.progressBar2);
        progressDoacoes.setMax(100);
        progressDoacoes.setProgress(50);
        progressDoacoes.getProgressDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnDenuncia)
        {
            startActivity(new Intent(this, DenunciaActivity.class));
        }
    }
}
