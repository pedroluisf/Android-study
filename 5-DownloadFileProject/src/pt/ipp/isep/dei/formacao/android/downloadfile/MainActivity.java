package pt.ipp.isep.dei.formacao.android.downloadfile;

import pt.ipp.isep.dei.formacao.android.downloadfile.DownloadService.DownloadServiceBinder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button btnStartDownload;
	private ProgressDialog mProgressDialog = null;

	private DownloadServiceBinder mDownloadServiceBinder = null;
	private MyDownloadReceiver mDownloadReceiver = null;
	private IntentFilter mIntentFilter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

	    btnStartDownload = (Button) findViewById(R.id.btnDownload);
	    btnStartDownload.setOnClickListener(this);

	    mProgressDialog = new ProgressDialog(MainActivity.this);
	    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    mProgressDialog.setMessage("Downloading...");
	    mProgressDialog.setCancelable(true);
	    mProgressDialog.setOnCancelListener(new OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				mDownloadServiceBinder.getService().cancelDownload();
			}
		});

	    mIntentFilter = new IntentFilter(DownloadFileAsyncTask.PERCENT_CHANGED);
	    mDownloadReceiver = new MyDownloadReceiver();

	    getApplicationContext().startService(new Intent(this, DownloadService.class));
	}
	
	@Override
	protected void onResume() {
	    getApplicationContext().bindService(new Intent(this, DownloadService.class), mConnection, Context.BIND_AUTO_CREATE);
	    registerReceiver(mDownloadReceiver, mIntentFilter);
	    super.onResume();
	}

	@Override
	protected void onPause() {
	    getApplicationContext().unbindService(mConnection);
	    unregisterReceiver(mDownloadReceiver);
	    super.onPause();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onClick(View v) {

        if((v.getId() == R.id.btnDownload) && (mDownloadServiceBinder != null)) {
            mProgressDialog.setProgress(0);
            mProgressDialog.show();
            mDownloadServiceBinder.getService().startDownload();
        }        
    }
    
    class MyDownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context c, Intent i) {

            int mPercentage = i.getIntExtra(DownloadFileAsyncTask.PERCENT_EXTRA, 0);
            
            if(mPercentage < 0) {
            	mProgressDialog.dismiss();
            	Toast.makeText(MainActivity.this, "Canceled or problem downloading file...", Toast.LENGTH_LONG).show();
            } else {
	            if(mPercentage < 100)
	                mProgressDialog.setProgress(mPercentage);
	            else
	                mProgressDialog.dismiss();
            }
        }        
    }
    
 // "listener" para eventos relacionados com o serviço DownloadService
    private ServiceConnection mConnection = new ServiceConnection() {

        // executado quando o onBind ou onRebind do serviço é concluido
        public void onServiceConnected(ComponentName className, IBinder service) {
            mDownloadServiceBinder = ((DownloadService.DownloadServiceBinder) service);
        }

        // executado quando a ligação ao serviço é destruída
        // normalmente acontece quando o serviço falha ou é morto
        public void onServiceDisconnected(ComponentName className) {
            mDownloadServiceBinder = null;
        }
    };

    
}
