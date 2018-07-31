package co.com.corrientazodomicilio.modelling.dominio.servicios

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}
import co.com.corrientazodomicilio.modelling.dominio.entidades._

import scala.concurrent.ExecutionContext.Implicits.global

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
      case NORTE() => Drone(Coordenada(drone.coordenada.x,drone.coordenada.y + 1, drone.coordenada.orientacion))
      case ESTE() => Drone(Coordenada(drone.coordenada.x + 1, drone.coordenada.y, drone.coordenada.orientacion))
      case SUR() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y - 1, drone.coordenada.orientacion))
      case OESTE() => Drone(Coordenada(drone.coordenada.x - 1, drone.coordenada.y, drone.coordenada.orientacion))
      //case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
  }

  def moverIzquierda(drone: Drone):Drone = {
    drone.coordenada.orientacion match {
      case NORTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,OESTE()))
      case ESTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,NORTE()))
      case SUR() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,ESTE()))
      case OESTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,SUR()))
      //case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
  }

  def moverDerecha(drone: Drone):Drone = {
    drone.coordenada.orientacion match {
      case NORTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,ESTE()))
      case ESTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,SUR()))
      case SUR() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,OESTE()))
      case OESTE() => Drone(Coordenada(drone.coordenada.x, drone.coordenada.y ,NORTE()))
      //case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
  }

  def realizarEntrega(drone: Drone, ruta: List[Instruccion]):Drone = {
    val listDrone: List[Drone] = List(drone)
    val entrega: List[Drone] = ruta.foldLeft(listDrone){ (resultado, item) =>
      resultado :+ Instruccion.newInstruccion(resultado.last, item)
    }
    entrega.last
  }
}

// Trait Object
object servicioDroneInterprete extends servicioDroneInterprete