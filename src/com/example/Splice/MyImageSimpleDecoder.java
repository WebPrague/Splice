package com.example.Splice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.io.ByteArrayOutputStream;

/**
 * Created by HUPENG on 2016/9/5.
 */
public class MyImageSimpleDecoder implements ProtocolDecoder{

    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        int length = ioBuffer.getInt();

        System.out.println("接受文件长度：" +  length);

        byte dest[] = new byte[length];
        ioBuffer.get(dest);
        Bitmap bmp;
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        bmp = BitmapFactory.decodeByteArray(dest, 0, dest.length);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, outPut);
        protocolDecoderOutput.write(bmp);
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
