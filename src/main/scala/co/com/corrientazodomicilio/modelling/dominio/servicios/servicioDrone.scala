package co.com.corrientazodomicilio.modelling.dominio.servicios

import scala.concurrent.Future
import co.com.corrientazodomicilio.modelling.dominio.entidades._
import scala.concurrent.ExecutionContext.Implicits.global

// Algebra del API
sealed trait AlgebraServicioDrone {
  def moverAdelante(d:Drone):Future[Drone]
  def moverIzquierda(d:Drone):Future[Drone]
  def moverDerecha(d:Drone): Future[Drone]
}

//Interpretacion del algebra
sealed trait servicioDroneInterprete extends AlgebraServicioDrone{
  def moverAdelante(drone: Drone): Future[Drone] = {
    val direccion = drone.coordenada.orientacion
    var y = drone.coordenada.y
    var x = drone.coordenada.x

    direccion match {
      case "NORTE" => y = drone.coordenada.y + 1
      case "ESTE" => x = drone.coordenada.x + 1
      case "SUR" => y = drone.coordenada.y -1
      case "OESTE" => x = drone.coordenada.x -1
      //case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
    Future(Drone(Coordenada(x , y, direccion)))
  }

  def moverIzquierda(drone: Drone): Future[Drone] = {
    val direccion = drone.coordenada.orientacion
    val y = drone.coordenada.y
    val x = drone.coordenada.x
    var nuevaDireccion = ""

    direccion match {
      case "NORTE" => nuevaDireccion = "OESTE"
      case "ESTE" => nuevaDireccion = "NORTE"
      case "SUR" => nuevaDireccion = "ESTE"
      case "OESTE" => nuevaDireccion = "SUR"
      //case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
    Future(Drone(Coordenada(x , y, nuevaDireccion)))
  }

  def moverDerecha(drone: Drone): Future[Drone] = {
    val direccion = drone.coordenada.orientacion
    val y = drone.coordenada.y
    val x = drone.coordenada.x
    var nuevaDireccion = ""

    direccion match {
      case "NORTE" => nuevaDireccion = "ESTE"
      case "ESTE" => nuevaDireccion = "SUR"
      case "SUR" => nuevaDireccion = "OESTE"
      case "OESTE" => nuevaDireccion = "NORTE"
      //case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
    Future(Drone(Coordenada(x , y, nuevaDireccion)))
  }
}

// Trait Object
object servicioDroneInterprete extends servicioDroneInterprete

object Instruccion {
  def newInstruccion(drone: Drone, c:Char): Future[Drone] ={
    c match {
      case 'A' => servicioDroneInterprete.moverAdelante(drone)
      case 'D' => servicioDroneInterprete.moverDerecha(drone)
      case 'I' => servicioDroneInterprete.moverDerecha(drone)
      //case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
  }
}
