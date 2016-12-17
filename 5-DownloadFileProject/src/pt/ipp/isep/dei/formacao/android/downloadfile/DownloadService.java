package pt.ipp.isep.dei.formacao.android.downloadfile;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DownloadService extends Service {
	
	private final static String TAG = "DOWNLOAD_SERVICE";

	private final static String DOWNLOAD_URL = "http://www.gecad.isep.ipp.pt:8844/files/android.zip";    
	
	private final IBinder mBinder = new DownloadServiceBinder();
	
	private DownloadFileAsyncTask asyncTask;
	
	/**
	 * Binder implementation which passes through a reference to this service.
	 * Since this is a local service, the activity can then call directly
	 * methods on this service instance.
	 */
	public class DownloadServiceBinder extends Binder {

	  public DownloadService getService() {
	    return DownloadService.this;
	  }
	}

	@Override
	public IBinder onBind(Intent arg0) {
	  Log.d(TAG, "onBind called.");
	  return mBinder;
	}

	@Override
	public void onRebind(Intent intent) {
	  Log.d(TAG, "onRebind called.");
	  super.onRebind(intent);
	}

	@Override
	public boolean onUnbind(Intent intent) {
	  Log.d(TAG, "onUnbind called.");
	  // se não for devolvido true, o onRebind não será executado em futuros binds
	  return true;
	}
	
	public void startDownload() {
		URL url;
		try {
			url = new URL(DOWNLOAD_URL);
			asyncTask = new DownloadFileAsyncTask(this);
			asyncTask.execute(url);
		} catch (MalformedURLException e) {
			Log.e(TAG, "Invalid URL", e);
		}
	}
	
	public void cancelDownload() {
		asyncTask.cancelDownload();
	}

}
