package pt.ipp.isep.dei.formacao.android.mediaframework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;

public class MyImageButton extends View {

	// Texto a aparecer no controlo
	private String mText;
	// Imagem do controlo
	private Drawable imgRectangle;
	
	private Paint paint = new Paint(); 

	public MyImageButton(Context context, String text) {
		super(context);

	    imgRectangle = context.getResources().getDrawable(R.drawable.rectangle);
	    mText = text;
	}

	public String getmText() {
		return mText;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Definir o tamanho da View.
	    setMeasuredDimension(imgRectangle.getIntrinsicWidth(), imgRectangle.getIntrinsicHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Desenhar a imagem com o tamanho da mesma.
	    imgRectangle.setBounds(0, 0, imgRectangle.getIntrinsicWidth(), imgRectangle.getIntrinsicHeight());        
	    imgRectangle.draw(canvas);

	    // Criar um objecto paint que irá ter as características 
	    // necessárias para a formatação do texto.
	    paint.setStyle(Paint.Style.FILL); 
	    paint.setColor(Color.WHITE); 
	    paint.setTextSize(20);

	    // Escrever o texto.
	    // Parâmetros:
	    // 1. Texto
	    // 2. Ponto onde começa na largura
	    // 3. Ponto onde começa na altura
	    // 4. Objecto Paint        
	    canvas.drawText(mText, imgRectangle.getIntrinsicWidth()/2 - paint.measureText(mText)/2, 
	            imgRectangle.getIntrinsicHeight()/2 + ((-paint.ascent() - paint.descent())/2), paint);

	    super.onDraw(canvas);
	}
}