package com.mindorks.rxjava3.android.examples.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mindorks.rxjava3.android.examples.R
import com.mindorks.rxjava3.android.examples.utils.getQueryTextChangeObservable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class SearchActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SearchActivity"
    }

    private lateinit var searchView: SearchView
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchView = findViewById(R.id.searchView)
        textViewResult = findViewById(R.id.textViewResult)
        setUpSearchObservable()
    }

    private fun setUpSearchObservable() {
        searchView.getQueryTextChangeObservable()
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter { text ->
                if (text.isEmpty()) {
                    textViewResult.text = ""
                    return@filter false
                } else {
                    return@filter true
                }
            }
            .distinctUntilChanged()
            .switchMap { query ->
                dataFromNetwork(query)
                    .doOnError {
                        // handle error
                    }
                    .onErrorReturn { "" }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                textViewResult.text = result
            }
    }

    /**
     * Simulation of network data
     */
    private fun dataFromNetwork(query: String): Observable<String> {
        return Observable.just(true)
            .delay(2, TimeUnit.SECONDS)
            .map {
                query
            }
    }

}
