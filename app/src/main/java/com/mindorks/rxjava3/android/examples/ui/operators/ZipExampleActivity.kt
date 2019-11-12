package com.mindorks.rxjava3.android.examples.ui.operators

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mindorks.rxjava3.android.examples.R
import com.mindorks.rxjava3.android.examples.model.User
import com.mindorks.rxjava3.android.examples.utils.AppConstant
import com.mindorks.rxjava3.android.examples.utils.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers

class ZipExampleActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ZipExampleActivity"
    }

    private lateinit var btn: Button
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        btn = findViewById(R.id.btn)
        textView = findViewById(R.id.textView)

        btn.setOnClickListener {
            doSomeWork()
        }
    }

    /*
     * Here we are getting two user list
     * One, the list of cricket fans
     * Another one, the list of football fans
     * Then we are finding the list of users who loves both
     */
    private fun doSomeWork() {
        Observable.zip(getCricketFansObservable(), getFootballFansObservable(),
            BiFunction<List<User>, List<User>, List<User>> { cricketFans, footballFans ->
                return@BiFunction Utils.filterUserWhoLovesBoth(cricketFans, footballFans)
            })
            // Run on a background thread
            .subscribeOn(Schedulers.io())
            // Be notified on the main thread
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    private fun getCricketFansObservable(): Observable<List<User>> {
        return Observable.create(ObservableOnSubscribe<List<User>> { e ->
            if (!e.isDisposed) {
                e.onNext(Utils.getUserListWhoLovesCricket())
                e.onComplete()
            }
        }).subscribeOn(Schedulers.io())
    }

    private fun getFootballFansObservable(): Observable<List<User>> {
        return Observable.create(ObservableOnSubscribe<List<User>> { e ->
            if (!e.isDisposed) {
                e.onNext(Utils.getUserListWhoLovesFootball())
                e.onComplete()
            }
        }).subscribeOn(Schedulers.io())
    }

    private fun getObserver(): Observer<List<User>> {
        return object : Observer<List<User>> {

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed)
            }

            override fun onNext(userList: List<User>) {
                textView.append(" onNext")
                textView.append(AppConstant.LINE_SEPARATOR)
                for (user in userList) {
                    textView.append(" firstname : ${user.firstname}")
                    textView.append(AppConstant.LINE_SEPARATOR)
                }
                Log.d(TAG, " onNext : " + userList.size)
            }

            override fun onError(e: Throwable) {
                textView.append(" onError : " + e.message)
                textView.append(AppConstant.LINE_SEPARATOR)
                Log.d(TAG, " onError : " + e.message)
            }

            override fun onComplete() {
                textView.append(" onComplete")
                textView.append(AppConstant.LINE_SEPARATOR)
                Log.d(TAG, " onComplete")
            }
        }
    }

}