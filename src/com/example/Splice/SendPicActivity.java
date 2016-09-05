package com.example.Splice;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Created by HUPENG on 2016/9/4.
 */
public class SendPicActivity extends Activity{
    private Button btnChoicePic;
    private Button btnSendPic;
    private Bitmap bitmap;
    private MinaClient minaClient ;
    private byte[] picByte;

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
                minaClient = new MinaClient(new SimpleListener() {
                    @Override
                    public void onSuccess(Object obj) {

                    }

                    @Override
                    public void onFailure(String msg) {

                    }
                });

                minaClient.sendPic(bitmap);
            }
        }).start();

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



