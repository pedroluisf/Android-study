package pt.ipp.isep.dei.formacao.android.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HelloAndroid extends Activity implements OnClickListener {

	private final String TAG = "HELLO_ANDROID";

	private EditText myEditText;
	private Button myButtonToShowMessage, myButtonToOpenNewActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// obt�m a inst�ncia da EditText definida no XML do layout
		myEditText = (EditText) findViewById(R.id.myEditText);

		// idem...
		myButtonToShowMessage = (Button) findViewById(R.id.myButtonToShowMessage);
		myButtonToShowMessage.setOnClickListener(this);

		// idem
		myButtonToOpenNewActivity = (Button) findViewById(R.id.myButtonToOpenNewActivity);
		myButtonToOpenNewActivity.setOnClickListener(this);

		Log.i(getString(R.string.app_name), TAG + " - onCreate()");
	}

	public void onClick(View v) {
		// executado quando o utilizador clica no myButtonToOpenNewActivity
		if (v.getId() == R.id.myButtonToOpenNewActivity) {
			Intent intentToOpenNewActivity = new Intent(this, NewActivity.class);
			// coloca o texto da myEditText nos extras do intent (key/value
			// pairs)
			intentToOpenNewActivity.putExtra("MyText", myEditText.getText()
					.toString());
			startActivity(intentToOpenNewActivity);
		}

		// cria e apresenta a toast message com o texto da myEditText
		if (v.getId() == R.id.myButtonToShowMessage) {
			Toast toast = Toast.makeText(this, myEditText.getText(),
					Toast.LENGTH_SHORT);
			toast.show();
		}
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