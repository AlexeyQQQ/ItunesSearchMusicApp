package ru.easycode.intensive2itunessearch.core.presentation

interface UpdateUi<T : Any> {
    fun updateUiState(uiState: T)
}

interface UpdateObserver<T : Any> {
    fun updateObserver(observer: UpdateUi<T>)
    fun clearObserver()
    fun clear()
}

interface UiObservable<T : Any> : UpdateObserver<T>, UpdateUi<T> {

    abstract class Abstract<T : Any> : UiObservable<T> {

        protected var observer: (UpdateUi<T>)? = null
        protected var cache: T? = null

        override fun updateUiState(uiState: T) {
            this.cache = uiState
            if (observer != null) {
                observer!!.updateUiState(uiState)
            }
        }

        override fun updateObserver(observer: UpdateUi<T>) {
            this.observer = observer
            if (cache != null) {
                this.observer!!.updateUiState(cache!!)
            }
        }

        override fun clearObserver() {
            observer = null
        }

        override fun clear() {
            cache = null
        }
    }
}