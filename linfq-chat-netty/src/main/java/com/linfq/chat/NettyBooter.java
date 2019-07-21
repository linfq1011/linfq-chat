package com.linfq.chat;

import com.linfq.chat.netty.WebSocketServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * NettyBooter.
 *
 * @author linfq
 * @date 2019/7/21 17:48
 */
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			try {
				WebSocketServer.getInstance().start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
