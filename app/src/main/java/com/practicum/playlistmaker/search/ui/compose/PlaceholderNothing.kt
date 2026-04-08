import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
fun PlaceholderNothingPreview() {
    PlaceholderNothing(stringResource(R.string.nothing_was_found))
}
@Composable
fun PlaceholderNothing(title:String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(104.dp))

        Image(
            painter = painterResource(R.drawable.ic_placeholder_nothing),
            contentDescription = title
        )

        Text(
            text = title,
            modifier = Modifier.padding(top = 16.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.ys_display_medium))
            )
        )
    }
}
