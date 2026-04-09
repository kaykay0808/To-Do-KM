package org.example.project.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.domain.Priority
import org.example.project.presentation.component.PriorityColor.getColor

@Composable
fun PriorityChip(
    priority: Priority,
    size: PriorityChipSize = PriorityChipSize.Medium,
    isCompleted: Boolean = false,
    isSelected: Boolean = false, // 👈 current selected (based on this we can stylize this)
    onSelect: ((Priority) -> Unit)? = null // 👈 callback to update it

) {
    val padding = size.toPadding()
    // Should be Gray
    val shouldUseOutline = if (onSelect != null) !isSelected else isCompleted
    //                    👆 1. checking null    👆 2. flipping   👆 3. flipping
    val chipColor = if(shouldUseOutline) MaterialTheme.colorScheme.outline else priority.getColor()

    Text(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp)) // clip modifier should go first?
            .then(
                if (onSelect != null) {
                    Modifier.clickable { onSelect(priority) }
                } else {
                    Modifier
                }
            )
            .background(chipColor)
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = chipColor
            )
            .padding(
                horizontal = padding.horizontal,
                vertical = padding.vertical
            ),
        text = priority.name,
        style = TextStyle(
            fontWeight = FontWeight.Medium
        ),
        color = Color.White
    )
}

/**
Defines padding options for PriorityChip component.
PriorityChipSize → toPadding() → PriorityChipPadding → Modifier.padding()
 * */
// -> Usage PriorityChipPadding.Small
data class PriorityChipPadding(
    val horizontal: Dp,
    val vertical: Dp
) {
    // pre-made padding sizes. usage -> PriorityChipPadding.Small
    companion object {
        val Small = PriorityChipPadding(6.dp, 2.dp)
        val Medium = PriorityChipPadding(8.dp, 4.dp)
        val Large = PriorityChipPadding(10.dp, 6.dp)
    }
}

// usage -> val size = PriorityChipSize.LOW
enum class PriorityChipSize {
    Small,
    Medium,
    Large;

    fun toPadding(): PriorityChipPadding = when (this) {
        Small -> PriorityChipPadding.Small
        Medium -> PriorityChipPadding.Medium
        Large -> PriorityChipPadding.Large
    }
}

// Define indicator colors for each priority.
object PriorityColor {
    // Light theme colors
    private val lightLow = Color(0xFF2E7D32)
    private val lightMedium = Color(0xFFEF6C00)
    private val lightHigh = Color(0xFFC62828)

    // Dark theme colors
    private val darkLow = Color(0xFF4CAF50)
    private val darkMedium = Color(0xFFFF9800)
    private val darkHigh = Color(0xFFE53935)

    @Composable
    fun Priority.getColor(): Color {
        val isDark = isSystemInDarkTheme()
        return when (this) {
            Priority.LOW -> if (isDark) darkLow else lightLow
            Priority.MEDIUM -> if (isDark) darkMedium else lightMedium
            Priority.HIGH -> if (isDark) darkHigh else lightHigh
        }
    }
}


@Composable
@Preview
fun PriorityChipLowPreview() {
    PriorityChip(
        priority = Priority.LOW,
        size = PriorityChipSize.Small,
        isCompleted = false
    )
}

@Composable
@Preview
fun PriorityChipMediumPreview() {
    PriorityChip(
        priority = Priority.MEDIUM,
        size = PriorityChipSize.Medium,
        isCompleted = false
    )
}

@Composable
@Preview
fun PriorityChipHighPreview() {
    PriorityChip(
        priority = Priority.HIGH,
        size = PriorityChipSize.Large,
        isCompleted = false
    )
}

// Completed
@Composable
@Preview
fun PriorityChipLowCompletedPreview() {
    PriorityChip(
        priority = Priority.LOW,
        size = PriorityChipSize.Small,
        isCompleted = true
    )
}

@Composable
@Preview
fun PriorityChipMediumCompletedPreview() {
    PriorityChip(
        priority = Priority.MEDIUM,
        size = PriorityChipSize.Medium,
        isCompleted = true
    )
}

@Composable
@Preview
fun PriorityChipHighCompletedPreview() {
    PriorityChip(
        priority = Priority.HIGH,
        size = PriorityChipSize.Large,
        isCompleted = true
    )
}
