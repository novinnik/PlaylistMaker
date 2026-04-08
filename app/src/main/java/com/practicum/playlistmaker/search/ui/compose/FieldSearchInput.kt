
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R


@Composable
fun FieldSearchInput(
    query:String,
    onTextChanged: (String) -> Unit,
    onClearClick: () -> Unit
){
    var textQuery by remember { mutableStateOf(query) }

    TextField(
        value = textQuery,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(fontSize = 16.sp, color = colorResource(R.color.yp_black)),
        shape = RoundedCornerShape(8.dp),
        onValueChange = {
            textQuery = it
            onTextChanged(it)
        },
        //поле ввода
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        //иконка поиска слева
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.search_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        },
        //кнопка очистки
        trailingIcon = {
            if (textQuery.isNotEmpty()){
                IconButton(
                    onClick = {
                        textQuery = ""
                        onClearClick()
                    }
                ){
                    Icon(
                        painter = painterResource(R.drawable.ic_clear_light),
                        contentDescription = stringResource(R.string.clear_string_search),
                        tint = MaterialTheme.colorScheme.secondary //primary
                    )
                }
            }
        },
        //цвет состояния активности / фокуса
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )

    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun FieldSearchInputPreview() {
    FieldSearchInput(
        query = "",
        onTextChanged = {},
        onClearClick = {},
    )
}