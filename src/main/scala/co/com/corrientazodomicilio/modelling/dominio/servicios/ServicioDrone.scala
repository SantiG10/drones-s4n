package co.com.corrientazodomicilio.modelling.dominio.servicios

import co.com.corrientazodomicilio.modelling.dominio.entidades._

import scala.util.{Failure, Success, Try}

class sinAlcanceExcepcion extends Exception("FUERA DE ALCANCE")

// Algebra del Drone
sealed trait AlgebraServicioDron {
  def moverAdelante(d:Drone):Drone
  def moverIzquierda(d:Drone):Drone
  def moverDerecha(d:Drone):Drone
  def realizarRuta(drone: Drone, ruta: Try[List[List[Instruccion]]]): List[Drone]
  protected def realizarEntrega(drone: Drone, entrega: List[Instruccion]):Drone
  protected def validarCoordenada(coordenada: Coordenada): Try[Coordenada]
  protected def volverCasa(drone: Drone): Drone
}

//Interpretacion del algebra del drone
sealed trait ServicioDron extends AlgebraServicioDron{

  def moverAdelante(drone: Drone):Drone = {
    drone.coordenada.orientacion match {
      case NORTE() =>
        if (validarCoordenada(Coordenada(drone.coordenada.x,drone.coordenada.y + 1, drone.coordenada.orientacion)).isSuccess){
          Drone(drone.id, Coordenada(drone.coordenada.x,drone.coordenada.y + 1, drone.coordenada.orientacion), drone.capacidad)
        } else {
          Drone("ErrorMovimiento", Coordenada(), drone.capacidad)
        }
      case ESTE() =>
        if (validarCoordenada(Coordenada(drone.coordenada.x + 1, drone.coordenada.y, drone.coordenada.orientacion)).isSuccess){
          Drone(drone.id, Coordenada(drone.coordenada.x + 1, drone.coordenada.y, drone.coordenada.orientacion), drone.capacidad)
        } else {
          Drone("ErrorMovimiento", Coordenada(), drone.capacidad)
        }
      case SUR() =>
        if (validarCoordenada(Coordenada(drone.coordenada.x, drone.coordenada.y - 1, drone.coordenada.orientacion)).isSuccess){
          Drone(drone.id, Coordenada(drone.coordenada.x, drone.coordenada.y - 1, drone.coordenada.orientacion), drone.capacidad)
        } else {
          Drone("ErrorMovimiento", Coordenada(), drone.capacidad)
        }
      case OESTE() =>
        if (validarCoordenada(Coordenada(drone.coordenada.x - 1, drone.coordenada.y, drone.coordenada.orientacion)).isSuccess){
          Drone(drone.id, Coordenada(drone.coordenada.x - 1, drone.coordenada.y, drone.coordenada.orientacion), drone.capacidad)
        } else {
          Drone("ErrorMovimiento", Coordenada(), drone.capacidad)
        }
    }
  }

  def moverIzquierda(drone: Drone):Drone = {
    drone.coordenada.orientacion match {
      case NORTE() => Drone(drone.id, Coordenada(drone.coordenada.x, drone.coordenada.y ,OESTE()), drone.capacidad)
      case ESTE() => Drone(drone.id, Coordenada(drone.coordenada.x, drone.coordenada.y ,NORTE()), drone.capacidad)
      case SUR() => Drone(drone.id, Coordenada(drone.coordenada.x, drone.coordenada.y ,ESTE()), drone.capacidad)
      case OESTE() => Drone(drone.id, Coordenada(drone.coordenada.x, drone.coordenada.y ,SUR()), drone.capacidad)
    }
  }

  def moverDerecha(drone: Drone):Drone = {
    drone.coordenada.orientacion match {
      case NORTE() => Drone(drone.id, Coordenada(drone.coordenada.x, drone.coordenada.y ,ESTE()), drone.capacidad)
      case ESTE() => Drone(drone.id, Coordenada(drone.coordenada.x, drone.coordenada.y ,SUR()), drone.capacidad)
      case SUR() => Drone(drone.id, Coordenada(drone.coordenada.x, drone.coordenada.y ,OESTE()), drone.capacidad)
      case OESTE() => Drone(drone.id, Coordenada(drone.coordenada.x, drone.coordenada.y ,NORTE()), drone.capacidad)
    }
  }

   protected def validarCoordenada(coordenada: Coordenada): Try[Coordenada] = {
    if (coordenada.y.abs > 10 || coordenada.x.abs >= 10) {
      Try(throw new sinAlcanceExcepcion)
    }
    Try(coordenada)
  }

  protected def volverCasa(drone: Drone): Drone = {
    Drone(drone.id,Coordenada())
  }

  protected def realizarEntrega(drone: Drone, entrega: List[Instruccion]):Drone = {
    if (drone.capacidad == 0) {
      volverCasa(drone)
    } else {
      val listDrone: List[Drone] = List(drone)

      val rutaEntrega: List[Drone] =
        entrega
          .foldLeft(listDrone) {
            (resultado, item) =>
              resultado :+ Instruccion.newInstruccion(resultado.last, item)
          }
      /*
      if (rutaEntrega.last.coordenada.x.abs > 10 || rutaEntrega.last.coordenada.y.abs > 10) {
        //volverCasa(drone)
        Drone(drone.id,Coordenada(0, 0, NORTE()), drone.capacidad)
      } else {
        val resultEntrega = rutaEntrega.last.copy(capacidad = rutaEntrega.last.capacidad - 1)
        resultEntrega
      }*/
      val resultEntrega = rutaEntrega.last.copy(capacidad = rutaEntrega.last.capacidad - 1)
      resultEntrega
    }
  }

  def realizarRuta(drone: Drone, ruta: Try[List[List[Instruccion]]]): List[Drone] = {

    val threadName1 = Thread.currentThread().getName

    println(s"Hilo Drone: $threadName1")

    val listEntregas: List[Drone] = List(drone)

    ruta match {
      case Success(v) =>
        val resultRutas = v
          .foldLeft(listEntregas){
            (resultado, item) =>
              resultado :+ realizarEntrega(resultado.last, item)
          }
        resultRutas
      case Failure(e) =>
        List(Drone("ErrorArchivo", Coordenada()), Drone(drone.id, Coordenada()))
    }
  }
}

// Trait Object Drone
object ServicioDron extends ServicioDron