package co.com.corrientazodomicilio.modelling.dominio.servicios

import co.com.corrientazodomicilio.modelling.dominio.entidades._

import scala.io.Source
import java.io.{File, PrintWriter}

sealed trait AlgebraServicioArchivo{
  def leerArchivo(filename:String): List[List[Instruccion]]
  def crearArchivo(entregas: List[Drone])
}

sealed trait servicioArchivoInterprete extends AlgebraServicioArchivo{
  def leerArchivo(filename:String): List[List[Instruccion]] = {
    val list: List[String] = Source.fromFile(filename).getLines.toList
    list.map { x => x.split("").toList.map(y => Orientacion.newOrientacion(y)) }
  }

  def crearArchivo(entregas: List[Drone]) = {
    val c = entregas.tail
    val nuevoArchivo = new File("files/out.txt")
    val w = new PrintWriter(nuevoArchivo)
    w.write("== Reporte de entregas ==\n")
    c.foreach{
      entrega =>
        w.write(entrega.toString + "\n")
    }
    w.close()
  }
}

object servicioArchivoInterprete extends servicioArchivoInterprete

/*
trait DronesService{
  def iniciar = {
    val listInstructions:List[List[Instruccion]]  = servicioArchivoInterprete.leerArchivo("/home/s4n/in.txt")
  }
}
*/