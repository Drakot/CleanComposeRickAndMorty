package com.aluengo.cleancomposerickandmorty.detailCharacter.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.aluengo.cleancomposerickandmorty.core.ui.HorizontalSpacer

@Composable
fun Info(title:String, value:String?){
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = "$title:",
            textAlign = TextAlign.Center
        )
        HorizontalSpacer(2)
        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = value ?: "",
            textAlign = TextAlign.Center
        )
    }
}