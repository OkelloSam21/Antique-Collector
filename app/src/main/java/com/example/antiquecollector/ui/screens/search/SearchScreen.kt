package com.example.antiquecollector.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.antiquecollector.R
import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.model.MuseumArtifact
import com.example.antiquecollector.util.ArtifactId
import com.example.antiquecollector.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToArtifactDetail: (ArtifactId) -> Unit,
    sourceType: SearchSourceType,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Set search source type when screen is first composed
    LaunchedEffect(sourceType) {
        viewModel.setSearchSourceType(sourceType)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Search bar at the top
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = { viewModel.onSearchQueryChange(it) },
            onSearch = { viewModel.onSearchQueryChange(it) },
            active = true,
            onActiveChange = { if (!it) onNavigateBack() },
            placeholder = { 
                Text(
                    if (sourceType == SearchSourceType.LOCAL) 
                        "Search your collection..." 
                    else 
                        "Search artifacts, periods, cultures..."
                ) 
            },
            leadingIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            trailingIcon = {
                if (uiState.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.clearSearch() }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {}
        
        // Status indicators
        if (uiState.isSearching) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        if (uiState.error != null) {
            Text(
                text = uiState.error ?: "",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
        
        // Search results
        if (!uiState.isSearching && uiState.searchQuery.isNotEmpty()) {
            if (uiState.localResults.isEmpty() && uiState.remoteResults.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No results found for \"${uiState.searchQuery}\"")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (uiState.localResults.isNotEmpty()) {
                        item {
                            Text(
                                "My Collection",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        
                        items(uiState.localResults) { antique ->
                            AntiqueSearchResultItem(
                                antique = antique,
                                onAntiqueClick = { 
                                    onNavigateToArtifactDetail(ArtifactId.Local(antique.id))
                                },
                                currencyFormatter = CurrencyFormatter()
                            )
                        }
                    }
                    
                    if (uiState.remoteResults.isNotEmpty()) {
                        item {
                            Text(
                                "Museum Artifacts",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        
                        items(uiState.remoteResults) { artifact ->
                            ArtifactSearchResultItem(
                                artifact = artifact,
                                onArtifactClick = {
                                    onNavigateToArtifactDetail(ArtifactId.Remote(artifact.id))
                                }
                            )
                        }
                    }
                }
            }
        } else if (!uiState.isSearching && uiState.searchQuery.isEmpty()) {
            // Show search suggestions/history here if you want
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Search for antiques or artifacts...")
            }
        }
    }
}

@Composable
fun AntiqueSearchResultItem(
    antique: Antique,
    onAntiqueClick: () -> Unit,
    currencyFormatter: CurrencyFormatter
) {
    ListItem(
        headlineContent = { Text(antique.name) },
        supportingContent = {
            Text(
                currencyFormatter.formatCurrency(antique.currentValue),
                color = MaterialTheme.colorScheme.primary
            )
        },
        leadingContent = {
            if (antique.images.isNotEmpty()) {
                AsyncImage(
                    model = antique.images.first(),
                    contentDescription = antique.name,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.onboarding_track),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        },
        modifier = Modifier
            .clickable(onClick = onAntiqueClick)
            .fillMaxWidth()
    )
}

@Composable
fun ArtifactSearchResultItem(
    artifact: MuseumArtifact,
    onArtifactClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(artifact.title) },
        supportingContent = {
            val details = buildString {
                if (!artifact.period.isNullOrBlank()) {
                    append(artifact.period)
                } else if (!artifact.objectDate.isNullOrBlank()) {
                    append(artifact.objectDate)
                }
                if (!artifact.culture.isNullOrBlank()) {
                    if (isNotEmpty() && !endsWith(", ")) append(", ")
                    append(artifact.culture)
                }
            }
            Text(details)
        },
        leadingContent = {
            AsyncImage(
                model = artifact.primaryImageUrl,
                contentDescription = artifact.title,
                error = painterResource(id = R.drawable.place_holder_image),
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        },
        modifier = Modifier
            .clickable(onClick = onArtifactClick)
            .fillMaxWidth()
    )
}