package it.connected

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.LayoutDirection
import it.connected.theme.ARMenuTheme
import it.connected.theme.TopBarColor

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ARMenuTheme {
                AboutScreen()
            }
        }
    }
}

@Composable
fun AboutScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "About IT ConnectEd",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                backgroundColor = TopBarColor,
                modifier = Modifier.height(64.dp)
            )
        },
        bottomBar = { AppBottomNavigationBar(selectedRoute = "about") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + 16.dp,
                    start = 20.dp,
                    end = 20.dp
                )
        ) {
            Text("Apa itu IoT?", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Internet of Things (IoT) adalah sebuah konsep di mana berbagai perangkat fisik (mulai dari peralatan rumah tangga, kendaraan, hingga perangkat industri) terhubung ke internet dan dapat saling berkomunikasi serta bertukar data. Bayangkan Anda bisa mengontrol lampu rumah dari jarak jauh, atau kulkas Anda bisa memesan susu secara otomatis saat persediaan menipis. Itulah sebagian kecil dari keajaiban IoT!",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Apa yang Akan Anda Pelajari di Aplikasi Ini?", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Aplikasi ini dirancang untuk memandu Anda dari dasar hingga konsep lanjutan IoT:",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            BulletPoint("Pengantar IoT: Memahami konsep dasar, arsitektur, dan ekosistem IoT.")
            BulletPoint("Perangkat Keras (Hardware): Mengenal berbagai jenis mikrokontroler (seperti Arduino, ESP32, Raspberry Pi), sensor, dan aktuator.")
            BulletPoint("Jaringan & Konektivitas: Mempelajari protokol komunikasi IoT (WiFi, Bluetooth, LoRaWAN, MQTT, dll.).")
            BulletPoint("Platform IoT & Cloud: Menggunakan platform cloud untuk mengelola perangkat dan data IoT.")
            BulletPoint("Analisis Data & Visualisasi: Mengolah dan menampilkan data yang dikumpulkan dari perangkat IoT.")
            BulletPoint("Keamanan IoT: Memahami tantangan dan praktik terbaik dalam mengamankan sistem IoT.")
            BulletPoint("Proyek Praktis: Menerapkan pengetahuan Anda melalui berbagai proyek menarik dan studi kasus.")
        }
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
    ) {
        Text(text = "• ", fontSize = 16.sp)
        Text(text = text, fontSize = 16.sp)
    }
}