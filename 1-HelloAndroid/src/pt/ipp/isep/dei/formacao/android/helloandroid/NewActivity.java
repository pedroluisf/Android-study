package pt.ipp.isep.dei.formacao.android.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NewActivity extends Activity {

	private final static String TAG = "NEW_ACTIVITY";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newactivitylayout);

		// obtém a instância da myNewTextView e coloca o texto
		// recebido através do intent com a chave "MyText"
		TextView myNewTextView = (TextView) findViewById(R.id.myNewTextView);
		myNewTextView.setText(getIntent().getExtras().getString("MyText"));

		Log.i(getString(R.string.app_name), TAG + " - onCreate()");
	}

	@Override
	protected void onDestroy() {
		Log.i(getString(R.string.app_name), TAG + " - onDestroy()");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.i(getString(R.string.app_name), TAG + " - onPause()");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.i(getString(R.string.app_name), TAG + " - onRestart()");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.i(getString(R.string.app_name), TAG + " - onResume()");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.i(getString(R.string.app_name), TAG + " - onStart()");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.i(getString(R.string.app_name), TAG + " - onStop()");
		super.onStop();
	}
}
