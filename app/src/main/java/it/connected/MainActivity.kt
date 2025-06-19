package it.connected

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.LayoutDirection
import it.connected.theme.ARMenuTheme
import it.connected.theme.TopBarColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ARMenuTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "IT ConnectEd",
                        style = MaterialTheme.typography.h6,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                backgroundColor = TopBarColor,
                modifier = Modifier.height(64.dp)
            )
        },
        bottomBar = { AppBottomNavigationBar(selectedRoute = "home") }
    ) { padding ->
        val models = remember { getModelList().take(4) }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(16.dp))
                    Text("Selamat Datang di IT ConnectED", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Mulailah Petualangan Anda di Dunia Internet of Things (IoT)", style = MaterialTheme.typography.body2)
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.main),
                        contentDescription = "Illustration",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Perangkat yang Tersedia", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(models.chunked(2)) { rowItems ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { model ->
                        Box(Modifier.weight(1f)) {
                            DeviceCard(model = model, onClick = {
                                val intent = Intent(context, ModelDetailActivity::class.java)
                                intent.putExtra("modelName", model.name)
                                context.startActivity(intent)
                            })
                        }
                    }
                    if (rowItems.size < 2) {
                        Spacer(Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { context.startActivity(Intent(context, ExploreIotActivity::class.java)) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = TopBarColor)
                ) {
                    Text("Lihat Semua", style = MaterialTheme.typography.button, color = Color.White)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}