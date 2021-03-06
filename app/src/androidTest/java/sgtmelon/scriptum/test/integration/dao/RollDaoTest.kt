package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.dao.IRollDao
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.data.room.extension.inRoomTest
import sgtmelon.scriptum.data.room.extension.safeDelete
import sgtmelon.scriptum.data.room.extension.safeDeleteByList
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.test.parent.ParentRoomTest
import kotlin.random.Random

/**
 * Integration test for [IRollDao]
 */
@RunWith(AndroidJUnit4::class)
class RollDaoTest : ParentRoomTest() {

    //region Variables

    private data class Model(val entity: NoteEntity, val rollList: List<RollEntity>)

    private fun getRandomRoll(id: Long, position: Int, noteId: Long): RollEntity {
        return RollEntity(id, noteId, position, Random.nextBoolean(), nextString())
    }

    private val firstModel = Model(
        NoteEntity(id = 1, create = DATE_1, change = DATE_2, type = NoteType.ROLL),
        List(size = 20) { getRandomRoll(it.toLong(), it, noteId = 1) }
    )

    private val secondModel = Model(
        NoteEntity(id = 2, create = DATE_3, change = DATE_4, type = NoteType.ROLL),
        List(size = 20) { getRandomRoll((firstModel.rollList.size + it).toLong(), it, noteId = 2) }
    )

    //endregion

    private suspend fun RoomDb.insertRollRelation(model: Model) = with(model) {
        noteDao.insert(entity)
        for (it in rollList.asReversed()) {
            rollDao.insert(it)
        }
    }

    // Dao common functions

    @Test fun insertWithUnique() = inRoomTest {
        insertRollRelation(firstModel)

        with(firstModel) {
            for (it in rollList) {
                assertEquals(RoomDb.UNIQUE_ERROR_ID, rollDao.insert(it))
            }

            assertEquals(rollList, rollDao.get(entity.id))
        }
    }

    @Test fun update() = inRoomTest {
        insertRollRelation(secondModel)

        with(secondModel) {
            rollList[0].copy(position = 4, isCheck = true, text = "00000").let {
                rollDao.update(it.id!!, it.position, it.text)
                rollDao.update(it.id!!, it.isCheck)

                assertTrue(rollDao.get(entity.id).contains(it))
            }
        }
    }

    @Test fun updateCheck() = inRoomTest {
        insertRollRelation(secondModel)

        with(secondModel) {
            rollDao.updateAllCheck(entity.id, check = true)

            val newList = copy().rollList.apply { for (it in this) it.isCheck = true }
            assertEquals(newList, rollDao.get(entity.id))
        }
    }

    @Suppress("DEPRECATION")
    @Test fun deleteAfterSwipe() = inRoomTest {
        insertRollRelation(firstModel)

        val saveList = firstModel.rollList.filter { it.isCheck }
        val id = firstModel.entity.id

        rollDao.delete(id, saveList.map { it.id!! })
        assertEquals(saveList, rollDao.get(id))
    }

    @Suppress("DEPRECATION")
    @Test fun deleteByList() = inRoomTest {
        insertRollRelation(firstModel)

        val rollList = firstModel.rollList
        val id = firstModel.entity.id

        val filterValue = Random.nextBoolean()
        val deleteList = ArrayList(rollList.filter { it.isCheck == filterValue })
        val saveList = ArrayList(rollList).apply { removeAll(deleteList) }

        rollDao.deleteByList(id, deleteList.map { it.id!! })
        assertEquals(saveList, rollDao.get(id))
    }

    @Test fun deleteAll() = inRoomTest {
        insertRollRelation(firstModel)

        val id = firstModel.entity.id
        rollDao.delete(id)
        assertTrue(rollDao.get(id).isEmpty())
    }

    @Test fun deleteCrowd() = inRoomTest {
        val noteId = firstModel.entity.id
        val rollList = List(size = 5000) { getRandomRoll(it.toLong(), it, noteId) }
        val model = firstModel.copy(rollList = rollList)

        insertRollRelation(model)

        val saveList = model.rollList.filter { it.isCheck }

        rollDao.safeDelete(noteId, saveList.map { it.id!! })

        assertEquals(saveList, rollDao.get(noteId))
    }

    @Test fun deleteByListCrowd() = inRoomTest {
        val noteId = firstModel.entity.id
        val rollList = List(size = 5000) { getRandomRoll(it.toLong(), it, noteId) }
        val model = firstModel.copy(rollList = rollList)

        insertRollRelation(model)

        val filterValue = Random.nextBoolean()
        val deleteList = ArrayList(rollList.filter { it.isCheck == filterValue })
        val saveList = ArrayList(rollList).apply { removeAll(deleteList) }

        rollDao.safeDeleteByList(noteId, deleteList.map { it.id!! })

        assertEquals(saveList, rollDao.get(noteId))
    }

    // Dao get functions

    @Test fun get() = inRoomTest {
        insertRollRelation(secondModel)
        insertRollRelation(firstModel)

        val expectedList = mutableListOf<RollEntity>()
        expectedList.addAll(firstModel.rollList)
        expectedList.addAll(secondModel.rollList)

        assertEquals(expectedList, rollDao.get())
    }

    @Test fun getById() = inRoomTest {
        firstModel.let {
            insertRollRelation(it)
            assertEquals(it.rollList, rollDao.get(it.entity.id))
        }

        secondModel.let {
            insertRollRelation(it)
            assertEquals(it.rollList, rollDao.get(it.entity.id))
        }
    }

    @Test fun getByIdList() = inRoomTest {
        insertRollRelation(firstModel)
        insertRollRelation(secondModel)

        val noteIdList = listOf(firstModel.entity.id, secondModel.entity.id)
        val resultList = rollDao.get(noteIdList)

        assertEquals(firstModel.rollList.size + secondModel.rollList.size, resultList.size)
        assertTrue(resultList.containsAll(firstModel.rollList))
        assertTrue(resultList.containsAll(secondModel.rollList))
    }

    @Test fun getByIdListCrowd() = inRoomTest { rollDao.get(crowdList) }

    @Test fun getView() = inRoomTest {
        firstModel.let {
            insertRollRelation(it)
            assertEquals(
                it.rollList.filter { roll -> roll.position < 4 },
                rollDao.getView(it.entity.id)
            )
        }

        secondModel.let {
            insertRollRelation(it)
            assertEquals(
                it.rollList.filter { roll -> roll.position < 4 },
                rollDao.getView(it.entity.id)
            )
        }
    }

    @Test fun getViewHide() = inRoomTest {
        firstModel.let {
            insertRollRelation(it)
            assertEquals(
                it.rollList.filter { roll -> !roll.isCheck }.take(n = 4),
                rollDao.getViewHide(it.entity.id)
            )
        }

        secondModel.let {
            insertRollRelation(it)
            assertEquals(
                it.rollList.filter { roll -> !roll.isCheck }.take(n = 4),
                rollDao.getViewHide(it.entity.id)
            )
        }
    }
}