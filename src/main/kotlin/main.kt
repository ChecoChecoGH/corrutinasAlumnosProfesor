import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

const val TAMCLASE = 30-1

fun main() {
    //Listas
    val listaNombres = mutableListOf("Pepe", "Juan", "Pablo", "Paula", "Elena", "Paloma")
    val listaAlumnos = mutableListOf<Alumno>()
    val listaExamenes = mutableListOf<Examen>()

    //aux y tamaño de listaNombres
    var auxListaNombres = 0
    val tamListaNombres = listaNombres.size

    //relleno la lista de examenes con examenes vacíos
    for(i in 0..TAMCLASE){
        val examen = Examen("", 0.0)
        listaExamenes.add(examen)
    }

    //relleno la listaAlumnos, cada alumno con su nombre y su examen
    for(i in 0..TAMCLASE){
        if(auxListaNombres >= tamListaNombres)
            auxListaNombres = 0
        val alumno = Alumno(listaNombres[auxListaNombres++]+(i+1), listaExamenes[i])
        listaAlumnos.add(alumno)
    }

    //creo el profesor con su nombre, la listaAlumnos y la listaExamenes
    val profesor = Profesor("Carlos", listaAlumnos, listaExamenes)

    //Llegada de alumnos a clase
    runBlocking {
        listaAlumnos.forEach {
            this.launch { println(it.nombre + it.llegar()) }
        }
    }

    //Como ya estan todos los alumnos el profesor reparte los examenes
    println(profesor.repartirExamen())

    //los examenes se rellenan y cada alumno dice cuando ha terminado el examen
    runBlocking {
        listaAlumnos.forEach { alumno ->
            this.launch {
                delay(alumno.examen.rellenarExamen()*1000L)
                println(alumno.acabadoExamen())
            }
        }
    }

    //los examenes están todos entregados asique el profesor pone las notas
    println("\n${profesor.nombre} ya tiene todos los examenes y los corrige\n")
    profesor.corregirExamenes()
}

class Profesor(val nombre: String, private val listaAlumnos : MutableList<Alumno>, private val listaExamen: MutableList<Examen>){
    //el profesor asigna a cada examen el nombre de cada alumno
    fun repartirExamen(): String{
        var i = 0
        //cambiar la lista de examenes por la de alumnos y pasarle el examen
        listaExamen.forEach { examen -> examen.nombre = listaAlumnos[i++].nombre }
        return "\n$nombre ha repartido el examen a todos los alumnos\n"
    }

    //a cada examen.nota le asigno un numero aleatorio y muestro el examen.nombre y el examen.nota
    fun corregirExamenes(){
        //doy una nota a cada alumno
        listaExamen.forEach { examen -> examen.nota = String.format("%.2f", Random.nextDouble(0.0, 10.01)).replace(',','.').toDouble() }

        //ordeno y muestro ordenado de menor a mayor nota
        listaExamen.sortBy { it.nota }
        listaExamen.forEach { examen -> println("El alumno ${examen.nombre} ha sacado un ${examen.nota}") }

    }
}

class Alumno(var nombre : String, var examen : Examen){
    suspend fun llegar() : String{
        delay((1..6).random()*1000L)
        return " he llegado"}
    fun acabadoExamen() : String{ return "El alumno $nombre ha terminado el examen"}
}

//rellenarExamen es para el tiempo que tarda en rellenarlo
class Examen(var nombre: String, var nota: Double) { fun rellenarExamen() : Int{ return ((1..4).random())} }