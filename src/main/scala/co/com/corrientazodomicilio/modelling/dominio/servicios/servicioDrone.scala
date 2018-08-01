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
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $s")
    }
  }
}

class sinAlcanceExcepcion extends Exception("SIN ALCANCE")

// Algebra del API
sealed trait AlgebraServicioDrone {
  def moverAdelante(d:Drone):Drone
  def moverIzquierda(d:Drone):Drone
  def moverDerecha(d:Drone):Drone
}

//Interpretacion del algebra
sealed trait servicioDroneInterprete extends AlgebraServicioDrone{

  def moverAdelante(drone: Drone):Drone = {
    drone.coordenada.orientacion match {
      case NORTE() => Drone(validarCoordenada(Coordenada(drone.coordenada.x,drone.coordenada.y + 1, drone.coordenada.orientacion)))
      case ESTE() => Drone(validarCoordenada(Coordenada(drone.coordenada.x + 1, drone.coordenada.y, drone.coordenada.orientacion)))
      case SUR() => Drone(validarCoordenada(Coordenada(drone.coordenada.x, drone.coordenada.y - 1, drone.coordenada.orientacion)))
      case OESTE() => Drone(validarCoordenada(Coordenada(drone.coordenada.x - 1, drone.coordenada.y, drone.coordenada.orientacion)))
    }
  }

  def moverIzquierda(drone: Drone):Drone = {
    drone.coordenada.orientacion match {
      case NORTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,OESTE()))
      case ESTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,NORTE()))
      case SUR() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,ESTE()))
      case OESTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,SUR()))
    }
  }

  def moverDerecha(drone: Drone):Drone = {
    drone.coordenada.orientacion match {
      case NORTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,ESTE()))
      case ESTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,SUR()))
      case SUR() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,OESTE()))
      case OESTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,NORTE()))
    }
  }

  private def validarCoordenada(coordenada: Coordenada): Coordenada = {
    if (coordenada.y >= 10 || coordenada.x >= 10) {
      throw new sinAlcanceExcepcion
    }
    coordenada
  }

  def realizarRuta(drone: Drone, entrega: List[Instruccion]):Drone = {
    val listDrone: List[Drone] = List(drone)
    val ruta: List[Drone] = entrega.foldLeft(listDrone){ (resultado, item) =>
      resultado :+ Instruccion.newInstruccion(resultado.last, item)
    }
    ruta.last
  }

  def realizarEntregas(drone: Drone, rutas: List[List[Instruccion]]): List[Drone] = {
    val listEntregas: List[Drone] = List(drone)
    rutas.foldLeft(listEntregas){ (resultado, item) =>
      resultado :+ realizarRuta(resultado.last, item)
    }
  }
}

// Trait Object
object servicioDroneInterprete extends servicioDroneInterprete