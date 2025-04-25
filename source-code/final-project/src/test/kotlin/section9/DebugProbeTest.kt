package section9

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.debug.DebugProbes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DebugProbeTest {

    @Test
    fun `dump coroutines with DebugProbes`() {
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
}