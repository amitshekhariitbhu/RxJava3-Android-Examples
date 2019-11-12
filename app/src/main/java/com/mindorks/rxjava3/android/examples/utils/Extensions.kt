package com.mindorks.rxjava3.android.examples.utils

import android.widget.SearchView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

fun SearchView.getQueryTextChangeObservable(): Observable<String> {

    val subject = PublishSubject.create<String>()

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            subject.onComplete()
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            subject.onNext(newText)
            return true
        }
    })

    return subject

}