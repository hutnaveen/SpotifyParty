package coroutines

import kotlinx.coroutines.*
import java.lang.Runnable

fun startInfCor(run : Runnable): Job
{
    return GlobalScope.launch (Dispatchers.Default){
        while (isActive) {
            run.run()
        }
    }
}

fun startCor(run : Runnable): Job
{
    return GlobalScope.launch (Dispatchers.Default){
            run.run()
    }
}
