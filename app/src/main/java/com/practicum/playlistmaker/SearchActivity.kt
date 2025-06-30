package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    var textSearch : String = TEXT_EMPTY
    lateinit var inputSearchText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_search)


        val backButtonToolbar = findViewById<MaterialToolbar>(R.id.toolbar_activity_search)
        backButtonToolbar.setNavigationOnClickListener{finish()}

        inputSearchText = findViewById<EditText>(R.id.inputSearchText)
        val clearButtonSearch = findViewById<ImageView>(R.id.clearButtonSearch)


        clearButtonSearch.setOnClickListener{
            inputSearchText.setText(TEXT_EMPTY)
            val inputMM = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            val currentView = currentFocus ?: View(this)
//            inputMM?.hideSoftInputFromWindow(`currentView`.windowToken, 0)
              inputMM?.hideSoftInputFromWindow(inputSearchText.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
              // Toast.makeText(this@SearchActivity, "Not yet implemented", Toast.LENGTH_SHORT).show()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButtonSearch.visibility = clearButtonVisibility(s)
                textSearch = if (!s.isNullOrEmpty()) s.toString() else TEXT_EMPTY
            }

            override fun afterTextChanged(s: Editable?) {
                //Toast.makeText(this@SearchActivity, "Not yet implemented", Toast.LENGTH_SHORT).show()
            }
        }

        inputSearchText.addTextChangedListener(simpleTextWatcher)
    }

    /*видимость кнопки очистки*/
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    /*сохранить введенные данные*/
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, textSearch)
    }

    /*восстановить введенные данные*/
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(SEARCH_STRING, TEXT_EMPTY)
    }

    /*при восстановлении вернуть введенное значение обратно*/
//    override fun onResume() {
//        super.onResume()
//        inputTextSearch?.setText(textSearch)
//    }

    companion object{
        const val TEXT_EMPTY = ""
        const val SEARCH_STRING = "SEARCH_STRING"
    }
}