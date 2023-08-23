package cz.nejakejtomas.bluemapminimap

import kotlinx.coroutines.*
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

abstract class LoopDispatcher(private val fastRun: Boolean = true) : ExecutorCoroutineDispatcher(), Executor {
    private val dispatcher = this.asCoroutineDispatcher()
    private val queue = mutableListOf<Runnable>()

    override val executor: Executor
        get() = this

    override fun close() {
        error("Cannot be invoked on LoopScheduler")
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatcher.dispatch(context, block)
    }

    override fun execute(command: Runnable) {
        synchronized(this) {
            queue.add(command)
        }
    }

    protected fun run() {
        if (fastRun) {
            synchronized(this) {
                queue.forEach {
                    it.run()
                }
                queue.clear()
            }
        }
        else {
            while (queue.isNotEmpty()) {
                synchronized(this) {
                    if (queue.isEmpty()) return

                    val current = queue.removeLast()
                    current.run()
                }
            }
        }
    }
}