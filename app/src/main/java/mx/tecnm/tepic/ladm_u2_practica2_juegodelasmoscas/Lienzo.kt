package mx.tecnm.tepic.ladm_u2_practica2_juegodelasmoscas

import android.graphics.*
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.util.ArrayList

class Lienzo(p: MainActivity): View(p) {

    val pantalla = p
    var moscas = ArrayList<Mosca>()
    var manchas = ArrayList<Mancha>()
    val botonPlay = Boton( pantalla )
    val botonReiniciar = Boton( pantalla )
    val hiloMosca = HiloMosca(this)
    val hiloMancha = HiloMancha(this)
    val hiloTiempo = HiloTiempo( this )
    var wCanva = 0f
    var hCanva = 0f
    val INICIO = 1
    var RESULTADO = 2
    var JUEGO = 3
    var interfazVisible = INICIO
    var totalMoscasVivas = 0
    var tiempoRestante = 60
    val fuenteLetra = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)


    init {
        hiloTiempo.start()
        //totalMoscasVivas = 10
        totalMoscasVivas = Math.round( (Math.random() * 20f).toFloat() ) + 80
        (1..totalMoscasVivas).forEach {
            moscas.add( Mosca(pantalla) )
        }
        hiloMosca.start()
        hiloMancha.start()
    }


    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        wCanva = c.width.toFloat()
        hCanva = c.height.toFloat()
        val pincel = Paint()


        // ---- DIBUJAR MANCHAS
        var index = 0
        while( index < manchas.size )
        {
            manchas.get(index ).dibujar(c)
            index++
        }


        // ---- DIBUJAR MOSCAS
        index = 0
        while( index < moscas.size ) {
            moscas.get( index ).crear(c)
            moscas.get( index ).dibujar(c)
            index++
        }

        // ---- DATOS DEL JUEGO
        if( interfazVisible == JUEGO )
        {
            pincel.color = Color.argb(200, 27, 27, 57)  // AZUL CON TRASPARENCIA
            c.drawRect(
                posX(0f, 0f),
                posY(0f, 0f),
                posX(100f, 0f),
                posY(5f, 0f), pincel
            )
            // IMAGEN MOSCA
            val altura = mostrarImagen( c , R.drawable.icono_mosca , 4f ,
                posX( 10f , 0f ) ,
                posY( 0.5f , 0f )
            )
            // IMAGEN TIEMPO
            mostrarImagen( c , R.drawable.icono_reloj , 4f ,
                posX( 60f , 0f ) ,
                posY( 0.5f , 0f )
            )
            // TEXTOS
            pincel.color = Color.rgb( 230 , 230 , 230 )
            pincel.typeface = fuenteLetra
            pincel.textSize = posY(3f, 0f)
            c.drawText(totalMoscasVivas.toString() , posX(20f, 0f), posY(3.5f, 0f), pincel)
            c.drawText(tiempoRestante.toString() + "s", posX(70f, 0f), posY(3.5f, 0f), pincel)
        }

        // ----- PANTALLA DE INICIO
        if( interfazVisible == INICIO ) {
            pincel.color = Color.argb(200, 27, 27, 57)  // AZUL CON TRASPARENCIA
            c.drawRect(
                posX(0f, 0f),
                posY(0f, 0f),
                posX(100f, 0f),
                posY(100f, 0f), pincel
            )
            // IMAGEN MOSCA
            val altura = mostrarImagen( c , R.drawable.icono_mosca , 15f ,
                posX( 15f , 0f ) ,
                posY( 25f , 0f )
            )
            // IMAGEN TIEMPO
            mostrarImagen( c , R.drawable.icono_reloj , 15f ,
                posX( 85f - 25f , 0f ) ,
                posY( 25f , 0f )
            )
            // TEXTOS DEL TOTAL DE MOSCAS A MATAR Y TIEMPO TOTAL
            pincel.color = Color.rgb( 230 , 230 , 230 )
            pincel.typeface = fuenteLetra
            pincel.textSize = posY(6f, 0f)
            c.drawText(totalMoscasVivas.toString() , posX(20f, 0f), posY(25f + 6.5f, altura.toFloat()), pincel)
            c.drawText("60s", posX(85f - 25f + 5f, 0f), posY(25f + 6.5f, altura.toFloat()), pincel)

            // BOTON PLAY
            botonPlay.crear( c , R.drawable.icono_jugar )
            botonPlay.dibujar( c )
        }

        // ----- PANTALLA DE RESULTADO
        if( interfazVisible == RESULTADO ) {
            pincel.color = Color.argb(200, 27, 27, 57)  // AZUL CON TRASPARENCIA
            c.drawRect(
                posX(0f, 0f),
                posY(0f, 0f),
                posX(100f, 0f),
                posY(100f, 0f), pincel
            )
            pincel.color = Color.rgb( 230 , 230 , 230 )
            pincel.typeface = fuenteLetra
            pincel.textSize = posY(8f, 0f)
            if( totalMoscasVivas == 0 )
            {
                // VICTORIA
                c.drawText("Ganaste", posX(25f, 0f), posY(10f + 8f , 0f) , pincel)
                // IMAGEN PULGAR ARRIBA
                val altura = mostrarImagen( c , R.drawable.icono_victoria , 25f ,
                    posX( 20f , 0f ) ,
                    posY( 10f + 8f + 3f , 0f )
                )
            }
            else
            {
                // DERROTA
                c.drawText("Perdiste", posX(25f, 0f), posY(10f + 8f , 0f) , pincel)
                // IMAGEN PULGAR ABAJO
                val altura = mostrarImagen( c , R.drawable.icono_derrota , 25f ,
                    posX( 20f , 0f ) ,
                    posY( 10f + 8f + 3f , 0f )
                )
            }
            // BOTON REINICIAR
            botonReiniciar.crear( c , R.drawable.icono_reiniciar )
            botonReiniciar.dibujar( c )
        }
    }


    override fun onTouchEvent(e: MotionEvent): Boolean {
        if( e.action == MotionEvent.ACTION_DOWN )
        {
            when( interfazVisible )
            {
                INICIO -> {
                    if(botonPlay.botonPresionado( e.x , e.y ) )
                    {
                        interfazVisible = JUEGO
                        invalidate()
                    }
                }
                JUEGO -> {
                    var indexMosca = moscas.size
                    while( indexMosca > 0 ) {
                        indexMosca--
                        val mosca = moscas.get(indexMosca)
                        if( mosca.moscaPresionada(e.x,e.y) )
                        {
                            totalMoscasVivas--
                            manchas.add( Mancha(
                                mosca.x + mosca.xi + (mosca.wMosca / 2) ,
                                mosca.y + mosca.yi + (mosca.hMosca / 2)
                            ))
                            moscas.remove( mosca )
                            if( totalMoscasVivas == 0 ) { interfazVisible = RESULTADO }
                            invalidate()
                            return true
                        }
                    }
                }
                RESULTADO -> {
                    if( botonReiniciar.botonPresionado( e.x , e.y ) )
                    {
                        interfazVisible = INICIO
                        tiempoRestante = 60
                        //totalMoscasVivas = 10
                        totalMoscasVivas = Math.round( (Math.random() * 20f).toFloat() ) + 80
                        moscas.clear()
                        manchas.clear()
                        (1..totalMoscasVivas).forEach {
                            moscas.add( Mosca(pantalla) )
                        }
                        invalidate()
                    }
                }
            }
        }
        return true
    }


    fun mostrarImagen( c: Canvas , img: Int , hImagen: Float , x: Float , y: Float ): Int {
        val imgOriginal = BitmapFactory.decodeResource( pantalla.resources , img )
        val altura = posY( hImagen , 0f ).toInt()
        val factor = altura.toFloat() / imgOriginal.height
        val ancho = (imgOriginal.width * factor).toInt()
        val imagen = Bitmap.createScaledBitmap( imgOriginal , ancho , altura , false )
        c.drawBitmap( imagen , x , y , Paint() )
        return altura
    }

    fun posX( porcentaje: Float , desfase: Float ): Float  { return ( porcentaje * wCanva / 100f ) + desfase  }
    fun posY( porcentaje: Float , desfase: Float ): Float { return ( porcentaje * hCanva / 100f ) + desfase }

}




class HiloMosca( l: Lienzo ): Thread()
{
    val lienzo = l
    override fun run() {
        super.run()
        while( true )
        {
            sleep(40)
            var index = 0
            while( index < lienzo.moscas.size ) {
                lienzo.moscas.get( index ).mover()
                index++
            }
            lienzo.pantalla.runOnUiThread {
                lienzo.invalidate()
            }
        }
    }
}



class HiloMancha( l: Lienzo ): Thread()
{
    val lienzo = l
    override fun run() {
        super.run()
        while( true )
        {
            sleep(60)
            var indexMancha = 0
            while( indexMancha < lienzo.manchas.size ) {
                lienzo.manchas.get( indexMancha ).opacidad--
                if( lienzo.manchas.get( indexMancha ).opacidad == 0 ) { lienzo.manchas.removeAt( indexMancha ) }
                indexMancha++
            }
            lienzo.pantalla.runOnUiThread {
                lienzo.invalidate()
            }
        }
    }
}


class HiloTiempo( l: Lienzo ): Thread()
{
    val lienzo = l
    override fun run() {
        super.run()
        while( true )
        {
            if( lienzo.interfazVisible == lienzo.JUEGO )
            {
                sleep( 1000 )
                lienzo.tiempoRestante--
                lienzo.pantalla.runOnUiThread {
                    lienzo.invalidate()
                }
                if( lienzo.tiempoRestante == 0 )
                {
                    lienzo.interfazVisible = lienzo.RESULTADO
                    lienzo.pantalla.runOnUiThread {
                        lienzo.invalidate()
                    }
                }
            }
            else{  sleep(50)  }
        }
    }
}