
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track


@Composable
fun ShowHistoryScreen(
    tracks: ArrayList<Track>,
    onClearHistoryClick: () -> Unit,
    onClick: (Track) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (tracks.isNotEmpty()){

            Text(
                text = stringResource(R.string.your_history),
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ){
                items(tracks) { track ->
                    ItemTrack(track = track, onClick = onClick)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClearHistoryClick,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.background,
                    containerColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = stringResource(id = R.string.clear_history),
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                    fontSize = 14.sp)
            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ShowHistoryScreenPreview() {

    val trackList = arrayListOf<Track>(
        Track(
            1,
            "Smells Like Teen Spirit",
            "Nirvana",
            293000,//"5:01",
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg",
            "Album",
            "2009",
            "Rock",
            "USA",
            ""
        ),
        Track(
            2,
            "Billie Jean",
            "Michael Jackson",
            293000,//"4:35",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg",
            "Album",
            "2009",
            "Rock",
            "USA",
            ""
        ),
        Track(
            3,
            "Stayin' Alive",
            "Bee Gees",
            293000,//"4:10",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg",
            "Album",
            "2012",
            "Pop",
            "USA",
            ""
        ),
        Track(
            4,
            "Whole Lotta Love",
            "Led Zeppelin",
            3600000,//"5:33",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg",
            "Album",
            "2009",
            "Rock",
            "USA",
            ""
        ),

        Track(
            5,
            "Sweet Child O'Mine",
            "Guns N' Roses",
            3560000,//"5:03",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg",
            "Album",
            "2009",
            "Rock",
            "USA",
            ""
        )
    )

    ShowHistoryScreen(
        trackList,
        onClearHistoryClick = {},
        onClick = {},
    )
}