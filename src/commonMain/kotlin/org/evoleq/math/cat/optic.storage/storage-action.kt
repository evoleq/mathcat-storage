package org.evoleq.math.cat.optic.storage

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.evoleq.math.cat.marker.MathCatDsl

typealias Action<Data> = suspend Storage<Data>.(oldValue: Data, newValue: Data) -> Unit

@MathCatDsl
infix fun <Data> Storage<Data>.onChange(
    action: Action<Data>
) = Storage(
    read = read
) { newData ->
    if(read() != newData) {
        val oldValue = read()
        write(newData)
        CoroutineScope(Job()).launch {
            this@onChange.action(oldValue, newData)
        }
    }
}

@MathCatDsl
infix fun <Data> Storage<Data>.onWrite(
    action: Action<Data> //suspend Storage<Data>.(oldValue: Data, newValue: Data) -> Unit
) = Storage(
    read = read

) { newData ->
        val oldValue = read()
    //    write(newData)
        CoroutineScope(Job()).launch {
            // try {
            this@onWrite.action(oldValue, newData)
            //} catch(exception: Exception) {
            //    this@onChange.write(oldValue)
            //}
        }



}