package com.jp.todeolho;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jp.util.GPSTracker;
import com.jp.util.NetworkUtil;
import com.jp.util.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapaActivity extends AppCompatActivity {

    private LatLng latLng = null;
    private GoogleMap mapa = null;
    private double lat = -10.2059183;
    private double lng = -48.3424584;
    private GPSTracker gps = null;
    private String endereco = "Palmas";
    AlertDialog dialog = null;
    private CameraUpdate update = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        //Instancia o mapa
        mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();

        //Configura o tipo de mapa
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Adiciona a localizacao no para o mapa
        mapa.addMarker(new MarkerOptions().position(new LatLng(-10.2059183, -48.3424584)).title("Quartel do Comando Geral dos Bombeiros").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_default)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-10.170012, -48.335339)).title("Escola Tempo Integral Padre Josimo").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_default)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-10.195139, -48.311331)).title("Escola Adventista de Palmas").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_default)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-10.190801, -48.338923)).title("Defesa Civil").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_default)));

        //Configura o tipo de mapa
        mapa.getUiSettings().setZoomControlsEnabled(false);
        
        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                startActivity(new Intent(MapaActivity.this, PostActivity.class));
                return false;
            }
        });
        

        //Instancia o GPS
        gps = new GPSTracker(this);

        //Pega as coordenadas a partir do GPS
        if(gps.canGetLocation())
        {
            lat = gps.getLatitude();
            lng = gps.getLongitude();
        }
        else
        {
            gps.showSettingsAlert();
        }

        //Configura o posicionamento no mapa
        configurePosition(new LatLng(lat, lng));

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.flbBuscar);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            startActivity(new Intent(MapaActivity.this, DenunciaActivity.class));
            }
        });

        mapa.setMyLocationEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        buscarLocalizacao();
    }

    public void buscarLocalizacao()
    {
        if(!NetworkUtil.getIpAddress().equals("0.0.0.0"))
        {
            View layout = getLayoutInflater().inflate(R.layout.layout_dialog, null);
            Button btnBuscaPerto = (Button) layout.findViewById(R.id.btnBuscaPerto);
            btnBuscaPerto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(gps.canGetLocation())
                    {
                        lat = gps.getLatitude();
                        lng = gps.getLongitude();
                        configurePosition(new LatLng(lat, lng));
                    }
                    else
                    {
                        gps.showSettingsAlert();
                    }
                    dialog.dismiss();
                }
            });

            final AutoCompleteTextView txvEndereco = (AutoCompleteTextView) layout.findViewById(R.id.txvEndereco);
            txvEndereco.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //Reset Error
                    txvEndereco.setError(null);
                    return false;
                }
            });

            Button btnBuscaEndereco = (Button) layout.findViewById(R.id.btnBuscarEndereco);
            btnBuscaEndereco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endereco = txvEndereco.getText().toString();
                    if(!endereco.equals(""))
                    {
                        AutoTask task = new AutoTask();
                        task.execute();
                        dialog.dismiss();
                    }
                    else
                    {
                        txvEndereco.setError(null);
                        txvEndereco.setError("Preencha o campo!");
                        txvEndereco.requestFocus();
                    }
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(MapaActivity.this);
            //builder.setTitle("Bem Vindo");
            builder.setView(layout);
            dialog = builder.create();
            dialog.show();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapaActivity.this);
            builder.setTitle("Atenção!");
            builder.setMessage("Não possui conexão com a internet!!!");
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public void configurePosition(LatLng latLng)
    {
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng) 	//localização
                .bearing(0) 		//Rotação da câmera em graus
                .tilt(0) 			//Ângulo que a câmera está posicionada em graus
                .zoom(12) 			//Zoom
                .build();
        update = CameraUpdateFactory.newCameraPosition(position);

        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng , 1.5f));
        mapa.animateCamera(update, 100, null);
    }


    public class AutoTask extends AsyncTask<Void, Void, Boolean> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */

        private String resposta;
        private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapaActivity.this);
            progressDialog.setMessage("Buscando endereço...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            WebService webService = new WebService("https://maps.googleapis.com/maps/api/geocode/json?address="+ endereco +"&key");
            resposta = webService.get()[1];
            if(!resposta.equals("erro"))
            {
                try {
                    JSONObject json = new JSONObject(resposta);
                    if(json.getString("status").equals("OK"))
                    {
                        JSONArray jsonArray = json.getJSONArray("results");
                        JSONObject json2 = new JSONObject(new JSONObject(jsonArray.getJSONObject(0).getString("geometry")).getString("location"));
                        lat = Double.parseDouble(json2.getString("lat"));
                        lng = Double.parseDouble(json2.getString("lng"));
                        latLng = new LatLng(lat, lng);
                    }
                    else
                    {
                        resposta = "";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                resposta = "";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //Configura endereco
            configurePosition(latLng);
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favoritos) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("activity", "main");
            startActivity(intent);
            return true;
        } else if(id == R.id.action_buscar) {
            buscarLocalizacao();
            return true;
        } else if(id == R.id.action_perfil) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("activity", "perfil");
            startActivity(intent);
            return true;
        }
        else if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
