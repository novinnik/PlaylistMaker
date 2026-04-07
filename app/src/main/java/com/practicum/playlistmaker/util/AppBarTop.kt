import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R

@Composable
fun AppBarTop(title: String, isViewIcon: Boolean, onClick:()-> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.toolbar_height))
           // .background(MaterialTheme.colorScheme.background)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isViewIcon){
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 12.dp).clickable{onClick()},
            )
        }
        Text(
            text = title,
            modifier = Modifier.padding(start = 12.dp),
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontWeight = FontWeight(500),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    AppBarTop("Настройки", false) {}
}