package se.chimps.cameltow.framework.handlers

import java.nio.ByteBuffer

import io.undertow.server.HttpHandler
import io.undertow.websockets.core._
import io.undertow.websockets.spi.WebSocketHttpExchange
import io.undertow.websockets.{WebSocketConnectionCallback, WebSocketProtocolHandshakeHandler}
import se.chimps.cameltow.framework.Handler

object WebSocket {
  def apply(delegate:WebSocketListenerDelegate):WebSocket = new WebSocket(delegate)
}

class WebSocket(val delegate:WebSocketListenerDelegate) extends Handler {
  override def httpHandler: HttpHandler = new WebSocketProtocolHandshakeHandler(new WebSocketCallback {
    override def onConnect(exchange: WebSocketHttpExchange, channel: WebSocketChannel) = {
      channel.getReceiveSetter.set(WebSocketListener(delegate))
      channel.resumeReceives()
    }
  })
}

trait WebSocketCallback extends WebSocketConnectionCallback {
}

object WebSocketListener {
  def apply(delegate:WebSocketListenerDelegate):WebSocketListener = new WebSocketListener(delegate)
}

class WebSocketListener(delegate:WebSocketListenerDelegate) extends AbstractReceiveListener {
  override def onError(channel: WebSocketChannel, error: Throwable): Unit = {
    delegate.onError(channel, error)
  }

  override def onFullTextMessage(channel: WebSocketChannel, message: BufferedTextMessage): Unit = {
    delegate.onFullTextMessage(channel, message)
  }

  override def onFullBinaryMessage(channel: WebSocketChannel, message: BufferedBinaryMessage): Unit = {
    delegate.onFullBinaryMessage(channel, message)
  }

  override def onFullCloseMessage(channel: WebSocketChannel, message: BufferedBinaryMessage): Unit = {
    delegate.onFullCloseMessage(channel, message)
  }
}

trait WebSocketListenerDelegate {
  def onError(channel: WebSocketChannel, error: Throwable): Unit
  def onFullTextMessage(channel: WebSocketChannel, message: BufferedTextMessage): Unit
  def onFullBinaryMessage(channel: WebSocketChannel, message: BufferedBinaryMessage): Unit
  def onFullCloseMessage(channel: WebSocketChannel, message: BufferedBinaryMessage): Unit

  def text(text:String, channel:WebSocketChannel):Unit = WebSockets.sendText(text, channel, null)
  def binary(bytes:Array[Byte], channel:WebSocketChannel):Unit = WebSockets.sendBinary(ByteBuffer.wrap(bytes), channel, null)
}