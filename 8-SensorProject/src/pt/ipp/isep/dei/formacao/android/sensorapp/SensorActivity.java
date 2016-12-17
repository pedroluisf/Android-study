package pt.ipp.isep.dei.formacao.android.sensorapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class SensorActivity extends Activity {

	private SensorManager mSensorManager;
	private CompassView mCompassView;
	private AccelerometerView mAccelerometerView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// obtém a instância da CompassView
		mCompassView = (CompassView)findViewById(R.id.compassview);
		// obtém a instância da AccelerometerView
		mAccelerometerView = (AccelerometerView)findViewById(R.id.accelerometerview);

		// sensor manager
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		// obtém os sensores (acelerómetro e para o campo magnético)
		Sensor aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		// regista o listener para ambos os sensores
		mSensorManager.registerListener(mSensorListener, aSensor, SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onDestroy() {
		// desregista o listener
		mSensorManager.unregisterListener(mSensorListener);
		super.onDestroy();
	}

	private final SensorEventListener mSensorListener = new SensorEventListener() {

		private float[] mGData = new float[3]; // para o acelerómetro
		private float[] mMData = new float[3]; // para o campo magnético
		private float[] mR = new float[16]; // matriz de rotação
		private float[] mI = new float[16]; // matriz de inclinação
		private float[] mOrientation = new float[3];
		private int mCount;

		public void onSensorChanged(SensorEvent evt) {

			int type = evt.sensor.getType();

			float[] data;
			if (type == Sensor.TYPE_ACCELEROMETER) {
				data = mGData;
			} else if (type == Sensor.TYPE_MAGNETIC_FIELD) {
				data = mMData;
			} else {
				// não deveria chegar aqui
				return;
			}
			for (int i = 0; i < 3; i++)
				data[i] = evt.values[i];

			if(mCount > 50) 
				Log.d("SENSORACTIVITY1", "X=" + mGData[0] + " Y=" + mGData[1] + " Z=" + mGData[2]);
			
			// calcula a matriz de rotação e inclinação com base na informação
			// proveniente de ambos os sensores: gravidade e campo magnético
			SensorManager.getRotationMatrix(mR, mI, mGData, mMData);
			// calcula a orientação com base na matriz de rotação
			SensorManager.getOrientation(mR, mOrientation);

			// apenas para debug...
			if(mCount++ > 50) {
				Log.d("SENSORACTIVITY2", "X=" + mGData[0] + " Y=" + mGData[1] + " Z=" + mGData[2]);
				mCount = 0;
			}
			
			// envia a orientação à compass view
			mCompassView.updateDirection((float) mOrientation[0]);
			// envia os valores do acelerómetro à accelerometer view (apenas X e Y)
			mAccelerometerView.updateAccelerometer(mGData[0], mGData[1]);
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
}