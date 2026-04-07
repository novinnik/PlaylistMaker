import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.favorites.model.FavoriteState
import com.practicum.playlistmaker.media.favorites.ui.view_model.FavoritesViewModel
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.media.playlists.model.PlaylistState
import com.practicum.playlistmaker.media.playlists.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch


@Composable
fun MediaScreen(
    playlistState: PlaylistState,
    favoritesState: FavoriteState,
    onClickOpenPlayer: (Track) -> Unit,
    onClickVewDetails: (Int) -> Unit,
    onClickNewPlaylist:() -> Unit
){
    val tabs = listOf(
        stringResource(R.string.select_tracks),
        stringResource(R.string.playlists)
    )

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    Column (
        modifier = Modifier
            .fillMaxSize()
        //    .background(MaterialTheme.colorScheme.background)
    ){
        AppBarTop(stringResource(id = R.string.media), false) { }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.background,
           // contentColor = MaterialTheme.colorScheme.onPrimary,
            indicator = {tabPosition ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPosition[pagerState.currentPage])
                        .padding(horizontal = 16.dp)
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            divider = {},
        ){
            tabs.forEachIndexed {index, string ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                    },
                    text = {
                        Text(
                            text = string,
                            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {page ->
            when (page) {
                0 -> FavoritesScreen(
                    state = favoritesState,
                    onClick = onClickOpenPlayer
                )

                1 -> PlaylistsScreen(
                    state = playlistState,
                    onPlaylistClick = onClickVewDetails,
                    onCreatePlaylistClick = onClickNewPlaylist
                )
            }
        }
    }
}


