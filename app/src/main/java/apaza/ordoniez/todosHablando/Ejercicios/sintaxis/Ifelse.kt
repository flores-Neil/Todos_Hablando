package apaza.ordoniez.todosHablando.Ejercicios.sintaxis

fun main(){
    var iterador = 0
        for (i in 0 until 100 step 1){
            if(i%2 == 0){
                println(i)
            }
        }
    getMonth(3)

}
fun getMonth(moth:Int){
    when(moth){
        1 -> println("ENERO")
        2 -> println("FEBRERO")
        3 -> println("MARZO")
        4 -> println("abril")
        5 -> println("junio")
        6 -> println("julio")
        7 -> println("agosto")
        8 -> println("septiembre")
        9 -> println("octubre")
        else -> println("No se puede procesar: ")

    }
}