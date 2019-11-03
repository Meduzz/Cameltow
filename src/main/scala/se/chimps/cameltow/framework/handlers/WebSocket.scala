package se.chimps.cameltow.framework.handlers

import java.nio.ByteBuffer

import io.undertow.server.HttpHandler
import io.undertow.websockets.core._
import io.undertow.websockets.spi.WebSocketHttpExchange
import io.undertow.websockets.{WebSocketConnectionCallback, WebSocketProtocolHandshakeHandler}
import se.chimps.cameltow.framework.Handler

object WebSocket {
  def apply(delegate:WebSocketListenerDelegateFactory):WebSocket = new WebSocket(delegate)
}

class WebSocket(val factor:WebSocketListenerDelegateFactory) extends Handler {
  override def httpHandler: HttpHandler = new WebSocketProtocolHandshakeHandler(new WebSocketCallback {
    override def onConnect(exchange: WebSocketHttpExchange, channel: WebSocketChannel):Unit = {
      val req = new WebSocketRequest(exchange)
      val delegate = factor.delegate(channel, req)

      channel.getReceiveSetter.set(WebSocketListener(delegate))
      channel.resumeReceives()
    }
  })
}

trait WebSocketCallback extends WebSocketConnectionCallback {
}

class WebSocketRequest(exchange:WebSocketHttpExchange) {

  def query(name:String):Option[String] = {
    exchange.getQueryString
      .split("&")
      .map(kv => {
        val pair = kv.split("=")

        if (pair.length == 2) {
          (pair(0), pair(1))
        } else {
          (pair(0), "")
        }
      }).toMap
      .get(name)
  }

  def header(name:String):Option[String] = Option(exchange.getRequestHeader(name))

}

trait WebSocketListenerDelegateFactory {
  def delegate(channel:WebSocketChannel, req:WebSocketRequest):WebSocketListenerDelegate
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

abstract class WebSocketListenerDelegate(channel:WebSocketChannel, request:WebSocketRequest) {
  def onError(channel: WebSocketChannel, error: Throwable): Unit
  def onFullTextMessage(channel: WebSocketChannel, message: BufferedTextMessage): Unit
  def onFullBinaryMessage(channel: WebSocketChannel, message: BufferedBinaryMessage): Unit
  def onFullCloseMessage(channel: WebSocketChannel, message: BufferedBinaryMessage): Unit

  def text(text:String, channel:WebSocketChannel):Unit = WebSockets.sendText(text, channel, null)
  def binary(bytes:Array[Byte], channel:WebSocketChannel):Unit = WebSockets.sendBinary(ByteBuffer.wrap(bytes), channel, null)
}