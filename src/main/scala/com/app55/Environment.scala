package com.app55

case class Environment(private val server: String, private val port: Int, private val ssl: Boolean, private val version: Int) {

  def baseUrl(): String =
    {
      return scheme() + "://" + host() + "/v" + version
    }

  private def scheme(): String =
    {
      if (ssl) "https" else "http"
    }

  private def host(): String =
    {
      if (port == 443 && ssl)
        server
      else if (port == 80 && !ssl)
        server
      else
        server + ":" + port
    }
}

object Environment {
  val Development = Environment("dev.app55.com", 80, false, 1)
  val Sandbox = Environment("sandbox.app55.com", 443, true, 1)
  val Production = Environment("api.app55.com", 443, true, 1)
}
