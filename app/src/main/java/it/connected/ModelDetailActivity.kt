package it.connected

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.connected.theme.ARMenuTheme
import it.connected.theme.ButtonColor
import it.connected.theme.TopBarColor

class ModelDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val modelName = intent.getStringExtra("modelName")
        val model = getModelList().find { it.name == modelName }

        setContent {
            ARMenuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (model != null) {
                        ModelDetailScreen(model = model, onBack = {
                            val intent = Intent(this@ModelDetailActivity, ExploreIotActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        })
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Model not found")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModelDetailScreen(model: Model, onBack: () -> Unit) {
    val context = LocalContext.current
    val imageName = model.name
    val imageResId = remember(imageName) {
        context.resources.getIdentifier(
            imageName,
            "drawable",
            context.packageName
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = model.displayText,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                backgroundColor = TopBarColor,
                modifier = Modifier.height(64.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = if (imageResId != 0) {
                    painterResource(id = imageResId)
                } else {
                    painterResource(id = R.drawable.ic_launcher_background)
                },
                contentDescription = model.displayText,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val intent = Intent(context, ArActivity::class.java)
                    intent.putExtra("modelName", model.name)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor)
            ) {
                Text(text = "View in AR", color = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                if (model.specs.isNotEmpty()) {
                    Text(
                        text = "Spesifikasi:",
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    model.specs.forEach { spec ->
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(spec.first)
                                }
                                append(": ${spec.second}")
                            },
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (model.applications.isNotEmpty()) {
                    Text(
                        text = "Aplikasi:",
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                    )
                    model.applications.forEach { app ->
                        Text(
                            text = "• $app",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }
                }
            }
        }
    }
} 