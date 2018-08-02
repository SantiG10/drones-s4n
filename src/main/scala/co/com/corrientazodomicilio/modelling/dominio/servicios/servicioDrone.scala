package co.com.corrientazodomicilio.modelling.dominio.servicios

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}
import co.com.corrientazodomicilio.modelling.dominio.entidades._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

object Instruccion {
  def newInstruccion(drone: Drone, c:Instruccion):Drone = {
    c match {
      case A() => servicioDroneInterprete.moverAdelante(drone)
      case D() => servicioDroneInterprete.moverDerecha(drone)
      case I() => servicioDroneInterprete.moverIzquierda(drone)
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
  }
}

object Orientacion {
  def newOrientacion(s:String):Instruccion = {
    s match {
      case "A" => A()
      case "D" => D()
      case "I" => I()
      case _ => throw new Exception(s"Caracter invalido para la creación de la ruta: $s")
    }
  }
}

class sinAlcanceExcepcion extends Exception("FUERA DE ALCANCE")

// Algebra del Drone
sealed trait AlgebraServicioDrone {
  def moverAdelante(d:Drone):Drone
  def moverIzquierda(d:Drone):Drone
  def moverDerecha(d:Drone):Drone
}

//Interpretacion del algebra del drone
sealed trait servicioDroneInterprete extends AlgebraServicioDrone{

  type Entrega = List[Instruccion]
  type Ruta = List[Entrega]

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))

  def moverAdelante(drone: Drone):Drone = {
    drone.coordenada.orientacion match {
      case NORTE() =>
        if (validarCoordenada(Coordenada(drone.coordenada.x,drone.coordenada.y + 1, drone.coordenada.orientacion)).isSuccess){
          Drone(drone.id, Coordenada(drone.coordenada.x,drone.coordenada.y + 1, drone.coordenada.orientacion), drone.capacidad)
        } else {
          Drone(drone.id, Coordenada(drone.coordenada.x,drone.coordenada.y + 1, drone.coordenada.orientacion), drone.capacidad)
        }
      case ESTE() =>
        if (validarCoordenada(Coordenada(drone.coordenada.x + 1, drone.coordenada.y, drone.coordenada.orientacion)).isSuccess){
          Drone(drone.id, Coordenada(drone.coordenada.x + 1, drone.coordenada.y, drone.coordenada.orientacion), drone.capacidad)
        } else {
          Drone(drone.id, Coordenada(drone.coordenada.x,drone.coordenada.y + 1, drone.coordenada.orientacion), drone.capacidad)
        }
      case SUR() =>
        if (validarCoordenada(Coordenada(drone.coordenada.x, drone.coordenada.y - 1, drone.coordenada.orientacion)).isSuccess){
          Drone(drone.id, Coordenada(drone.coordenada.x, drone.coordenada.y - 1, drone.coordenada.orientacion), drone.capacidad)
        } else {
          Drone(drone.id, Coordenada(drone.coordenada.x,drone.coordenada.y + 1, drone.coordenada.orientacion), drone.capacidad)
        }
      case OESTE() =>
        if (validarCoordenada(Coordenada(drone.coordenada.x - 1, drone.coordenada.y, drone.coordenada.orientacion)).isSuccess){
          Drone(drone.id, Coordenada(drone.coordenada.x - 1, drone.coordenada.y, drone.coordenada.orientacion), drone.capacidad)
        } else {
          Drone(drone.id, Coordenada(drone.coordenada.x,drone.coordenada.y + 1, drone.coordenada.orientacion), drone.capacidad)
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

  private def validarCoordenada(coordenada: Coordenada): Try[Coordenada] = {
    if (coordenada.y.abs > 10 || coordenada.x.abs >= 10) {
      Try(throw new sinAlcanceExcepcion)
    }
    Try(coordenada)
  }

  private def volverCasa(drone: Drone): Drone = {
    Drone(drone.id,Coordenada())
  }

  def realizarEntrega(drone: Drone, entrega: Entrega):Drone = {
    val listDrone: List[Drone] = List(drone)
    val rutaEntrega: List[Drone] = entrega.foldLeft(listDrone){ (resultado, item) =>
      resultado :+ Instruccion.newInstruccion(resultado.last, item)
    }
    if (rutaEntrega.last.coordenada.x.abs > 10 || rutaEntrega.last.coordenada.y.abs > 10){
      volverCasa(drone)
    } else {
      val resultEntrega = rutaEntrega.last.copy(capacidad = rutaEntrega.last.capacidad - 1)
      resultEntrega
    }
  }

  def realizarRuta(drone: Drone, rutas: Ruta): Future[List[Drone]] = {
    Future{
      val threadName1 = Thread.currentThread().getName
      println(s"Hilo Drone: $threadName1")
      val listEntregas: List[Drone] = List(drone)
      val resultRutas = rutas.foldLeft(listEntregas){ (resultado, item) =>
        resultado :+ realizarEntrega(resultado.last, item)
      }
      servicioArchivoInterprete.crearArchivo(resultRutas)
      resultRutas
    }
  }
}

// Trait Object Drone
object servicioDroneInterprete extends servicioDroneInterprete