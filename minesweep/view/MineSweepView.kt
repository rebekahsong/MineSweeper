package hu.ait.minesweep.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import hu.ait.minesweep.MainActivity
import hu.ait.minesweep.R
import hu.ait.minesweep.model.MineSweepModel

class MineSweepView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var paintBackground: Paint = Paint()
    var paintLine: Paint = Paint()
    var paintText: Paint = Paint()

    var bombImg: Bitmap = BitmapFactory.decodeResource(
        context?.resources, R.drawable.bombimg
    )
    var flagImg: Bitmap = BitmapFactory.decodeResource(
        context?.resources, R.drawable.flagimg
    )

    init {
        paintBackground.color = Color.GRAY
        paintBackground.style = Paint.Style.FILL
        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 6f
        paintText.color = Color.WHITE }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintText.textSize = height/6f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0f,0f,width.toFloat(), height.toFloat(), paintBackground)
        drawBoard(canvas)
        drawPlayers(canvas)
    }

    private fun drawBoard(canvas: Canvas?) {
        paintLine.color = Color.WHITE
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        for (i in 1..4) {
            canvas?.drawLine(
                0f, (i * height / 5).toFloat(), width.toFloat(), (i * height / 5).toFloat(), paintLine)
            canvas?.drawLine(
                (i * width / 5).toFloat(), 0f, (i * width / 5).toFloat(), height.toFloat(), paintLine)
        }
        MineSweepModel.calculateBoard()
    }


    private fun drawPlayers(canvas: Canvas?) {
        for (i in 0..4) {
            for (j in 0..4) {
                bombImg = Bitmap.createScaledBitmap(bombImg, width/6, height/6, false)
                flagImg = Bitmap.createScaledBitmap(flagImg, width/7, height/7, false)
                val centerX = (i * width / 5).toFloat() + width/20
                val centerY = (j * height / 5 + height / 5).toFloat() - width/25
                if (MineSweepModel.checkUnhidden(i,j) == MineSweepModel.UNHIDDEN &&
                    MineSweepModel.checkFlag(i,j) == MineSweepModel.EMPTY){
                when (MineSweepModel.getFieldContent(i,j)) {
                    MineSweepModel.EMPTY -> canvas?.drawText(context.getString(R.string.ZERO_STR), centerX, centerY, paintText)
                    MineSweepModel.ONE ->  canvas?.drawText(context.getString(R.string.ONE_STR), centerX, centerY, paintText)
                    MineSweepModel.TWO ->  canvas?.drawText(context.getString(R.string.TWO_STR), centerX, centerY, paintText)
                    MineSweepModel.THREE ->  canvas?.drawText(context.getString(R.string.THREE_STR), centerX, centerY, paintText)
                    MineSweepModel.BOMB -> canvas?.drawBitmap(bombImg, centerX - width/35, centerY - width/5 + width/17, null)
                }} else if (MineSweepModel.checkFlag(i,j) != MineSweepModel.EMPTY){
                    canvas?.drawBitmap(flagImg, centerX - width/35, centerY - width/5 + width/17, null) }
            }}
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val tX = event.x.toInt() / (width/5)
            val tY = event.y.toInt() / (height/5)
            if (tX < 5 && tY < 5 &&
                MineSweepModel.checkUnhidden(tX,tY) == MineSweepModel.EMPTY &&
                MineSweepModel.state == MineSweepModel.PLAYING) {
                MineSweepModel.flagState(tX,tY)
                invalidate() }
            MineSweepModel.checkGameover()
            if (MineSweepModel.state == MineSweepModel.LOST) {
                (context as MainActivity).showText(context.getString(R.string.LOSING_TEXT))
            } else if (MineSweepModel.state == MineSweepModel.WON) {
                (context as MainActivity).showText(context.getString(R.string.WINNING_TEXT)) }
        }
    return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h
        setMeasuredDimension(d, d)
    }

    fun clearGame(){
        MineSweepModel.resetModel()
        invalidate()
    }
}


