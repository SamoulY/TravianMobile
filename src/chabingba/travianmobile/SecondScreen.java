package chabingba.travianmobile;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class SecondScreen extends Activity {

	String url = "http://ts1.travian.de/dorf1.php";
	WebView view;
	WebSettings webSettings;
	Document doc;
	Element input1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secondview);

//		webSettings = view.getSettings();
//		webSettings.setJavaScriptEnabled(true);
		
		view = (WebView) findViewById(R.id.wvDorf1);
		view.loadUrl(url);
	}

}
