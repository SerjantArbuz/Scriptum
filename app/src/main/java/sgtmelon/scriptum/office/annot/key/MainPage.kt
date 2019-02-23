package sgtmelon.scriptum.office.annot.key

import sgtmelon.scriptum.R

object MainPage {

    val id = intArrayOf(R.id.item_page_rank, R.id.item_page_notes, R.id.item_page_bin)

    fun getById(id: Int): Name = when (id) {
        R.id.item_page_rank -> Name.RANK
        R.id.item_page_notes -> Name.NOTES
        R.id.item_page_bin -> Name.BIN
        else -> throw NoSuchFieldException("Id doesn't match any of page")
    }

    enum class Name { RANK, NOTES, BIN }

}