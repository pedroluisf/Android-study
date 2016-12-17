package pt.ipp.isep.dei.formacao.android.sensorapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AccelerometerView extends View {

	private float[] accelerometer = new float[2];
	private float[] position;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final float RADIUS = 4.0f;

	// construtores herdados da class View
	public AccelerometerView(Context context) {
		super(context);
		init();
	}

	public AccelerometerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AccelerometerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// inicializa o objecto Paint que define como irá ser
		// desenhada a circunferência e a border
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setColor(Color.WHITE);
		paint.setTextSize(30);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// se for a primeira vez, define a posição no centro da view
		if (position == null) {
			position = new float[2];
			position[0] = getMeasuredWidth() / 2;
			position[1] = getMeasuredHeight() / 2;
		}

		// altera (de uma forma maneira muito simplista)
		// a posição de acordo com os valores do acelerómetro
		position[0] -= accelerometer[0];
		position[1] += accelerometer[1];

		// verifica se a posição não está fora da área de desenho
		if (position[0] > (getMeasuredWidth() - RADIUS))
			position[0] = getMeasuredWidth() - RADIUS;
		if (position[0] < RADIUS)
			position[0] = RADIUS;
		if (position[1] > (getMeasuredHeight() - RADIUS))
			position[1] = getMeasuredHeight() - RADIUS;
		if (position[1] < RADIUS)
			position[1] = RADIUS;

		// desenha uma border a definir a view e a circunferência
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
		canvas.drawCircle(position[0], position[1], RADIUS, paint);
	}

	// método que recebe os valores do acelerómetro
	public void updateAccelerometer(float x, float y) {
		accelerometer[0] = x;
		accelerometer[1] = y;
		invalidate();
	}

}
