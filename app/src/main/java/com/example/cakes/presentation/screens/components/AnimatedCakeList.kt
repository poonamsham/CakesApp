package com.example.cakes.presentation.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cakes.data.model.CakeModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AnimatedCakeItem(
    cakeModel: CakeModel,
) {
    var visible by remember(cakeModel.title) {
        mutableStateOf(false)
    }

    LaunchedEffect(cakeModel.title) {
        delay(( 60L).milliseconds)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(500)
        ) + slideInVertically(
            initialOffsetY = { -it / 2 },
            animationSpec = tween(500)
        )
    ) {
        CakeCard(cakeModel)
    }
}