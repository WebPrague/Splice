package com.example.Splice;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by HUPENG on 2016/9/4.
 */
public class ShowPicActivity extends Activity{

    private ImageView imageView;
    private MinaServer minaServer;
    private Bitmap bitmap= null;

    private Bitmap old_bitmap= null;
    private void init(){
        imageView = (ImageView)findViewById(R.id.imageView);
        minaServer = MinaServer.getInstant();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pic);
        init();
        minaServer.startServer(new SimpleListener() {
            @Override
            public void onSuccess(Object obj) {
                //ShowPicActivity.this.bitmap = (Bitmap)obj;
                //imageView.setImageDrawable(new BitmapDrawable(new MyFileUtil().byteToObject()));

                //Looper.prepare();
                //Toast.makeText(ShowPicActivity.this,(String)obj,Toast.LENGTH_LONG).show();
                Message message = new Message();
                message.obj = obj;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //imageView.setImageDrawable(new BitmapDrawable(ImageEncodeUtil.base64ToBitmap((String)msg.obj)));
            //Toast.makeText(ShowPicActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
            Bitmap bitmap = (Bitmap)msg.obj;
            imageView.setImageDrawable(new BitmapDrawable(bitmap));
            if (old_bitmap != null){
                old_bitmap.recycle();
            }
            old_bitmap = bitmap;
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
