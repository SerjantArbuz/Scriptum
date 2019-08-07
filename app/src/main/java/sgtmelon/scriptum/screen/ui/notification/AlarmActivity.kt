package sgtmelon.scriptum.screen.ui.notification

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.control.alarm.MelodyControl
import sgtmelon.scriptum.control.alarm.VibratorControl
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import sgtmelon.scriptum.view.RippleContainer

/**
 * Экран с уведомлением запущенным по таймеру
 *
 * @author SerjantArbuz
 */
class AlarmActivity : AppActivity(), IAlarmActivity {

    private val iViewModel: IAlarmViewModel by lazy {
        ViewModelProviders.of(this).get(AlarmViewModel::class.java).apply {
            callback = this@AlarmActivity
        }
    }

    private val iMelodyControl by lazy { MelodyControl.getInstance(context = this) }
    private val iVibratorControl by lazy { VibratorControl.getInstance(context = this) }

    private val openState = OpenState()

    private val adapter: NoteAdapter by lazy {
        NoteAdapter(ItemListener.Click { _, _ -> openState.tryInvoke { iViewModel.onClickNote() } })
    }

    private val parentContainer: ViewGroup? by lazy {
        findViewById<ViewGroup>(R.id.alarm_parent_container)
    }
    private val rippleContainer: RippleContainer? by lazy {
        findViewById<RippleContainer>(R.id.alarm_ripple_background)
    }

    private val logoView: View? by lazy {
        findViewById<View>(R.id.alarm_logo_view)
    }
    private val recyclerView: RecyclerView? by lazy {
        findViewById<RecyclerView>(R.id.alarm_recycler)
    }

    private val buttonContainer: ViewGroup? by lazy {
        findViewById<ViewGroup>(R.id.alarm_button_container)
    }
    private val disableButton: Button? by lazy {
        findViewById<Button>(R.id.alarm_disable_button)
    }
    private val postponeButton: Button? by lazy {
        findViewById<Button>(R.id.alarm_postpone_button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        iViewModel.onSetup(bundle = savedInstanceState ?: intent.extras)

        parentContainer?.viewTreeObserver?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                parentContainer?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                iViewModel.onStart()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        iViewModel.onDestroy()
        rippleContainer?.stopAnimation()

        iMelodyControl.release()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { iViewModel.onSaveData(bundle = this) })

    override fun setupView(@Theme theme: Int) {
        adapter.theme = theme

        recyclerView?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }

        disableButton?.setOnClickListener { openState.tryInvoke { iViewModel.onClickDisable() } }
        postponeButton?.setOnClickListener { openState.tryInvoke { iViewModel.onClickPostpone() } }
    }

    override fun setupMelody(uri: Uri) = iMelodyControl.setupPlayer(uri, isLooping = true)

    override fun notifyDataSetChanged(noteModel: NoteModel) =
            adapter.notifyDataSetChanged(arrayListOf(noteModel))


    override fun startRippleAnimation(@Theme theme: Int, @ColorInt fillColor: Int) {
        logoView?.let { rippleContainer?.setupAnimation(theme, fillColor, it)?.startAnimation() }
    }

    override fun startButtonFadeInAnimation() {
        parentContainer?.let { group ->
            TransitionManager.beginDelayedTransition(group, Fade().apply {
                startDelay = 500
                duration = 500

                recyclerView?.let { addTarget(it) }
                buttonContainer?.let { addTarget(it) }
            })
        }

        recyclerView?.visibility = View.VISIBLE
        buttonContainer?.visibility = View.VISIBLE
    }


    override fun melodyStart() = iMelodyControl.start()

    override fun melodyStop() = iMelodyControl.stop()

    override fun vibrateStart(pattern: LongArray) = iVibratorControl.start(pattern)

    override fun vibrateCancel() = iVibratorControl.cancel()

    companion object {
        fun getInstance(context: Context, id: Long, @Color color: Int): Intent =
                Intent(context, AlarmActivity::class.java)
                        .putExtra(NoteData.Intent.ID, id)
                        .putExtra(NoteData.Intent.COLOR, color)
    }

}