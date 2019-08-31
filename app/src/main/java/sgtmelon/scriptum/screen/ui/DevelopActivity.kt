package sgtmelon.scriptum.screen.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.ui.callback.IDevelopActivity
import sgtmelon.scriptum.screen.vm.DevelopViewModel
import sgtmelon.scriptum.screen.vm.callback.IDevelopViewModel

/**
 * Экран для подробного отображения информации из бд
 *
 * @author SerjantArbuz
 */
class DevelopActivity : AppCompatActivity(), IDevelopActivity {

    private val iViewModel: IDevelopViewModel by lazy {
        ViewModelProviders.of(this).get(DevelopViewModel::class.java).apply {
            callback = this@DevelopActivity
        }
    }

    private val introButton: Button? by lazy { findViewById<Button>(R.id.develop_intro_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_develop)

        iViewModel.onSetup()

        introButton?.setOnClickListener { iViewModel.onIntroClick() }
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
    }

    override fun fillAboutNoteTable(data: String) {
        findViewById<ProgressBar?>(R.id.develop_note_progress)?.visibility = View.GONE
        findViewById<ScrollView?>(R.id.develop_note_scroll)?.visibility = View.VISIBLE

        findViewById<TextView?>(R.id.develop_note_text)?.text = data
    }

    override fun fillAboutRollTable(data: String) {
        findViewById<ProgressBar?>(R.id.develop_roll_progress)?.visibility = View.GONE
        findViewById<ScrollView?>(R.id.develop_roll_scroll)?.visibility = View.VISIBLE

        findViewById<TextView?>(R.id.develop_roll_text)?.text = data
    }

    override fun fillAboutRankTable(data: String) {
        findViewById<ProgressBar?>(R.id.develop_rank_progress)?.visibility = View.GONE
        findViewById<ScrollView?>(R.id.develop_rank_scroll)?.visibility = View.VISIBLE

        findViewById<TextView?>(R.id.develop_rank_text)?.text = data
    }

    override fun fillAboutPreference(data: String) {
        findViewById<ProgressBar?>(R.id.develop_preference_progress)?.visibility = View.GONE
        findViewById<ScrollView?>(R.id.develop_preference_scroll)?.visibility = View.VISIBLE

        findViewById<TextView?>(R.id.develop_preference_text)?.text = data
    }

}