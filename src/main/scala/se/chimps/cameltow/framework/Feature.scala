package se.chimps.cameltow.framework

import io.undertow.server.HttpHandler

trait Feature {
  private[cameltow] def httpHandler:HttpHandler
}

/*
  Alot of these could be both features and handlers...

  IPAddressAccessControlHandler
  MetricsHandler
  OriginHandler
  RequestLimitHandler
  ResponseRateLimitHandler
  GracefulShutdownHandler (should be default)
  ExceptionHandler
  ChannelUpgradeHandler (or what ever enables websocket and http2)
  HttpContinueAcceptingHandler (for 100 Continue calls, perhaps use HttpContinueReadHandler instead)
  ProxyPeerAddressHandler
  EncodingHandler & RequestEncodingHandler + simple en/decryption Provider (could also be a handler)
  EagerFormParsingHandler (should be default)
 */