package it.connected

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.connected.theme.TopBarColor

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.home, "Home")
    object Explore : NavigationItem("explore", R.drawable.iot, "Explore IoT")
    object About : NavigationItem("about", R.drawable.info, "About")
}

@Composable
fun AppBottomNavigationBar(selectedRoute: String) {
    val context = LocalContext.current
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Explore,
        NavigationItem.About
    )
    BottomNavigation(
        modifier = Modifier.height(72.dp),
        backgroundColor = MaterialTheme.colors.background,
        contentColor = TopBarColor
    ) {
        items.forEach { item ->
            val selected = selectedRoute == item.route
            BottomNavigationItem(
                icon = {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                            color = if (selected) TopBarColor else Color.Gray
                        )
                    }
                },
                label = null,
                selectedContentColor = TopBarColor,
                unselectedContentColor = Color.Gray,
                alwaysShowLabel = false,
                selected = selected,
                onClick = {
                    if (selectedRoute != item.route) {
                        val intent = when (item.route) {
                            "home" -> Intent(context, MainActivity::class.java)
                            "explore" -> Intent(context, ExploreIotActivity::class.java)
                            "about" -> Intent(context, AboutActivity::class.java)
                            else -> null
                        }
                        intent?.let {
                            context.startActivity(it)
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeviceCard(model: Model, onClick: () -> Unit) {
    val context = LocalContext.current
    val imageResId = remember(model.name) {
        context.resources.getIdentifier(
            model.name,
            "drawable",
            context.packageName
        )
    }
    Card(
        modifier = Modifier
            .height(180.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = if (imageResId != 0) painterResource(id = imageResId) else painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = model.displayText,
                modifier = Modifier
                    .height(120.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )
            Text(
                text = model.displayText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

data class Model(
    val name: String,
    val displayText: String,
    val specs: List<Pair<String, String>>,
    val applications: List<String>
)

fun getModelList(): List<Model> {
    return listOf(
        Model(
            name = "dht22",
            displayText = "DHT22 Sensor",
            specs = listOf(
                "Model" to "DHT22 (AM2302)",
                "Jenis Sensor" to "Sensor Suhu dan Kelembaban Digital",
                "Tegangan Operasional" to "3.3V hingga 6V DC",
                "Arus Operasional" to "1.5mA (maksimum)",
                "Rentang Pengukuran Suhu" to "-40°C hingga +80°C",
                "Akurasi Suhu" to "±0.5°C",
                "Rentang Pengukuran Kelembaban" to "0% hingga 100% RH",
                "Akurasi Kelembaban" to "±2% hingga ±5% RH",
                "Respon Waktu" to "Suhu: ≤2 detik; Kelembaban: ≤5 detik",
                "Interval Pengukuran" to "2 detik",
                "Dimensi" to "15.1mm x 25mm x 7.7mm",
                "Output Data" to "Data digital melalui komunikasi satu kawat (single wire)"
            ),
            applications = listOf(
                "Monitoring suhu dan kelembaban dalam ruangan",
                "Proyek IoT",
                "Sistem HVAC",
                "Pertanian cerdas",
                "Stasiun cuaca"
            )
        ),
        Model(
            name = "ultrasonic",
            displayText = "Ultrasonic Sensor",
            specs = listOf(
                "Model" to "HC-SR04",
                "Jenis Sensor" to "Sensor Jarak Ultrasonik",
                "Tegangan Operasional" to "5V DC",
                "Arus Operasional" to "15mA (maksimum)",
                "Rentang Pengukuran Jarak" to "2cm hingga 400cm",
                "Akurasi Jarak" to "±0.3cm",
                "Frekuensi Ultrasonik" to "40kHz",
                "Respon Waktu" to "Sekitar 10ms per pengukuran",
                "Interval Pengukuran" to "60ms",
                "Dimensi" to "45mm x 20mm x 15mm",
                "Output Data" to "Sinyal pulsa digital (PWM)"
            ),
            applications = listOf(
                "Pengukuran jarak non-kontak",
                "Menghindari rintangan pada robot",
                "Sistem parkir",
                "Pengisian level cairan",
                "Sistem keamanan"
            )
        ),
        Model(
            name = "servo",
            displayText = "Servo Motor",
            specs = listOf(
                "Model" to "SG90 Mini Servo",
                "Jenis Komponen" to "Motor Servo Rotasi Mini",
                "Tegangan Operasional" to "4.8V hingga 6V DC",
                "Arus Operasional" to "100mA (saat idle) hingga 700mA (saat beban puncak)",
                "Torsi" to "1.8 kg/cm (pada 4.8V)",
                "Sudut Rotasi" to "0° hingga 180°",
                "Kecepatan Operasional" to "0.10 detik/60° (pada 4.8V)",
                "Tipe Konektor" to "JR (GND, VCC, Sinyal)",
                "Dimensi" to "23mm x 12.2mm x 29mm",
                "Output Data" to "Pengendalian posisi melalui sinyal PWM"
            ),
            applications = listOf(
                "Robotika (lengan robot, sendi)",
                "Pesawat model dan drone",
                "Mekanisme kamera",
                "Pintu otomatis",
                "Proyek otomasi kecil"
            )
        ),
        Model(
            name = "rfid",
            displayText = "RFID Reader/Writer",
            specs = listOf(
                "Model" to "RC522",
                "Jenis Komponen" to "Modul Pembaca/Penulis RFID (Radio-Frequency Identification)",
                "Tegangan Operasional" to "3.3V DC",
                "Arus Operasional" to "13mA hingga 26mA (saat beroperasi), <10uA (mode tidur)",
                "Frekuensi Operasional" to "13.56MHz",
                "Jarak Baca" to "Hingga 5cm (tergantung tag)",
                "Tipe Antarmuka" to "SPI (Serial Peripheral Interface)",
                "Protokol Komunikasi" to "ISO/IEC 14443 Type A",
                "Dimensi" to "40mm x 60mm",
                "Output Data" to "Data digital (UID, data tag)"
            ),
            applications = listOf(
                "Sistem kontrol akses",
                "Pelacakan inventaris",
                "Sistem kehadiran",
                "Pembayaran nirsentuh",
                "Sistem identifikasi barang"
            )
        ),
        Model(
            name = "relay",
            displayText = "Relay Module",
            specs = listOf(
                "Model" to "SRD-05VDC-SL-C (Single Channel Relay Module)",
                "Jenis Komponen" to "Sakelar Elektromekanis",
                "Tegangan Kontrol" to "5V DC (untuk koil)",
                "Tegangan Beban Maksimal" to "250V AC / 30V DC",
                "Arus Beban Maksimal" to "10A",
                "Konfigurasi Kontak" to "SPDT (Single Pole Double Throw) - NC/NO (Normally Closed/Normally Open)",
                "Waktu Pengalihan" to "Sekitar 10ms",
                "Indikator" to "LED indikator status (ON/OFF)",
                "Dimensi" to "43mm x 17mm x 19mm (modul)",
                "Output Data" to "Mengaktifkan/menonaktifkan sirkuit berdaya tinggi"
            ),
            applications = listOf(
                "Mengontrol perangkat AC/DC berdaya tinggi",
                "Otomasi rumah (lampu, kipas)",
                "Sistem keamanan",
                "Kontrol motor",
                "Proyek otomasi industri"
            )
        ),
        Model(
            name = "raspi",
            displayText = "Raspberry Pi",
            specs = listOf(
                "Model" to "Raspberry Pi 4 Model B",
                "Jenis Komponen" to "Komputer Mikro (Single-Board Computer)",
                "Prosesor" to "Broadcom BCM2711, Quad-core Cortex-A72 (ARM v8) 64-bit SoC @ 1.5GHz",
                "RAM" to "2GB/4GB/8GB LPDDR4-3200 SDRAM",
                "Penyimpanan" to "Slot kartu microSD untuk sistem operasi dan data",
                "Konektivitas Nirkabel" to "Dual-band 2.4 GHz dan 5.0 GHz IEEE 802.11ac wireless, Bluetooth 5.0, BLE",
                "Port" to "2x USB 3.0, 2x USB 2.0, Gigabit Ethernet, 2x micro-HDMI (mendukung 4Kp60), MIPI DSI display port, MIPI CSI camera port, 40-pin GPIO header",
                "Sumber Daya" to "5V DC via USB-C (minimal 3A)",
                "Sistem Operasi" to "Raspberry Pi OS (berbasis Debian Linux)",
                "Dimensi" to "85mm x 56mm x 17mm",
                "Output Data" to "Output video (HDMI), I/O digital/analog melalui GPIO, komunikasi jaringan"
            ),
            applications = listOf(
                "Server mini",
                "Pusat media",
                "Robotika",
                "Proyek IoT kompleks",
                "Edukasi dan pengembangan perangkat lunak"
            )
        ),
        Model(
            name = "pir",
            displayText = "PIR Sensor",
            specs = listOf(
                "Model" to "HC-SR501 (Pyroelectric Infrared Sensor)",
                "Jenis Sensor" to "Sensor Gerak Inframerah Pasif",
                "Tegangan Operasional" to "5V hingga 20V DC",
                "Arus Operasional" to "<60uA",
                "Jarak Deteksi" to "3m hingga 7m (dapat disesuaikan)",
                "Sudut Deteksi" to "120°",
                "Waktu Tunda" to "0.5 detik hingga 200 detik (dapat disesuaikan)",
                "Waktu Blokade" to "2.5 detik",
                "Output Data" to "Sinyal digital (HIGH/LOW)",
                "Dimensi" to "32mm x 24mm x 24mm"
            ),
            applications = listOf(
                "Sistem keamanan (detektor penyusup)",
                "Lampu otomatis",
                "Bel pintu otomatis",
                "Penghemat energi",
                "Proyek otomatisasi gerak"
            )
        ),
        Model(
            name = "mq",
            displayText = "MQ Sensor",
            specs = listOf(
                "Model" to "MQ-2 (Gas Sensor Module)",
                "Jenis Sensor" to "Sensor Gas (untuk asap, LPG, butana, propana, metana, alkohol, hidrogen)",
                "Tegangan Operasional" to "5V DC",
                "Arus Pemanas" to "Sekitar 150mA",
                "Konsentrasi Deteksi" to "300ppm hingga 10000ppm (tergantung gas)",
                "Waktu Respon" to "≤10 detik",
                "Waktu Pemulihan" to "≤30 detik",
                "Tipe Output" to "Analog dan Digital",
                "Kepekaan" to "Dapat disesuaikan melalui potensiometer",
                "Dimensi" to "32mm x 20mm x 22mm",
                "Output Data" to "Tegangan analog sebanding dengan konsentrasi gas, sinyal digital jika melebihi ambang batas"
            ),
            applications = listOf(
                "Detektor kebocoran gas",
                "Sistem alarm asap",
                "Monitoring kualitas udara",
                "Ventilasi otomatis",
                "Sistem keamanan industri"
            )
        ),
        Model(
            name = "ldr",
            displayText = "LDR Sensor",
            specs = listOf(
                "Model" to "GL55 Series (Photoresistor / Light Dependent Resistor)",
                "Jenis Sensor" to "Sensor Cahaya (Resistor Bergantung Cahaya)",
                "Tegangan Operasional" to "Tidak ada tegangan operasional langsung (resistansi berubah)",
                "Resistansi dalam Gelap" to ">1MΩ",
                "Resistansi dalam Cahaya" to "10KΩ hingga 20KΩ (tergantung intensitas cahaya)",
                "Waktu Respon" to "Naik: 20ms; Turun: 30ms",
                "Sensitivitas Spektral" to "Mirip dengan mata manusia",
                "Dimensi" to "Beragam, umumnya 5mm hingga 12mm diameter",
                "Output Data" to "Perubahan resistansi (biasanya diubah menjadi tegangan analog menggunakan pembagi tegangan)"
            ),
            applications = listOf(
                "Detektor cahaya",
                "Lampu jalan otomatis",
                "Pengukur intensitas cahaya",
                "Kontrol pencahayaan otomatis",
                "Robot pengikut cahaya"
            )
        ),
        Model(
            name = "ir",
            displayText = "IR Sensor",
            specs = listOf(
                "Model" to "IR Proximity Sensor (misalnya, TCRT5000 atau modul IR)",
                "Jenis Sensor" to "Sensor Inframerah (Proximity / Garis)",
                "Tegangan Operasional" to "3.3V hingga 5V DC",
                "Arus Operasional" to "20mA (emitter LED)",
                "Jarak Deteksi" to "1cm hingga 30cm (tergantung model dan permukaan)",
                "Sudut Deteksi" to "Terbatas (umumnya 15°-30°)",
                "Tipe Output" to "Analog atau Digital",
                "Spektrum IR" to "940nm (inframerah)",
                "Dimensi" to "Beragam (misalnya, modul 32mm x 14mm)",
                "Output Data" to "Perubahan tegangan analog atau sinyal digital (HIGH/LOW)"
            ),
            applications = listOf(
                "Deteksi objek",
                "Robot pengikut garis",
                "Penghitung objek",
                "Sistem keamanan",
                "Deteksi tangan (misalnya, keran otomatis)"
            )
        ),
        Model(
            name = "esp32",
            displayText = "ESP32",
            specs = listOf(
                "Model" to "ESP32 (misalnya, ESP32-WROOM-32)",
                "Jenis Komponen" to "Mikrokontroler dengan Wi-Fi dan Bluetooth",
                "Prosesor" to "Dual-core Tensilica Xtensa LX6",
                "Frekuensi CPU" to "Hingga 240MHz",
                "RAM" to "520KB SRAM",
                "Flash Memory" to "4MB",
                "Konektivitas Nirkabel" to "Wi-Fi 802.11 b/g/n, Bluetooth v4.2 BR/EDR dan BLE",
                "Antarmuka" to "GPIO, UART, SPI, I2C, I2S, ADC, DAC, PWM, Hall Sensor, Sensor Suhu",
                "Tegangan Operasional" to "2.2V hingga 3.6V DC",
                "Arus Operasional" to "Sekitar 80mA (Wi-Fi aktif), <10uA (mode tidur)",
                "Sistem Operasi" to "FreeRTOS (umumnya)",
                "Dimensi" to "Beragam (misalnya, modul 18mm x 25.5mm)",
                "Output Data" to "Komunikasi nirkabel (TCP/IP, UDP), I/O digital/analog"
            ),
            applications = listOf(
                "Proyek IoT lanjutan",
                "Jaringan sensor nirkabel",
                "Otomasi rumah pintar",
                "Wearable electronics",
                "Pengembangan aplikasi berbasis cloud"
            )
        ),
        Model(
            name = "cam",
            displayText = "ESP32-CAM",
            specs = listOf(
                "Model" to "ESP32-CAM (AI-Thinker ESP32-CAM)",
                "Jenis Komponen" to "Mikrokontroler dengan Wi-Fi, Bluetooth, dan Modul Kamera",
                "Prosesor" to "Dual-core Tensilica Xtensa LX6",
                "Frekuensi CPU" to "Hingga 240MHz",
                "RAM" to "520KB SRAM + 4MB PSRAM",
                "Flash Memory" to "4MB",
                "Kamera yang Didukung" to "OV2640 (2MP) atau OV7670 (0.3MP)",
                "Konektivitas Nirkabel" to "Wi-Fi 802.11 b/g/n, Bluetooth v4.2 BR/EDR dan BLE",
                "Antarmuka" to "GPIO, UART, SPI, I2C, I2S, ADC, PWM",
                "Tegangan Operasional" to "5V DC",
                "Arus Operasional" to "180mA (LED flash bisa hingga 300mA)",
                "Dimensi" to "27mm x 40.5mm x 4.5mm",
                "Output Data" to "Streaming video (MJPEG), gambar, I/O digital/analog"
            ),
            applications = listOf(
                "Sistem keamanan (kamera IP)",
                "Pemantauan video",
                "Pengenalan wajah/objek (dengan AI)",
                "Proyek IoT dengan visual",
                "Drone dan robot dengan kamera"
            )
        ),
        Model(
            name = "buzzer",
            displayText = "Active Buzzer",
            specs = listOf(
                "Model" to "Buzzer Aktif (Active Buzzer Module)",
                "Jenis Komponen" to "Perangkat Penghasil Suara",
                "Tegangan Operasional" to "3.3V hingga 5V DC",
                "Arus Operasional" to "<30mA",
                "Frekuensi Suara" to "Tetap (biasanya 2kHz - 4kHz)",
                "Jenis Suara" to "Nada tunggal, terus menerus (dibandingkan buzzer pasif yang membutuhkan PWM untuk nada)",
                "Dimensi" to "18.5mm x 15mm",
                "Output Data" to "Suara (berbunyi atau tidak berbunyi)"
            ),
            applications = listOf(
                "Alarm sederhana",
                "Indikator status (misalnya, boot lengkap)",
                "Notifikasi kesalahan",
                "Pengingat waktu",
                "Mainan edukasi"
            )
        ),
        Model(
            name = "arduino",
            displayText = "Arduino Uno",
            specs = listOf(
                "Model" to "Arduino Uno R3",
                "Jenis Komponen" to "Papan Mikrokontroler",
                "Mikrokontroler" to "ATmega328P",
                "Tegangan Operasional" to "5V",
                "Tegangan Input (Disarankan)" to "7V hingga 12V DC",
                "Tegangan Input (Batas)" to "6V hingga 20V DC",
                "Pin I/O Digital" to "14 (6 di antaranya dapat digunakan sebagai output PWM)",
                "Pin Input Analog" to "6",
                "Arus DC per Pin I/O" to "20mA",
                "Arus DC untuk Pin 3.3V" to "50mA",
                "Memori Flash" to "32KB (ATmega328P)",
                "SRAM" to "2KB (ATmega328P)",
                "EEPROM" to "1KB (ATmega328P)",
                "Kecepatan Clock" to "16MHz",
                "Konektivitas" to "USB (untuk pemrograman dan komunikasi serial), ICSP header",
                "Dimensi" to "68.6mm x 53.4mm",
                "Output Data" to "I/O digital/analog, komunikasi serial (UART)"
            ),
            applications = listOf(
                "Proyek prototipe elektronik",
                "Robotika sederhana",
                "Sistem otomasi dasar",
                "Proyek hobi dan edukasi",
                "Antarmuka sensor dan aktuator"
            )
        )
    )
} 