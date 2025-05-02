package com.bestamibakir.visualgo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bestamibakir.visualgo.viewmodel.SortingAlgorithm
import com.bestamibakir.visualgo.viewmodel.SortingViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingScreen(
    viewModel: SortingViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Logo",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "VisuALGO",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
//                    Text(
//                        text = "Sıralama Algoritmaları",
//                        style = MaterialTheme.typography.titleMedium,
//                        modifier = Modifier.padding(end = 16.dp)
//                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(8.dp))
            
            AlgorithmSelector(
                currentAlgorithm = viewModel.currentAlgorithm,
                onAlgorithmSelected = { viewModel.currentAlgorithm = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SortingVisualizer(
                numbers = viewModel.numbers,
                comparingIndices = viewModel.comparingIndices,
                swappingIndices = viewModel.swappingIndices,
                currentOperation = viewModel.currentOperation,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Yavaş")
                Slider(
                    value = viewModel.sortingSpeed,
                    onValueChange = { viewModel.sortingSpeed = it },
                    valueRange = 50f..950f,
                    modifier = Modifier.weight(1f)
                )
                Text("Hızlı")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.generateRandomArray() },
                    enabled = !viewModel.isSorting
                ) {
                    Text("Rastgele Liste Oluştur")
                }

                if (viewModel.isSorting) {
                    Button(
                        onClick = { viewModel.stopSorting() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = "Durdur")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Durdur")
                    }
                } else {
                    Button(
                        onClick = { viewModel.startSorting() }
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Başlat")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sıralamayı Başlat")
                    }
                }
            }
        }
    }
}


@Composable
fun AlgorithmSelector(
    currentAlgorithm: SortingAlgorithm,
    onAlgorithmSelected: (SortingAlgorithm) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text("Algoritma Seçiniz:")
        Box {
            Button(onClick = { expanded = true }) {
                Text(currentAlgorithm.displayName)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                SortingAlgorithm.entries.forEach { algorithm ->
                    DropdownMenuItem(
                        text = { Text(algorithm.displayName) },
                        onClick = {
                            onAlgorithmSelected(algorithm)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}