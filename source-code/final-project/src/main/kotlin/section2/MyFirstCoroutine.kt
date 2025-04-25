package org.example.section2
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.log

/*fun main(): Unit = runBlocking {
    log("main          ")

    runBlocking {
        log("runBlocking launched        ")
        delay(1000)                         // <--- add delay
        log("runBlocking finished")
    }

    log("program completes")

    /*log("main runBlocking    ")

    runBlocking {
        log("runBlocking started ")
        cancel()
        val result = 1 + 2 + 3
        log("runBlocking finished. Result: $result")
    }
    log("program completes")*/
}**/

fun main() = runBlocking  {
    log("main                           ")

    runBlocking {
        log("runBlocking launched           ")
        delay(2000)
        val result = 1 + 2 + 3                                              // <-- add here
        log ("runBlocking finished. Result: $result")     // <-- add here
    }
    log("program completes              ")
}
