package hu.ait.minesweep.model

object MineSweepModel{
    val EMPTY: Short = 0
    val ONE: Short = 1
    val TWO: Short = 2
    val THREE: Short = 3
    val BOMB: Short = 9
    val FLAG: Short = 10
    val UNHIDDEN: Short = 11
    val FLAGMODE: Short = -1
    val REVEALMODE: Short = -2
    val LOST: Short = -3
    val WON: Short = -4
    val PLAYING: Short = -5
    var mode = REVEALMODE
    var state = PLAYING

    private val model = arrayOf(
        arrayOf(
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY)
        ),
        arrayOf(
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY)
        ),
        arrayOf(
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY)
        ),
        arrayOf(
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY)
        ),
        arrayOf(
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY),
            shortArrayOf(EMPTY,EMPTY, EMPTY)
        )
    )

    fun placeBombs() {
        var bomb1 = (0..23).random()
        var bomb2 = (0..23).random()
        if (bomb1 == bomb2) {
            bomb2 +=1 }
        var bomb3 = (0..22).random()
        if (bomb1 == bomb3) {
            bomb3 +=1
        } else if (bomb2 == bomb3) {
            bomb3 +=1 }
        setFieldContent(bomb1.div(5), bomb1.rem(5),BOMB)
        setFieldContent(bomb2.div(5), bomb2.rem(5),BOMB)
        setFieldContent(bomb3.div(5), bomb3.rem(5),BOMB) }

    fun checkGameover() {
        var bombCount = 0
        val bombAmount = 3
        for (i in 0..4){
            for (j in 0..4) {
                if (model[i][j][0] == BOMB &&
                    model[i][j][1] == FLAG &&
                    model[i][j][2] == EMPTY) {
                    bombCount += 1
                } else if (model[i][j][0] == BOMB &&
                            model[i][j][2] == UNHIDDEN) {
                    state = LOST }
                if (bombCount == bombAmount) {
                    state = WON }
            }
        }
    }

    fun getFieldContent(x: Int, y: Int) = model[x][y][0]

    fun setFieldContent(x: Int, y:Int, value: Short) {
        model[x][y][0] = value }

    fun checkFlag(x:Int,y:Int) = model[x][y][1]

    fun markFlag(x: Int,y:Int) {
        model[x][y][1] = FLAG }

    fun unFlag(x:Int,y:Int){
        model[x][y][1] = EMPTY }

    fun checkUnhidden(x: Int, y:Int) = model[x][y][2]

    fun markUnhidden(x: Int, y:Int) {
        model[x][y][2] = UNHIDDEN }

    fun findBounds(x:Int,y:Int):IntArray{
        var lowerX = x-1
        var upperX = x+1
        var lowerY = y-1
        var upperY = y+1
        if (x==0) {
            lowerX = 0 }
        if (y==0) {
            lowerY = 0 }
        if (y==4){
            upperY = 4 }
        if (x==4){
            upperX = 4 }
        return intArrayOf(lowerX,upperX,lowerY,upperY)
    }

    fun uncoverSurrounding(x:Int,y:Int) {
        if (getFieldContent(x,y) != BOMB &&
                checkUnhidden(x,y) != UNHIDDEN) {
            val boundArray = findBounds(x,y)
            for (i in boundArray[0]..boundArray[1]){
                for (j in boundArray[2]..boundArray[3]) {
                    if (getFieldContent(i,j) == EMPTY){
                        markUnhidden(i,j)
                    }
                }
            }
        }
    }

    fun calculateValue(x: Int, y:Int) {
        if (getFieldContent(x,y) != BOMB) {
            val boundArray = findBounds(x,y)
            var count = 0
            for (i in boundArray[0]..boundArray[1]){
                for (j in boundArray[2]..boundArray[3]) {
                    if (getFieldContent(i,j) == BOMB){
                        count +=1 } } }
            when (count) {
                0 -> setFieldContent(x,y,EMPTY)
                1 -> setFieldContent(x,y,ONE)
                2 -> setFieldContent(x,y, TWO)
                3 -> setFieldContent(x,y,THREE) }
        }
    }

    fun calculateBoard() {
        for (i in 0..4) {
            for (j in 0..4) {
                calculateValue(i, j)
            }
        }
    }

    fun resetModel(){
        for (i in 0..4){
            for (j in 0..4) {
                model[i][j][2] = EMPTY
                model[i][j][1] = EMPTY
                model[i][j][0] = EMPTY
            }
        }
        state = PLAYING
        placeBombs()
    }

    fun flagState(tX: Int, tY: Int) {
        when (mode) {
            REVEALMODE -> {uncoverSurrounding(tX,tY)
                            markUnhidden(tX,tY)
                           unFlag(tX,tY)}
            FLAGMODE -> markFlag(tX,tY)
        }
    }


}