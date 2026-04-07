import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PlaceholderErrorPreview() {
    PlaceholderError(
        retrySearch = {}
    )
}

@Composable
fun PlaceholderError(
    retrySearch: () -> Unit
){
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.ic_placeholder_no_connection),
            contentDescription = stringResource(R.string.no_connection)
        )

        Text(
            text = stringResource(R.string.no_connection),
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.ys_display_medium))
        ))

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {retrySearch()}
        ){
            Text(text = stringResource(R.string.refresh))
        }
    }
}