package sgtmelon.scriptum.app.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.room.converter.BoolConverter

/**
 * Класс для общения Dao списка [RoomDb]
 */
@Dao
@TypeConverters(BoolConverter::class)
interface RollDao {

    @Insert fun insert(rollItem: RollItem): Long

    @Query(value = "SELECT * FROM ROLL_TABLE ORDER BY RL_NOTE_ID ASC, RL_POSITION ASC")
    fun get(): List<RollItem>

    @Query(value = "SELECT * FROM ROLL_TABLE WHERE RL_NOTE_ID = :idNote ORDER BY RL_POSITION")
    fun get(idNote: Long): MutableList<RollItem>

    /**
     * Получение списка всех пунктов с позиции 0 по 3 (4 пунка)
     */
    @Query(value = """SELECT * FROM ROLL_TABLE
            WHERE RL_NOTE_ID = :idNote AND RL_POSITION BETWEEN 0 AND 3
            ORDER BY RL_POSITION ASC""")
    fun getView(idNote: Long): MutableList<RollItem>

    @Query(value = "UPDATE ROLL_TABLE SET RL_POSITION = :position, RL_TEXT = :text WHERE RL_ID = :id")
    fun update(id: Long, position: Int, text: String)

    /**
     * Обновление выполнения конкретного пункта
     *
     * @param id    - Id пункта
     * @param check - Состояние отметки
     */
    @Query(value = "UPDATE ROLL_TABLE SET RL_CHECK = :check WHERE RL_ID = :id")
    fun update(id: Long, check: Boolean)

    /**
     * Обновление выполнения для всех пунктов
     *
     * @param id - Id заметки
     * @param check  - Состояние отметки
     */
    @Query(value = "UPDATE ROLL_TABLE SET RL_CHECK = :check WHERE RL_NOTE_ID = :id")
    fun updateAllCheck(id: Long, check: Boolean)

    /**
     * Удаление пунктов при сохранении после свайпа
     *
     * @param idNote - Id заметки
     * @param idSave - Id, которые остались в заметке
     */
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :idNote AND RL_ID NOT IN (:idSave)")
    fun delete(idNote: Long, idSave: List<Long>)

    /**
     * @param idNote - Id удаляемой заметки
     */
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :idNote")
    fun delete(idNote: Long)

}