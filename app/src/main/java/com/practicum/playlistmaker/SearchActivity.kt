package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.practicum.playlistmaker.searchResult.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {

    private val iTunesService = ITunesService().service
    private var trackList = arrayListOf<Track>()

    private var textSearch: String = TEXT_EMPTY
    private lateinit var inputSearchText: EditText
    private lateinit var clearButtonSearch: ImageView
    private lateinit var recyclerSearch: RecyclerView

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var errorPlaceholder: LinearLayout
    private lateinit var noConnectionPlaceholder: LinearLayout
    private lateinit var btnUpdatePlaceholder: Button

    //история
    private lateinit var historyGroup: LinearLayout
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyClearButton: Button

    private val historyList = arrayListOf<Track>()
    private lateinit var historyAdapter: TrackAdapter

    private lateinit var historySearch: SearchHistory

    private lateinit var historySharedPreferences: SharedPreferences
    private lateinit var historylistener: SharedPreferences.OnSharedPreferenceChangeListener

    //прогресс бар
    private lateinit var progressBar: ProgressBar
    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private var isClickAllowed = true

    enum class StateSearch {
        CLEAR,    //очистка
        ERROR,    //ошибка
        EMPTY,    //ни чего, пусто
        SUCCESS   //успешно
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButtonToolbar = findViewById<MaterialToolbar>(R.id.toolbar_activity_search)
        backButtonToolbar.setNavigationOnClickListener { finish() }

        //отслеживнаие состояния прогресс база
        progressBar = findViewById(R.id.search_progress_bar)

        historySharedPreferences = getSharedPreferences(SEARCH_HISTORY_PREF, MODE_PRIVATE)
        historySearch = SearchHistory(historySharedPreferences)

        //Строка поиска

        inputSearchText = findViewById(R.id.inputSearchText)
        clearButtonSearch = findViewById(R.id.clearButtonSearch)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButtonSearch.visibility = clearButtonVisibility(s)
                textSearch = if (!s.isNullOrEmpty()) s.toString() else TEXT_EMPTY
                historyChangeVisibility(inputSearchText.hasFocus(), s.isNullOrEmpty())

                searchRunnable?.let { runnable -> searchHandler.removeCallbacks(runnable) }
                searchRunnable = Runnable {
                    if (!s.isNullOrEmpty() && isClickAllowed){
                        searchTrack(textSearch)
                    }
                }
                searchHandler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        inputSearchText.addTextChangedListener(simpleTextWatcher)

        clearButtonSearch.setOnClickListener {
            updateUI(StateSearch.CLEAR)
            inputSearchText.setText(TEXT_EMPTY)
            val inputMM = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMM?.hideSoftInputFromWindow(inputSearchText.windowToken, 0)
            searchRunnable?.let { runnable -> searchHandler.removeCallbacks (runnable) }
        }

        inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = inputSearchText.text.toString()
                if (query.isNotEmpty()) {
                    searchRunnable?.let { runnable -> searchHandler.removeCallbacks (runnable) }
                    searchTrack(query)
                }
            }
            false
        }

        inputSearchText.setOnFocusChangeListener { view, hasFocus ->
            historyChangeVisibility(hasFocus, inputSearchText.text.isEmpty())
        }

        //Список треков

        recyclerSearch = findViewById(R.id.track_search_recycler)

        errorPlaceholder = findViewById(R.id.placeholder_error)
        noConnectionPlaceholder = findViewById(R.id.placeholder_no_connection)
        btnUpdatePlaceholder = findViewById(R.id.bt_placeholder_update)

        trackAdapter = TrackAdapter(trackList){track ->
            historySearch.addToHistory(track)
            updateHistorySearch()
            startActivityPlayer(track)
        }

        recyclerSearch.adapter = trackAdapter

        btnUpdatePlaceholder.setOnClickListener {
            val query = inputSearchText.text.toString()
            if (query.isNotEmpty()) {
                searchTrack(query)
            }
        }

        //История поиска

        historyGroup = findViewById(R.id.search_history_layout)
        historyRecyclerView = findViewById(R.id.search_history_recycler_view)
        historyClearButton = findViewById(R.id.search_history_button_clear)

        historyClearButton.setOnClickListener {clearHistorySearch()}

        historylistener = SharedPreferences.OnSharedPreferenceChangeListener{
                sharedPrefs, key ->
            if (key == SEARCH_HISTORY_PREF){
                updateHistorySearch()
            }
        }

        historySharedPreferences.registerOnSharedPreferenceChangeListener(historylistener)

        historyAdapter = TrackAdapter(historyList){track ->
            historySearch.addToHistory(track)
            updateHistorySearch()
            startActivityPlayer(track)
        }

        historyRecyclerView.adapter = historyAdapter

        updateHistorySearch()

    }

    private fun historyChangeVisibility(hasFocus: Boolean, isEmpty: Boolean){
        historyGroup.visibility =
            if (hasFocus && isEmpty && historyList.isNotEmpty()){
                View.VISIBLE
            } else {
                View.GONE
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

    @SuppressLint("NotifyDataSetChanged")
    private fun updateHistorySearch(){
        //очищаем
        historyList.clear()
        //получаем и добавляем
        historyList.addAll(historySearch.getHistory())
        //обновляем
        historyAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearHistorySearch(){
        historyList.clear()
        historySearch.clearHistory()
        historyAdapter.notifyDataSetChanged()
        historyGroup.visibility = View.GONE
        updateHistorySearch()
    }

    private fun searchTrack(text: String) {
        updateUI(StateSearch.CLEAR)
        progressBar.visibility = View.VISIBLE

        iTunesService //сервис api
            .search(text) //вызываем метод отправляем запрос
            .enqueue(object : Callback<TracksResponse> { //получаем ответ и обрабатываем его
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    trackList.clear()
                    if (response.isSuccessful()) {
                        val resultResponse = response.body()?.results
                        if (resultResponse?.isNotEmpty() == true) {
                            trackList.addAll(resultResponse)
                            trackAdapter.notifyDataSetChanged()
                            updateUI(StateSearch.SUCCESS)
                        } else {
                           trackAdapter.notifyDataSetChanged()
                            updateUI(StateSearch.EMPTY)
                        }
                    } else {
                        trackAdapter.notifyDataSetChanged()
                        updateUI(StateSearch.ERROR)
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                    updateUI(StateSearch.ERROR)
                }

            })
    }

    private fun updateUI(state: StateSearch) {
        when (state) {
            StateSearch.EMPTY -> {
                recyclerSearch.visibility = View.GONE
                noConnectionPlaceholder.visibility = View.GONE
                errorPlaceholder.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }

            StateSearch.ERROR -> {
                recyclerSearch.visibility = View.GONE
                noConnectionPlaceholder.visibility = View.VISIBLE
                errorPlaceholder.visibility = View.GONE
                progressBar.visibility = View.GONE
            }

            StateSearch.SUCCESS -> {
                recyclerSearch.visibility = View.VISIBLE
                noConnectionPlaceholder.visibility = View.GONE
                errorPlaceholder.visibility = View.GONE
                progressBar.visibility = View.GONE
            }

            StateSearch.CLEAR ->  {
                recyclerSearch.visibility = View.GONE
                noConnectionPlaceholder.visibility = View.GONE
                errorPlaceholder.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun startActivityPlayer(trackClicked:Track){
        if (clickDebounce()){
            val mediaIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
            mediaIntent.putExtra(MEDIA_TRACK_KEY, trackClicked)
            startActivity(mediaIntent)
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            searchHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onDestroy() {
        super.onDestroy()
        searchHandler.removeCallbacksAndMessages(null)
    }

    companion object {
        const val TEXT_EMPTY = ""
        const val SEARCH_STRING = "SEARCH_STRING"
        const val SEARCH_HISTORY_PREF = "search_history_pref"
        const val MEDIA_TRACK_KEY = "media_track_key"
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}