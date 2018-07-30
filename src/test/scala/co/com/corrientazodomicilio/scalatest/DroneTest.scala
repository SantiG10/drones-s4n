package co.com.corrientazodomicilio.scalatest

import org.scalatest.FunSuite
import co.com.corrientazodomicilio.modelling.dominio.entidades._
import co.com.corrientazodomicilio.modelling.dominio.servicios.servicioDroneInterprete
import com.sun.java.swing.plaf.gtk.GTKConstants.Orientation

import scala.concurrent.Await
import scala.concurrent.duration._


class DroneTest extends FunSuite{

  test("probando") {
    val drone = Drone(Coordenada(0,0, NORTE()))

    println(s"Antes: $drone")
    val nuevoDrone = servicioDroneInterprete.moverAdelante(drone)

    println(s"Adelante: $nuevoDrone")

    assert(nuevoDrone == Drone(Coordenada(0, 1, NORTE())))

    //-- Adelante
    val nuevoDrone2 = servicioDroneInterprete.moverAdelante(nuevoDrone)
    println(s"Adelante: $nuevoDrone2")

    assert(nuevoDrone2 == Drone(Coordenada(0, 2, NORTE())))

    //-- Izquierda
    val nuevoDrone3 = servicioDroneInterprete.moverIzquierda(nuevoDrone2)
    println(s"Izquierda: $nuevoDrone3")

    assert(nuevoDrone3 == Drone(Coordenada(0, 2, OESTE())))

    //-- Adelante
    val nuevoDrone4 = servicioDroneInterprete.moverAdelante(nuevoDrone3)
    println(s"Adelante: $nuevoDrone4")

    assert(nuevoDrone4 == Drone(Coordenada(-1, 2, OESTE())))

    //-- Derecha
    val nuevoDrone5 = servicioDroneInterprete.moverDerecha(nuevoDrone4)
    println(s"Derecha: $nuevoDrone5")

    assert(nuevoDrone5 == Drone(Coordenada(-1, 2, NORTE())))

    val a = List("AADAAIAA", "ADDIAAA", "AADIAA")

    val ruta = Ruta(drone, a)

    val b = ruta.entregas.map { x => x.split("").toList }

    println("Lista antes:" + a)
    println("Lista despues:" + b)

    b.foreach { x =>
      x.map(y =>
        println(y)
      )
    }
  }
}
