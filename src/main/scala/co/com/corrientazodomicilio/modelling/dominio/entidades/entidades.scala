package co.com.corrientazodomicilio.modelling.dominio.entidades

import scala.io.Source
import scala.util.Try

trait Instruccion
case class A() extends Instruccion
case class I() extends Instruccion
case class D() extends Instruccion

sealed trait Orientacion
case class NORTE() extends Orientacion { // Sentido Norte ^
  override def toString: String = {
    "dirección Norte"
  }
}
case class ESTE() extends Orientacion { // Sentido Este ->
  override def toString: String = {
    "dirección Oriente"
  }
}
case class SUR() extends Orientacion { // Sentido Sur
  override def toString: String = {
    "dirección Sur"
  }
}
case class OESTE() extends Orientacion { // Sentido Oeste <-
  override def toString: String = {
    "dirección Sur"
  }
}
case class Coordenada(x:Int = 0, y:Int = 0, orientacion:Orientacion = NORTE())

case class Drone(id:String = "00", coordenada: Coordenada, capacidad:Int = 10){
  override def toString: String = {
    "(" + coordenada.x + ", " + coordenada.y + ") " + coordenada.orientacion
  }
}
