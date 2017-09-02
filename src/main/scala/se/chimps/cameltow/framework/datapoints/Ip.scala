package se.chimps.cameltow.framework.datapoints

import se.chimps.cameltow.framework.feaures.RequestDataToHeaders.DataPoint

object Ip {
  // be ware that localhost might be ipv6...
  def apply(headerName:String):DataPoint = (exchange) => {
    val ip = if (exchange.getRequestHeaders.contains("X-Forwarded-For")) {
      exchange.getRequestHeaders.get("X-Forwarded-For", 0)
    } else {
      exchange.getSourceAddress.getAddress.getHostAddress
    }

    (headerName, ip)
  }
}
