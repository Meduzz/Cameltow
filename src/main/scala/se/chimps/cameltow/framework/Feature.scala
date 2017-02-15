package se.chimps.cameltow.framework

import io.undertow.server.HttpHandler

trait Feature {
  private[cameltow] def httpHandler:HttpHandler
  private[cameltow] def setNext(httpHandler: HttpHandler)
}

/*
  # Handler
  + OriginHandler
  + RequestLimitHandler
  + ResponseRateLimitHandler
  HttpContinueAcceptingHandler (for 100 Continue calls, takes a predicate...)
  MetricsHandler (Metrics.handler & Metrics.data)
  IPAddressAccessControlHandler (Whitelist(delegate) & Blacklist(delegate))
  ChannelUpgradeHandler (or what ever enables websocket and http2) (WebSocketText() & WebSocketBinary())

  # Features
  + ExceptionHandler
  + EagerFormParsingHandler (should be default)
  + GracefulShutdownHandler (should be default)
  + EncodingHandler (for gzip and in the future, encryption) (should be default)
  ProxyPeerAddressHandler

 */