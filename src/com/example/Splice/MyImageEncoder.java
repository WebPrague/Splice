package com.example.Splice;

import android.graphics.Bitmap;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by HUPENG on 2016/9/4.
 */
public class MyImageEncoder implements ProtocolEncoder{

    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {
        Bitmap bitmap = null;
        if (message instanceof Bitmap){
            bitmap = (Bitmap)message;
        }
        if (bitmap != null){
            //读取图片到ByteArrayOutputStream

//            CharsetEncoder charsetEncoder = (CharsetEncoder)ioSession.getAttribute("encoder");
//            if(charsetEncoder == null){
//                charsetEncoder = Charset.defaultCharset().newEncoder();
//                ioSession.setAttribute("encoder", charsetEncoder);
//            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 1, baos);
            byte[] picByte = baos.toByteArray();

            int length = picByte.length;

            System.out.println("发送的文件长度：" + length);

            IoBuffer ioBuffer;
            ioBuffer = IoBuffer.allocate(picByte.length+4);

            ioBuffer.setAutoExpand(true);
            ioBuffer.putInt(length);
            ioBuffer.put(picByte);
            ioBuffer.flip();
            out.write(ioBuffer);
        }
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
