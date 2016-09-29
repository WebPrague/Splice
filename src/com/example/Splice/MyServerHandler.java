//package com.example.Splice;
//
//import org.apache.mina.core.service.IoHandlerAdapter;
//import org.apache.mina.core.session.IdleStatus;
//import org.apache.mina.core.sesion.IoSession;
//
//public class MyServerHandler extends IoHandlerAdapter{
//
//	public void exceptionCaught(IoSession session, Throwable cause)
//			throws Exception {
//		super.exceptionCaught(session, cause);
//		System.out.println(session.getId());
//		System.out.println("messageCaught");
//	}
//
//	public void messageReceived(IoSession session, Object message)
//			throws Exception {
//		System.out.println(session.getId());
//		System.out.println("messageReceived");
//		String s = (String)message;
//		System.out.println(s);
//		session.write("server reply:" + s);
//	}
//
//	public void messageSent(IoSession session, Object message) throws Exception {
//		System.out.println(session.getId());
//		System.out.println("messageSent");
//	}
//
//	public void sessionClosed(IoSession session) throws Exception {
//		System.out.println(session.getId());
//		System.out.println("sessionClosed");
//	}
//
//	public void sessionCreated(IoSession session) throws Exception {
//		System.out.println(session.getId());
//		System.out.println("sessionCreated");
//	}
//
//	public void sessionIdle(IoSession session, IdleStatus status)
//			throws Exception {
//
//		System.out.println(session.getId());
//		System.out.println("sessionIdle");
//	}
//
//	public void sessionOpened(IoSession session) throws Exception {
//		System.out.println(session.getId());
//		System.out.println("sessionOpened");
//	}
//
//}
