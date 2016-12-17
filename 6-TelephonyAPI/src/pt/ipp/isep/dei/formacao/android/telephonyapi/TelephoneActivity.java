package pt.ipp.isep.dei.formacao.android.telephonyapi;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TelephoneActivity extends Activity implements OnClickListener {

	private final static String TAG = "TelephonyApi";

	private Button btnMakeCall, btnSendSms;
	private EditText txtNumber, txtSmsText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);

    	btnMakeCall = (Button) findViewById(R.id.btnMakeCall);
    	btnMakeCall.setOnClickListener(this);

    	btnSendSms = (Button) findViewById(R.id.btnSendSms);
    	btnSendSms.setOnClickListener(this);

    	txtNumber = (EditText) findViewById(R.id.txtNumber);
    	txtSmsText = (EditText) findViewById(R.id.txtSmsText);

    	// Registar o listener para receber notificações quando o estado das chamadas é alterado
    	TelephonyManager mTelephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
    	mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);        
    }

	public void onClick(View v) {
		// Botão para efectuar chamadas        
		if(v.getId() == R.id.btnMakeCall) {
			Intent makeTheCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + txtNumber.getText().toString().trim()));
		    startActivity(makeTheCall);
		}        

		// Botão para enviar SMS
		if(v.getId() == R.id.btnSendSms) {
			sendSMS(txtNumber.getText().toString(), txtSmsText.getText().toString());
		}
	}
	
	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if(TelephonyManager.CALL_STATE_RINGING == state) {
				Log.i(TAG, "A receber chamada de: " + incomingNumber);
		    }

		    if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
		    	Log.i(TAG, "Chamada Terminada");
		    }
		}
	};
	
	private void sendSMS(String phoneNumber, String message) {        
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

		// BroadcastReceiver que nos irá notificar se a mensagem foi enviada ou não. 
		registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {

				switch (getResultCode()) {
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
						Log.i(TAG, "SMS sent");
						break;

					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
						Log.i(TAG, "Generic failure");
						break;
				}
			}
		}, new IntentFilter(SENT));

		// BroadcastReceiver que nos irá notificar se a mensagem foi entregue ou não
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
						Log.i(TAG, "SMS delivered");
						break;

					case Activity.RESULT_CANCELED:
						Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
						Log.i(TAG, "SMS not delivered");                        
						break;                        
				}
			}
		}, new IntentFilter(DELIVERED));        

		// SmsManager que permite o envio de SMS.
		SmsManager mSms = SmsManager.getDefault();
		mSms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
	}
}
