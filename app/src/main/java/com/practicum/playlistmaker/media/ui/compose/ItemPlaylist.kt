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
import androidx.compose.ui.res.stringResource
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
import com.practicum.playlistmaker.media.playlists.domain.model.Playlist
import com.practicum.playlistmaker.util.Converter.getNoun

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun ItemPlaylistPreview() {
//    ItemPlaylist(
//        playlist = Playlist(
//            0,
//            image = null,
//            title = "Playlist name",
//            description = "",
//            listIds = emptyList(),
//            count = 90
//        ),
//        onClick = {}
//    )
//}

@Composable
fun ItemPlaylist(
    playlist: Playlist,
    onClick: (id: Int) -> Unit){

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            //  .height(60.dp)
            .clickable { onClick(playlist.id) },
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(playlist.image.toString())
                .crossfade(true)
                .listener(
                    onError = { _, result ->
                        Log.e("IMAGE_ERROR", result.throwable.toString())
                    }
                )
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = playlist.title,
            placeholder = painterResource(R.drawable.ic_placeholder),
            error = painterResource(R.drawable.ic_placeholder),
            fallback = painterResource(R.drawable.ic_placeholder),
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp)),

            )
       // Spacer(modifier = Modifier.width(8.dp))
        Column (
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ){

            Text(
                text = playlist.title,
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = countString(playlist.count),
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                fontSize = 11.sp,
                color = colorResource(R.color.grey),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}

@Composable
private fun countString(count: Int): String{
    val countStr = getNoun(
        count,
        stringResource(R.string.one_track),
        stringResource(R.string.two_four_track),
        stringResource(R.string.zero_many_track))

    return "$count $countStr"
}