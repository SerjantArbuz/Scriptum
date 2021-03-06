package sgtmelon.scriptum.test.ui.auto.screen.preference.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IThemeTest
import sgtmelon.scriptum.ui.dialog.preference.ThemeDialogUi

/**
 * Test for [PreferenceFragment] and [ThemeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class PreferenceThemeTest : ParentUiTest(),
    IPreferenceTest,
    IThemeTest {

    @Test fun dialogClose() = runTest({ preferenceRepo.theme = Theme.LIGHT }) {
        openThemeDialog { onClickCancel() }
        assert()
        openThemeDialog { onCloseSoft() }
        assert()
    }

    @Test override fun themeLight() = super.themeLight()

    @Test override fun themeDark() = super.themeDark()

    @Test override fun themeSystem() = super.themeSystem()

    override fun startTest(@Theme value: Int) {
        val initValue = switchValue(value)

        assertNotEquals(initValue, value)

        runTest {
            openThemeDialog {
                onClickItem(value).onClickItem(initValue).onClickItem(value).onClickApply()
            }
            assert()
        }

        assertEquals(value, preferenceRepo.theme)
    }

    /**
     * Switch [Theme] to another one.
     */
    @Theme private fun switchValue(@Theme value: Int): Int {
        val list = Theme.list
        var initValue: Int

        do {
            initValue = list.random()
            preferenceRepo.theme = initValue
        } while (preferenceRepo.theme == value)

        return initValue
    }
}