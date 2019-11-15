package sgtmelon.scriptum.repository.note

import android.content.Context
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.Complete
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.NoteConverter
import sgtmelon.scriptum.room.converter.RollConverter
import sgtmelon.scriptum.room.dao.IRankDao
import sgtmelon.scriptum.room.dao.IRollDao
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Repository of [RoomDb] which work with notes
 *
 * @param context for open [RoomDb]
 */
class NoteRepo(override val context: Context) : INoteRepo, IRoomWork {

    private val noteConverter = NoteConverter()
    private val rollConverter = RollConverter()

    /**
     * [optimisation] - need for note lists where displays short information.
     */
    override fun getList(@Sort sort: Int, bin: Boolean,
                         optimisation: Boolean): MutableList<NoteItem> {
        val itemList = ArrayList<NoteItem>().apply {
            inRoom {
                val rankIdVisibleList = iRankDao.getIdVisibleList()
                var list = when (sort) {
                    Sort.CHANGE -> iNoteDao.getByChange(bin)
                    Sort.CREATE -> iNoteDao.getByCreate(bin)
                    Sort.RANK -> iNoteDao.getByRank(bin)
                    Sort.COLOR -> iNoteDao.getByColor(bin)
                    else -> return@inRoom
                }

                /**
                 * Notes must be showed in list if [bin] != true even if rank not visible.
                 */
                if (!bin) list = list.filter {
                    noteConverter.toItem(it).isVisible(rankIdVisibleList)
                }

                list.forEach {
                    val rollList = rollConverter.toItem(iRollDao.getOptimal(it.id, optimisation))
                    add(noteConverter.toItem(it, rollList, iAlarmDao[it.id]))
                }
            }
        }

        /**
         * If [Sort.RANK] and list have items with rank them move items without rank to list end.
         */
        if (sort == Sort.RANK && itemList.any { it.haveRank() } && itemList.any { !it.haveRank() } ) {
            while(!itemList.first().haveRank()) {
                val item = itemList.first()
                itemList.removeAt(0)
                itemList.add(item)
            }
        }

        return itemList
    }

    /**
     * Return null if note doesn't exist.
     *
     * [optimisation] - need for note lists where displays short information.
     */
    override fun getItem(id: Long, optimisation: Boolean): NoteItem? {
        val item: NoteItem?

        openRoom().apply {
            item = iNoteDao[id]?.let {
                val rollList = rollConverter.toItem(iRollDao.getOptimal(it.id, optimisation))
                return@let noteConverter.toItem(it, rollList, iAlarmDao[id])
            }
        }.close()

        return item
    }

    private fun IRollDao.getOptimal(id: Long, optimisation: Boolean): MutableList<RollEntity> {
        return if (optimisation) getView(id) else get(id)
    }

    /**
     * Return empty list if don't have [RollEntity] for this [noteId]
     */
    override fun getRollList(noteId: Long) = ArrayList<RollItem>().apply {
        inRoom { addAll(rollConverter.toItem(iRollDao[noteId])) }
    }

    /**
     * Have hide notes in list or not.
     */
    override fun isListHide(): Boolean {
        val isListHide: Boolean

        openRoom().apply {
            isListHide = iNoteDao[false].any {
                noteConverter.toItem(it).isNotVisible(iRankDao.getIdVisibleList())
            }
        }.close()

        return isListHide
    }

    override suspend fun clearBin() = inRoom {
        val noteList = iNoteDao[true].apply {
            forEach { iRankDao.clearConnection(it.id, it.rankId) }
        }

        iNoteDao.delete(noteList)
    }


    override suspend fun deleteNote(noteItem: NoteItem) = inRoom {
        iAlarmDao.delete(noteItem.id)
        iNoteDao.update(noteConverter.toEntity(noteItem.delete()))
    }

    override suspend fun restoreNote(noteItem: NoteItem) = inRoom {
        iNoteDao.update(noteConverter.toEntity(noteItem.restore()))
    }

    /**
     * Delete note forever and clear related categories
     */
    override suspend fun clearNote(noteItem: NoteItem) = inRoom {
        iRankDao.clearConnection(noteItem.id, noteItem.rankId)
        iNoteDao.delete(noteConverter.toEntity(noteItem))
    }


    override fun convertToRoll(noteItem: NoteItem) {
        if (noteItem.type != NoteType.TEXT) return

        inRoom {
            noteItem.rollList.clear()

            var p = 0
            noteItem.textToList().forEach {
                noteItem.rollList.add(rollConverter.toItem(RollEntity().apply {
                    noteId = noteItem.id
                    position = p++
                    text = it
                    id = iRollDao.insert(rollEntity = this)
                }))
            }

            noteItem.convert().updateComplete(Complete.EMPTY)

            iNoteDao.update(noteConverter.toEntity(noteItem))
        }
    }

    override fun convertToText(noteItem: NoteItem) {
        if (noteItem.type != NoteType.ROLL) return

        inRoom {
            noteItem.rollList.clear()
            noteItem.convert().text = rollConverter.toItem(iRollDao[noteItem.id]).getText()

            iNoteDao.update(noteConverter.toEntity(noteItem))
            iRollDao.delete(noteItem.id)
        }
    }

    override suspend fun getCopyText(noteItem: NoteItem) = StringBuilder().apply {
        if (noteItem.name.isNotEmpty()) {
            append(noteItem.name).append("\n")
        }

        when (noteItem.type) {
            NoteType.TEXT -> append(noteItem.text)
            NoteType.ROLL -> inRoom {
                append(rollConverter.toItem(iRollDao[noteItem.id]).getText())
            }
        }
    }.toString()

    override fun saveTextNote(noteItem: NoteItem, isCreate: Boolean) {
        if (noteItem.type != NoteType.TEXT) return

        inRoom {
            val entity = noteConverter.toEntity(noteItem)

            if (isCreate) {
                noteItem.id = iNoteDao.insert(entity)
            } else {
                iNoteDao.update(entity)
            }
        }
    }

    override fun saveRollNote(noteItem: NoteItem, isCreate: Boolean) {
        if (noteItem.type != NoteType.ROLL) return

        noteItem.rollList.removeAll { it.text.isEmpty() }

        inRoom {
            val noteEntity = noteConverter.toEntity(noteItem)

            if (isCreate) {
                noteItem.id = iNoteDao.insert(noteEntity)

                /**
                 * Write roll to db
                 */
                noteItem.rollList.forEachIndexed { i, item ->
                    val rollEntity = rollConverter.toEntity(noteItem.id, item)
                    item.apply { position = i }.id = iRollDao.insert(rollEntity)
                }
            } else {
                iNoteDao.update(noteEntity)

                /**
                 * List of roll id's, which wasn't swiped
                 */
                val idSaveList = ArrayList<Long>()

                noteItem.rollList.forEachIndexed { i, item ->
                    item.position = i

                    val id = item.id
                    if (id == null) {
                        val rollEntity = rollConverter.toEntity(noteItem.id, item)
                        item.id = iRollDao.insert(rollEntity)
                    } else {
                        iRollDao.update(id, i, item.text)
                    }

                    item.id?.let { idSaveList.add(it) }
                }

                /**
                 * Remove swiped rolls
                 */
                iRollDao.delete(noteItem.id, idSaveList)
            }
        }
    }

    override fun updateRollCheck(noteItem: NoteItem, rollItem: RollItem) {
        val rollId = rollItem.id ?: return

        inRoom {
            iRollDao.update(rollId, rollItem.isCheck)
            iNoteDao.update(noteConverter.toEntity(noteItem))
        }
    }

    override fun updateRollCheck(noteItem: NoteItem, check: Boolean) = inRoom {
        iRollDao.updateAllCheck(noteItem.id, check)
        iNoteDao.update(noteConverter.toEntity(noteItem))
    }

    override fun updateNote(noteItem: NoteItem) = inRoom {
        iNoteDao.update(noteConverter.toEntity(noteItem))
    }


    /**
     * Remove relation between [RankEntity] and [NoteItem] which will be delete
     */
    private fun IRankDao.clearConnection(noteId: Long, rankId: Long) {
        val rankEntity = get(rankId)?.apply {
            this.noteId.remove(noteId)
        } ?: return

        update(rankEntity)
    }

}