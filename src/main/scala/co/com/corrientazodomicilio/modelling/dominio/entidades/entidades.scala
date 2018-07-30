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

case class Coordenada(x:Int = 0, y:Int = 0, orientacion:Orientacion = NORTE())

//case class Drone(id:Int, entregas:List[String], capacidad:Int)
case class Drone(coordenada: Coordenada)

case class Ruta(drone: Drone, entregas:List[String])

// ARCHIVO -> co.com.drones.Inputs.services.InputsReaderService
trait InputsReaderService{
  def leerArchivo(): List[Instrucion]
  
  def crearArchivo(): Unit
}

// ARCHIVO -> co.com.drones.Inputs.services.InputsReaderServiceImpl
trait InputsReaderServiceImpl extends InputsReaderService{
    def leerArchivo(): List[Instrucion] = {
      ??? 
    }
}

object InputsReaderServiceImpl extends InputsReaderServiceImpl

// ARCHIVO -> co.com.drones.drones.services.dronesService
trait DronesService{
   def iniciar = {
      val listInstructions:List[Instrucion]  = InputsReaderServiceImpl.leerArchivo() 
   }
}
