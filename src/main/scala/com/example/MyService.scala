package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute3)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService with Common {

  val jsonVal = "{\"test\":\"testval\",\"test2\":\"testval\"}"
  val jsonVal2 = "{\"lang1\":\"Scala\", \"lang2\":\"Haskell\", \"lang3\":\"Swift\"}"

  val myRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    }

  val myRoute2 =
    parameters('color, 'backgroundColor) { (color, backgroundColor) =>

    path("hell" / IntNumber / IntNumber) {
      (num, num2) =>

        get {
          respondWithMediaType(`application/json`) {
            // XML is marshalled to `text/xml` by default, so we simply override here
            complete {
              "{\"val1\":\"" + addDiv(num) + "\", \"color\":\"" + color + "\" }"
              //            jsonVal
            }
          }
        } ~
        post {
          respondWithMediaType(`application/json`) {
            complete {
              jsonVal2
            }
          }
        }
      }
    }

  val myRoute3 =
    parameters('num1 ? 0, 'num2 ? 0) { (num1, num2) =>
      path("calculations" / Segment ) {
        calType =>
        get {
          respondWithMediaType(`application/json`){
            complete{
              calType match {
                case "add" => "{\"add\":\"" + add(num1, num2) + "\"}"
              }
            }
          }
        }
      }
    }


}

trait Common {
  lazy val square: Int => Int = n => n*n
  lazy val div2: Int => Int = n => n/2
  lazy val add: (Int, Int) => Int = (n1, n2) => n1+n2
  def addDiv = div2 compose square

  //JSON形式で関数を返す
  def listConvertToJson[A](ls: Seq[A]): String = {
    "test"
  }
}

}