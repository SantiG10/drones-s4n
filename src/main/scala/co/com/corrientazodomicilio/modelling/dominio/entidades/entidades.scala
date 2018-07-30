package co.com.corrientazodomicilio.modelling.dominio.entidades

sealed trait Instrucion
case class A(drone: Drone, c:Char) extends Instrucion
case class I(drone: Drone, c:Char) extends Instrucion
case class D(drone: Drone, c:Char) extends Instrucion

sealed trait Orientacion
case class NORTE() extends Orientacion      // Sentido Norte ^
case class ESTE() extends Orientacion       // Sentido Este ->
case class SUR() extends Orientacion        // Sentido Sur
case class OESTE() extends Orientacion      // Sentido Oeste <-

case class Coordenada(x:Int, y:Int, orientacion:Orientacion) {
  def apply(): Coordenada = new Coordenada(0, 0, NORTE())

  def apply(x: Int, y: Int, orientacion: Orientacion): Coordenada = new Coordenada(x, y, orientacion)
}

//case class Drone(id:Int, entregas:List[String], capacidad:Int)
case class Drone(coordenada: Coordenada)

case class Ruta(drone: Drone, entregas:List[String])