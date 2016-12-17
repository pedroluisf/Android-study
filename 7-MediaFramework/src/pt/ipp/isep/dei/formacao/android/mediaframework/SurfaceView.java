package pt.ipp.isep.dei.formacao.android.mediaframework;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;

public class SurfaceView extends android.view.SurfaceView implements SurfaceHolder.Callback {

	private Camera mCamera;

	public SurfaceView(Context context) {
		super(context);

	    // Objectos para controlar a camera.
	    getHolder().addCallback(this);
	    getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public Camera getCamera() {
		return mCamera;
	}

	public void surfaceCreated(SurfaceHolder arg0) {

		// Obter acesso à camera
	    mCamera = Camera.open();

	    try {
	      mCamera.setPreviewDisplay(arg0);
	    } catch (IOException e) {}    
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		Camera.Parameters parameters = mCamera.getParameters();
	    parameters.setPreviewSize(480, 320);

	    // Começar a capturar a imagem obtida pela camera.
	    mCamera.setParameters(parameters);
	    mCamera.startPreview();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {

		// Para a captura da camera e fazer release do objecto.
	    try {
	      mCamera.stopPreview();

	    } catch (Exception e) {}

	    mCamera.release();
	    mCamera = null;        
	}
}