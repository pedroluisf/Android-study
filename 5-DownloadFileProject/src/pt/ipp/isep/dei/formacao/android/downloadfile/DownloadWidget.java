package pt.ipp.isep.dei.formacao.android.downloadfile;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class DownloadWidget extends AppWidgetProvider {

	public static final String TAG = "DownloadWidget";
	private int mDownloadPercentage = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) {

	    ComponentName thisWidget = new ComponentName(context, DownloadWidget.class);
	    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

	    mDownloadPercentage = intent.getIntExtra(DownloadFileAsyncTask.PERCENT_EXTRA, 0);

	    // executa onUpdate sempre que recebe um Intent neste caso trata-se de um Intents do tipo
	    // pt.ipp.isep.dei.formacao.android.downloadfile.PERCENT_CHANGED
	    // de acordo com o intent filter deste broadcast receiver
	    onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(thisWidget));

	    super.onReceive(context, intent);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

	    // update a cada instância da widget:
	    final int N = appWidgetIds.length;
	    for (int i = 0; i < N; i++) {
	        int appWidgetId = appWidgetIds[i];
	        updateAppWidget(context, appWidgetManager, appWidgetId);
	    }
	}
	
	private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

	    // cria um objecto RemoteViews com o layout da widget
	    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

	    if(mDownloadPercentage < 0) {
	    	views.setTextViewText(R.id.txtName, "Download Canceled or with Error");
	    } else {
	    	if(mDownloadPercentage < 100) 
		        views.setTextViewText(R.id.txtName, "Downloaded " + mDownloadPercentage + "% of the file.");
		    else
		        views.setTextViewText(R.id.txtName, "Download Complete");
	    }
	    
	    appWidgetManager.updateAppWidget(appWidgetId, views);
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
	  Log.d(TAG, "onDeleted called.");
	}

	@Override
	public void onEnabled(Context context) {
	  Log.d(TAG, "onEnabled called.");
	}

	@Override
	public void onDisabled(Context context) {
	  Log.d(TAG, "onDisabled called.");
	}

}
