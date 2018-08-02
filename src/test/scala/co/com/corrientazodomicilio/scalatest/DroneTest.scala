package co.com.corrientazodomicilio.scalatest

import java.util.concurrent.Executors

import org.scalatest.FunSuite
import co.com.corrientazodomicilio.modelling.dominio.entidades._
import co.com.corrientazodomicilio.modelling.dominio.main.DronSystem
import co.com.corrientazodomicilio.modelling.dominio.main.DronSystem.{drones, drones1}
import co.com.corrientazodomicilio.modelling.dominio.servicios._
import com.sun.java.swing.plaf.gtk.GTKConstants.Orientation

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.io.Source


class DroneTest extends FunSuite{

  test("Creación de drone"){
    val drone = Drone("01", Coordenada())
    assert(drone == Drone("01", Coordenada(0,0,NORTE()), 10))
  }

  test("Mover drone hacia adelante"){
    val drone = Drone("01", Coordenada())
    val nuevoDrone = servicioDroneInterprete.moverAdelante(drone)
    assert(nuevoDrone == Drone("01", Coordenada(0, 1, NORTE()), 10))
  }

  test("Girar drone hacia la izquierda"){
    val drone = Drone("01", Coordenada())
    val nuevoDrone = servicioDroneInterprete.moverIzquierda(drone)
    assert(nuevoDrone == Drone("01", Coordenada(0, 0, OESTE()), 10))
  }

  test("Girar drone hacia la derecha"){
    val drone = Drone("01", Coordenada())
    val nuevoDrone = servicioDroneInterprete.moverDerecha(drone)
    assert(nuevoDrone == Drone("01", Coordenada(0, 0, ESTE()), 10))
  }

  test("Leer archivo de text que convierte en una lista de instruciones"){
    val filename = "files/test/in.txt"
    val list2 = servicioArchivoInterprete.leerArchivo(filename)
    assert(list2 == List(List(A(), A(), A(), D(), D(), A(), A(), I(), A())))
  }

  test("Test General") {

    val drone = Drone("01", Coordenada())

    val filename = "files/in/in01.txt"
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

    val listViajes: List[List[List[Instruccion]]] = DronSystem.drones.map(x =>
      servicioArchivoInterprete.leerArchivo("files/in/in" + x.id + ".txt")
    )

    val tuplaDronesViajes = drones.zip(listViajes)

    val resulTuplaViajes: List[Future[List[Drone]]] = tuplaDronesViajes.map(x =>
      servicioDroneInterprete.realizarRuta(x._1,x._2)
    )

    implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))

    val resSequence: Future[List[List[Drone]]] = Future.sequence {
      resulTuplaViajes
    }

    val resEntrega2: List[List[Drone]] = Await.result(resSequence, 10 seconds)

    println("Prueba final: " + resEntrega2)

  }
}
