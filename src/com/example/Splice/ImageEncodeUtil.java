package com.example.Splice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *  @author HUPENG
 *  @version 1.0
 *  将图片与base64相互装换的工具类
 */
public class ImageEncodeUtil {
    /**
     * bitmap装base64,能将二进制转换成可见的ascII码
     * @param bitmap 图像资源对象
     * @return base64编码
     * */
    public static String bitmapToBase64(Bitmap bitmap){
        String result="";
        ByteArrayOutputStream bos=null;
        try {
            if(null!=bitmap){
                bos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos);//将bitmap放入字节数组流中

                bos.flush();//将bos流缓存在内存中的数据全部输出，清空缓存
                bos.close();

                byte []bitmapByte=bos.toByteArray();
                result=Base64.encodeToString(bitmapByte, Base64.NO_CLOSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(null!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        result = result.replaceAll("\n","^");
        return result;
    }

    /**
     * base64转Bitmap对象
     * @param base64String Base64字符串
     * @return bitmap 对象
     * */
    public static Bitmap base64ToBitmap(String base64String){
        if (base64String == null || base64String.equals("")){
            return null;
        }
        base64String = base64String.replaceAll("^","\n");
        byte[] bytes;
        //消除在网络传输的过程中可能出现的+变成空格的错误
        base64String = base64String.replaceAll(" ","+");
        bytes = Base64.decode(base64String, Base64.NO_CLOSE);
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
}
