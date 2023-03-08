package org.evoleq.math.cat.optic.storage

import org.evoleq.math.cat.marker.MathCatDsl
import org.evoleq.math.cat.morphism.by
import org.evoleq.math.cat.optic.lens.Lens
import org.evoleq.math.cat.optic.lens.times
import org.evoleq.math.cat.optic.lens.update
import org.evoleq.math.cat.optic.lens.view

interface ReadOnlyStorage<T> {
    @MathCatDsl
    val read: ()->T
}

interface Storage<T>: ReadOnlyStorage<T> {
    @MathCatDsl
    override val read: () -> T
    @MathCatDsl
    val write: (t: T) -> Unit
    companion object {
        @MathCatDsl
        infix fun <T> read(value: ()->T): ()->T = value
    }

}

@MathCatDsl
@Suppress("FunctionName")
fun <T> Storage(read: ()->T, write: (T)->Unit): Storage<T> = object : Storage<T> {
    override val read: () -> T
        get() = read
    override val write: (t: T) -> Unit
        get() = write
}

@MathCatDsl
@Suppress("FunctionName")
fun <T> Storage(read: ()->T): ReadOnlyStorage<T> = object : ReadOnlyStorage<T> {
    override val read: () -> T
        get() = read
}

