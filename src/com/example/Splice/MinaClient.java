package com.example.Splice;

import android.graphics.Bitmap;
import android.util.Log;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * Created by HUPENG on 2016/9/4.
 */
public class MinaClient {
    private IoSession session = null;
    private SimpleListener simpleListener;
    private Bitmap oldBitmap = null;
    public MinaClient(SimpleListener simpleListener){
        this.simpleListener = simpleListener;
        NioSocketConnector connector = new NioSocketConnector();
        connector.setHandler(new MyClientHandler());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyImageFactory()));
        ConnectFuture future = connector.connect(new InetSocketAddress("192.168.43.1", 9191));
        future.awaitUninterruptibly();
        session = future.getSession();
    }

    public void sendPic(Object obj){
        session.write(obj);
        Bitmap bitmap = (Bitmap)obj;
        if (oldBitmap != null){
            oldBitmap.recycle();
        }
        oldBitmap = bitmap;

    }

    class MyClientHandler extends IoHandlerAdapter {

        public void exceptionCaught(IoSession session, Throwable cause)
                throws Exception {
            super.exceptionCaught(session, cause);
            System.out.println(session.getId());
            System.out.println("messageCaught");
            System.out.println(cause.getMessage());
        }

        public void messageReceived(IoSession session, Object message)
                throws Exception {
            System.out.println(session.getId());
            System.out.println("messageReceived");

        }

        public void messageSent(IoSession session, Object message) throws Exception {
            System.out.println(session.getId());
            System.out.println("messageSent");
            //session.getService().dispose();
        }

        public void sessionClosed(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionClosed");
        }

        public void sessionCreated(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionCreated");
        }

        public void sessionIdle(IoSession session, IdleStatus status)
                throws Exception {
            //客户端空闲的时候进行回调
            System.out.println(session.getId());
            System.out.println("sessionIdle");
        }

        public void sessionOpened(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionOpened");
        }

    }

}
