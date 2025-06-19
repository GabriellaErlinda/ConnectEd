package it.connected

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.LayoutDirection
import it.connected.theme.ARMenuTheme
import it.connected.theme.TopBarColor

class ExploreIotActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ARMenuTheme {
                ExploreIotScreen()
            }
        }
    }
}

@Composable
fun ExploreIotScreen() {
    val context = LocalContext.current
    val allModels = remember { getModelList() }
    var searchQuery by remember { mutableStateOf("") }
    val filteredModels = remember(searchQuery, allModels) {
        if (searchQuery.isEmpty()) {
            allModels
        } else {
            allModels.filter { it.displayText.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Available Device",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                backgroundColor = TopBarColor,
                modifier = Modifier.height(64.dp)
            )
        },
        bottomBar = { AppBottomNavigationBar(selectedRoute = "explore") }
    ) { padding ->
        Column(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                start = padding.calculateStartPadding(LayoutDirection.Ltr),
                end = padding.calculateEndPadding(LayoutDirection.Ltr)
            )
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search Icon")
                }
            )

            if (filteredModels.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No devices found")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = padding.calculateBottomPadding() + 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredModels) { model ->
                        DeviceCard(model = model) {
                            val intent = Intent(context, ModelDetailActivity::class.java)
                            intent.putExtra("modelName", model.name)
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
} 