package com.linfq.chat.netty;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;


/**
 * 继承ChannelInboundHandlerAdapter，从而不需要实现channelRead0方法.
 *
 * @author linfq
 * @date 2019/8/11 16:01
 */
@Slf4j
public class HeartBeadHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲）
		if (evt instanceof IdleStateEvent) {
			// 强制类型转换
			IdleStateEvent event = (IdleStateEvent) evt;

			if (event.state() == IdleState.READER_IDLE) {
				log.debug("进入读空闲...");
			} else if (event.state() == IdleState.WRITER_IDLE) {
				log.debug("进入写空闲...");
			} else if (event.state() == IdleState.ALL_IDLE) {
				log.info("channel关闭前，users的数量为：" + ChatHandler.users.size());
				Channel channel = ctx.channel();
				// 关闭无用的channel，以防资源浪费
				channel.close();
				log.info("channel关闭后，users的数量为：" + ChatHandler.users.size());
			}
		}
	}
}
