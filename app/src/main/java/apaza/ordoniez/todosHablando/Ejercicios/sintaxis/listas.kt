package apaza.ordoniez.todosHablando.Ejercicios.sintaxis

fun main(){

}
fun inmutableList(){
    val readOnly:List<String> = listOf("lunes","martes","miercoles","jueves","viernes","sabado","domingo")

    /*println(readOnly.size)
    println(readOnly.toString())
    println(readOnly[1])
    println(readOnly.first())
    println(readOnly.last())*/

    val example = readOnly.filter { it.contains("a") }
    println(example)

    readOnly.forEach{ weekDay -> println(weekDay)}
}
fun listaMutable(){

    var weekDays:MutableList<String> = mutableListOf("\"lunes\",\"martes\",\"miercoles\",\"jueves\",\"viernes\",\"sabado\",\"domingo\"")
    weekDays.add(index = 0, "neil")
    println(weekDays)

    if (weekDays.isEmpty()){
        println("lista vacia")
    }
    if(weekDays.isNotEmpty()){
        println("acceso otorgado")
    }
}