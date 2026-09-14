package com.example.cakes.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.cakes.data.model.CakeModel
import com.example.cakes.R

/**
 * A card component that displays a cake's image and title.
 * Clicking the card opens a detailed popup.
 *
 * @param cakeModel The [CakeModel] containing the cake's information.
 */
@Composable
fun CakeCard(cakeModel: CakeModel) {
    var showPopup by remember {
        mutableStateOf(false)
    }
    
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier.fillMaxWidth().height(300.dp).padding(16.dp),
        onClick = {
            showPopup = true
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Loads the cake image from the provided URL.
            AsyncImage(
                model = cakeModel.image,
                contentDescription = "Cake image ${cakeModel.title}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.food) // Fallback image on error.
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.65f)
                            )
                        )
                    )
            )
            // Overlays the title at the bottom of the image.
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = cakeModel.title,
                    color = Color.White,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Displays the detailed popup when requested.
            if (showPopup) {
                CakePopup(
                    cakeModel = cakeModel,
                    onDismiss = {
                        showPopup = false
                    }
                )
            }
        }
    }

}
