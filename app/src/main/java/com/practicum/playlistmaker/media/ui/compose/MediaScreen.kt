
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.favorites.model.FavoriteState
import com.practicum.playlistmaker.media.playlists.model.PlaylistState
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
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        AppBarTop(stringResource(id = R.string.media), false) { }

        TabRow(
            selectedTabIndex = selectedTabIndex.value,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            indicator = {tabPosition ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPosition[selectedTabIndex.value])
                        .padding(horizontal = 16.dp)
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            divider = {},
        ){
            tabs.forEachIndexed {index, string ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    onClick = {scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                    },
                    text = {
                        Text(
                            text = string,
                            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
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


