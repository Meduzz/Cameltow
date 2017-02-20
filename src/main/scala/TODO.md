## Handlers

* MetricsHandler incl some kind of delegate.
* ProxyPeerAddressHandler (read x-forward etc and put in their expected headers)

## Features

* HttpContinueAcceptingHandler (for 100 Continue calls, takes a predicate...)
* IPAddressAccessControlHandler (Whitelist(delegate) & Blacklist(delegate))
* ChannelUpgradeHandler (or what ever enables websocket and http2) (WebSocketText() & WebSocketBinary())

