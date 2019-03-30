package sgtmelon.scriptum.app.model

import sgtmelon.scriptum.app.model.item.RankItem

/**
 * Репозиторий категории
 *
 * @author SerjantArbuz
 * @version 1.0
 */
class RankModel(val itemList: MutableList<RankItem>) {

    // TODO rename потому что по факту это репозиторий

    val nameList: MutableList<String>

    init {
        nameList = ArrayList<String>().apply { itemList.forEach { add(it.name.toUpperCase()) } }
    }

    fun size(): Int = itemList.size

    fun set(p: Int, name: String) {
        itemList[p].name = name
        nameList[p] = name.toUpperCase()
    }

    fun add(p: Int, item: RankItem) {
        itemList.add(p, item)
        nameList.add(p, item.name.toUpperCase())
    }

    fun remove(p: Int) {
        itemList.removeAt(p)
        nameList.removeAt(p)
    }

    fun move(from: Int, to: Int) {
        val item = itemList[from]

        remove(from)
        add(to, item)
    }

}