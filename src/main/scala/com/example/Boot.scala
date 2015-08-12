package com.example

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import spray.can.Http

import scala.concurrent.duration._
import scala.util.Try

object Boot extends App {

  val config = ConfigFactory.load()

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val service = system.actorOf(Props[MyServiceActor], "demo-service")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "localhost", port = Try(config.getInt("service.port")).getOrElse(8080))
}

//object Main extends App with SimpleRoutingApp {
//  implicit val system = ActorSystem("my-system")
//  val ls = (1 to 10).toList.map(_*3).filter(_>10)
//
//  startServer(interface = "localhost", port = 8080) {
//    path("hello") {
//      get {
//        complete(ls.toString)
//      }
//    }
//  }
//}