package sgtmelon.scriptum.repository.room.callback

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.repository.room.RankRepo

/**
 * Interface for communicate with [RankRepo]
 */
interface IRankRepo {

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<RankItem>

    suspend fun getIdVisibleList(): List<Long>


    suspend fun insert(name: String): Long

    suspend fun delete(rankItem: RankItem)

    suspend fun update(rankItem: RankItem)

    suspend fun update(rankList: List<RankItem>)

    suspend fun updatePosition(rankList: List<RankItem>, noteIdList: List<Long>)

    suspend fun updateConnection(noteItem: NoteItem)


    suspend fun getDialogItemArray(): Array<String>

    suspend fun getId(position: Int): Long

}