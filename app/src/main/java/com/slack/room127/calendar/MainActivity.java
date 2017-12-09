package com.slack.room127.calendar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;


public class MainActivity extends Activity {

    private LinearLayout mLinearLayout;
    private SurfaceView mSurfaceView;

    private Camera mCamera;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mSurfaceView = (SurfaceView) findViewById( R.id.surfaceView );
//        mCamera = Camera.open();
//        mLinearLayout = (LinearLayout) findViewById( R.id.parent );

//        mCamera.setPreviewCallback( new CameraPreviewCallback() );
//
//        SurfaceHolder holder = mSurfaceView.getHolder();
//
//        holder.addCallback( new CameraCallback() );
    }

    class CameraCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated( SurfaceHolder holder ) {


            try {
                int d = 90;
                mCamera.setDisplayOrientation( d );

                mCamera.setPreviewDisplay( holder );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged( SurfaceHolder holder, int format, int width, int height ) {

            mCamera.startPreview();
        }

        @Override
        public void surfaceDestroyed( SurfaceHolder holder ) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCamera = Camera.open();
//        cameraCurrentlyLocked = defaultCameraId;

        mCamera.setPreviewCallback( new CameraPreviewCallback() );

        SurfaceHolder holder = mSurfaceView.getHolder();

        holder.addCallback( new CameraCallback() );
    }

    @Override
    protected void onPause() {
        super.onPause();

        if ( mCamera != null ) {
//            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }

    class CameraPreviewCallback implements Camera.PreviewCallback {

        @Override
        public void onPreviewFrame( byte[] data, Camera camera ) {

            int previewWidth = camera.getParameters().getPreviewSize().width;
            int previewHeight = camera.getParameters().getPreviewSize().height;

            // プレビューデータから Bitmap を生成
            Bitmap bmp = getBitmapImageFromYUV(data, previewWidth, previewHeight);
        }

        public Bitmap getBitmapImageFromYUV( byte[] data, int width, int height ) {
            YuvImage yuvimage = new YuvImage( data, ImageFormat.NV21, width, height, null );
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvimage.compressToJpeg( new Rect( 0, 0, width, height ), 80, baos );
            byte[] jdata = baos.toByteArray();
            BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
//            bitmapFactoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmapFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bmp = BitmapFactory.decodeByteArray( jdata, 0, jdata.length, bitmapFactoryOptions );
            return bmp;
        }
    }
}
