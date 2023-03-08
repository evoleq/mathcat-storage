package org.evoleq.math.cat.optic.storage

import org.evoleq.math.cat.adt.Left
import org.evoleq.math.cat.adt.Right
import org.evoleq.math.cat.adt.map2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StorageDslTest {

    @Test
    fun listStorageToPrism() {
        var stored = listOf<String>(
            "item_0",
            "item_1",
            "item_2",
            "item_3",
            "item_4"
        )
        val storage = Storage(
            read = { stored },
            write = { stored = it }
        )

        val prism = storage.toListPrism()
        val value = "item"
        val index = 1

        val result = prism.build(index to value)
        assertEquals(1, result.first)

        val found = prism.match(1)
        assertEquals(Right(1 to "item"),found)

        val notFound1 = prism.match(-1)
        assertTrue(notFound1 is Left)

        val notFound2 = prism.match(10)
        assertTrue(notFound2 is Left)

        (0..5).forEach {
            assertTrue { prism.match(it) is Right }
        }
    }

    @Test
    fun mapStorageToPrism() {
        var stored = mapOf<Int, String>(
            0 to "zero"
        )
        val storage = Storage(
            read = {stored},
            write = {stored = it}
        )

        val prism = storage.toMapPrism()
        prism.build(1 to "one")
        val one = prism.match(1)
        assertTrue(one is Right)
        one.map2{ assertEquals("one", it) }

        val exception = prism.match(-1)
        assertTrue(exception is Left)
    }
}