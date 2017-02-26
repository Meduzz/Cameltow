## Handlers

* ProxyPeerAddressHandler (read x-forward etc and put in their expected headers)
* CacheHandler could be nice with a cacheHandler, if it can be done in a neat api. Or wrap the google cache? Or both?

## Features

* ChannelUpgradeHandler (or what ever enables websocket and http2) (WebSocketText() & WebSocketBinary())
* SessionAttachmentHandler perhaps with a delegate, based on undertow interfaces.
