package co.com.corrientazodomicilio.modelling.dominio

import co.com.corrientazodomicilio.modelling.dominio.entidades._
import co.com.corrientazodomicilio.modelling.dominio.servicios._

import scala.util.Try
import scala.concurrent.{ExecutionContext, Future}



object main extends App {

  DronSystem.cargarViajes()

  object DronSystem {
    val drones = List(Drone("01", Coordenada(), 10), Drone("02", Coordenada(), 10), Drone("03", Coordenada(), 10), Drone("04", Coordenada(), 10), Drone("05", Coordenada(), 10),
      Drone("06", Coordenada(), 10), Drone("07", Coordenada(), 10), Drone("08", Coordenada(), 10), Drone("09", Coordenada(), 10), Drone("10", Coordenada(), 10))

    val drones1 = List(Drone("01", Coordenada()), Drone("02", Coordenada()))
    def cargarViajes():Unit = {

      val listViajes: List[List[List[Instruccion]]] = DronSystem.drones.map(x =>
        servicioArchivoInterprete.leerArchivo("files/in/in" + x.id + ".txt")
      )

      val tuplaDronesViajes = drones.zip(listViajes)

      val resulTuplaViajes: List[Future[List[Drone]]] = tuplaDronesViajes.map(x =>
        servicioDroneInterprete.realizarRuta(x._1,x._2)
      )

      println(s"\n Lista de viajes: $listViajes")

      println(s"\n Resultado viajes: $resulTuplaViajes")

      /*
      val resSequence: Future[List[List[Drone]]] = Future.sequence {
        result
      }*/


      /*
      Thread.sleep(10000)
      result.foreach( x =>
        x.foreach( y =>
          servicioArchivoInterprete.crearArchivo(y)
        )
      )
      */
    }
  }
}
