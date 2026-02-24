package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.practicum.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?=null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
): View(context, attrs, defStyleAttr, defStyleRes) {

    private val imagePlayBitmap: Bitmap?
    private val imagePauseBitmap: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)

    var isPlaying: Boolean = false
        //установить / сменить значение
        set (value) {
            if (field == value) return
            field = value
            invalidate() //перерисовать
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imagePlayBitmap = getDrawable(R.styleable.PlaybackButtonView_imagePlay)?.toBitmap()
                imagePauseBitmap =
                    getDrawable(R.styleable.PlaybackButtonView_imagePause)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        when(isPlaying){
            true ->
                imagePauseBitmap?.let {
                    canvas.drawBitmap(imagePauseBitmap, null, imageRect, null)
                }
            false ->
                imagePlayBitmap?.let {
                    canvas.drawBitmap(imagePlayBitmap, null, imageRect, null)
                }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isPlaying) return super.onTouchEvent(event) //если не проигрывается трек

        when(event?.action){
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    //запускается при обнаружении нажатия
    override fun performClick(): Boolean {
        super.performClick() //оставить для совместимости
        return true
    }


}