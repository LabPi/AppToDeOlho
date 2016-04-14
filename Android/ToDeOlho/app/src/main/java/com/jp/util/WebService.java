package com.jp.util;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

public class WebService {
	private String url = "";
	private HttpURLConnection con;
	private OutputStream os;
	private String delimiter = "--";
	private String boundary = "SwA" + Long.toString(System.currentTimeMillis())
			+ "SwA";
	private String resposta = "nothing";

	// Construtor
	public WebService(String url) {
		this.url = url;
	}

	/**
	 * @return
	 */
	public final String[] get() {

		String[] result = new String[2];
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
		try {
			response = HttpClientSingleton.getHttpClientInstace().execute(
					httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				result[0] = String.valueOf(response.getStatusLine()
						.getStatusCode());
				InputStream instream = entity.getContent();
				result[1] = toString(instream);
				instream.close();

				// Log.i("get", "Result from post JsonPost : " + result[0] +
				// " : " + result[1]);
			}
		}
		catch (ConnectTimeoutException e)
		{
			Log.e("NGVL", "Falha ao acessar Web service", e);
			result[0] = "0";
			result[1] = "erro";
		} catch (Exception e) {
			Log.e("NGVL", "Falha ao acessar Web service", e);
			result[0] = "0";
			result[1] = "erro";
		}
		return result;
	}

	/**
	 * @param json
	 * @return
	 */
	public final String[] post(String json) {
		String[] result = new String[2];
		try {
			HttpPost httpPost = new HttpPost(new URI(url));
			httpPost.setHeader("content-type", "application/json");
			StringEntity sEntity = new StringEntity(json);
			httpPost.setEntity(sEntity);

			HttpResponse response;
			response = HttpClientSingleton.getHttpClientInstace().execute(
					httpPost);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				result[0] = String.valueOf(response.getStatusLine()
						.getStatusCode());
				InputStream instream = entity.getContent();
				result[1] = toString(instream);
				instream.close();

				Log.d("post", "Result from post JsonPost : " + result[0]
						+ " : " + result[1]);
			}

		} catch (Exception e) {
			Log.e("NGVL", "Falha ao acessar Web service", e);
			result[0] = "0";
			result[1] = "erro";
		}
		return result;
	}

	/**
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private String toString(InputStream is) throws IOException {

		byte[] bytes = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int lidos;
		while ((lidos = is.read(bytes)) > 0) {
			baos.write(bytes, 0, lidos);
		}
		return new String(baos.toByteArray());
	}


    public byte[] downloadImage(String img) {
        // initilize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttoGet request
        final HttpGet getRequest = new HttpGet(url + img);
        try {

            HttpResponse response = client.execute(getRequest);

            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                Bitmap bitmap = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();
                    // decoding stream data back into image Bitmap that android understands
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];
                    //Armazena a stream em um buffer
                    StringBuffer buffer = new StringBuffer();
                    for (int i; (i = inputStream.read(b)) != -1;) {
                        buffer.append(new String(b, 0, i));
                    }
                    //Converte o codificacao base64
					byte[] array = Base64.decode(buffer.toString(), Base64.DEFAULT);
                    //Retorna o array de bytes
                    return array;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
            e.printStackTrace();
        }
        return null;
    }

	public void connectForMultipart() throws Exception {
		con = (HttpURLConnection) (new URL(url)).openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
				+ boundary);
		con.connect();
		os = con.getOutputStream();
	}

	public void addFormPart(String paramName, String value) throws Exception {
		writeParamData(paramName, value);
	}

	public void addFilePart(String paramName, String fileName, byte[] data)
			throws Exception {
		os.write((delimiter + boundary + "\r\n").getBytes());
		os.write(("Content-Disposition: form-data; name=\"" + paramName
				+ "\"; filename=\"" + fileName + "\"\r\n").getBytes());
		os.write(("Content-Type: application/octet-stream\r\n").getBytes());
		os.write(("Content-Transfer-Encoding: binary\r\n").getBytes());
		os.write("\r\n".getBytes());
		os.write(data);
		os.write("\r\n".getBytes());
	}

	public void finishMultipart() throws Exception {
		os.write((delimiter + boundary + delimiter + "\r\n").getBytes());
	}

	public String getResponse() throws Exception {
		InputStream is = con.getInputStream();
		byte[] b1 = new byte[1024];
		StringBuffer buffer = new StringBuffer();
		for (int i; (i = is.read(b1)) != -1;) {
			buffer.append(new String(b1, 0, i));
		}
		/*while (is.read(b1) != -1)
			buffer.append(new String(b1));*/
		con.disconnect();
		return buffer.toString();
	}

	private void writeParamData(String paramName, String value)
			throws Exception {
		os.write((delimiter + boundary + "\r\n").getBytes());
		os.write("Content-Type: text/plain\r\n".getBytes());
		os.write(("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n")
				.getBytes());
		os.write(("\r\n" + value + "\r\n").getBytes());
	}
	
	public String uploadImage(RequestParams params)
	{
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				resposta = "" + statusCode;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				resposta = "" +  statusCode;
			}
		});
		
		return resposta;
	}
}