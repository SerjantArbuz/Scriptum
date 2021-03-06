package sgtmelon.scriptum.test.ui.auto.screen.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test keyboard ime click for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankImeTest : ParentUiTest() {

    @Test fun toolbarImeAdd() = launch({ data.fillRank() }) {
        val name = nextString()

        mainScreen {
            rankScreen {
                /**
                 * Check ime action with wrong input data. (Empty name)
                 */
                toolbar {
                    onEnterName(name = " ", isEnabled = false).onImeOptionClick(isSuccess = false)
                }

                toolbar { onEnterName(name).onImeOptionClick() }
                openRenameDialog(name, p = count - 1) { onCloseSoft() }

                /**
                 * Check ime action with wrong input data. (Name from list)
                 */
                toolbar {
                    onEnterName(name, isEnabled = false).onImeOptionClick(isSuccess = false)
                }

                onClickCancel(p = count - 1)

                toolbar { onEnterName(name).onImeOptionClick() }
                openRenameDialog(name, p = count - 1)
            }
        }
    }

    @Test fun renameImeResult() = data.insertRank().let {
        val newName = nextString()

        launch {
            mainScreen {
                rankScreen {
                    /**
                     * Check ime action with wrong input data. (Empty name)
                     */
                    openRenameDialog(it.name) {
                        onEnter(name = " ", isEnabled = false).onImeOptionClick(isSuccess = false)
                        onCloseSoft()
                    }

                    openRenameDialog(it.name) { onEnter(newName).onImeOptionClick() }

                    it.name = newName
                    onAssertItem(it)
                }
            }
        }
    }

}