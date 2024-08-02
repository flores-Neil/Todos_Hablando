package apaza.ordoniez.todosHablando.Ejercicios.sintaxis

fun main(){
    var weekdays = arrayOf("lunes","martes","miercoles","jueves","viernes","sabado","domingo")

    for(i in weekdays.indices) {
        println(weekdays[i])
    }
    for((i, value) in weekdays.withIndex()){
        println("feliz dia $i este $value")
    }
    for(position in weekdays){
        println("hoy dia es $position")
    }
}