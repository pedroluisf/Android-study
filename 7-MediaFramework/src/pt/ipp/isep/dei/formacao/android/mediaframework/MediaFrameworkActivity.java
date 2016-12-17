package pt.ipp.isep.dei.formacao.android.mediaframework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;


public class MediaFrameworkActivity extends Activity {

	// Imagem da camera
	private SurfaceView mPreview;

	// Custom View
	private MyImageButton mImageButtonVibrate, mImageButtonSound;

	// Layout a utilizar
	private LinearLayout mLinearLayout;

	private boolean photoTaken = false;    
	private byte[] photoData = null;

	// Objectos Multimedia
	private Vibrator mVibrator;
	private MediaPlayer mMediaPlayer;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	// Inicializar os objectos Multimedia
    	mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    	mMediaPlayer = MediaPlayer.create(this, R.raw.bacardimojito);

    	// Inicializar formatação do ecrã.
    	getWindow().setFormat(PixelFormat.TRANSPARENT);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Orientação
    	requestWindowFeature(Window.FEATURE_NO_TITLE); // Não mostrar o título da aplicação
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Utilizar FullScreen

    	// Inicializar a nossa Custom View na qual quando se clicar vai iniciar/parar a reprodução da música.
    	mImageButtonSound = new MyImageButton(this, "Sound");
    	mImageButtonSound.setOnTouchListener(new OnTouchListener(){
    	    public boolean onTouch(View mView, MotionEvent mEvent) {
    			switch(mEvent.getAction()) {
    				case MotionEvent.ACTION_DOWN:
    					if(mMediaPlayer.isPlaying())
    						mMediaPlayer.pause();
    					else
    						mMediaPlayer.start();

    					return true;
    			}

    			return false;
    	    }
    	});

    	// Inicializar a nossa Custom View na qual quando se clicar vai colocar o dispositivo a vibrar
    	mImageButtonVibrate = new MyImageButton(this, "Vibrate");        
    	mImageButtonVibrate.setOnTouchListener(new OnTouchListener() {
    		public boolean onTouch(View mView, MotionEvent mEvent) {
    			switch(mEvent.getAction()){
    				// Quando clicamos
    	        	case MotionEvent.ACTION_DOWN:
    	        		mVibrator.vibrate(1000000000);
    	        		return true;

    	        	// Quando levantamos o dedo
    	        	case MotionEvent.ACTION_UP:
    	        		mVibrator.cancel();
    	        		return true;
    			}

    			return false;
    	    }
    	});

    	// Adicionar à Activity a view da camera
    	mPreview = new SurfaceView(this);
    	setContentView(mPreview);

    	// Inicializar o layout
    	mLinearLayout = new LinearLayout(this);
    	mLinearLayout.setOrientation(LinearLayout.VERTICAL);    

    	// Margem entre os objectos
    	LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	mLayoutParams.setMargins(50, 70, 0, 0);

    	// Adicionar as nossas Custom Views ao Layout
    	mLinearLayout.addView(mImageButtonSound, mLayoutParams);
    	mLinearLayout.addView(mImageButtonVibrate, mLayoutParams);

    	// Adicionar o layout na Activity, o qual irá ficar por cima da View da camera
    	addContentView(mLinearLayout, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

    	// Quando se carrega na track ball, ele tira a fotografia
    	if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {

    		// Parar a camera
    		mPreview.getCamera().stopPreview();
    		mPreview.getCamera().takePicture(null, null, jpegCallback);
    		photoTaken = true;

    		return true;
    	}

    	// Na tecla back, vamos voltar a activar a camera ou terminar esta Activity
    	if (keyCode == KeyEvent.KEYCODE_BACK) {

    		if(photoTaken) {
    			mPreview.getCamera().startPreview();
    			photoTaken = false;
    		} else
    			MediaFrameworkActivity.this.finish();

    		return true;
    	}

    	return false;
    }
    
    private PictureCallback jpegCallback = new PictureCallback() {
    	public void onPictureTaken(byte[] imageData, Camera camera) {                        
    		photoData = imageData;
    	}    
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.setQwertyMode(true);
    	MenuItem mnu1 = menu.add(0, 0, 0, R.string.savemenu);
    	{
    		mnu1.setAlphabeticShortcut('s');
    		mnu1.setIcon(R.drawable.ic_menu_save);
    	}

    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch (item.getItemId()) {
    		case 0:
    			// Criar um novo Bitmap
    			Bitmap mBitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);

    			// Um nome para dar ao ficheiro, neste caso a data
    			Date date = new Date();
    			SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");

    			try {
    				// Criar o objecto ficheiro, na raiz do cartão 
    				File file = new File(Environment.getExternalStorageDirectory().toString(), "/" + sdf.format(date) + ".jpg");
    				file.createNewFile();
    				OutputStream fOut = new FileOutputStream(file);

    				mBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
    				fOut.flush();
    				fOut.close();

    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    			return true;
    	}
    	return super.onMenuItemSelected(featureId, item);
    }
}