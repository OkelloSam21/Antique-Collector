package com.example.antiquecollector.ui.screens.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.antiquecollector.R
import com.example.antiquecollector.domain.model.Category
import com.example.antiquecollector.domain.model.MuseumArtifact
import com.example.antiquecollector.ui.components.CategoryIconMap

@Composable
fun ExploreScreen(
    onNavigateToArtifactDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory: (Long) -> Unit,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F3E8)) // Cream background
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            SearchBar(
                onSearchClick = onNavigateToSearch,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            // Featured Collection
            FeaturedCollectionSection(
                featuredArtifacts = uiState.featuredArtifacts,
                onArtifactClick = onNavigateToArtifactDetail
            )

            // Browse by Category
            CategorySection(
                onCategoryClick = onNavigateToCategory,
                categories = uiState.categories
            )

            // Popular Artifacts
            PopularArtifactsSection(
                popularArtifacts = uiState.popularArtifacts,
                onArtifactClick = onNavigateToArtifactDetail
            )

            Spacer(modifier = Modifier.height(80.dp)) // Space for bottom navigation
        }
    }
}

@Composable
fun SearchBar(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = "",
        onValueChange = { },
        placeholder = { Text("Search artifacts, periods, cultures...") },
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSearchClick),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        enabled = false,
        colors = TextFieldDefaults.colors().copy(
//            disabledBorderColor = Color.LightGray,
//            focusedBorderColor = Color.LightGray,
            disabledTextColor = Color.Black,
            disabledPlaceholderColor = Color.Gray,
            focusedContainerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp, bottom = 16.dp)
    )
}

@Composable
fun FeaturedCollectionSection(
    featuredArtifacts: List<MuseumArtifact>,
    onArtifactClick: (String) -> Unit
) {
    SectionTitle(title = "Featured Collection")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(featuredArtifacts) { artifact ->
            FeaturedArtifactCard(
                artifact = artifact,
                onClick = { onArtifactClick(artifact.id) }
            )
        }
    }
}

@Composable
fun FeaturedArtifactCard(
    artifact: MuseumArtifact,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = artifact.primaryImageUrl,
                        error = painterResource(id = R.drawable.place_holder_image)
                    ),
                    contentDescription = artifact.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = artifact.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val description = buildString {
                    append(artifact.medium ?: "")
                    if (!artifact.medium.isNullOrBlank() && !artifact.culture.isNullOrBlank()) {
                        append(" with ")
                    }
                    if (!artifact.culture.isNullOrBlank()) {
                        append(artifact.culture)
                        append(" influence")
                    }
                }

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )

                val periodText = artifact.period ?: artifact.objectDate ?: ""
                if (periodText.isNotBlank()) {
                    Text(
                        text = periodText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySection(
    onCategoryClick: (Long) -> Unit,
    categories: List<Category>
) {
    SectionTitle(title = "Browse by Category")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { (iconRes, name, categoryId) ->
            CategoryItem(
                category = Category(iconRes, name, categoryId),
                onCategoryClick = onCategoryClick
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onCategoryClick: (Long) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable { onCategoryClick(category.id)}
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFFF2EDE4))
        ) {
            Icon(
                painter = painterResource(id = CategoryIconMap.getIconRes(category.iconName)),
                contentDescription = CategoryIconMap.getIconRes(category.name).toString(),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = CategoryIconMap.getIconRes(category.name).toString(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun PopularArtifactsSection(
    popularArtifacts: List<MuseumArtifact>,
    onArtifactClick: (String) -> Unit
) {
    SectionTitle(title = "Popular Artifacts")

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        // Display popular artifacts in a grid (2 columns)
        for (i in popularArtifacts.indices step 2) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First item in the row
                PopularArtifactCard(
                    artifact = popularArtifacts[i],
                    onClick = { onArtifactClick(popularArtifacts[i].id) },
                    modifier = Modifier.weight(1f)
                )

                // Second item in the row (if exists)
                if (i + 1 < popularArtifacts.size) {
                    PopularArtifactCard(
                        artifact = popularArtifacts[i + 1],
                        onClick = { onArtifactClick(popularArtifacts[i + 1].id) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    // Empty space to maintain the grid
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun PopularArtifactCard(
    artifact: MuseumArtifact,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = artifact.primaryImageUrl,
                        error = painterResource(id = R.drawable.place_holder_image)
                    ),
                    contentDescription = artifact.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = artifact.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val subtext = buildString {
                    if (!artifact.period.isNullOrBlank()) {
                        append(artifact.period)
                    } else if (!artifact.objectDate.isNullOrBlank()) {
                        append(artifact.objectDate)
                    }
                }

                if (subtext.isNotBlank()) {
                    Text(
                        text = subtext,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                val locationText = artifact.culture ?: artifact.department ?: ""
                if (locationText.isNotBlank()) {
                    Text(
                        text = locationText,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}