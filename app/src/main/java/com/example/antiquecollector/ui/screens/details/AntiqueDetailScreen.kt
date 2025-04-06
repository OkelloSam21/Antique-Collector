package com.example.antiquecollector.ui.screens.details

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.antiquecollector.R
import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.model.Condition
import com.example.antiquecollector.domain.model.MuseumArtifact
import com.example.antiquecollector.util.ArtifactId
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AntiqueDetailScreen(
    artifactId: ArtifactId,
    onNavigateUp: () -> Unit,
    onShareAntique: () -> Unit,
    onEditAntique: (String) -> Unit,
    viewModel: AntiqueDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        Log.d("Artifact details screen" ,"the argument received is $artifactId")
        viewModel.loadAntique(artifactId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Item Details", color = Color(0xFF8B4513)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF8B4513))
                    }
                },
                actions = {
                    IconButton(onClick = onShareAntique) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color(0xFF8B4513))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        when {
            uiState.antique != null -> {
                AntiqueDetailContent(
                    antique = uiState.antique!!,
                    onEditAntique = {
                        // onEditAntique(uiState.antique!!.id) // Check your edit logic with the correct ID type
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            uiState.museumArtifact != null -> {
                MuseumArtifactDetailContent(
                    museumArtifact = uiState.museumArtifact!!,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF8B4513))
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${uiState.error}", color = Color.Red)
                }
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Item not found", color = Color.Gray)
                }
            }
        }
    }
}



@Composable
fun AntiqueDetailContent(
    antique: Antique,
    onEditAntique: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Main image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = antique.images.first(),
                    error = painterResource(id = R.drawable.place_holder_image)
                ),
                contentDescription = antique.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Title and category
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = antique.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = antique.category?.name ?: "Uncategorized",
                    fontSize = 16.sp,
                    color = Color(0xFF8B4513),
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = "•",
                    fontSize = 16.sp,
                    color = Color(0xFF8B4513),
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF8B4513),
                    modifier = Modifier.size(16.dp)
                )

                Text(
                    text = "Acquired ${antique.acquisitionDate}",
                    fontSize = 16.sp,
                    color = Color(0xFF8B4513),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        // Current value and condition
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF6F0)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Current Value",
                            fontSize = 16.sp,
                            color = Color(0xFF8B4513)
                        )

                        Text(
                            text = formatCurrency(antique.currentValue),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8B4513)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Condition",
                            fontSize = 16.sp,
                            color = Color(0xFF8B4513)
                        )

                        RatingBar(
                            rating = antique.condition,
                            maxRating = 5
                        )
                    }
                }

                Text(
                    text = "Last appraised: ${antique.lastModified}",
                    fontSize = 14.sp,
                    color = Color(0xFF8B4513),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // About this item
        SectionTitle(title = "About This Item")

        Text(
            text = antique.description.toString(),
            fontSize = 16.sp,
            color = Color(0xFF8B4513),
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Historical context
        SectionTitle(title = "Historical Context")

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF6F0)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = antique.description ?: "No historical context provided.",
                fontSize = 16.sp,
                color = Color(0xFF8B4513),
                lineHeight = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Edit button
        Button(
            onClick = onEditAntique,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8B4513)
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = "Edit Item Details",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Bottom space for better UX
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF8B4513),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun RatingBar(
    rating: Condition,
    maxRating: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i < rating.stars) Icons.Filled.StarBorder else Icons.Filled.Star,
                contentDescription = null,
                tint = Color(0xFF8B4513),
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 2.dp)
            )
        }
    }
}

fun formatCurrency(value: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(value)
}




@Composable
fun MuseumArtifactDetailContent(
    museumArtifact: MuseumArtifact,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Adapt your UI elements to display museumArtifact data
        // Example structure; adjust based on your MuseumArtifact data class

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = museumArtifact.additionalImageUrls.first(), // Assuming your model has imageUrl
                    error = painterResource(id = R.drawable.place_holder_image)
                ),
                contentDescription = museumArtifact.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = museumArtifact.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
            // Add more Text and UI elements to display other museumArtifact properties
            // e.g.,
            Text(
                text = museumArtifact.reign ?: "",
                fontSize = 16.sp,
                color = Color(0xFF8B4513),
                modifier = Modifier.padding(top = 4.dp)
            )
            // ... and so on for other fields like museumArtifact.description, etc.
        }
        // Add more sections and UI components as needed to display all relevant info
    }
}