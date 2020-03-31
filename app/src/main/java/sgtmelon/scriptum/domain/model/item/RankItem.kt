package sgtmelon.scriptum.domain.model.item

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import sgtmelon.scriptum.data.room.converter.model.StringConverter
import sgtmelon.scriptum.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.domain.model.data.DbData.Rank
import sgtmelon.scriptum.domain.model.data.DbData.Rank.Default
import sgtmelon.scriptum.presentation.adapter.RankAdapter
import kotlin.random.Random

/**
 * Model for store short information about rank, use in [RankAdapter]
 */
@TypeConverters(BoolConverter::class, StringConverter::class)
data class RankItem(
        @ColumnInfo(name = Rank.ID) val id: Long,
        @ColumnInfo(name = Rank.NOTE_ID) val noteId: MutableList<Long> = Default.NOTE_ID,
        @ColumnInfo(name = Rank.POSITION) var position: Int = Default.POSITION,
        @ColumnInfo(name = Rank.NAME) var name: String,
        @ColumnInfo(name = Rank.VISIBLE) var isVisible: Boolean = Default.VISIBLE
) {

    var hasBind: Boolean = Random.nextBoolean()
    var hasNotification: Boolean = Random.nextBoolean()

    fun switchVisible() = apply { isVisible = !isVisible }

}