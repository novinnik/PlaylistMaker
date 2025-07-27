package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.searchResult.ITunesService
import com.practicum.playlistmaker.searchResult.Track
import com.practicum.playlistmaker.searchResult.TrackAdapter
import com.practicum.playlistmaker.searchResult.TrackMock
import com.practicum.playlistmaker.searchResult.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {

    private val iTunesService = ITunesService().service
    private var trackList = ArrayList<Track>()

    private var textSearch: String = TEXT_EMPTY
    private lateinit var inputSearchText: EditText
    private lateinit var recyclerSearch: RecyclerView

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var errorPlaceholder: LinearLayout
    private lateinit var noConnectionPlaceholder: LinearLayout
    private lateinit var btnUpdatePlaceholder: Button

    enum class StateSearch {
        LOADING,    //загрузка
        ERROR,      //ошибка
        EMPTY,      //ни чего, пусто
        SUCCESS     //успешно
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButtonToolbar = findViewById<MaterialToolbar>(R.id.toolbar_activity_search)
        backButtonToolbar.setNavigationOnClickListener { finish() }

        inputSearchText = findViewById(R.id.inputSearchText)
        val clearButtonSearch = findViewById<ImageView>(R.id.clearButtonSearch)

        recyclerSearch = findViewById(R.id.track_search_recycler)

        errorPlaceholder = findViewById(R.id.placeholder_error)
        noConnectionPlaceholder = findViewById(R.id.placeholder_no_connection)
        btnUpdatePlaceholder = findViewById(R.id.bt_placeholder_update)

        clearButtonSearch.setOnClickListener {
            inputSearchText.setText(TEXT_EMPTY)
            val inputMM = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMM?.hideSoftInputFromWindow(inputSearchText.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButtonSearch.visibility = clearButtonVisibility(s)
                textSearch = if (!s.isNullOrEmpty()) s.toString() else TEXT_EMPTY
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        inputSearchText.addTextChangedListener(simpleTextWatcher)

        trackAdapter = TrackAdapter(trackList)
        recyclerSearch.adapter = trackAdapter

        inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = inputSearchText.text.toString()
                if (query.isNotEmpty()) {
                    searchTrack(query)
                }
            }
            false
        }

        btnUpdatePlaceholder.setOnClickListener {
            val query = inputSearchText.text.toString()
            if (query.isNotEmpty()) {
                searchTrack(query)
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, textSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(SEARCH_STRING, TEXT_EMPTY)
    }

    private fun searchTrack(text: String) {
        updateUI(StateSearch.LOADING)

        iTunesService //сервис api
            .search(text) //вызываем метод отправляем запрос
            .enqueue(object : Callback<TracksResponse> { //получаем ответ и обрабатываем его
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    trackList.clear()
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                            updateUI(StateSearch.SUCCESS)
                        } else {
                           // trackAdapter.notifyDataSetChanged()
                            updateUI(StateSearch.EMPTY)
                        }
                    } else {
                      //  trackAdapter.notifyDataSetChanged()
                        updateUI(StateSearch.ERROR)
                    }
//                    trackAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    trackList.clear()
                 //   trackAdapter.notifyDataSetChanged()
                    updateUI(StateSearch.ERROR)
                }

            })
    }

    private fun updateUI(state: StateSearch) {
        when (state) {
            StateSearch.EMPTY -> {
                recyclerSearch.visibility = View.INVISIBLE
                noConnectionPlaceholder.visibility = View.INVISIBLE
                errorPlaceholder.visibility = View.VISIBLE
            }

            StateSearch.ERROR -> {
                recyclerSearch.visibility = View.INVISIBLE
                noConnectionPlaceholder.visibility = View.VISIBLE
                errorPlaceholder.visibility = View.INVISIBLE
            }

            StateSearch.SUCCESS -> {
                recyclerSearch.visibility = View.VISIBLE
                noConnectionPlaceholder.visibility = View.INVISIBLE
                errorPlaceholder.visibility = View.INVISIBLE
            }

            StateSearch.LOADING ->  {
                recyclerSearch.visibility = View.INVISIBLE
                noConnectionPlaceholder.visibility = View.INVISIBLE
                errorPlaceholder.visibility = View.INVISIBLE
            }
        }
    }


    companion object {
        const val TEXT_EMPTY = ""
        const val SEARCH_STRING = "SEARCH_STRING"
    }
}