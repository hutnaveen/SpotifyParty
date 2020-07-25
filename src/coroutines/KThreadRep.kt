package coroutines

import kotlinx.coroutines.*
import java.lang.Runnable


fun startCor(run : Runnable): Job
{
    return GlobalScope.launch (Dispatchers.Default){
           while (isActive)
               run.run()
    }
}
