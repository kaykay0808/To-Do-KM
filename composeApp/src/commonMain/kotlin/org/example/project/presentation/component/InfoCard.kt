package org.example.project.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.util.Resource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    message: String,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    lightModeIcon: DrawableResource = Resource.Image.PEN_PAPER_LIGHT,
    darkModeIcon: DrawableResource = Resource.Image.PEN_PAPER_DARK
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(containerColor)
            .padding(all = 48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(80.dp),
            painter = painterResource(
                if (isSystemInDarkTheme()) darkModeIcon else lightModeIcon
            ),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            color = contentColor
        )
    }
}

@Composable
@Preview
private fun InfoCardPreview() {
    InfoCard(
        message = "This is a preview",
        // lightModeIcon = Resource.Image.PEN_PAPER_LIGHT
    )
}

