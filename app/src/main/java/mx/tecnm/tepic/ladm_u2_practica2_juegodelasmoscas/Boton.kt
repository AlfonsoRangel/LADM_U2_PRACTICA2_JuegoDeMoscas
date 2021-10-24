package mx.tecnm.tepic.ladm_u2_practica2_juegodelasmoscas

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint

class Boton(p: MainActivity) {

    val pantalla = p
    var x = 0f
    var y = 0f
    var wBoton = 0
    var hBoton = 0
    var wCanva = 0f
    var hCanva = 0f
    var imagen: Bitmap? = null


    fun crear( c: Canvas , img: Int) {
        if( imagen != null ) { return }
        wCanva = c.width.toFloat()
        hCanva = c.height.toFloat()
        val imgOriginal = BitmapFactory.decodeResource( pantalla.resources , img )
        wBoton = posX( 30f , 0f ).toInt()
        val factor = wBoton.toFloat() / imgOriginal.width
        hBoton = (imgOriginal.height * factor).toInt()
        imagen = Bitmap.createScaledBitmap( imgOriginal , wBoton , hBoton , false )
        x = (wCanva / 2f) - (wBoton / 2f)
        y = posY( 70f , - (hBoton / 2f) )
    }


    fun dibujar( c: Canvas )
    {
        wCanva = c.width.toFloat()
        hCanva = c.height.toFloat()
        c.drawBitmap( imagen!! , x , y , Paint() )
    }


    fun botonPresionado( toqueX: Float , toqueY: Float ): Boolean
    {
        if( toqueX >= x && toqueX <= x + wBoton )
        {
            if( toqueY >= y && toqueY <= y + hBoton )
            {
                return true
            }
        }
        return false
    }



    fun posX( porcentaje: Float , desfase: Float ): Float { return ( porcentaje * wCanva / 100f ) + desfase }

    fun posY( porcentaje: Float , desfase: Float ): Float { return ( porcentaje * hCanva / 100f ) + desfase }
}