package com.example.Splice;

import android.graphics.Bitmap;
import android.util.Log;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by HUPENG on 2016/9/4.
 */
public class MinaServer {
    private static MinaServer minaServer = null;
    private static NioSocketAcceptor acceptor;
    private MinaServer(){
    }

    public static MinaServer getInstant(){
        if (minaServer == null){
            minaServer = new MinaServer();
        }
        return minaServer;
    }

    private SimpleListener simpleListener;

    public void startServer(SimpleListener simpleListener){
        this.simpleListener = simpleListener;
        try {
            if(acceptor == null){
                acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
            }
            acceptor.setReuseAddress(true);
            acceptor.getSessionConfig().setReadBufferSize(2048*10);
            acceptor.setHandler(new MyServerHandler());
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyImageFactory()));
            //acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 5);Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.11
            acceptor.bind(new InetSocketAddress(9191));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    class MyServerHandler extends IoHandlerAdapter {

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
            Log.i("imudges","receive");
            //System.out.println(()message);
            simpleListener.onSuccess(message);
        }

        public void messageSent(IoSession session, Object message) throws Exception {
            System.out.println(session.getId());
            System.out.println("messageSent");
        }

        public void sessionClosed(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionClosed");
        }

        public void sessionCreated(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionCreated");
            System.out.println(session.getRemoteAddress());
        }

        public void sessionIdle(IoSession session, IdleStatus status)
                throws Exception {

            System.out.println(session.getId());
            System.out.println("sessionIdle");
        }

        public void sessionOpened(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionOpened");
        }

    }
}

