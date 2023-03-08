package org.evoleq.math.cat.optic.storage

import org.evoleq.math.cat.adt.Either
import org.evoleq.math.cat.adt.Left
import org.evoleq.math.cat.adt.Right
import org.evoleq.math.cat.marker.MathCatDsl
import org.evoleq.math.cat.morphism.by
import org.evoleq.math.cat.morphism.id
import org.evoleq.math.cat.optic.lens.Lens
import org.evoleq.math.cat.optic.lens.update
import org.evoleq.math.cat.optic.lens.view
import org.evoleq.math.cat.optic.lens.x as xL
import org.evoleq.math.cat.optic.prism.Prism

@MathCatDsl
infix fun <T> (()->T).write(value: (t:T)->Unit): Storage<T> = Storage(this,value)

@MathCatDsl
fun <T> Storage<T>.update(update: T.()->T) = write(update(read()))

@MathCatDsl
fun <T> Storage<T>.readOnly(): ReadOnlyStorage<T> = Storage(read)

@MathCatDsl
fun <T> Storage<T>.toLens(): Lens<Unit, T> = Lens(
    view = {read()},
    update = {write}
)

@MathCatDsl
fun <T> Lens<Unit, T>.toStorage(): Storage<T> = Storage(
    { by(view())(Unit)},
    {t:T -> by(update(t))(Unit)}
)

@MathCatDsl
infix fun <F, S> Storage<F>.x(second: Storage<S>): Storage<Pair<F, S>> = Storage(
    read = {Pair(read() , second.read())},
    write = {
        write(it.first)
        second.write(it.second)
    }
)

@MathCatDsl
inline fun <reified T> Storage<List<T>>.toListPrism(): Prism<Int, Pair<Int, T>, Exception, Pair<Int, T>> = Prism(
    match = { index ->
        try {
            Right(index to read()[index]!!)
        } catch (exception: Exception) {
            Left(exception)
        }
    },
    build = {pair -> with(read()){
        val before = dropLastWhile { indexOf(it) >= pair.first }.toTypedArray()
        val after = drop(pair.first -1).toTypedArray()
        val newList = listOf(
            *before,
            pair.second,
            *after
        )
        write(newList)
        newList.indexOf(pair.second) to pair.second
    } }
)

@MathCatDsl
fun <K : Any, V> Storage<Map<K, V>>.toMapPrism(): Prism<K, V, Exception, Pair<K, V>> = Prism(
    match = { key ->
        try {
            Right(read()[key]!!)
        } catch(exception: Exception) {
            Left(exception)
        }
    },
    build = { pair ->
        val (key, value) = pair
        write(with(read()){mapOf(
            *entries.map{it.key to it.value}.toTypedArray(),
            key to value
        )})
        value
    }
)

@MathCatDsl
fun <T> Storage<T?>.toNullablePrism(): Prism<Unit, T, Unit, T> = Prism(
    match = {when(val read = read()){
        null -> Left(Unit)
        else -> Right(read)
    } },
    {t -> write(t); t}
)

@MathCatDsl
fun <T> Storage<Iterable<T>>.forEach(action: T.()->Unit) = read().forEach(action)