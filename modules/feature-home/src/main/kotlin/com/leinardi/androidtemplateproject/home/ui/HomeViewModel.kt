package com.leinardi.androidtemplateproject.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val counterLiveData: LiveData<Int>
        get() = counter

    private val counter = MutableLiveData<Int>()
    private var count = savedStateHandle.get<Int>(SAVED_COUNT) ?: 0

    fun increaseCounter() {
        Timber.d("increaseCounter")
        counter.value = ++count
        savedStateHandle.set(SAVED_COUNT, count)
    }

    companion object {
        private const val SAVED_COUNT = "saved_count"
    }

}
