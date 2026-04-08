
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.setting.ui.view_model.SettingViewModel


//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun PreviewScreen(){
//    SettingScreen()
//}
@Composable
fun SettingScreen(viewModel: SettingViewModel){

    val isDarkTheme = viewModel.getDarkTheme().observeAsState()
    val shareApp = { viewModel.shareApp() }
    val writeSupport = { viewModel.writeSupport() }
    val userAgreement = { viewModel.userAgreement() }
    val switchDarkTheme = {checked:Boolean -> viewModel.switchDarkTheme(checked)}

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){

        AppBarTop(stringResource(id = R.string.setting), false) { }

        RowSwitch(
            title = stringResource(id = R.string.dark_themes),
            checked = isDarkTheme.value ?: false,
            switchDarkTheme
        )

        RowSetting(
            title = stringResource(id = R.string.share_application),
            icon = R.drawable.ic_share,
            onClick = shareApp
        )

        RowSetting(
            title = stringResource(id = R.string.write_support),
            icon = R.drawable.ic_support,
            onClick = writeSupport
        )

        RowSetting(
            title = stringResource(id = R.string.user_agreement),
            icon = R.drawable.ic_arrow_forward,
            onClick = userAgreement
        )
    }
}

@Composable
fun RowSwitch(
    title: String,
    checked: Boolean,
    onChangedClickable:(Boolean) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
            start = dimensionResource(R.dimen.padding_16),
            end = dimensionResource(R.dimen.padding_16),
            bottom = dimensionResource(R.dimen.padding_20),
            top = dimensionResource(R.dimen.padding_20))
            .clickable { onChangedClickable(!checked)},
        verticalAlignment = Alignment.CenterVertically
    ) {
        val colorThumbNo = colorResource(R.color.switch_thumb_color)
        val colorTrackNo = colorResource(R.color.switch_track_color)
        val colorThumb = colorResource(R.color.grey)
        val colorTrack = colorResource(R.color.light_grey)

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Switch(
            checked = checked,
            onCheckedChange = {onChangedClickable(it)},
            colors = SwitchDefaults.colors(
                //Бегунок
                checkedThumbColor = colorThumbNo,
                uncheckedThumbColor = colorThumb,
                //Переключатель
                checkedTrackColor = colorTrackNo,
                uncheckedTrackColor = colorTrack,
                //Граница переключателя
                checkedBorderColor = colorThumbNo,
                uncheckedBorderColor = colorTrack
            )
        )
    }
}

@Composable
fun RowSetting(
    title: String,
    icon: Int? = null,
    onClick:() -> Unit
){
    val colorContent = MaterialTheme.colorScheme.onBackground
    val iconTint = MaterialTheme.colorScheme.surface

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = dimensionResource(R.dimen.padding_16),
            end = dimensionResource(R.dimen.padding_16),
            bottom = dimensionResource(R.dimen.padding_20),
            top = dimensionResource(R.dimen.padding_20))
        .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            color = colorContent,
            fontWeight = FontWeight(400),
        )
        icon?.let {res ->
            Icon(
                painter = painterResource(res),
                contentDescription = null,
                tint = iconTint)
        }
    }


}