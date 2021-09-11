package com.leinardi.androidtemplateproject.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    val counterLiveData: LiveData<Int>
        get() = counter

    private val counter = MutableLiveData<Int>()
    private var count = 0

    fun increaseCounter() {
        Timber.d("increaseCounter")
        counter.value = ++count
    }

}
