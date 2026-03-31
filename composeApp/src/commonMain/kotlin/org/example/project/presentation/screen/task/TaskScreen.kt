package org.example.project.presentation.screen.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.rememberNavBackStack
import org.example.project.util.Alpha
import org.example.project.util.Resource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    id: String?,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Task") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(Resource.Icon.BACK_ARROW),
                            contentDescription = "Back arrow icon"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.ime) // Automatically pushes content up when the keyboard opens,
        ) { // ⬇️
            Column( // ⬇️
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TaskInputSection(
                    title = "Title",
                    value = "",
                    onValueChanged = {},
                    placeHolder = "Enter Task Title....",
                    isRequired = true
                )

                TaskInputSection(
                    title = "Task Description",
                    value = "",
                    onValueChanged = {},
                    placeHolder = "Enter Task Description....",
                    minLines = 3,
                    maxLines = 6
                )
            }
            Box(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .windowInsetsPadding(WindowInsets.ime)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onClick = {}
                ) {
                    Text(text = if (id != null) "Update" else "Create Task")
                }
            }
        }
    }
}

@Composable
fun TaskInputSection(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onValueChanged: (String) -> Unit,
    placeHolder: String,
    isRequired: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = 1,
) {
    Column(modifier = modifier) { // ⬇️
        Row(verticalAlignment = Alignment.CenterVertically) { // →
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        }
        // One more text is added to right if isRequired is true
        if (isRequired) {
            Text(
                text = " *",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChanged,
            placeholder = {
                Text(
                    text = placeHolder,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = Alpha.HALF)
                )
            },
            shape = RoundedCornerShape(size = 12.dp),
            minLines = minLines,
            maxLines = maxLines
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TaskScreenPreview() {
    TaskScreen(
        id = "",
        navigateBack = {}
    )
}


@Composable
@Preview(showBackground = true)
fun TaskInputSectionPreview() {
    TaskInputSection(
        title = "Title",
        value = "Value is the text",
        onValueChanged = {},
        placeHolder = "Place Holder",
        isRequired = true
    )
}

