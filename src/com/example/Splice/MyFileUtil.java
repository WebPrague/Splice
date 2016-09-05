package com.example.Splice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.apache.mina.core.buffer.IoBuffer;

import java.io.ByteArrayOutputStream;

/**
 * Created by HUPENG on 2016/9/4.
 */
public class MyFileUtil {

    public byte[] objectToByte(Object obj){

        Bitmap bitmap = null;
        if (obj instanceof Bitmap){
            bitmap = (Bitmap)obj;
        }
        if (bitmap != null){
            //读取图片到ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] picByte = baos.toByteArray();
            return picByte;
        }
        return null;
    }

    public Object byteToObject(byte[] bytes){
        Bitmap bmp;
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, outPut);
        return bmp;
    }
}
