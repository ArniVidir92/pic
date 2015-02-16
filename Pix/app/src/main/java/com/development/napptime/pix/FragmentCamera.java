package com.development.napptime.pix;

import android.app.Activity;
import android.app.Fragment;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Napptime on 2/10/15.
 */
public class FragmentCamera extends Fragment implements Camera.PictureCallback, Camera.ShutterCallback {

    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    private Camera camera = null;
    private ImageButton takePicture;
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
        preview.getHolder().addCallback(surfaceCallback);
        previewHolder = preview.getHolder();
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

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

        takePicture = (ImageButton) view.findViewById(R.id.btnOne);

        takePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                takePicture(view);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        camera = Camera.open();
        startPreview();
    }

    @Override
    public void onPause() {
        if (isPreviewOn) {
            camera.stopPreview();
        }

        camera.release();
        camera = null;
        isPreviewOn = false;

        super.onPause();
    }

    public void takePicture(View v) {
        camera.takePicture(this, null, null, this);
    }


    @Override
    public void onShutter() {
        Toast.makeText(getActivity(), "Clickity clack!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date();
        String fileName = "Image-" + dateFormat.format(date) + ".jpg";

        Bitmap myImage = BitmapFactory.decodeByteArray(data , 0, data.length);
        File folder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);

        File pictureFile = new File(folder, fileName);
        if (pictureFile.exists ()) pictureFile.delete ();
        FileOutputStream fop = null;

        try {

            fop = new FileOutputStream(pictureFile);
            myImage.compress(Bitmap.CompressFormat.JPEG, 80, fop);
            fop.flush();
            fop.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {

        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {

            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                }

                else {
                    int resultArea = result.width*result.height;
                    int newArea = size.width*size.height;

                    if (newArea>resultArea) {
                        result = size;
                    }
                }
            }
        }
        return result;
    }

    private void initPreview(int width, int height) {
        if (camera != null && previewHolder.getSurface() != null) {
            try {
                camera.setPreviewDisplay(previewHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!cameraReady) {
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = getBestPreviewSize(width, height, parameters);

                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);
                    camera.setParameters(parameters);
                    cameraReady = true;
                }
            }
        }
    }

    private void startPreview() {
        if (cameraReady && camera != null) {
            camera.startPreview();
            isPreviewOn = true;
        }
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId,
                                                   android.hardware.Camera camera) {

        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();

        android.hardware.Camera.getCameraInfo(cameraId, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
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
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            setCameraDisplayOrientation(getActivity(),defaultCameraId, camera);
            initPreview(width, height);
            startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };
}


