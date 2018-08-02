package co.com.corrientazodomicilio.modelling.dominio

import co.com.corrientazodomicilio.modelling.dominio.entidades._
import co.com.corrientazodomicilio.modelling.dominio.servicios._

import scala.util.Try
import scala.concurrent.{ExecutionContext, Future}

object main extends App {

  DronSystem.cargarViajes()

  object DronSystem {
    val drones = List(Drone("01", Coordenada()), Drone("02", Coordenada()), Drone("03", Coordenada()), Drone("04", Coordenada()), Drone("05", Coordenada()),
      Drone("06", Coordenada()), Drone("07", Coordenada()), Drone("08", Coordenada()), Drone("09", Coordenada()), Drone("10", Coordenada()))

    def cargarViajes():Unit = {
      val listViajes: List[List[List[Instruccion]]] = DronSystem.drones.map { x =>
        servicioArchivoInterprete.leerArchivo("files/in/in" + x.id + ".txt")
      }
      val tuplaDronesViajes = drones.zip(listViajes)
      val resulTuplaViajes: List[Future[List[Drone]]] = tuplaDronesViajes.map { x =>
        servicioDroneInterprete.realizarRuta(x._1, x._2)
      }
      println(s"\n Lista de viajes: $listViajes")
      println(s"\n Resultado viajes: $resulTuplaViajes")
    }
  }
}
