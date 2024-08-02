package apaza.ordoniez.todosHablando.Ejercicios.sintaxis

fun main(){
    var name:String? = null
    print(name?.get(3) ?: "el nulo")
}