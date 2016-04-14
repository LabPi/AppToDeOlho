package com.jp.todeolho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class InicialActivity extends AppCompatActivity implements View.OnClickListener{
    //Componentes
    private ImageView btnBuscar = null, btnFavoritos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        btnBuscar = (ImageView) findViewById(R.id.imgBuscar);
        btnBuscar.setOnClickListener(this);
        btnFavoritos = (ImageView) findViewById(R.id.imgFavoritos);
        btnFavoritos.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imgBuscar) {
            startActivity(new Intent(this, MapaActivity.class));

        } else if(v.getId() == R.id.imgFavoritos) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("activity", "main");
            startActivity(intent);
        }
    }
}
