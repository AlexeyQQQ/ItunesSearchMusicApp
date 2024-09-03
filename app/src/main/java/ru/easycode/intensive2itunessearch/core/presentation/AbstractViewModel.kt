package ru.easycode.intensive2itunessearch.core.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class AbstractViewModel<U : Any, X : UiObservable<U>>(
    private val runAsync: RunAsync,
    protected val uiObservable: X
) : ViewModel() {

    private val viewModelScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    protected fun <T : Any> runAsync(background: suspend () -> T, ui: (T) -> Unit) {
        runAsync.runAsync(viewModelScope, background, ui)
    }

    protected fun launchUi(function: suspend () -> Unit) {
        runAsync.launchUi(viewModelScope, function)
    }

    fun startGettingUpdates(observer: UpdateUi<U>) {
        uiObservable.updateObserver(observer)
    }

    fun stopGettingUpdates() {
        uiObservable.clearObserver()
    }
}

interface RunAsync {

    fun <T : Any> runAsync(
        coroutineScope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit
    )

    fun launchUi(
        coroutineScope: CoroutineScope,
        function: suspend () -> Unit
    )

    class Delay(
        private val delayInMillis: Long = 300,
    ) : RunAsync {

        private var job: Job? = null

        override fun <T : Any> runAsync(
            coroutineScope: CoroutineScope,
            background: suspend () -> T,
            ui: (T) -> Unit
        ) {
            job?.cancel()
            job = coroutineScope.launch(Dispatchers.IO) {
                delay(delayInMillis)
                val result: T = background.invoke()
                withContext(Dispatchers.Main) {
                    ui.invoke(result)
                }
            }
        }

        override fun launchUi(
            coroutineScope: CoroutineScope,
            function: suspend () -> Unit
        ) {
            coroutineScope.launch {
                function.invoke()
            }
        }
    }

    class Base : RunAsync {

        override fun <T : Any> runAsync(
            coroutineScope: CoroutineScope,
            background: suspend () -> T,
            ui: (T) -> Unit
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                val result: T = background.invoke()
                withContext(Dispatchers.Main) {
                    ui.invoke(result)
                }
            }
        }

        override fun launchUi(
            coroutineScope: CoroutineScope,
            function: suspend () -> Unit
        ) {
            coroutineScope.launch {
                function.invoke()
            }
        }
    }
}