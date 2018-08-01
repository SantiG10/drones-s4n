package co.com.corrientazodomicilio.scalatest

import org.scalatest.FunSuite
import co.com.corrientazodomicilio.modelling.dominio.entidades._
import co.com.corrientazodomicilio.modelling.dominio.servicios._
import com.sun.java.swing.plaf.gtk.GTKConstants.Orientation

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.io.Source


class DroneTest extends FunSuite{

  test("Test") {

    // foldleft agregando
    val drone = Drone("01", Coordenada(), 10)

    println(s"Antes: $drone")
    val nuevoDrone = servicioDroneInterprete.moverAdelante(drone)

    println(s"Adelante: $nuevoDrone")

    assert(nuevoDrone == Drone("01", Coordenada(0, 1, NORTE()), 10))

    //-- Adelante
    val nuevoDrone2 = servicioDroneInterprete.moverAdelante(nuevoDrone)
    println(s"Adelante: $nuevoDrone2")

    assert(nuevoDrone2 == Drone("01", Coordenada(0, 2, NORTE()), 10))

    //-- Izquierda
    val nuevoDrone3 = servicioDroneInterprete.moverIzquierda(nuevoDrone2)
    println(s"Izquierda: $nuevoDrone3")

    assert(nuevoDrone3 == Drone("01", Coordenada(0, 2, OESTE()), 10))

    //-- Adelante
    val nuevoDrone4 = servicioDroneInterprete.moverAdelante(nuevoDrone3)
    println(s"Adelante: $nuevoDrone4")

    assert(nuevoDrone4 == Drone("01", Coordenada(-1, 2, OESTE()), 10))

    //-- Derecha
    val nuevoDrone5 = servicioDroneInterprete.moverDerecha(nuevoDrone4)
    println(s"Derecha: $nuevoDrone5")

    assert(nuevoDrone5 == Drone("01", Coordenada(-1, 2, NORTE()), 10))

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

    val filename = "files/in01.txt"
    val list: List[String] = Source.fromFile(filename).getLines.toList

    val list2 = servicioArchivoInterprete.leerArchivo(filename)

    //val list2: List[List[Instruccion]] = list.map { x => x.split("").toList.map(y => Orientacion.newOrientacion(y)) }

    println("\nFile lista: " + list2)

    if (list2.head.isEmpty){
      println("\nError en el archivo")
    }
    // Drone(id:Int, entregas:List[String], capacidad:Int)

    val listEntregas: Future[List[Drone]] = servicioDroneInterprete.realizarRuta(drone, list2)

    val resEntrega: List[Drone] = Await.result(listEntregas, 10 seconds)

    // Lista de coordenadas de entrega
    println(s"\nLista de entregas: ${resEntrega.tail}")

    val rutas = list2
    val ids = 1 to 10

    val rutasIds = rutas.zip(ids)

    println(s"\nRutas Ids: $rutasIds")

    // Crea el archivo
    //servicioArchivoInterprete.crearArchivo(resEntrega)

  }
}
