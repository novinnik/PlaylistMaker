
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.favorites.model.FavoriteState
import com.practicum.playlistmaker.media.favorites.ui.view_model.FavoritesViewModel
import com.practicum.playlistmaker.search.domain.models.Track

@Composable
fun FavoritesScreen(
    state: FavoriteState,
    onClick: (Track) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        when (state) {
            is FavoriteState.Loading ->
                Box(modifier = Modifier
                    .fillMaxSize(),
                     contentAlignment = Alignment.Center)
                    {
                    CircularProgressIndicator()
                }

            is FavoriteState.Empty -> {
                PlaceholderNothing(stringResource(R.string.no_media))
            }

            is FavoriteState.Content -> {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    items(state.tracks) { track ->
                        ItemTrack(track, onClick)
                    }
                }
            }

        }
    }
}

