package sgtmelon.scriptum.extension

import sgtmelon.scriptum.model.item.RollItem

fun <T> MutableList<T>.removeAtOrNull(index: Int): T? {
    return if (index in 0..lastIndex) removeAt(index) else null
}

private const val ND_INDEX = -1

fun <T> List<T>.correctIndexOf(item: T): Int? {
    return indexOf(item).takeIf { it != ND_INDEX }
}

fun <T> List<T>.correctIndexOfFirst(predicate: (T) -> Boolean): Int? {
    return indexOfFirst(predicate).takeIf { it != ND_INDEX }
}


/**
 * Move item by positions. If [to] is not indicated - move to last position.
 */
fun <T> MutableList<T>.move(from: Int, to: Int = -1) {
    val item = removeAt(from)

    if (to == -1) add(item) else add(to, item)
}

fun <T> MutableList<T>.clearAddAll(replace: List<T>) = apply {
    clear()
    addAll(replace)
}

fun List<RollItem>.getText(): String = joinToString(separator = "\n") { it.text }