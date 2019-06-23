package sgtmelon.scriptum.repository.develop

/**
 * Интерфейс для общения с [DevelopRepo]
 *
 * @author SerjantArbuz
 */
interface IDevelopRepo {

    suspend fun getNoteTableData() : String

    suspend fun getRollTableData() : String

    suspend fun getRankTableData() : String

}