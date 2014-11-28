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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity implements OnClickListener {

	int errorNumber = 0;
	// TextView tvParsed;
	Intent intent1;
	EditText etInput1, etInput2;
	String name = "", password = "", line = "", newLine = System
			.getProperty("line.separator"), data = "", error = "",
			url = "http://ts1.travian.de/login.php", webData = "";
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
	WebView wvTest;
	WebSettings webSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		init();
		mainContext = this;

		if (isConnected()) {
			// tvParsed.setText("Connection successfull.");
		} else {
			errorNumber = 1;
			Greda(errorNumber);
		}

	}

	public void init() {
		// Инициализиране на променливите.

		etInput1 = (EditText) findViewById(R.id.etInput1);
		etInput2 = (EditText) findViewById(R.id.etInput2);
		// tvParsed = (TextView) findViewById(R.id.tvParsed);
		bLogin = (Button) findViewById(R.id.bLogin);
		bLogin.setOnClickListener(this);
		wvTest = (WebView) findViewById(R.id.wvTest);
		webSettings = wvTest.getSettings();
		webSettings.setJavaScriptEnabled(true);

	}

	private void Greda(int errorNumber2) {
		// Финкция за съобщения за грешка.
		switch (errorNumber2) {
		case 1:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					mainContext);
			alertDialogBuilder.setTitle("No internet connection.");
			alertDialogBuilder
					.setMessage("No internet connection. Please try to connect to a network.");
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton("Exit",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// if this button is clicked, close
							// current activity
							MainActivity.this.finish();
						}
					});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			break;
		}
	}

	public boolean isConnected() {
		// Проверка дали сме се свързали.

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	public void postLoginData() {
		// Въвеждане на информацията за акаунта във формата.

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
		// Четене на иформацията, върната от заявката.

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
			name = etInput1.getText().toString().trim();
			password = etInput2.getText().toString().trim();

			namePair = new BasicNameValuePair("name", name);
			passwordPair = new BasicNameValuePair("password", password);
			break;
		}
	}

	private class TravianAsync extends AsyncTask<Void, Void, Void> {
		// Нова нишка, в която се прави входа.

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
			super.onPostExecute(result);
			progressDialog.dismiss();
			Toast.makeText(MainActivity.this, "Finished", Toast.LENGTH_LONG)
					.show();
			// intent1 = new Intent(mainContext, SecondScreen.class);
			// startActivity(intent1);
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

			webData = data;
			webData = webData.replace("#", "%23");
			webData = webData.replace("%", "%25");
			webData = webData.replace("\\", "%27");
			webData = webData.replace("?", "%3f");
			wvTest.loadDataWithBaseURL(url, webData, "text/html", "UTF-8", url);
			// if (data.contains("login"))
			// tvParsed.setText(data);
			// else
			// tvParsed.setText(data);

		}

	}

}