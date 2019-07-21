package com.linfq.chat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

/**
 * WebSocketServer.
 *
 * @author linfq
 * @date 2019/7/21 9:36
 */
@Component
public class WebSocketServer {

	/**
	 * 使用内部类维护单例实例，WebSocketServer被加载时，内部类并不会初始化，因此可以做到延迟载入.
	 */
	private static class SingletonHolder {
		private static final WebSocketServer INSTANCE = new WebSocketServer();
	}

	/**
	 * 由于创建实例是在类加载时完成的，因此绝对线程安全.
	 */
	public static WebSocketServer getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private EventLoopGroup mainGroup;
	private EventLoopGroup subGroup;
	private ServerBootstrap server;
	private ChannelFuture future;

	private WebSocketServer () {
		mainGroup = new NioEventLoopGroup();
		subGroup = new NioEventLoopGroup();
		server = new ServerBootstrap();
		server.group(mainGroup, subGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new WebSocketServerInitializer());
	}

	public void start() {
		this.future = server.bind(8088);
		System.err.println("netty websocket server 启动完毕...");
	}

}
