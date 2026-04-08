
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.playlists.model.PlaylistState

@Composable
fun PlaylistsScreen(
    state: PlaylistState,
    onPlaylistClick: (id: Int) -> Unit,
    onCreatePlaylistClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
              .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.background,
                containerColor = MaterialTheme.colorScheme.onBackground
            ),
            onClick = onCreatePlaylistClick,
        ) {
            Text(
                text = stringResource(id = R.string.new_playlist),
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                fontSize = 14.sp
            )
        }
        when (state) {
            is PlaylistState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(44.dp)
                        .align(Alignment.CenterHorizontally),
                    color = colorResource(R.color.progress_bar)
                )

            PlaylistState.Empty -> {
                PlaceholderNothing(stringResource(R.string.no_playlist))
            }

            is PlaylistState.Content -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 8.dp, top = 16.dp)
                ) {
                    items(state.playlist) { it ->
                        ItemPlaylist(it, onPlaylistClick)
                    }
                }
            }
        }
    }
}
