package mx.tecnm.tepic.ladm_u2_practica2_juegodelasmoscas

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Mancha(x:Float , y:Float) {
    var x = x
    var y = y
    var opacidad = 255

    fun dibujar( c:Canvas )
    {
        val pincel = Paint()
        pincel.color = Color.argb( opacidad , 255 , 0 , 0 )  // ROJO
        c.drawCircle( x , y , 18f , pincel )
    }

}