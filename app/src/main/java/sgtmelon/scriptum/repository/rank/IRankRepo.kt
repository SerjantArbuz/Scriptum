package sgtmelon.scriptum.repository.rank

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem

/**
 * Interface for communicate with [RankRepo]
 */
interface IRankRepo {

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