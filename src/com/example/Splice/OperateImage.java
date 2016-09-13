package com.example.Splice;


import android.graphics.Bitmap;

public class OperateImage {
    public static Bitmap imageSplit(Bitmap bitmap, int x1,int y1, int x2, int y2){
        return Bitmap.createBitmap(bitmap, x1, y1, x2, y2);
    }
}