package com.example.Splice;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.Splice.Listener.*;
import com.example.Splice.Listener.SimpleListener;
import com.example.Splice.Mina.MinaUtil;
import org.apache.mina.core.session.IoSession;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by HUPENG on 2016/9/6.
 */
public class MinaActivity extends Activity implements SurfaceHolder.Callback,Camera.PreviewCallback{
    private MinaUtil minaUtil = null;

    private Button btnChoicePic, btnSendPic, btnSendVideo, btnStopVideo;
    private ImageView imageView;

    private Bitmap oldBitmap = null;
    private Bitmap oldBitmap2 = null;

    private Bitmap bitmap;

    private byte[] picByte;
    private Camera camera = null;
    private SurfaceHolder surfaceHolder ;
    private SurfaceView surfaceView;

    private boolean videoFlag = false;

    private void init(){
        btnChoicePic = (Button)findViewById(R.id.btn_choice_pic);
        btnSendPic = (Button)findViewById(R.id.btn_send_pic);
        btnSendVideo = (Button)findViewById(R.id.btn_send_video);
        btnStopVideo = (Button)findViewById(R.id.btn_stop_video);
        imageView = (ImageView)findViewById(R.id.imageView);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        btnChoicePic.setOnClickListener(myOnClickListener);
        btnSendPic.setOnClickListener(myOnClickListener);
        btnSendVideo.setOnClickListener(myOnClickListener);
        btnStopVideo.setOnClickListener(myOnClickListener);

        try {
            String ipAddr = getIpAddress();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (ipAddr.equals("0.0.0.0")) {
                        Looper.prepare();
                        minaUtil = MinaUtil.getInstance(new SimpleListener() {
                            @Override
                            public void onReceive(Object obj, IoSession ioSession) {
                                setBitmap(obj);
                            }
                        }, true);
                    }else {
                        Looper.prepare();
                        minaUtil = MinaUtil.getInstance(new SimpleListener() {
                            @Override
                            public void onReceive(Object obj, IoSession ioSession) {
                                setBitmap(obj);
                            }
                        }, false);
                    }
                }
            }).start();

            //Toast.makeText(MinaActivity.this,getIpAddress(),Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MinaActivity.this,"获取ip地址异常,请先组网:" + e.getMessage(),Toast.LENGTH_SHORT).show();
        }


        surfaceView.getHolder().addCallback(MinaActivity.this);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_pic);
        init();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap)msg.obj;
            imageView.setImageDrawable(new BitmapDrawable(bitmap));
            if(oldBitmap != null){
                oldBitmap.recycle();
            }
            oldBitmap = bitmap;
        }
    };

    private void setBitmap(Object obj){
        Message message = new Message();
        message.obj = obj;
        handler.sendMessage(message);



    }

    private String getIpAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String maxText = info.getMacAddress();
        String ipText = intToIp(info.getIpAddress());

        String status = "";
        if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
        {
            status = "WIFI_STATE_ENABLED";
        }
        String ssid = info.getSSID();
        int networkID = info.getNetworkId();
        int speed = info.getLinkSpeed();
        return ipText;
    }

    private String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
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
            params.setRotation(90);
            camera.setDisplayOrientation(90);

            camera.setParameters(params);
//            camera.startPreview() ;

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
                        70, stream);
                Bitmap bmp = BitmapFactory.decodeByteArray(
                        stream.toByteArray(), 0, stream.size());
                this.bitmap = rotateBitmapByDegree(bmp,90);
                if (videoFlag){
                    sendPic();
                }else {
                    bitmap.recycle();
                }
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
                    sendPic(1);
                    break;
                case R.id.btn_send_video:
                    if (!videoFlag){
                        camera.startPreview();
                    }
                    videoFlag = true;
                    break;
                case R.id.btn_stop_video:
                    if (videoFlag){
                        camera.stopPreview();
                    }
                    videoFlag = false;
                    break;
            }
        }
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
        minaUtil.send(bitmap);
    }

    private void sendPic(int style){
        List<IoSession>sessions = minaUtil.getSessions();
        if(style == 1){
            int pieceWidth = bitmap.getWidth() / 2;
            int pieceHight = bitmap.getHeight() /2;
            for (int i = 0 ; i <= sessions.size() ; i ++){
                Bitmap tempBitmap = OperateImage.imageSplit(bitmap, (i % 2) * pieceWidth,(i / 2) * pieceHight,pieceWidth, pieceHight );
                if (i < sessions.size()){
                    minaUtil.send(tempBitmap,sessions.get(i));
                }else {
                    imageView.setImageDrawable(new BitmapDrawable(tempBitmap));
                }

            }
        }
        //minaUtil.send(bitmap);
    }

    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
}
