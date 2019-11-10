package sgtmelon.scriptum.repository.rank

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Interface for communicate with [RankRepo]
 */
interface IRankRepo {

    fun insert(name: String): Long

    fun getList(): MutableList<RankItem>

    fun delete(rankEntity: RankEntity)

    fun update(rankEntity: RankEntity)

    fun updatePosition(rankList: List<RankEntity>, noteIdList: List<Long>)

    fun updateConnection(noteModel: NoteModel)

}