package com.eventorio.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class InternetConnection extends AsyncTask<String, String, String>{

	private static final int GET = 0;
	private static final int POST = 1;
	private static final String TAG = "Internet...";
	private Context act;
	private String string_url;
	private int tipo;
	private Message mMessage;
	private Handler mHandler;
	private HttpParams mHttpParams;
	private String parameter;
	private Header[] mHeaders;
	private String autorization;
	private String codification="UTF-8";


	public InternetConnection(Context act) {
		this.act=act;
	}
	
	@Override
    protected void onPreExecute() {
		Log.i(InternetConnection.TAG, "onPreExecute");
		if (mMessage==null){
			mMessage=new Message();
		}
		if (mHandler == null){
			mHandler = new Handler();
		}
		
    }
	
	@Override
	protected void onProgressUpdate(String ... values) {
        Log.i(InternetConnection.TAG, "onProgressUpdate");
    }
    
	@Override
    protected void onPostExecute(String result) {
        Log.d(InternetConnection.TAG, "onPostExecute "+ mMessage.obj);
        mHandler.sendMessage(mMessage);
    }

	@Override
	protected String doInBackground(String ... params) {
		Log.i(InternetConnection.TAG, "doInBackground"+act.toString()+" "+string_url);		
		mMessage.what=0;
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpUriRequest httprequest;
			
			switch (tipo) {
			case GET: 
				httprequest=new HttpGet(string_url);
				HttpResponse response = httpClient.execute(httprequest);
				
	            InputStream inputStream = response.getEntity().getContent();
	            
	            if (response.getStatusLine().getStatusCode()!=200){
	            	mMessage.what=3;
	    			mMessage.obj=response.getStatusLine().getReasonPhrase();
	    			break;
	            }
	            
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, codification);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            
	            StringBuilder stringBuilder = new StringBuilder();

	            String bufferedStrChunk = null;

	            int i=0;
	            
	            while((bufferedStrChunk = bufferedReader.readLine()) != null){
	            	Log.d("LINEA "+i, bufferedStrChunk);
					i++;
	               stringBuilder.append(bufferedStrChunk);
	            }
	            
	            mMessage.obj=stringBuilder.toString();
	            stringBuilder=null;
				break;
			case POST:
				URL url = new URL(string_url);
				OutputStream os = null;
				HttpURLConnection conexion=(HttpURLConnection) url.openConnection();
				conexion.setRequestMethod("POST");
				conexion.setDoInput(true);
				conexion.setInstanceFollowRedirects(false);
				//conexion.setDoOutput(false);
				
				conexion.addRequestProperty("Autorization", autorization);
				conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				
				os = conexion.getOutputStream();
				os.write(parameter.getBytes());
				os.flush();
				os.close();
				
				InputStreamReader isr = new InputStreamReader(conexion.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				String linea;
				StringBuilder sb = new StringBuilder();
				
				i=0;
				
				while ((linea = br.readLine()) != null){
					i++;
					sb.append(linea);	
				}
				
				mMessage.obj=sb.toString();
				
				break;

			default:
				httprequest=new HttpGet(string_url);
				break;
			}
			
			
		} catch (MalformedURLException e) {
			mMessage.obj=e.toString();
			mMessage.what=1;
			e.printStackTrace();
		} catch (IOException e) {
			mMessage.what=2;
			mMessage.obj=e.toString();
			e.printStackTrace();
		}
		
		return null;
	}
	
	

	public void setString_url(String string_url) {
		this.string_url = string_url;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}
	
	public HttpParams getmHttpParams() {
		return mHttpParams;
	}

	public void setmHttpParams(HttpParams mHttpParams) {
		this.mHttpParams = mHttpParams;
	}

	public String getparameter() {
		return parameter;
	}

	public void setparameter(String parameter) {
		this.parameter = parameter;
	}
	
	public Header[] getHeaders() {
		return mHeaders;
	}

	public void setHeaders(Header[] mHeaders) {
		this.mHeaders = mHeaders;
	}

	public String getAutorization() {
		return autorization;
	}

	public void setAutorization(String autorization) {
		this.autorization = autorization;
	}
	
	public String getCodification() {
		return codification;
	}

	public void setCodification(String codification) {
		this.codification = codification;
	}

}