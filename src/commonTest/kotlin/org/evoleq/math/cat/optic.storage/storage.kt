package org.evoleq.math.cat.optic.storage

import org.evoleq.math.cat.morphism.by
import org.evoleq.math.cat.optic.lens.Lens
import org.evoleq.math.cat.optic.lens.update
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StorageTest {


    @Test fun dsl() {
        var stored: Int = 0
        val storage = Storage read { stored } write {x: Int -> stored = x}

        assertEquals(0, storage.read())

        val value = 5
        storage.write(value)
        assertEquals(value, storage.read())
    }

    @Test fun toLens() {
        var stored: Int = 0
        val storage = Storage read {stored} write {x: Int -> stored = x}

        val lens = storage.toLens()
        val value = 5
        by(lens.update(value))(Unit)
        assertEquals(value, storage.read())

    }

    @Test fun multWithLens() {
        var stored: Int = 0
        val storage = Storage read {stored} write {x: Int -> stored = x}
        val lens = Lens<Int, String>({x -> "$x"}){{s -> s.length}}

        val newStorage = storage * lens

        assertEquals("0", newStorage.read())

        val value = "12345"
        newStorage.write(value)
        assertEquals("${value.length}", newStorage.read())

    }

    @Test fun readOnly() {
        var stored: Int = 0
        val storage = Storage read {stored} write {x: Int -> stored = x}
        val readONlyStorage = storage.readOnly()

        assertEquals(0, readONlyStorage.read())

    }
}