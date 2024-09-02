package com.razorquake.sih2k24.presentation.history.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.razorquake.sih2k24.R
import com.razorquake.sih2k24.domain.SpeechLog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun SpeechLog(
    modifier: Modifier = Modifier,
    speechLog: SpeechLog,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row (
        modifier.fillMaxWidth().clickable(onClick = onClick),
    ){

        Column(
        ) {
            Text(
                text = speechLog.text,
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(id = R.color.text_title),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = speechLog.timestamp.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.text_medium),
                maxLines = 3,
            )

        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = onDelete,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = colorResource(id = R.color.text_medium)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = modifier.fillMaxHeight()
            )
        }
    }

}
@Preview(showBackground = true)
@Composable
fun SpeechLogPreview() {
    SpeechLog(
        speechLog = SpeechLog(
            text = "Hello World",
            timestamp = LocalDateTime.now()
        ),
        onClick = {},
        onDelete = {}
    )
}