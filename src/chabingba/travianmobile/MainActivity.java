package chabingba.travianmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	int statusCode = 0, flagExecute = 0;
	TextView tvParsed;
	Intent intent1;
	EditText etInput1, etInput2;
	String name = "", password = "", line = "", newLine = System
			.getProperty("line.separator"), data = "", error = "",
			url = "http://ts1.travian.de/login.php";
	Button bLogin;
	HttpClient client;
	HttpPost post;
	HttpResponse response;
	List<NameValuePair> urlParameters;
	BufferedReader bufferedReader;
	StringBuffer stringBuffer = new StringBuffer("");
	Context mainContext;
	StatusLine statusLine;
	List<NameValuePair> postData;
	BasicNameValuePair namePair, passwordPair;
	UrlEncodedFormEntity urlEncodedFormEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		init();
		mainContext = this;
	}

	public void init() {
		etInput1 = (EditText) findViewById(R.id.etInput1);
		etInput2 = (EditText) findViewById(R.id.etInput2);
		tvParsed = (TextView) findViewById(R.id.tvParsed);
		bLogin = (Button) findViewById(R.id.bLogin);
		bLogin.setOnClickListener(this);

		name = etInput1.getText().toString().trim();
		password = etInput2.getText().toString().trim();

		namePair = new BasicNameValuePair("name", name);
		passwordPair = new BasicNameValuePair("password", password);
	}

	public void postLoginData() {
		client = new DefaultHttpClient();

		post = new HttpPost(url);

		postData = new ArrayList<NameValuePair>();
		postData.add(namePair);
		postData.add(passwordPair);

		try {
			urlEncodedFormEntity = new UrlEncodedFormEntity(postData);
			post.setEntity(urlEncodedFormEntity);
			response = client.execute(post);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			error = e.toString();
			Log.v("ENTITY_TAG", error);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			error = e.toString();
			Log.v("EXECUTE_TAG", error);
		} catch (IOException e) {
			e.printStackTrace();
			error = e.toString();
			Log.v("EXECUTE_TAG2", error);
		}

		try {
			bufferedReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
		} catch (IllegalStateException e) {
			e.printStackTrace();
			error = e.toString();
			Log.v("BUFFEREDREADER_TAG", error);
		} catch (IOException e) {
			e.printStackTrace();
			error = e.toString();
			Log.v("BUFFEREDREADER_TAG2", error);
		}
	}

	private void getPostData(BufferedReader bufferedReader1) {
		try {
			while ((line = bufferedReader1.readLine()) != null) {
				stringBuffer.append(line + newLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
			error = e.toString();
			Log.v("BUFREADER_APPEND", error);
		}

		try {
			bufferedReader1.close();
		} catch (IOException e) {
			e.printStackTrace();
			error = e.toString();
			Log.v("BUFREADER_CLOSE2", error);
		}
		data = stringBuffer.toString();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bLogin:
			new TravianAsync().execute();
			break;
		}
	}

	private class TravianAsync extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setTitle("Trying to login...");
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			postLoginData();
			publishProgress();
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			getPostData(bufferedReader);
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
				error = e.toString();
				Log.v("BUFREADER_CLOSE", error);
			}

			if (data.contains("login"))
				tvParsed.setText("Pak greda...");
			else
				tvParsed.setText("Ti si genii!!!");

		}

	}

}