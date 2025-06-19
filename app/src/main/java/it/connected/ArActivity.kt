package it.connected

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.ar.core.Config
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import it.connected.theme.ARMenuTheme
import it.connected.theme.ButtonColor
import it.connected.theme.TopBarColor

class ArActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val modelName = intent.getStringExtra("modelName")
        setContent {
            ARMenuTheme {
                AppNavigation(
                    initialModelName = modelName,
                    onFinishActivity = { this.finish() }
                )
            }
        }
    }
}

@Composable
fun AppNavigation(initialModelName: String?, onFinishActivity: () -> Unit) {
    val navController = rememberNavController()
    val itemsList = remember { getModelList() }
    val initialIndex = remember(initialModelName, itemsList) {
        itemsList.indexOfFirst { it.name == initialModelName }.coerceAtLeast(0)
    }

    NavHost(navController = navController, startDestination = "ar_view") {
        composable("ar_view") {
            IotApp(
                navController = navController,
                itemsList = itemsList,
                initialIndex = initialIndex,
                onBack = onFinishActivity
            )
        }
        composable("model_detail/{modelName}") { backStackEntry ->
            val modelName = backStackEntry.arguments?.getString("modelName")
            val model = itemsList.firstOrNull { it.name == modelName }
            if (model != null) {
                ModelDetailScreen(navController = navController, model = model)
            } else {
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun IotApp(
    navController: NavController,
    itemsList: List<Model>,
    initialIndex: Int,
    onBack: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(initialIndex) }
    val currentModel = itemsList[currentIndex]

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth(),
                color = TopBarColor,
                contentColor = Color.White,
                elevation = AppBarDefaults.TopAppBarElevation
            ) {
                Box(Modifier.fillMaxSize()) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp)
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                    IconButton(
                        onClick = { navController.navigate("model_detail/${currentModel.name}") },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                    ) {
                        Icon(Icons.Filled.Info, contentDescription = "Model Info")
                    }
                    Text(
                        text = "Model Viewer",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ARScreen(currentModel.name)
            Menu(
                modifier = Modifier.align(Alignment.BottomCenter),
                items = itemsList,
                currentIndex = currentIndex,
                onIndexChange = { newIndex ->
                    currentIndex = newIndex
                }
            )
        }
    }
}

@Composable
fun Menu(
    modifier: Modifier,
    items: List<Model>,
    currentIndex: Int,
    onIndexChange: (Int) -> Unit
) {
    fun updateIndex(offset: Int) {
        val newIndex = (currentIndex + offset + items.size) % items.size
        onIndexChange(newIndex)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background.copy(alpha = 0.8f))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = { updateIndex(-1) },
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.circle_arrow_left),
                    contentDescription = "previous",
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = items[currentIndex].displayText,
                style = MaterialTheme.typography.subtitle1,
            )

            IconButton(
                onClick = { updateIndex(1) },
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.circle_arrow_right),
                    contentDescription = "next",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun ARScreen(model: String) {
    val nodes = remember { mutableListOf<ArNode>() }
    val modelNode = remember { mutableStateOf<ArModelNode?>(null) }
    var isModelPlaced by remember { mutableStateOf(false) }

    var scale by remember { mutableStateOf(1.0f) }
    var rotationX by remember { mutableStateOf(0f) }
    var rotationY by remember { mutableStateOf(0f) }
    var rotationZ by remember { mutableStateOf(0f) }

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, rotation ->
                if (isModelPlaced) {
                    scale *= zoom
                    modelNode.value?.scale = Scale(scale)

                    rotationX += pan.y * 0.5f
                    rotationY += pan.x * 0.5f
                    rotationZ += rotation
                    modelNode.value?.rotation = Rotation(rotationX, rotationY, rotationZ)
                }
            }
        }
    ) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = { arSceneView ->
                arSceneView.lightEstimationMode = Config.LightEstimationMode.DISABLED
                arSceneView.planeRenderer.isShadowReceiver = false
                modelNode.value = ArModelNode(arSceneView.engine, PlacementMode.INSTANT).apply {
                    loadModelGlbAsync(
                        glbFileLocation = "models/${model}.glb",
                        scaleToUnits = 0.5f
                    )
                    onAnchorChanged = {
                        isModelPlaced = isAnchored
                    }
                    onHitResult = { node, _ ->
                        if (!node.isAnchored) {
                            node.anchor()
                        }
                    }
                }
                nodes.add(modelNode.value!!)
            }
        )
    }

    LaunchedEffect(key1 = model) {
        modelNode.value?.detachAnchor()
        isModelPlaced = false
        modelNode.value?.loadModelGlbAsync(
            glbFileLocation = "models/${model}.glb",
            scaleToUnits = 0.5f
        )
    }
}

@Composable
fun ModelDetailScreen(navController: NavController, model: Model) {
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(64.dp))
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
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor)
            ) {
                Text(text = "Back to AR View", color = Color.White)
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