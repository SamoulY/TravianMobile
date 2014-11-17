package chabingba.travianmobile;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView tvData1, tvParsed;
	HttpClient client;
	final static String url = "http://ts1.travian.de";
	WebView webView;
	WebSettings settings;
	Uri uri = Uri.parse(url);
	Intent intent1;
	Document doc;
	EditText etInput1, etInput2;
	String nameString = "";
	Element loginName;
	Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		etInput1 = (EditText) findViewById(R.id.etInput1);
		etInput2 = (EditText) findViewById(R.id.etInput2);
		login = (Button) findViewById(R.id.bLogin);

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("username", etInput1
						.getText().toString()));
				postParameters.add(new BasicNameValuePair("password", etInput2
						.getText().toString()));

				String response = null;
				try {
					response = CustomHttpClient.executeHttpPost(
							"http://ts1.travian.de/login.php", postParameters);
					String res = response.toString();
					res = res.replaceAll("\\s+", "");
					if (res.equals("1"))
						tvData1.setText("Correct Username or Password");
					else
						tvData1.setText("Sorry!! Incorrect Username or Password");
				} catch (Exception e) {
					etInput1.setText(e.toString());
				}

			}
		});

		//
		// try {
		// doc = Jsoup.connect(url).get();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// String parsed = doc.select("tr").text();
		// tvParsed = (TextView) findViewById(R.id.tvParsed);
		// tvParsed.setText(parsed);

		// etInput1 = (EditText) findViewById(R.id.etInput1);
		// nameString = etInput1.toString();
		//
		// loginName = doc.getElementById("name");
		// loginName.appendText(nameString);
		//

		/*
		 * intent1 = new Intent(Intent.ACTION_VIEW, uri);
		 * startActivity(intent1);
		 */

		// webView = (WebView) findViewById(R.id.wView); settings =
		// webView.getSettings(); settings.setJavaScriptEnabled(true);
		// webView.loadUrl(url);

		// tvData1 = (TextView) findViewById(R.id.tvData1);
		// GetData test = new GetData();
		// try {
		// String returned = test.getD();
		// tvData1.setText(returned);
		// } catch (Exception a) {
		// a.printStackTrace();
		// }

	}

}