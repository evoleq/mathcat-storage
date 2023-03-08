package org.evoleq.math.cat.optic.storage

import kotlin.test.Test
import kotlin.test.assertEquals

class StorageActionTest {
    @Test fun onChange() {
        var stored: Int = 0
        val storage = Storage read { stored } write {x: Int -> stored = x} onChange { o,n ->
            println("changed $o -> $n")
            write(4)
        }
        storage.write(1)
        assertEquals(4, storage.read())
        println(storage.read())
    }
}