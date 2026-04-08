import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Converter.timeConversion


@Composable
fun ItemTrack(track: Track, onClick: (Track) -> Unit){

    val timeTrack = remember { timeConversion(track.trackTime) }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clickable { onClick(track) },
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(

            model = ImageRequest.Builder(LocalContext.current)
                .data(track.albumPoster.toString())
                .crossfade(true)
                .listener(
                    onError = { _, result ->
                        Log.e("IMAGE_ERROR", result.throwable.toString())
                    }
                )
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = track.trackName,
            placeholder = painterResource(R.drawable.ic_placeholder),
            error = painterResource(R.drawable.ic_placeholder),
            fallback = painterResource(R.drawable.ic_placeholder),
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp)),

        )
        Spacer(modifier = Modifier.width(8.dp))
        Column (
            modifier = Modifier.weight(1f)
        ){
            track.trackName?.let {
                Text(
                    text = it,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                track.artistName?.let {
                    Text(
                        text = it,
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                        fontSize = 11.sp,
                        color = colorResource(R.color.grey),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    painter = painterResource(R.drawable.ic_comm),
                    contentDescription = null,
                    tint = colorResource(R.color.grey)
                )

                Text(
                    text = timeTrack,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    fontSize = 11.sp,
                    color = colorResource(R.color.grey),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
        Icon(
            modifier = Modifier.size(width = 8.dp, height = 16.dp),
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = null,
            tint = colorResource(R.color.grey)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ItemTrackPreview() {
    val track = Track(
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
    )

    ItemTrack(
        track,
        onClick = {},
    )
}
