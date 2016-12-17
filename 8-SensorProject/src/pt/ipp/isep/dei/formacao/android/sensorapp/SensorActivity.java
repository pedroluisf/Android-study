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
		
		// obt�m a inst�ncia da CompassView
		mCompassView = (CompassView)findViewById(R.id.compassview);
		// obt�m a inst�ncia da AccelerometerView
		mAccelerometerView = (AccelerometerView)findViewById(R.id.accelerometerview);

		// sensor manager
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		// obt�m os sensores (aceler�metro e para o campo magn�tico)
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

		private float[] mGData = new float[3]; // para o aceler�metro
		private float[] mMData = new float[3]; // para o campo magn�tico
		private float[] mR = new float[16]; // matriz de rota��o
		private float[] mI = new float[16]; // matriz de inclina��o
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
				// n�o deveria chegar aqui
				return;
			}
			for (int i = 0; i < 3; i++)
				data[i] = evt.values[i];

			if(mCount > 50) 
				Log.d("SENSORACTIVITY1", "X=" + mGData[0] + " Y=" + mGData[1] + " Z=" + mGData[2]);
			
			// calcula a matriz de rota��o e inclina��o com base na informa��o
			// proveniente de ambos os sensores: gravidade e campo magn�tico
			SensorManager.getRotationMatrix(mR, mI, mGData, mMData);
			// calcula a orienta��o com base na matriz de rota��o
			SensorManager.getOrientation(mR, mOrientation);

			// apenas para debug...
			if(mCount++ > 50) {
				Log.d("SENSORACTIVITY2", "X=" + mGData[0] + " Y=" + mGData[1] + " Z=" + mGData[2]);
				mCount = 0;
			}
			
			// envia a orienta��o � compass view
			mCompassView.updateDirection((float) mOrientation[0]);
			// envia os valores do aceler�metro � accelerometer view (apenas X e Y)
			mAccelerometerView.updateAccelerometer(mGData[0], mGData[1]);
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
}