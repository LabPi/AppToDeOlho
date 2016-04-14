package com.jp.todeolho;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jp.util.DataUtil;
import com.jp.util.GPSTracker;
import com.jp.util.GerenciadorTela;
import com.jp.util.ImageUtils;
import com.jp.util.SDCardUtils;
import com.jp.util.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class DenunciaActivity extends AppCompatActivity implements View.OnClickListener {
    //Componentes
    private AutoCompleteTextView txvEndereco = null;
    private ImageView imageView = null;
    private ImageButton btnFoto = null;

    //Variaveis
    private GPSTracker gpsTracker = null;
    private File file = null;

    //Constantes
    private static final int COD_FOTO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia);

        txvEndereco = (AutoCompleteTextView) findViewById(R.id.txvEndereco);

        imageView = (ImageView) findViewById(R.id.imageView);

        btnFoto = (ImageButton) findViewById(R.id.btnFoto);
        btnFoto.setOnClickListener(this);

        gpsTracker = new GPSTracker(this);

        GerenciadorTela.inicializaDisplay(this);

        if(gpsTracker.canGetLocation())
        {
            double lat = gpsTracker.getLatitude();
            double lng = gpsTracker.getLongitude();
            CoordenadaEnderecoTask coordenadaEnderecoTask = new CoordenadaEnderecoTask(lat, lng);
            coordenadaEnderecoTask.execute();
        }
        else
        {
            gpsTracker.showSettingsAlert();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnFoto)
        {
            file = SDCardUtils.getSdCardFile("todeolho", "TDO" + "_"
                    + DataUtil.timeStamp() + ".PNG");
            try {
                // Cria o caminho do arquivo no SDCARD
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //i.putExtra("crop", "false");
                i.putExtra("outputX", 800);
                i.putExtra("outputY", 600);
                //i.putExtra("aspectX", 1);
                //i.putExtra("aspectY", 1);
                //i.putExtra("scale", true);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                if(i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, COD_FOTO);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == COD_FOTO) {
            Toast.makeText(this, "Foto salva com sucesso!",
                    Toast.LENGTH_LONG).show();
            this.onResume();
        }
    }

    @Override
    protected void onResume() {
        if(file != null)
        {
            if(file.exists())
            {
                int altura = GerenciadorTela.alturaPercentagem(30);
                int largura = GerenciadorTela.larguraPercentagem(50);
                Bitmap foto = ImageUtils.getResizedImage(
                        Uri.fromFile(file), altura, largura);
                BitmapDrawable drawable = new BitmapDrawable(getResources(), foto);
                imageView.setImageDrawable(drawable);
            }
        }
        super.onResume();
    }

    public class CoordenadaEnderecoTask extends AsyncTask<Void, Void, Boolean> {
        //Variaveis
        private String resposta;
        private double latitude;
        private double longitude;

        public CoordenadaEnderecoTask(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

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
        @Override
        protected Boolean doInBackground(Void... params) {
            WebService webService = new WebService("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true");
            resposta = webService.get()[1];
            if(!resposta.equals("erro"))
            {
                try {
                    JSONObject json = new JSONObject(resposta);
                    if(json.getString("status").equals("OK"))
                    {
                        JSONArray jsonArray = json.getJSONArray("results");
                        JSONObject json2 = jsonArray.getJSONObject(0);
                        resposta = json2.getString("formatted_address");
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
            txvEndereco.setText(resposta.contains("ZERO_RESULTS")?"":resposta);
        }
    }
}
