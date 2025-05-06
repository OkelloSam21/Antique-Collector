package com.example.antiquecollector.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.antiquecollector.R
import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.model.Category
import com.example.antiquecollector.ui.components.CategoryIconMap
import com.example.antiquecollector.ui.components.DonutChart
import com.example.antiquecollector.ui.theme.AntiqueCollectorTheme
import com.example.antiquecollector.util.ArtifactId
import com.example.antiquecollector.util.CurrencyFormatter
import com.example.antiquecollector.util.DateUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (ArtifactId) -> Unit,
    onNavigateToCategory: (Long) -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAddItem: () -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    currencyFormatter: CurrencyFormatter = CurrencyFormatter(),
    dateUtils: DateUtils = DateUtils()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.my_collection)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { onNavigateToSettings() }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already on Collection screen */ },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_art),
                            contentDescription = "Collection"
                        )
                    },
                    label = { Text("Collection") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToExplore() },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_explore),
                            contentDescription = "Explore"
                        )
                    },
                    label = { Text("Explore") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToSettings,
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_settings),
                            contentDescription = "Settings"
                        )
                    },
                    label = { Text("Settings") }
                )
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            CollectionContent(
                paddingValues = paddingValues,
                uiState = uiState,
                onAntiqueClick = onNavigateToDetail,
                onCategoryClick = onNavigateToCategory,
                onNavigateToSearch = onNavigateToSearch,
                onAddItemClick = onNavigateToAddItem,
                currencyFormatter = currencyFormatter,
                dateUtils = dateUtils
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollectionContent(
    paddingValues: PaddingValues,
    uiState: HomeUiState,
    onAntiqueClick: (ArtifactId) -> Unit,
    onCategoryClick: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    onAddItemClick: () -> Unit,
    currencyFormatter: CurrencyFormatter,
    dateUtils: DateUtils
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF5F5F5))
    ) {
        // Search Box
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onNavigateToSearch() },
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Search your collection...",
                    color = Color.Gray
                )
            }
        }

        // Collection Statistics
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Total Items: ${uiState.statistics.totalItems}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Estimated Value: ${currencyFormatter.formatCurrency(uiState.statistics.totalValue)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Donut chart for category distribution
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(4.dp)
                ) {
                    if (uiState.statistics.categoryCounts.isNotEmpty()) {
                        DonutChart(
                            data = uiState.statistics.categoryCounts.entries.map {
                                it.key.name to it.value.toFloat()
                            }
                        )
                    }
                }
            }
        }

        // Recent Additions
        Text(
            text = "Recent Additions",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.recentAntiques.take(5)) { antique ->
                RecentAntiqueCard(
                    antique = antique,
                    onAntiqueClick = {
                        Log.d("Recent Addition click", "Recent addition click id is ${antique.id}")
                        onAntiqueClick(ArtifactId.Local(antique.id))
                    },
                    currencyFormatter = currencyFormatter,
                    dateUtils = dateUtils
                )
            }
        }

        // Categories
        Text(
            text = "Categories",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Log.d("HomeScreen", "Categories: ${uiState.categories}")


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height((uiState.categories.size * 56).dp + 100.dp.coerceAtMost(350.dp))
        ) {
            items(uiState.categories) { category ->
                Log.d("HomeScreen", "Category: $category")
                CategoryCard(
                    category = category,
                    onCategoryClick = onCategoryClick
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Action buttons at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FloatingActionButton(
                onClick = onAddItemClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }

            FloatingActionButton(
                onClick = { /* Report */ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_document),
                    contentDescription = "Report"
                )
            }

            FloatingActionButton(
                onClick = { /* Share */ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = "Share"
                )
            }
        }
    }

}

@Composable
fun RecentAntiqueCard(
    antique: Antique,
    onAntiqueClick: (Long) -> Unit,
    currencyFormatter: CurrencyFormatter,
    dateUtils: DateUtils
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable { onAntiqueClick(antique.id) },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            // Antique Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                if (antique.images.isNotEmpty()) {
                    AsyncImage(
                        model = antique.images.first(),
                        contentDescription = antique.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.onboarding_track),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            }

            // Antique Info
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = antique.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Acquired ${dateUtils.formatShortDate(antique.acquisitionDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Text(
                    text = currencyFormatter.formatCurrency(antique.currentValue),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onCategoryClick: (Long) -> Unit
) {
    Log.d("CategoryCard", "Category: $category")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onCategoryClick(category.id) },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = CategoryIconMap.getIconRes(category.iconName)),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${category.itemCount} items",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview(modifier: Modifier = Modifier) {
    AntiqueCollectorTheme {
        HomeScreen(
            onNavigateToDetail = {},
            onNavigateToCategory = {},
            onNavigateToExplore = {},
            onNavigateToSettings = {},
            onNavigateToAddItem = {},
            onNavigateToSearch = {},
            currencyFormatter = CurrencyFormatter(),
            dateUtils = DateUtils()
        )
    }
}