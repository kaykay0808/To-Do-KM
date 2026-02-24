package org.example.project.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.domain.Priority
import org.example.project.domain.ToDoTask
import org.example.project.util.Alpha

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: ToDoTask,
    onClick: (String) -> Unit// String is our task id
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick(task.id) },
        colors = CardDefaults.cardColors( containerColor = MaterialTheme.colorScheme.surfaceContainer ),
        elevation = CardDefaults.cardElevation( defaultElevation = 0.dp )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier
                        .alpha(if (task.isCompleted) Alpha.HALF else Alpha.FULL),
                    text = task.title,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Medium,
                        textDecoration = if (task.isCompleted)
                            TextDecoration.LineThrough
                        else
                            TextDecoration.None
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if(task.description.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .alpha(if (task.isCompleted) Alpha.HALF else Alpha.FULL),
                        text = task.description,
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontWeight = FontWeight.Medium,
                            textDecoration = if (task.isCompleted)
                                TextDecoration.LineThrough
                            else
                                TextDecoration.None
                        ),
                        //fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            PriorityChip(
                priority = task.priority,
                isCompleted = task.isCompleted
            )
        }
    }
}

@Composable
@Preview
private fun TaskCardCompletedPreview() {
    TaskCard(
        task = ToDoTask(
            title = "Title",
            description = "Description",
            isCompleted = true,
            priority = Priority.LOW
        ),
        onClick = {}
    )
}

@Composable
@Preview
private fun TaskCardNotCompletedPreview() {
    TaskCard(
        task = ToDoTask(
            title = "Title",
            description = "Description",
            isCompleted = false,
            priority = Priority.LOW
        ),
        onClick = {}
    )
}
