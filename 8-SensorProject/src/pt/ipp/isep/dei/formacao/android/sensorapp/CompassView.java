package pt.ipp.isep.dei.formacao.android.sensorapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {

	private float direction = 0;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	// construtores herdados da class View
	public CompassView(Context context) {
		super(context);
		init();
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// inicializa o objecto Paint que define como irá ser
		// desenhada a bússula
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setColor(Color.WHITE);
		paint.setTextSize(30);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// obtém a posição central da view
		int cxCompass = getMeasuredWidth() / 2;
		int cyCompass = getMeasuredHeight() / 2;
		float radiusCompass;

		// obtém o raio da bússula com base no menor valor entre
		// a width e a height da view; o raio corresponde a 90%
		// de metade do menor valor
		if (cxCompass > cyCompass) {
			radiusCompass = (float) (cyCompass * 0.9);
		} else {
			radiusCompass = (float) (cxCompass * 0.9);
		}

		// desenha a circunferência e a border da view
		canvas.drawCircle(cxCompass, cyCompass, radiusCompass, paint);
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

		// desenha a semi-recta que indica o norte
		canvas.drawLine(
				cxCompass,
				cyCompass,
				(float) (cxCompass + radiusCompass
						* Math.sin((double) -direction)),
				(float) (cyCompass - radiusCompass
						* Math.cos((double) -direction)), paint);

		// apresenta o valor em graus do ângulo obtido através do sensor
		canvas.drawText(String.valueOf((int) RadToDeg(direction)), cxCompass,
				cyCompass, paint);

	}

	// método que recebe os valores em graus do sensor
	public void updateDirection(float dir) {
		direction = dir;
		invalidate();
	}

	// converte de radianos para graus
	private double RadToDeg(double val) {
		return val * (180 / Math.PI);
	}

}
