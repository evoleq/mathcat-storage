package org.evoleq.math.cat.optic.storage

import org.evoleq.math.cat.optic.lens.Lens
import org.evoleq.math.cat.optic.lens.times


operator fun <S, T> Storage<S>.times(lens: Lens<S, T>): Storage<T> = (toLens().times(lens)).toStorage()

operator fun <S, T> ReadOnlyStorage<S>.times(lens: Lens<S, T>): ReadOnlyStorage<T> = (Storage(read){}.toLens() * lens).toStorage().readOnly()