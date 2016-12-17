package pt.ipp.isep.dei.formacao.android.weatherdroid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

public class DownloadService extends Service {

	public static final long NOTIFY_INTERVAL = 3 * 1000; // 10 seconds
	
	// timer handling
	private Timer mTimer = null;

	@Override
    public IBinder onBind(Intent intent) {
        return null;
    }
	 
	@Override
	public void onCreate() {

		// cancel if already existed
		if(mTimer != null) {
			mTimer.cancel();
		} else {
			// recreate new
			mTimer = new Timer();
		}
		//Get Preferences helper
		UserPreferencesHelper myPreferencesHelper = new UserPreferencesHelper(this);		
		//Get last Update time
		Calendar lastUpdate = myPreferencesHelper.getLastUpdate().getCalendar();
		//Calculate next Update time
		Calendar nextUpdate = lastUpdate;
		nextUpdate.add(Calendar.HOUR_OF_DAY, myPreferencesHelper.getUpdateFilter());		

		if (Calendar.getInstance().after(nextUpdate) || myPreferencesHelper.getNeedsUpdate()){
			mTimer.scheduleAtFixedRate(new DownloadTimerTask(), 0, myPreferencesHelper.getUpdateFilter() * 360000);
		}else{
			mTimer.scheduleAtFixedRate(new DownloadTimerTask(), nextUpdate.getTime(), myPreferencesHelper.getUpdateFilter() * 360000);
		}

	}	

	class DownloadTimerTask extends TimerTask {

		@Override
		public void run() {

			new UserPreferencesHelper(DownloadService.this).setNeedsUpdate(false);
			// Now load info			
			LoadWeatherCondsTask call = new LoadWeatherCondsTask();
			call.execute();
			
		}	    

	}	
	
	class LoadWeatherCondsTask extends AsyncTask<Location, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(Location... params) {

			publishProgress(true);
			
			IDatabase db = new Database(DownloadService.this);
			db.open();

			ArrayList<Place> places = Place.getAllByCountry(Long.parseLong((new UserPreferencesHelper(DownloadService.this)).getCountryFilter()) ,db);
			for (Place place : places) {
				WWOSaxHandler wwo = new WWOSaxHandler(db);
				wwo.updateWeatherConditions(place);
			}

			db.close(); 
			db = null;
			
			UserPreferencesHelper myPreferencesHelper = new UserPreferencesHelper(DownloadService.this);		
			myPreferencesHelper.setLastUpdate();
			myPreferencesHelper = null;
			
			return true;

		}

		@Override
		protected void onProgressUpdate(Boolean... values) {
			if (values[0]){
				Toast.makeText(DownloadService.this, R.string.updating_database, Toast.LENGTH_LONG).show();
			}
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Toast.makeText(DownloadService.this, R.string.finish_updating_database, Toast.LENGTH_LONG).show();
		}

	}		
	
}
