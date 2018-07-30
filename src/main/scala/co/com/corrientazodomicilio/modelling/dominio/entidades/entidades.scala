package co.com.corrientazodomicilio.modelling.dominio.entidades

sealed trait Instrucion
case class A(drone: Drone, c:Char) extends Instrucion
case class I(drone: Drone, c:Char) extends Instrucion
case class D(drone: Drone, c:Char) extends Instrucion

sealed trait Orientacion
case class NORTE(s:String) extends Orientacion  {
  "NORTE"
}// Sentido Norte ^

case class ESTE(s:String) extends Orientacion  {
  "ESTE"
}// Sentido Este ->

case class SUR(s:String) extends Orientacion  {
  "SUR"
}// Sentido Sur
case class OESTE(s:String) extends Orientacion  {
  "OESTE"
}// Sentido Oeste <-

case class Coordenada(x:Int, y:Int, orientacion:String)
//case class Drone(id:Int, entregas:List[String], capacidad:Int)
case class Drone(coordenada: Coordenada)
case class Ruta(ruta:String)