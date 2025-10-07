package com.practicum.playlistmaker.search.ui

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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.MEDIA_TRACK_KEY
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.SEARCH_HISTORY_PREF
import com.practicum.playlistmaker.search.data.local.SearchHistory
import com.practicum.playlistmaker.search.domain.api.TracksSearchInteractor
import com.practicum.playlistmaker.search.domain.models.ResultSearch
import com.practicum.playlistmaker.search.domain.models.Track


class SearchActivity : AppCompatActivity() {

    private var searchInteractor = Creator.provideTrackInteractor()
    private var historyInteractor= Creator.provideHistoryInteractor()

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

    private var historyList = arrayListOf<Track>()
    private lateinit var historyAdapter: TrackAdapter

    private lateinit var historySearch: SearchHistory

    private lateinit var historySharedPreferences: SharedPreferences
    private lateinit var historylistener: SharedPreferences.OnSharedPreferenceChangeListener

    //прогресс бар
    private lateinit var progressBar: ProgressBar
    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable = Runnable {searchTrack()}
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
                if (!s.isNullOrEmpty() && isClickAllowed){
                     debounceSearchTrack()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        inputSearchText.addTextChangedListener(simpleTextWatcher)

        clearButtonSearch.setOnClickListener {
            updateUI(StateSearch.CLEAR)
            inputSearchText.setText(TEXT_EMPTY)
            val inputMM = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMM?.hideSoftInputFromWindow(inputSearchText.windowToken, 0)
           // searchRunnable?.let { runnable -> searchHandler.removeCallbacks (runnable) }
        }

        inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                textSearch = inputSearchText.text.toString()
                if (textSearch.isNotEmpty()) {
                    debounceSearchTrack()
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
            addHistorySearch(track)
            startActivityPlayer(track)
        }

        recyclerSearch.adapter = trackAdapter

        btnUpdatePlaceholder.setOnClickListener {
            //val query = inputSearchText.text.toString()
            textSearch = inputSearchText.text.toString()
            debounceSearchTrack()
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
            addHistorySearch(track)
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
        historyList = historyInteractor.updateHistory(historyList)
        historyAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearHistorySearch(){
        historyInteractor.clearHistory(historyList)
        historyAdapter.notifyDataSetChanged()
        historyGroup.visibility = View.GONE
        updateHistorySearch()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addHistorySearch(track:Track){
        historyInteractor.addToHistory(historyList, track)
        historyAdapter.notifyDataSetChanged()
        updateHistorySearch()
    }

    private fun searchTrack() {

        updateUI(StateSearch.CLEAR)
        trackList.clear()

        if (textSearch.isNotEmpty()){

            progressBar.visibility = View.VISIBLE

            searchInteractor.searchTracks(
                textSearch,
                consumer = object : TracksSearchInteractor.TracksConsumer{
                @SuppressLint("NotifyDataSetChanged")
                override fun consume(tracksSearch: ResultSearch){
                    searchHandler.post {

                        if (tracksSearch.resultCode == 200) {

                            if (tracksSearch.listTracks.isNotEmpty()) {
                                trackList.addAll(tracksSearch.listTracks)
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
                }
                }
            )
        }
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

    private fun startActivityPlayer(trackClicked: Track){
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

    private fun debounceSearchTrack(){
        searchHandler.removeCallbacks(searchRunnable)
        searchHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        const val TEXT_EMPTY = ""
        const val SEARCH_STRING = "SEARCH_STRING"
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}