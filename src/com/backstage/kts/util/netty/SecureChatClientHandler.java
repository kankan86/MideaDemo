/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.backstage.kts.util.netty;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.ssl.SslHandler;

import android.content.Context;

/**
 * Handles a client-side channel.
 */
public class SecureChatClientHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger
			.getLogger(SecureChatClientHandler.class.getName());
	private static Context context;

	public SecureChatClientHandler(Context context) {
		this.context = context;
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent) {
			logger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		// Get the SslHandler from the pipeline
		// which were added in SecureChatPipelineFactory.
		SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);

		// Begin handshake.
		sslHandler.handshake();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		System.err.println(e.getMessage());
		String jsonstr = e.getMessage().toString();
		try {
			// 接收websocket信息
			System.out.println("webSocket接收到的信息：" + jsonstr);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(Level.WARNING, "Unexpected exception from downstream.",
				e.getCause());
		e.getChannel().close();
	}
}
