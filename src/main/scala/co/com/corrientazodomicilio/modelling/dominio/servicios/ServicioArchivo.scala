package co.com.corrientazodomicilio.modelling.dominio.servicios

import co.com.corrientazodomicilio.modelling.dominio.entidades._

import scala.io.Source
import scala.util.Try
import java.io.{File, PrintWriter}

sealed trait AlgebraServicioArchivo{
  def leerArchivo(filename:String): Try[List[List[Instruccion]]]
  def crearArchivo(entregas: List[Drone])
}

sealed trait ServicioArchivo extends AlgebraServicioArchivo{
  def leerArchivo(filename:String): Try[List[List[Instruccion]]] = {
    val listaVacia = List(List())
    val list: List[String] = Source.fromFile(filename).getLines.toList
    val rutas = Try{list.map { x => x.toUpperCase.split("").toList.map(y => Orientacion.newOrientacion(y)) }}
    //println("lista rutas: " + rutas)
    //rutas.getOrElse(listaVacia)
    rutas
  }

  def crearArchivo(entregas: List[Drone]):Unit = {
    val c = entregas.tail
    println("Aca: " + c)
    val nuevoArchivo = new File("files/out/out"+ c.head.id +".txt")
    val w = new PrintWriter(nuevoArchivo)
    w.write("== Reporte de entregas ==\n")
    if (entregas.head.id == "ErrorArchivo"){
      w.write("Error en el archivo")
    } else {
      c.foreach{ entrega =>
        if (entrega.coordenada.x.abs > 10 || entrega.coordenada.y.abs > 10){
          w.write("Entrega Fallida\n")
        } else if (entrega.coordenada.x.abs == 0 && entrega.coordenada.y.abs == 0 && entrega.coordenada.orientacion == NORTE()){
          w.write("Dron vuelve a (O,O)N por mas pedidos\n")
        } else {
          w.write(entrega.toString + "\n")
        }
      }
    }
    w.close()
  }
}

object ServicioArchivo extends ServicioArchivo
