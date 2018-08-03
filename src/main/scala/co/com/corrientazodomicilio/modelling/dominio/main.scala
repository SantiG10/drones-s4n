package co.com.corrientazodomicilio.modelling.dominio

import java.util.concurrent.Executors

import co.com.corrientazodomicilio.modelling.dominio.entidades._
import co.com.corrientazodomicilio.modelling.dominio.servicios._

import scala.util.Try
import scala.concurrent.{ExecutionContext, Future}

object main extends App {

  DronSystem.cargarViajes()

  object DronSystem {

    implicit val exDron = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))

    val drones = List(Drone("01", Coordenada()), Drone("02", Coordenada()), Drone("03", Coordenada()),
      Drone("04", Coordenada()), Drone("05", Coordenada()), Drone("06", Coordenada()),
      Drone("07", Coordenada()), Drone("08", Coordenada()), Drone("09", Coordenada()),
      Drone("10", Coordenada()), Drone("11", Coordenada()), Drone("12", Coordenada()),
      Drone("13", Coordenada()), Drone("14", Coordenada()), Drone("15", Coordenada()),
      Drone("16", Coordenada()), Drone("17", Coordenada()), Drone("18", Coordenada()),
      Drone("19", Coordenada()), Drone("20", Coordenada()))

    def cargarViajes():Unit = {
      val listViajes: List[Try[List[List[Instruccion]]]] = DronSystem.drones.map { x =>
        ServicioArchivo.leerArchivo("files/in/in" + x.id + ".txt")
      }
      val tuplaDronesViajes = drones.zip(listViajes)
      val resulTuplaViajes: List[Future[Unit]] = tuplaDronesViajes.map { x =>
        Future{
          ServicioDron.realizarRuta(x._1, x._2)
        }.map( x => ServicioArchivo.crearArchivo(x))
      }
      println(s"\n Lista de viajes: $listViajes")
      println(s"\n Resultado viajes: $resulTuplaViajes")
    }
  }
}
