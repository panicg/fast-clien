package com.panicdev.fast_clien.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    /**
     * 1 : showProgress with Dim
     * 2 : showProgress without Dim
     * 3 : hideProgress
     */
    private val _progress = MutableLiveData<Pair<Int, Boolean?>>()
    val progress: LiveData<Pair<Int, Boolean?>>
        get() = _progress

    protected val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    protected val _alert = MutableLiveData<String>()
    val alert: LiveData<String>
        get() = _alert

    protected val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    //토큰만료
    protected val _tokenExpire = MutableLiveData<Boolean>()
    val tokenExpire: LiveData<Boolean>
        get() = _tokenExpire

    //로그인이 필요한 서비스
    val _needSignIn = MutableLiveData<Boolean>()
    val needSignIn: LiveData<Boolean>
        get() = _needSignIn

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun showProgress(isDim: Boolean = true, widthFadeIn : Boolean = false) {
        _progress.postValue(Pair(if (isDim) 1 else 2, widthFadeIn))
    }

    fun hideProgress() {
        _progress.postValue(Pair(3, null))
    }

    protected fun throwError(`throw`: Throwable) {
        `throw`.printStackTrace()
        _error.postValue(`throw`.message)
    }

    protected fun errorMessage(message: String) {
        Log.e("BaseViewModel Error", message)
        _error.postValue(message)
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }


}