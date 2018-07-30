package co.com.corrientazodomicilio.modelling.dominio.servicios

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}
import co.com.corrientazodomicilio.modelling.dominio.entidades._

import scala.concurrent.ExecutionContext.Implicits.global

// Algebra del API
sealed trait AlgebraServicioDrone {
  def moverAdelante(d:Drone):Drone
  def moverIzquierda(d:Drone):Drone
  def moverDerecha(d:Drone):Drone
}

//Interpretacion del algebra
sealed trait servicioDroneInterprete extends AlgebraServicioDrone{

  def moverAdelante(drone: Drone):Drone = {
    val direccion = drone.coordenada.orientacion
    var y = drone.coordenada.y
    var x = drone.coordenada.x

    direccion match {
      case NORTE() => y = drone.coordenada.y + 1
      case ESTE() => x = drone.coordenada.x + 1
      case SUR() => y = drone.coordenada.y - 1
      case OESTE() => x = drone.coordenada.x - 1
      //case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
    Drone(Coordenada(x , y, direccion))
  }

  def moverIzquierda(drone: Drone):Drone = {
    val direccion = drone.coordenada.orientacion
    val y = drone.coordenada.y
    val x = drone.coordenada.x
    val nuevaDireccion = direccion match {
      case NORTE() => OESTE()
      case ESTE() => NORTE()
      case SUR() => ESTE()
      case OESTE() => SUR()
      //case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
    Drone(Coordenada(x , y, nuevaDireccion))
  }

  def moverDerecha(drone: Drone):Drone = {
    val direccion = drone.coordenada.orientacion
    val y = drone.coordenada.y
    val x = drone.coordenada.x
    val nuevaDireccion = direccion match {
      case NORTE() => ESTE()
      case ESTE() => SUR()
      case SUR() => OESTE()
      case OESTE() => NORTE()
      //case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
    Drone(Coordenada(x , y, nuevaDireccion))
  }
}

// Trait Object
object servicioDroneInterprete extends servicioDroneInterprete

object Instruccion {
  def newInstruccion(drone: Drone, c:Char):Drone ={
    c match {
      case 'A' => servicioDroneInterprete.moverAdelante(drone)
      case 'D' => servicioDroneInterprete.moverDerecha(drone)
      case 'I' => servicioDroneInterprete.moverDerecha(drone)
      //case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
  }
}


// sustantivos posicion