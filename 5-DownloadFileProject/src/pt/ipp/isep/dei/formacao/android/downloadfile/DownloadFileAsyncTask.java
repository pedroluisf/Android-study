package pt.ipp.isep.dei.formacao.android.downloadfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class DownloadFileAsyncTask extends AsyncTask<URL, Integer, Integer> {
	
	public static final String TAG = DownloadFileAsyncTask.class.getSimpleName();
	
	public static final String PERCENT_CHANGED = "pt.ipp.isep.dei.formacao.android.downloadfile.PERCENT_CHANGED";
	public static final String PERCENT_EXTRA = "PERCENT_EXTRA";
	private final static int PERCENT_DIFF = 1;
	
	public static final int OK = 0;
	public static final int NOK = -1;
	public static final int CANCELED = -2;
	
	private int downloadStatus = OK;
	
	private Context c;
	
	public DownloadFileAsyncTask(Context c) {
		super();
		this.c = c;
	}
	
	public void cancelDownload() {
		downloadStatus = CANCELED;
	}
	
	public int getDownloadStatus() {
		return downloadStatus;
	}

	protected Integer doInBackground(URL... url) {
				
		try {
			HttpURLConnection conn = (HttpURLConnection) url[0].openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			double mTotalSize = conn.getContentLength();    
			
			Log.d(TAG, "File Size: " + mTotalSize);
			
			if(mTotalSize < 0) {
				downloadStatus = NOK;
				return NOK;
			}
			
			double mDownloadedSize = 0;
			int mDownloadPercentage = 0;                        
			int mPreviousPercentage = 0;

			// Guardar Ficheiro no SDCard
			File file = new File(Environment.getExternalStorageDirectory() + "/" + url[0].getFile().substring(url[0].getFile().lastIndexOf("/")));
			FileOutputStream fileOutput = new FileOutputStream(file);

			InputStream inputStream = conn.getInputStream();

			byte[] buffer = new byte[1024];
			int bufferLength = 0; 

			while ((bufferLength = inputStream.read(buffer)) > 0 && downloadStatus == OK) {
				fileOutput.write(buffer, 0, bufferLength);
				mDownloadedSize += bufferLength;

				mDownloadPercentage = (int) (mDownloadedSize / mTotalSize * 100.0d);
				if(mDownloadPercentage - mPreviousPercentage >= PERCENT_DIFF) {
					mPreviousPercentage = mDownloadPercentage;
					publishProgress(mDownloadPercentage);
				}
			}

			fileOutput.close();
		} catch (Exception e) {
			Log.d(TAG, "Exception Downloading File", e);
			downloadStatus = NOK;
		}
        
		if(downloadStatus != OK) {
			return downloadStatus;
		}
		
        return 100;
        
    }

	protected void onProgressUpdate(Integer... progress) {
		Log.d(TAG, "Downloaded " + progress[0] + "%");

        Intent mPercentChangedIntent = new Intent(PERCENT_CHANGED);
        mPercentChangedIntent.putExtra(PERCENT_EXTRA, progress[0]);

        c.sendBroadcast(mPercentChangedIntent);
    }

	@Override
	protected void onPostExecute(Integer result) {
		Log.d(TAG, "onPostExecute: " + result);
		
		Intent mPercentChangedIntent = new Intent(PERCENT_CHANGED);
		mPercentChangedIntent.putExtra(PERCENT_EXTRA, result);

		c.sendBroadcast(mPercentChangedIntent);
	}

}
