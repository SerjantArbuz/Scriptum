package sgtmelon.scriptum.data.room.dao

import androidx.room.*
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.domain.model.data.DbData

/**
 * Interface for communication [DbData.Note.TABLE] with [RoomDb].
 */
@Dao
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
interface INoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(noteEntity: NoteEntity): Long

    @Delete
    suspend fun delete(noteEntity: NoteEntity)

    @Delete
    suspend fun delete(list: List<NoteEntity>)

    @Update
    suspend fun update(noteEntity: NoteEntity)

    @Update
    suspend fun update(list: List<NoteEntity>)

    @Query(value = """SELECT COUNT(NT_ID) FROM NOTE_TABLE
        WHERE NT_BIN = :bin AND (NT_RANK_ID = -1 OR NT_RANK_ID IN (:rankIdList))
    """)
    suspend fun getCount(bin: Boolean, rankIdList: List<Long>): Int

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID = :id")
    suspend fun get(id: Long): NoteEntity?

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID IN (:idList)")
    suspend fun get(idList: List<Long>): List<NoteEntity>

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_BIN = :bin")
    suspend fun get(bin: Boolean): List<NoteEntity>


    @Query(value = """SELECT * FROM NOTE_TABLE
        WHERE NT_BIN = :bin
        ORDER BY DATE(NT_CHANGE) DESC, TIME(NT_CHANGE) DESC
    """)
    suspend fun getByChange(bin: Boolean): List<NoteEntity>

    @Query(value = """SELECT * FROM NOTE_TABLE
        WHERE NT_BIN = :bin
        ORDER BY DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC
    """)
    suspend fun getByCreate(bin: Boolean): List<NoteEntity>

    @Query(value = """SELECT * FROM NOTE_TABLE
        WHERE NT_BIN = :bin
        ORDER BY NT_RANK_PS ASC, DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC
    """)
    suspend fun getByRank(bin: Boolean): List<NoteEntity>

    @Query(value = """SELECT * FROM NOTE_TABLE
        WHERE NT_BIN = :bin
        ORDER BY NT_COLOR ASC, DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC
    """)
    suspend fun getByColor(bin: Boolean): List<NoteEntity>

}