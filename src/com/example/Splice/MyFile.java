package com.example.Splice;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by HUPENG on 2016/9/4.
 */
public class MyFile implements Serializable{

    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
