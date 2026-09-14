package com.example.cakes.presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.example.cakes.R
import com.example.cakes.data.model.CakeModel
import com.example.cakes.ui.theme.CakeBackground
import com.example.cakes.ui.theme.CakeTypography

/**
 * A full-screen dialog (popup) that shows high-resolution details of a cake.
 *
 * @param cakeModel The cake data to display.
 * @param onDismiss Callback triggered to close the dialog.
 */
@Composable
fun CakePopup(
    cakeModel: CakeModel,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Allows custom width constraints.
        )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.70f),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CakeBackground),

                ) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    // Hero image section.
                    AsyncImage(
                        model = cakeModel.image,
                        contentDescription = "Cake image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f),
                        error = painterResource(R.drawable.food)
                    )
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    // Detailed information section.
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.35f)
                            .padding(horizontal = 24.dp).align(Alignment.Start),
                    ) {

                        Text(
                            text = cakeModel.title,
                            textAlign = TextAlign.Start,
                            style = CakeTypography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = cakeModel.desc,
                            style = CakeTypography.bodyLarge,
                            textAlign = TextAlign.Start,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Close button positioned at the top right.
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)

                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.outline_close_24
                        ),

                        contentDescription = "Close",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(20.dp).background(Color.Transparent)
                    )
                }
            }
        }
    }
}
