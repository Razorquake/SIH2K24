package com.razorquake.sih2k24

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onTextTranslationClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSignTranslationClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text("Features", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        LazyColumn {
            item {
                FeatureCard(
                    title = "Sign Translation",
                    description = "Sign to Text, Sign to Speech",
                    onClick = onSignTranslationClick,
                    icon = R.drawable.baseline_waving_hand_24
                )
            }
            item {
                FeatureCard(
                    title = "Text Translation",
                    description = "Speech to Sign, Text to Sign",
                    onClick = onTextTranslationClick,
                    icon = R.drawable.speech_to_text_24px
                )
            }
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int
    ){
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = icon), contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .padding(8.dp),
        )
        Column (
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(96.dp)
        ){
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(id = R.color.text_title),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.text_medium),
                maxLines = 3,
            )
        }
    }
}