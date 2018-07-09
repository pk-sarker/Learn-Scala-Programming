package ch11

import akka.actor._
import com.typesafe.config.ConfigFactory
import Manager.ShoppingList
import Mixer.Groceries

abstract class Store {
  lazy val store = ActorSystem("Store", ConfigFactory.load("grocery.conf"))

  lazy val seller: ActorRef = store.actorOf(Props(new Actor {
    override def receive: Receive = {
      case s: ShoppingList =>
        println("SHOPPING:" + sender().path)
        ShoppingList.unapply(s).map(Groceries.tupled).foreach(sender() ! _)
    }
  }), "Seller")

}
object Store extends Store with App
