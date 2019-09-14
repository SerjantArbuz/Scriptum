package sgtmelon.scriptum.screen.ui.intro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import sgtmelon.idling.AppIdlingResource
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.PagerAdapter
import sgtmelon.scriptum.extension.beforeFinish
import sgtmelon.scriptum.model.data.IntroData
import sgtmelon.scriptum.screen.ui.main.MainActivity

/**
 * Экран со вступительным туториалом
 */
class IntroActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private val pagerAdapter = PagerAdapter(supportFragmentManager)

    private val pageIndicator by lazy { findViewById<View?>(R.id.intro_page_indicator) }
    private val pageButtonEnd by lazy { findViewById<Button?>(R.id.intro_end_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        setupViewPager()
    }

    private fun setupViewPager() {
        pageButtonEnd?.apply {
            alpha = 0f
            isEnabled = false

            setOnClickListener {
                AppIdlingResource.worker.startHardWork()
                beforeFinish { startActivity(MainActivity[this@IntroActivity]) }
            }
        }

        for (i in 0 until IntroData.count) pagerAdapter.addItem(IntroFragment[i])

        findViewById<ViewPager>(R.id.intro_pager).apply {
            adapter = pagerAdapter
            offscreenPageLimit = pagerAdapter.count - 1
            addOnPageChangeListener(this@IntroActivity)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        pagerAdapter.notifyItem(position, 1 - positionOffset)

        if (position != pagerAdapter.count - 1) {
            pagerAdapter.notifyItem(position + 1, positionOffset)
        }

        pageButtonEnd?.isEnabled = position == pagerAdapter.count - 1

        if (position == pagerAdapter.count - 2) {
            pageIndicator?.apply {
                alpha = 1 - 2 * positionOffset
                translationY = -positionOffset * height
            }

            pageButtonEnd?.apply {
                alpha = positionOffset
                translationY = height - positionOffset * height
            }
        }
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    companion object {
        operator fun get(context: Context): Intent = Intent(context, IntroActivity::class.java)
    }

}