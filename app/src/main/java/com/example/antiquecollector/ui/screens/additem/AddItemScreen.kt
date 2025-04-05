package com.example.antiquecollector.ui.screens.additem

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.antiquecollector.R
import com.example.antiquecollector.domain.model.Category
import com.example.antiquecollector.domain.model.Condition
import com.example.antiquecollector.ui.components.ConditionRatingBar
import com.example.antiquecollector.util.DateUtils
import com.example.antiquecollector.util.ImageUtils
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    onNavigateUp: () -> Unit,
    onSaveComplete: () -> Unit,
    viewModel: AddItemViewModel = hiltViewModel(),
    dateUtils: DateUtils = DateUtils(),
    imageUtils: ImageUtils = ImageUtils(LocalContext.current)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    
    val context = LocalContext.current
    
    // For date picker
    var showDatePicker by remember { mutableStateOf(false) }
    
    // For category dropdown
    var expandedCategoryDropdown by remember { mutableStateOf(false) }
    
    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Save the image and get a persistent URI
            scope.launch {
                val savedUri = imageUtils.saveImage(it).toString()
                viewModel.onPhotoAdded(savedUri)
            }
        }
    }
    
    // Show error if any
    LaunchedEffect(error) {
        error?.let {
            // Show error message (you can use a SnackbarHost or other notification method)
            viewModel.clearError()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_new_item)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF9F3E8)) // Background cream color
        ) {
            // Form content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Photo section
                PhotoSection(
                    photos = uiState.photos,
                    onAddPhotoClick = { imagePickerLauncher.launch("image/*") }
                )
                
                // Name field
                FormField(
                    label = stringResource(R.string.item_name) + " *",
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    placeholder = stringResource(R.string.enter_item_name)
                )
                
                // Category dropdown
                CategoryDropdown(
                    label = stringResource(R.string.category) + " *",
                    categories = categories,
                    selectedCategoryId = uiState.categoryId,
                    expanded = expandedCategoryDropdown,
                    onExpandedChange = { expandedCategoryDropdown = it },
                    onCategorySelected = viewModel::onCategoryChange
                )
                
                // Acquisition date
                DateField(
                    label = stringResource(R.string.acquisition_date) + " *",
                    date = uiState.acquisitionDate,
                    onDateClick = { showDatePicker = true },
                    dateUtils = dateUtils
                )
                
                // Current value
                FormField(
                    label = stringResource(R.string.current_value) + " *",
                    value = uiState.valueText,
                    onValueChange = viewModel::onValueChange,
                    placeholder = stringResource(R.string.enter_value),
                    keyboardType = KeyboardType.Decimal,
                    leadingIcon = { Text("$") }
                )
                
                // Condition rating
                ConditionSection(
                    label = stringResource(R.string.condition),
                    condition = uiState.condition,
                    onConditionChange = viewModel::onConditionChange
                )
                
                // Description
                FormField(
                    label = stringResource(R.string.description),
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    placeholder = stringResource(R.string.enter_description),
                    singleLine = false,
                    minHeight = 120.dp
                )
                
                // Location
                FormField(
                    label = stringResource(R.string.location) + " *",
                    value = uiState.location,
                    onValueChange = viewModel::onLocationChange,
                    placeholder = stringResource(R.string.enter_location),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                
                // Save button
                Button(
                    onClick = { viewModel.saveItem(onSaveComplete) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    enabled = !isLoading
                ) {
                    Text(stringResource(R.string.save_item))
                }
                
                // Cancel button
                TextButton(
                    onClick = onNavigateUp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Show loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    
    // Date picker dialog
    if (showDatePicker) {
        com.example.antiquecollector.ui.components.DatePickerDialog (
            initialDate = uiState.acquisitionDate ?: Date(),
            onDateSelected = {
                viewModel.onAcquisitionDateChange(it)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
fun PhotoSection(
    photos: List<String>,
    onAddPhotoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color.White)
                .clickable { onAddPhotoClick() },
            contentAlignment = Alignment.Center
        ) {
            if (photos.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add Photo",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                // Show the first photo
                AsyncImage(
                    model = photos.first(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = onAddPhotoClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text("Choose Image")
        }
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    minHeight: Dp = 56.dp
) {
    Column {
        Text(
            text = label,
            color = Color(0xFF8B4513), // Dark brown color for labels
            style = MaterialTheme.typography.labelLarge
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}

@Composable
fun DateField(
    label: String,
    date: Date?,
    onDateClick: () -> Unit,
    dateUtils: DateUtils
) {
    Column {
        Text(
            text = label,
            color = Color(0xFF8B4513),
            style = MaterialTheme.typography.labelLarge
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        OutlinedTextField(
            value = date?.let { dateUtils.formatDate(it) } ?: "",
            onValueChange = { /* Read only */ },
            placeholder = { Text("Select date") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Select date",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onDateClick),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledBorderColor = Color.LightGray,
                disabledContainerColor = Color.White,
                disabledTextColor = Color.Black
            ),
            enabled = false
        )
    }
}

@Composable
fun CategoryDropdown(
    label: String,
    categories: List<Category>,
    selectedCategoryId: Long?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onCategorySelected: (Long) -> Unit
) {
    Column {
        Text(
            text = label,
            color = Color(0xFF8B4513),
            style = MaterialTheme.typography.labelLarge
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedCategoryId?.let { catId ->
                    categories.find { it.id == catId.toString() }?.name ?: ""
                } ?: "",
                onValueChange = { /* Read only */ },
                placeholder = { Text("Select category") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector =  Icons.Default.ArrowDropDown,
                        contentDescription = "Select category",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandedChange(!expanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledBorderColor = Color.LightGray,
                    disabledContainerColor = Color.White,
                    disabledTextColor = Color.Black
                ),
                enabled = false
            )
            
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategorySelected(category.id.toLong())
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ConditionSection(
    label: String,
    condition: Condition,
    onConditionChange: (Condition) -> Unit
) {
    Column {
        Text(
            text = label,
            color = Color(0xFF8B4513),
            style = MaterialTheme.typography.labelLarge
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        ConditionRatingBar(
            modifier = Modifier.fillMaxWidth(),
            rating = condition.stars,
            onRatingChanged = { rating ->
                val newCondition = Condition.entries.find { it.stars == rating } ?: Condition.GOOD
                onConditionChange(newCondition)
            },
            starSize = 32.dp
        )
    }
}