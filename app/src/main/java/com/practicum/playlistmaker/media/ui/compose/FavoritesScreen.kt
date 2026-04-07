import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.favorites.model.FavoriteState
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
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(44.dp)
                        .align(Alignment.CenterHorizontally),
                    color = colorResource(R.color.progress_bar)
                )

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

