package ru.easycode.intensive2itunessearch.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import ru.easycode.intensive2itunessearch.core.di.ProvideViewModel

abstract class AbstractFragment<U : Any, B : ViewBinding, X : UiObservable<U>, VM : AbstractViewModel<U, X>>(
    private val vmClass: Class<VM>,
) : Fragment(), UpdateUi<U> {

    protected abstract fun initBinding(layoutInflater: LayoutInflater, container: ViewGroup?): B

    private var _binding: B? = null
    protected val binding get() = _binding!!

    protected lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = initBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ProvideViewModel).viewModel(vmClass)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(observer = this)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}