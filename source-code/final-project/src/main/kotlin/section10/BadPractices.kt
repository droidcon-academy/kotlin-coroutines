package org.example.section9

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.debug.DebugProbes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.log
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
fun main() {

    DebugProbes.install()
    val scope = CoroutineScope(Job())

    val deferred = scope.async {
        // does not break yet
        repeat(5) {
            launch(Dispatchers.IO) {
                1 + 2 + 3
                delay(100)
            }
        }
        DebugProbes.dumpCoroutines()
        "Done"
    }

    println(deferred)
}

suspend fun scopeCheck(parentScope: CoroutineScope, scope: CoroutineScope) {
    println("\nparent scope: $parentScope | context: ${parentScope.coroutineContext}")
    println("current scope: $scope | context: $coroutineContext")
    println("is the same scope? ${parentScope === scope}\n")
}

@OptIn(DelicateCoroutinesApi::class)
fun CoroutineScope.badGlobalScope() {
    GlobalScope.launch {
        delay(10)
        log("work!" )
    }
    log("Done")
}
