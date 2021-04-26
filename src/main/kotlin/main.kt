import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val TAMCLASE = 30-1

fun main() {
    /*Vamos a imaginar el siguiente contexto, tenemos un profesor con 30 alumnos.
    Los alumnos son de una clase Alumno. Los alumnos tienen 2 métodos, decir "he llegado" y "hacer el examen".
    Cada alumno tarda de 1 a 6 segundo en llegar (Número aleatorio). Cuando llegan dicen "Alumno X ha llegado".
    Cuando todos han llegado, el profesor reparte los examenes.
    Los examenes son otra clase que tiene un método que se llama hacer y que lleva un número aleatorio de 1 a 4 en completarse.
    Cuando el alumno ha completado el examen, dice "El Alumno X ha terminado el examen".
    Cuando todos los alumnos han terminado, el profesor recoge los exámenes y da pro concluido el examen.
    */

    val listaNombres = mutableListOf("Pepe", "Juan", "Pablo", "Paula", "Elena", "Paloma")
    var auxListaNombres = 0
    val listaAlumnos = mutableListOf<Alumno>()
    val examen = Examen("")
    for(i in 0..TAMCLASE){
        if (auxListaNombres >= 6)
            auxListaNombres = 0
        val alumno = Alumno(listaNombres[auxListaNombres++]+(0..100).random(), examen)
        listaAlumnos.add(alumno)
    }

    val listaExamenes = mutableListOf<Examen>()
    for(i in 0..TAMCLASE){ listaExamenes.add(examen) }

    /*val matrizAlumnoYExamen = mutableListOf(listaAlumnos, listaExamenes)

    matrizAlumnoYExamen.forEach {
        it.forEach {
            if (it is Alumno){
                println(it.nombre)
            }else if(it is Examen){
                println(it.rellenarExamen())
            }else
                println("Tipo de dato incorrecto")
        }
    }
    */
    val profesor = Profesor("Carlos", listaAlumnos, listaExamenes)

    //Llegada de alumnos a clase
    runBlocking {
        listaAlumnos.forEach {
            this.launch {
                delay((1..6).random()*1000L)
                println(it.nombre + it.llegar())
            }
        }
    }

    println(profesor.nombre + profesor.repartirExamen())

    runBlocking {
        listaAlumnos.forEach { alumno ->
            this.launch {
                delay(alumno.examen.rellenarExamen()*1000L)
                println(alumno.acabadoExamen())
            }
        }
    }

    println("Hemos acabado el examen, podemos comenzar con la desinfección")










}

class Profesor(val nombre: String, val listaAlumnos : MutableList<Alumno>, val listaExamen: MutableList<Examen>){
    fun repartirExamen(): String{
        listaAlumnos.forEach { alumno ->
            listaExamen.forEach { examen ->
                examen.nombre = alumno.nombre
            }
        }
        return "ha repartido el examen a cada alumno"
    }

}
class Alumno(val nombre : String, val examen : Examen){
    fun llegar() : String{ return " he llegado"}
    fun acabadoExamen() : String{ return "El alumno $nombre ha terminado el examen"}
}
class Examen(var nombre : String) {
    fun rellenarExamen() : Int{ return ((1..4).random())}
}