package com.linfq.chat.netty;

import com.alibaba.fastjson.JSON;
import com.linfq.chat.common.constant.MsgActionEnum;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 处理消息的handler.
 * <p>
 * TextWebSocketFrame: 在netty中，是用于为WebSocket专门处理文本的对象，frame是消息的载体。
 *
 * @author linfq
 * @date 2019/7/21 9:54
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	/**
	 * 用于记录和管理所有客户端的channel.
	 */
	private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		// 获取客户端传输过来的消息
		String content = msg.text();

		Channel currentChannel = ctx.channel();

		// 1. 获取客户端发来的消息
		DataContent dataContent = JSON.parseObject(content, DataContent.class);
		// 2. 判断消息类型，根据不同的类型来处理不同的业务
		Integer action = dataContent.getAction();
		if (MsgActionEnum.CONNECT.type.equals(action)) {
			// 2.1 当websocket第一次open的时候，初始化channel，把用的channel和userid关联起来
			Integer senderId = dataContent.getChatMsg().getSenderId();
			UserChannelRel.put(senderId, currentChannel);
		} else if (MsgActionEnum.CHAT.type.equals(action)) {
			// 2.2 聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
		} else if (MsgActionEnum.SIGNED.type.equals(action)) {
			// 2.3 签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态[已签收]
		} else if (MsgActionEnum.KEEPALIVE.type.equals(action)) {
			// 2.4 心跳类型的消息
		}




	}

	/**
	 * 当客户端连接服务端之后（打开连接）
	 * 获取客户端的channel，并且放到ChannelGroup中去进行管理.
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		users.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
		users.remove(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		// 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
		ctx.channel().close();
		users.remove(ctx.channel());
	}
}
