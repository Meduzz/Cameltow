package se.chimps.cameltow.framework.datapoints

import se.chimps.cameltow.framework.feaures.RequestDataToHeaders.DataPoint

object RequestId {
  def apply(headerName:String, idGenerator:() => String):DataPoint = (exchange) => {
    val nextId = idGenerator()
    (headerName, nextId)
  }
}
