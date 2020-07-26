package coroutines

import kotlinx.coroutines.*
import java.lang.Runnable

fun startCor(run : Runnable): Job
{
    return GlobalScope.launch (Dispatchers.Default){
        while (isActive) {
            run.run()
        }
    }
}
fun startCorWithDelay(run : Runnable, del: Long): Job
{
    return GlobalScope.launch (Dispatchers.Default){
        while (isActive) {
            run.run()
            delay(del)
        }
    }
}

fun startCorTimeout(run: Runnable, timeout: Long)
{
    GlobalScope.async {
        while (isActive)
            runBlocking {
                startCorTimeout2(run, timeout)

            }
    }
}
private suspend fun startCorTimeout2(run: Runnable, timeout: Long)
{
    withTimeout(timeout)
    {
        run.run()
    }
}