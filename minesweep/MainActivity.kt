package hu.ait.minesweep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import hu.ait.minesweep.model.MineSweepModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MineSweepModel.placeBombs()

        btnRestart.setOnClickListener{
            mineView.clearGame()
            flagToggle.isChecked = false
        }

        flagToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showText(getString(R.string.SET_FLAG))
                MineSweepModel.mode = MineSweepModel.FLAGMODE
            } else {
                showText(getString(R.string.REVEAL_FIELD))
                MineSweepModel.mode = MineSweepModel.REVEALMODE
            }
        }
    }

    fun showText(text: String) {
        Snackbar.make(layoutMain,
            text, Snackbar.LENGTH_LONG).show()
    }

}
