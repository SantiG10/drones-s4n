package co.com.corrientazodomicilio.modelling.dominio.servicios

import co.com.corrientazodomicilio.modelling.dominio.entidades._

import scala.io.Source
import scala.util.Try
import java.io.{File, PrintWriter}

sealed trait AlgebraServicioArchivo{
  def leerArchivo(filename:String): List[List[Instruccion]]
  def crearArchivo(entregas: List[Drone])
}

sealed trait servicioArchivoInterprete extends AlgebraServicioArchivo{
  def leerArchivo(filename:String): List[List[Instruccion]] = {
    val listaVacia = List(List())
    val list: List[String] = Source.fromFile(filename).getLines.toList
    val rutas = Try{list.map { x => x.toUpperCase.split("").toList.map(y => Orientacion.newOrientacion(y)) }}
    rutas.getOrElse(listaVacia)
  }

  def crearArchivo(entregas: List[Drone]):Unit = {
    val c = entregas.tail
    val nuevoArchivo = new File("files/out"+ c.head.id +".txt")
    val w = new PrintWriter(nuevoArchivo)
    w.write("== Reporte de entregas ==\n")
    if (c.head == Drone(c.head.id, Coordenada())){
      w.write("Error en el archivo")
    } else {
      c.foreach{ entrega =>
        w.write(entrega.toString + "\n")
      }
    }
    w.close()
  }
}

object servicioArchivoInterprete extends servicioArchivoInterprete

/*
trait DronesService{
  def iniciar = {
    val listInstructions:List[List[Instruccion]]  = servicioArchivoInterprete.leerArchivo("/home/s4n/in01.txt")
  }
}
*/

// Realizar los futuros
// implicit val ex = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))