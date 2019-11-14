package sgtmelon.scriptum.repository.bind

/**
 * Interface for communicate with [BindRepo]
 */
interface IBindRepo {

    fun unbindNote(id: Long): Boolean

    fun getNotificationCount(): Int

}