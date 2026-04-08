import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.model.TracksState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    textSearch: String,
    onTrackClick: (Track) -> Unit,
    onTextChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onClearHistory: () -> Unit,
    onHistoryTrackClick: (Track) -> Unit,
    onRetry: () -> Unit
){

    val stateSearch by viewModel.observeState().observeAsState()
    val historyTracks by viewModel.observeHistory().observeAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        AppBarTop(stringResource(id = R.string.search), false) { }

        FieldSearchInput(
            query = textSearch,
            onTextChanged = onTextChanged,
            onClearClick = onClearClick)

        Spacer(modifier = Modifier.height(8.dp))

        Box (
            modifier = Modifier.fillMaxSize()
        ){
            when (stateSearch) {
                is TracksState.Loading ->
                    Box(modifier = Modifier
                        .fillMaxSize(),
                        contentAlignment = Alignment.Center)
                    {
                        CircularProgressIndicator()
                    }

                is TracksState.Content -> {
                    val tracks = (stateSearch as TracksState.Content).tracks
                    if (tracks.isNotEmpty()) {
                        ShowContentScreen(
                            tracks,
                            onTrackClick
                        )
                    } else {
                        ShowHistoryScreen(
                            tracks = historyTracks ?: arrayListOf<Track>(),
                            onClearHistoryClick = onClearHistory,
                            onClick = onHistoryTrackClick
                        )
                    }
                }

                is TracksState.Error ->
                    PlaceholderError(retrySearch = onRetry)

                is TracksState.Empty -> {
                    PlaceholderNothing(stringResource(R.string.nothing_was_found))
                }

                else -> {
                    ShowHistoryScreen(
                        tracks = historyTracks?:arrayListOf<Track>(),
                        onClearHistoryClick = onClearHistory,
                        onClick = onHistoryTrackClick)
                }
            }
        }

    }
}

