package mx.tecnm.tepic.ladm_u2_practica2_juegodelasmoscas

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint

class Mosca( p: MainActivity ) {

    val pantalla = p
    var x = 0f
    var y = 0f
    var xi = 0f
    var yi = 0f
    var pendiente = 0f
    var subiendo = false
    var wMosca = 0
    var hMosca = 0
    var wCanva = 0f
    var hCanva = 0f
    var imagen: Bitmap? = null
    var moscaAplastada = false


    fun crear( c: Canvas ) {
        if( imagen != null ) { return }
        wCanva = c.width.toFloat()
        hCanva = c.height.toFloat()
        val imgOriginal = BitmapFactory.decodeResource( pantalla.resources , R.drawable.mosca )
        wMosca = posX( 9f , 0f ).toInt()
        var factor = wMosca.toFloat() / imgOriginal.width
        hMosca = (imgOriginal.height * factor).toInt()
        imagen = Bitmap.createScaledBitmap( imgOriginal , wMosca , hMosca , false )
        pendiente = (Math.random() * 3f - 1.5f).toFloat()
        var aleatorio = Math.random()
        subiendo = aleatorio < 0.5f
        x = (wCanva / 2) - (wMosca / 2)
        y = (hCanva / 2) - (hMosca / 2)
    }



    fun mover()
    {
        if( subiendo == true )
        {
            if( pendiente >= 0 )
            {
                xi += 5f
                yi = - Math.abs( pendiente * xi )
            }
            else
            {
                xi -= 5f
                yi = - Math.abs( pendiente * xi )
            }
        }  // FIN SUBIENDO
        else
        {
            if( pendiente >= 0 )
            {
                xi -= 5f
                yi = Math.abs( pendiente * xi )
            }
            else
            {
                xi += 5f
                yi = Math.abs( pendiente * xi )
            }
        }

        // VERIFICAR REBOTES
        if( y + yi <= posY( 5f , 0f ) )
        {
            subiendo = false
            pendiente = (Math.random() * 3f - 1.5f).toFloat()
            y += yi
            x += xi
            xi = 0f
            yi = 0f
        }
        else if( (y + yi + hMosca) >= hCanva )
        {
            subiendo = true
            pendiente = (Math.random() * 3f - 1.5f).toFloat()
            y += yi
            x += xi
            xi = 0f
            yi = 0f
        }
        else if( (x + xi) <= 0 )
        {
            subiendo = Math.random() < 0.5
            y += yi
            x += xi
            xi = 0f
            yi = 0f
            if( subiendo == true ) { pendiente = (Math.random() * 1.5f).toFloat() }
            else{ pendiente = (Math.random() * -1.5f).toFloat() }
        }
        else if( (x + xi + wMosca.toFloat() ) >= wCanva )
        {
            subiendo = Math.random() < 0.5
            y += yi
            x += xi
            xi = 0f
            yi = 0f
            if( subiendo == true ) { pendiente = (Math.random() * -1.5f).toFloat() }
            else{  pendiente = (Math.random() * 1.5f).toFloat()  }
        }
    }



    fun dibujar( c: Canvas )
    {
        wCanva = c.width.toFloat()
        hCanva = c.height.toFloat()
        if( moscaAplastada == false ){  c.drawBitmap( imagen!! , x + xi , y + yi , Paint() ) }

    }


    fun moscaPresionada( toqueX: Float , toqueY: Float ): Boolean
    {
        if( toqueX >= x + xi + 0f && toqueX <= x + xi + wMosca - 0f )
        {
            if( toqueY >= y + yi + 0f && toqueY <= y + yi + hMosca - 0f )
            {
                return true
            }
        }
        return false
    }



    fun posX( porcentaje: Float , desfase: Float ): Float { return ( porcentaje * wCanva / 100f ) + desfase }

    fun posY( porcentaje: Float , desfase: Float ): Float { return ( porcentaje * hCanva / 100f ) + desfase }
}