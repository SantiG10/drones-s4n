package co.com.corrientazodomicilio.scalatest

import org.scalatest.FunSuite
import co.com.corrientazodomicilio.modelling.dominio.entidades._
import co.com.corrientazodomicilio.modelling.dominio.servicios._
import com.sun.java.swing.plaf.gtk.GTKConstants.Orientation

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.Source


class DroneTest extends FunSuite{

  test("Test") {

    // foldleft agregando
    val drone = Drone(Coordenada())

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

    /*val a = List("AADAAIAA", "ADDIAAA", "AADIAA")

    val ruta = Ruta(drone, a)

    val b = ruta.entregas.map { x => x.split("").toList }

    println("Lista antes:" + a)
    println("Lista despues:" + b)*/

    /*b.foreach { x =>
      x.map(y =>
        println(y)
      )
    }*/

    val filename = "files/in.txt"
    val list: List[String] = Source.fromFile(filename).getLines.toList

    val list2: List[List[Instruccion]] = list.map { x => x.split("").toList.map(y => Orientacion.newOrientacion(y)) }

    println("File lista: " + list2)

    // Drone(id:Int, entregas:List[String], capacidad:Int)

    val listEntregas = servicioDroneInterprete.realizarEntregas(drone, list2)

    // Lista de coordenadas de entrega
    println(s"\nLista de entregas: ${listEntregas.tail}")

    // Crea el archivo
    servicioArchivoInterprete.crearArchivo(listEntregas)

  }
}
