package co.com.corrientazodomicilio.modelling.dominio.entidades

import co.com.corrientazodomicilio.modelling.dominio.servicios.ServicioDron

trait Instruccion
case class A() extends Instruccion
case class I() extends Instruccion
case class D() extends Instruccion

object Instruccion {
  def newInstruccion(drone: Drone, c:Instruccion):Drone = {
    c match {
      case A() => ServicioDron.moverAdelante(drone)
      case D() => ServicioDron.moverDerecha(drone)
      case I() => ServicioDron.moverIzquierda(drone)
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

sealed trait Orientacion
case class NORTE() extends Orientacion { // Sentido Norte ^
  override def toString: String = "dirección Norte"

}
case class ESTE() extends Orientacion { // Sentido Este ->
  override def toString: String = "dirección Oriente"

}
case class SUR() extends Orientacion { // Sentido Sur
  override def toString: String = "dirección Sur"
}
case class OESTE() extends Orientacion { // Sentido Oeste <-
  override def toString: String = "dirección Sur"
}
case class Coordenada(x:Int = 0, y:Int = 0, orientacion:Orientacion = NORTE())

case class Drone(id:String = "00", coordenada: Coordenada, capacidad:Int = 10){
  override def toString: String = "(" + coordenada.x + ", " + coordenada.y + ") " + coordenada.orientacion
}

case class Entrega(instrucciones: List[Instruccion])
case class Ruta(entregas: List[Entrega])