package com.development.napptime.pix;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Napptime on 2/10/15.
 *
 *  Klasi sem skilgreinir myndavél og virkni hennar sem fragment inn í mainactivity. Þessi klasi
 *  sér einnig um þá virkni sem fylgir því að taka myndir og setja þær inn í gagnagrunn.
 */
public class FragmentCamera extends Fragment implements Camera.PictureCallback, Camera.ShutterCallback {

    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    private Camera camera = null;

    private final static double epsilon = 0.17;

    //Button that lets the user take pictures
    private ImageButton takePicture;
    //Button that lets the user switch between front facing and rear facing cameras
    private ImageButton switchCamera;

    //We need to know if the preview is on and if the camera is ready
    private boolean isPreviewOn = false;
    private boolean cameraReady = false;

    // Default camera id (rear facing)
    int defaultCameraId;
    //Camera ids
    int numberOfCameras;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        super.onCreate(savedInstanceState);

        preview = (SurfaceView) view.findViewById(R.id.preview);

        //callback so we are notified when the underlying
        //surface is created and destroyed
        preview.getHolder().addCallback(surfaceCallback);
        previewHolder = preview.getHolder();
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //Our buttons
        takePicture = (ImageButton) view.findViewById(R.id.btnTakePic);
        switchCamera = (ImageButton) view.findViewById(R.id.btnCameraSwitch);

        //When clicked: take picture
        takePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                takePicture(view);
            }
        });

        return view;
    }

    public void removeAnimation() {
        {
            try {
                getActivity().getActionBar().getClass().getDeclaredMethod("setShowHideAnimationEnabled", boolean.class).invoke(getActivity().getActionBar(), false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {

        removeAnimation();
        getCameraId();
        switchCamera();

        super.onResume();
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        camera.startPreview();
        preview.setVisibility(View.VISIBLE);
    }

    public void getCameraId() {
        //total number of cameras
        numberOfCameras = Camera.getNumberOfCameras();

        //get id of default camera
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }
    }

    public void switchCamera() {

        //When clicked: switch cameras
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We stop the preview before anything else
                if (isPreviewOn) {
                    camera.stopPreview();
                }
                camera.release();

                //We change the defaultCameraId to the one not currently
                //active
                if (defaultCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    defaultCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    defaultCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                //Open the camera again with the new defaultCameraId
                camera = Camera.open(defaultCameraId);

                //Reconfigure displayOrientation
                setCameraDisplayOrientation(getActivity(), defaultCameraId, camera);
                try {

                    camera.setPreviewDisplay(previewHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Start the preview
                camera.startPreview();
            }
        });

    }

    @Override
    public void onPause() {
        if (isPreviewOn) {
            camera.stopPreview();
        }
        camera.release();
        camera = null;
        preview.setVisibility(View.GONE);
        super.onPause();
    }

    //Take a picture of the surfaceView
    //Need to wait for the shutter- and pictureCallbacks to provide
    //the actual image data
    public void takePicture(View v) {
        camera.takePicture(this, null, null, this);
    }

    @Override
    public void onShutter() {
        Toast.makeText(getActivity(), "Clickity clack!", Toast.LENGTH_SHORT).show();
    }

    //When a picture is taken save it locally in Documents (for now)
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        //Use the picture date to provide each .jpg file with
        //a unique name
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        String fileName = "Image-" + dateFormat.format(date) + ".jpg";

        //Decode the data to a bitmap
        Bitmap myImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap img = Utility.getResizedBitmap(myImage, 2560, 1600);
        img = Utility.rotate(img, defaultCameraId);

        //Store pictures in Documents
        File folder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File pictureFile = new File(folder, fileName);
        if (pictureFile.exists()) pictureFile.delete();

        //Output the file
        FileOutputStream fop = null;
        try {
            fop = new FileOutputStream(pictureFile);
            img.compress(Bitmap.CompressFormat.JPEG, 100, fop);
            fop.flush();
            fop.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent myIntent = new Intent(getActivity(), EditPictureActivity.class);
        myIntent.putExtra("imgPath", pictureFile.getPath());
        myIntent.putExtra("defaultCameraId", defaultCameraId);
        startActivity(myIntent);
    }

    //Get best previewSize
    private Camera.Size getBestPreviewSize(int width, int height) {
        Camera.Size result=null;
        Camera.Parameters p = camera.getParameters();
        for (Camera.Size size : p.getSupportedPreviewSizes()) {
            if (size.width<=width && size.height<=height) {
                if (result==null) {
                    result=size;
                } else {
                    int resultArea=result.width*result.height;
                    int newArea=size.width*size.height;

                    if (newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }
        return result;
    }


    //Initiate our preview
    private void initPreview(int width, int height) {
        if (camera != null && previewHolder.getSurface() != null) {
            try {
                camera.setPreviewDisplay(previewHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //If it's not ready get get our camera parameters and
            //then set the camera as ready
            if (!cameraReady) {
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = getBestPreviewSize(width, height);

                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);
                    camera.setParameters(parameters);
                    cameraReady = true;
                }

            }
        }
    }


    private void startPreview() {
        if (!isPreviewOn && camera != null) {
            camera.startPreview();
            isPreviewOn = true;
        }
    }

    // same for stopping the preview
    private void stopPreview() {
        if (isPreviewOn && (camera != null)) {
            camera.stopPreview();
            isPreviewOn = false;
        }
    }

    //Configures the camera orientation
    public static void setCameraDisplayOrientation(Activity activity, int cameraId,
                                                   android.hardware.Camera camera) {

        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();

        android.hardware.Camera.getCameraInfo(cameraId, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            //When the surface is ready get the camera
            //and tell it where to draw

            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

            setCameraDisplayOrientation(getActivity(), defaultCameraId, camera);
            initPreview(width, height);
            startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            //Destroy the surface when we return and stop the preview
            if (camera != null) {
                camera.stopPreview();
                camera.release();
            }
        }
    };
}
