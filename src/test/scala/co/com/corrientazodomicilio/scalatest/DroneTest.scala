package co.com.corrientazodomicilio.scalatest

import org.scalatest.FunSuite
import co.com.corrientazodomicilio.modelling.dominio.entidades.{Coordenada, Drone}
import co.com.corrientazodomicilio.modelling.dominio.servicios.servicioDroneInterprete

import scala.concurrent.Await
import scala.concurrent.duration._


class DroneTest extends FunSuite{

  test("probando") {
    val drone = Drone(Coordenada(0, 0, "NORTE"))

    println(s"Antes: $drone")
    val nuevo = servicioDroneInterprete.moverAdelante(drone)
    val nuevoDrone = Await.result(nuevo, 10 seconds)
    println(s"Adelante: $nuevoDrone")

    assert(nuevoDrone == Drone(Coordenada(0, 1, "NORTE")))

    //-- Adelante
    val res = servicioDroneInterprete.moverAdelante(nuevoDrone)
    val nuevoDrone2 = Await.result(res, 10 seconds)
    println(s"Adelante: $nuevoDrone2")

    assert(nuevoDrone2 == Drone(Coordenada(0, 2, "NORTE")))

    //-- Izquierda
    val res2 = servicioDroneInterprete.moverIzquierda(nuevoDrone2)
    val nuevoDrone3 = Await.result(res2, 10 seconds)
    println(s"Izquierda: $nuevoDrone3")

    assert(nuevoDrone3 == Drone(Coordenada(0, 2, "OESTE")))

    //-- Adelante
    val res3 = servicioDroneInterprete.moverAdelante(nuevoDrone3)
    val nuevoDrone4 = Await.result(res3, 10 seconds)
    println(s"Adelante: $nuevoDrone4")

    assert(nuevoDrone4 == Drone(Coordenada(-1, 2, "OESTE")))

    //-- Derecha
    val res4 = servicioDroneInterprete.moverDerecha(nuevoDrone4)
    val nuevoDrone5 = Await.result(res4, 10 seconds)
    println(s"Derecha: $nuevoDrone5")

    assert(nuevoDrone5 == Drone(Coordenada(-1,2,"NORTE")))
  }

}
