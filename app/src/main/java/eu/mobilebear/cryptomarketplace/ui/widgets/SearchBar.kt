package eu.mobilebear.cryptomarketplace.ui.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchText: String,
    onTextChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                if (searchText.isEmpty()) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                } else {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                }
            }

            TextField(
                value = searchText,
                onValueChange = onTextChange,
                modifier = Modifier.padding(horizontal = 16.dp),
                placeholder = { Text("Search...") },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}