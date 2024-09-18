package alexschool.bookreader.ui.settings

import alexschool.bookreader.R
import alexschool.bookreader.data.AlexSchoolPrefManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ih.tools.readingpad.ui.UIStateViewModel
import ih.tools.readingpad.util.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
) {
//    LaunchedEffect(key1 = true) {
//        viewModel.eventFlow.collectLatest { event ->
//            when (event) {
//                is BookContentViewModel.UiEvent.NavigateBack -> {
//                    navController.navigateUp()
//                }
//            }
//        }
//    }

    val context = LocalContext.current
    val prefManager = AlexSchoolPrefManager(context)
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf(prefManager.getLanguage()) }
    val languages = listOf(
        stringResource(R.string.english), stringResource(R.string.arabic)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings_icon)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.settings))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    })
                    {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        }
    ) { values ->
        Column (
            Modifier
                .padding(values)
                .fillMaxSize()
        ) {
            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ){
                Text(text = stringResource(R.string.choose_app_language))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                    modifier = Modifier.weight(3f)
                ) {
                    TextField(
                        value = selectedLanguage,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        languages.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    selectedLanguage = selectionOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    modifier = Modifier.weight(1.5f),
                    onClick = {
                        prefManager.setLanguage(selectedLanguage)
                        updateAppLocale(context ,selectedLanguage)

                        //navController.navigate(Screens.HomeScreen.route)
                        // navController.popBackStack()
                    }) {
                    Text(stringResource(R.string.apply))
                }
            }



        }
//        LazyColumn(
//            Modifier
//                .fillMaxSize()
//                .padding(values)
//        ) {
//            item {
//                FontSizeSetting(viewModel = viewModel)
//                FontColorSetting(viewModel = viewModel)
//                BackgroundColorSetting(viewModel = viewModel)
//                FontWeightSetting(viewModel = viewModel)
//            }
//        }
    }
}

@Composable
fun FontSizeSetting(
    uiStateViewModel: UIStateViewModel
) {
    val fontSize = uiStateViewModel.uiSettings.collectAsState().value.fontSize
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            "Font Size",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Slider(
            value = fontSize,
            onValueChange = { newFontSize ->
                uiStateViewModel.setFontSize(newFontSize, 0f)
            },
            valueRange = 12f..42f,
            steps = 24
        )
        Text(text = "Current Font Size: ${fontSize.toInt()}")
    }
}

@Composable
fun FontColorSetting(
    uiStateViewModel: UIStateViewModel
) {
    val fontColor = uiStateViewModel.uiSettings.collectAsState().value.fontColor

    val availableFontColors = listOf(
        Color.Black,
        Color.Gray,
        Color.DarkGray,
        Color.Red,
        Color.Blue,
        Color.Green
        // Add more colors as needed
    )

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Font Color", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            availableFontColors.forEach { color ->
                ColorOptionButton(
                    color = color,
                    isSelected = fontColor == color.toArgb(),
                    onClick = {
                        uiStateViewModel.setFontColor(color.toArgb())

                    }
                )
            }
        }
    }
}

@Composable
fun ColorOptionButton(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color)
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}


@Composable
fun BackgroundColorSetting(
    uiStateViewModel: UIStateViewModel
) {
    val backgroundColor = uiStateViewModel.uiSettings.collectAsState().value.backgroundColor
    val availableBackgroundColors = listOf(
        Color.White, // Classic white background
        Color(0xFFCFCDCD), // Light gray background
        Color(0xFFEBD69F), // Beige background
        Color(0xFFE6FAE8) // Linen background
        // Add more colors as needed
    )

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            "Background Color",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            availableBackgroundColors.forEach { color ->
                ColorOptionButton(
                    color = color,
                    isSelected = backgroundColor == color.toArgb(),
                    onClick = {
                        uiStateViewModel.setBackgroundColor(color.toArgb())
                    }
                )
            }
        }
    }
}

@Composable
fun FontWeightSetting(
    uiStateViewModel: UIStateViewModel
) {
    val context = LocalContext.current
    val savedFontWeight = uiStateViewModel.uiSettings.collectAsState().value.fontWeight
    var selectedFontWeight: FontWeight by remember { mutableStateOf(FontWeight(savedFontWeight)) }
//    val fontSize by viewModel.fontSize.collectAsState()
//    val fontColor by viewModel.fontColor.collectAsState()
    val fontSize = uiStateViewModel.uiSettings.collectAsState().value.fontSize
    val fontColor = uiStateViewModel.uiSettings.collectAsState().value.fontColor
    val backgroundColor = uiStateViewModel.uiSettings.collectAsState().value.backgroundColor
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Font Weight",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Font weight options

        FontWeightOption(
            label = "Normal",
            fontWeight = FontWeight.Normal,
            selectedFontWeight = selectedFontWeight,
            onFontWeightSelected = { selectedFontWeight = it }
        )
        FontWeightOption(
            label = "Bald",
            fontWeight = FontWeight.Bold,
            selectedFontWeight = selectedFontWeight,
            onFontWeightSelected = { selectedFontWeight = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Font weight preview
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color(backgroundColor))
        ) {
            Text(
                text = "Preview text",
                fontFamily = FontFamily.SansSerif,
                fontWeight = selectedFontWeight,
                color = Color(fontColor),
                fontSize = fontSize.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save button
        Button(
            onClick = {
                uiStateViewModel.setFontWeight(selectedFontWeight.weight)
                showToast(context = context, "Font weight saved")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save font weight", fontSize = 24.sp)
        }
    }
}

@Composable
fun FontWeightOption(
    label: String,
    fontWeight: FontWeight,
    selectedFontWeight: FontWeight,
    onFontWeightSelected: (FontWeight) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFontWeightSelected(fontWeight) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedFontWeight == fontWeight,
            onClick = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, fontWeight = fontWeight)
    }
}

