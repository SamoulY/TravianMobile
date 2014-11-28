package chabingba.travianmobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class SecondScreen extends Activity {

	String url = "http://ts1.travian.de/dorf1.php";
	WebView webView;
	WebSettings webSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secondview);
		webView = (WebView) findViewById(R.id.wvDorf1);
		
		webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		webView.loadUrl(url);
	}

}
