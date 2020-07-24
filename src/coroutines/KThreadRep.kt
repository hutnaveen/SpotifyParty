package coroutines

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


fun startCor(run : Runnable): Deferred<Unit>
{
    return GlobalScope.async() {
        run.run()
    }
}
