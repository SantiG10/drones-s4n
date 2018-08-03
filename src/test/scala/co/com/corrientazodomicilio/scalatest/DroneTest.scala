package co.com.corrientazodomicilio.scalatest

import java.util.concurrent.Executors

import org.scalatest.FunSuite
import co.com.corrientazodomicilio.modelling.dominio.entidades._
import co.com.corrientazodomicilio.modelling.dominio.main.DronSystem
import co.com.corrientazodomicilio.modelling.dominio.main.DronSystem.drones
import co.com.corrientazodomicilio.modelling.dominio.servicios._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.Try

class DroneTest extends FunSuite{

  test("Creación de drone"){
    val drone = Drone("01", Coordenada())
    assert(drone == Drone("01", Coordenada(0,0,NORTE()), 10))
  }

  test("Mover drone hacia adelante"){
    val drone = Drone("01", Coordenada())
    val nuevoDrone = ServicioDron.moverAdelante(drone)
    assert(nuevoDrone == Drone("01", Coordenada(0, 1, NORTE()), 10))
  }

  test("Girar drone hacia la izquierda"){
    val drone = Drone("01", Coordenada())
    val nuevoDrone = ServicioDron.moverIzquierda(drone)
    assert(nuevoDrone == Drone("01", Coordenada(0, 0, OESTE()), 10))
  }

  test("Girar drone hacia la derecha"){
    val drone = Drone("01", Coordenada())
    val nuevoDrone = ServicioDron.moverDerecha(drone)
    assert(nuevoDrone == Drone("01", Coordenada(0, 0, ESTE()), 10))
  }

  test("Leer archivo de text que convierte en una lista de instruciones"){
    val filename = "files/test/in.txt"
    val list2 = ServicioArchivo.leerArchivo(filename)
    //assert(list2 == List(List(A(), A(), A(), D(), D(), A(), A(), I(), A())))
    assert(list2.isSuccess)
  }

  test("Leer archivo de text con errores"){
    val filename = "files/test/in1.txt"
    val list2 = ServicioArchivo.leerArchivo(filename)
    //assert(list2 == List(List(A(), A(), A(), D(), D(), A(), A(), I(), A())))
    assert(list2.isFailure)
  }


  test("Test General") {

    // Crea el archivo
    //servicioArchivoInterprete.crearArchivo(resEntrega)

    val listViajes: List[Try[List[List[Instruccion]]]] = DronSystem.drones.map(x =>
      ServicioArchivo.leerArchivo("files/in/in" + x.id + ".txt")
    )

    val tuplaDronesViajes = drones.zip(listViajes)

    implicit val exDrones = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))

    val resulTuplaViajes: List[Future[List[Drone]]] = tuplaDronesViajes.map(x =>
      Future{
        ServicioDron.realizarRuta(x._1, x._2)
      }
    )

    val resulSequence: Future[List[List[Drone]]] = Future.sequence {
      resulTuplaViajes
    }

    // ------ Generar Reportes ------

    val reportes: List[List[Drone]] = Await.result(resulSequence, 10 seconds)

    reportes.foreach(x =>
      ServicioArchivo.crearArchivo(x)
    )

    println("Reportes: " + reportes)

  }
}
