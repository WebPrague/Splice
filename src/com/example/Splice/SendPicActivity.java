package com.example.Splice;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.*;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * Created by HUPENG on 2016/9/4.
 */
public class SendPicActivity extends Activity implements SurfaceHolder.Callback,Camera.PreviewCallback {
    private Button btnChoicePic;
    private Button btnSendPic;
    private Bitmap bitmap;
    private MinaClient minaClient ;
    private byte[] picByte;
    private Camera camera = null;
    private SurfaceHolder surfaceHolder ;


    //初始化
    private void init(){
        btnChoicePic = (Button)findViewById(R.id.btn_choice_pic);
        btnSendPic = (Button)findViewById(R.id.btn_send_pic);

        MyOnClickListener myOnClickListener = new MyOnClickListener();

        btnChoicePic.setOnClickListener(myOnClickListener);
        btnSendPic.setOnClickListener(myOnClickListener);
//        btnChoicePic.setoncl
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_pic);
        //初始化
        init();

//
        SurfaceView view = (SurfaceView) findViewById(R.id.surface_view);
        view.getHolder().addCallback(this);
        view.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    private void choicPic(){
        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                /* 将Bitmap设定到ImageView */
                imageView.setImageBitmap(bitmap);

                this.bitmap = bitmap;

                //读取图片到ByteArrayOutputStream
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                picByte = baos.toByteArray();


            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void sendPic(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (minaClient == null){
                    minaClient = new MinaClient(new SimpleListener() {
                        @Override
                        public void onSuccess(Object obj) {

                        }

                        @Override
                        public void onFailure(String msg) {

                        }
                    });
                }
                minaClient.sendPic(bitmap);
            }
        }).start();

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try{
            camera = Camera.open();
            camera.setPreviewDisplay(holder);
            Camera.Parameters params = camera.getParameters();
            params.setPreviewSize(352, 288);
            camera.setParameters(params);
            camera.startPreview() ;
            camera.setPreviewCallback(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(camera != null) camera.release() ;
        camera = null ;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Camera.Size size = camera.getParameters().getPreviewSize();
        try {
            YuvImage image = new YuvImage(bytes, ImageFormat.NV21, size.width,
                    size.height, null);
            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height),
                        10, stream);
                Bitmap bmp = BitmapFactory.decodeByteArray(
                        stream.toByteArray(), 0, stream.size());
                this.bitmap = bmp;
                sendPic();
                stream.close();

            }
        } catch (Exception ex) {
            Log.e("Sys", "Error:" + ex.getMessage());
        }
    }


    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_choice_pic:
                    choicPic();
                    break;
                case R.id.btn_send_pic:
                    sendPic();
                    break;
            }
        }
    }


}



