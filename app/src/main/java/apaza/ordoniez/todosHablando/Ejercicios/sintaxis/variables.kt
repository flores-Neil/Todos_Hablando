package apaza.ordoniez.todosHablando.Ejercicios.sintaxis

fun main(){
    operaciones()
    var namberOne = 12;
    var namberTwo = 41
    add(namberOne,namberTwo)
}
fun add(namberOne:Int, namberTwo:Int):Int{
    return (namberOne + namberTwo)

}
fun addOne(namberOne:Int, namberTwo:Int):Int = (namberOne + namberTwo)


fun operaciones(){
    /*
    // los valores no pueden modificarse
    val age1:Int = 12
    val age:Int = 18

    val numero = 12.1f
    val numero1:Float = 12.8f

    val letra = 'a'
    val letra1:Char = '@'

    val mensaje = "holamundo"
    val mensaje1:String = "holamundo"

    val boleano = true
    val boleano1:Boolean = false

    // las variablers  pueden modificarse
    var año = 2004
    println(año)
    año = 2005
    println(año)
*/
    /*opreaciones aritmeticas
    var suma = age1 +age
    //println(suma)
    //suma entre entero y flotante flotante
    var suma1 = age + numero
    //convertir el resultado en otro tipo de dato float -> int
    var suma2 = age + numero.toInt()

    println(suma2)
    */
    val nombre:String = "Neil"
    var texto = "hola me llamo $nombre"
    println(texto)
}