package com.example.antiquecollector.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Shape definitions for the Antique Collector app.
 * Using rounded corners to provide a classic, elegant look.
 */
val Shapes = Shapes(
    // For small components like chips, small buttons, etc.
    small = RoundedCornerShape(8.dp),
    
    // For medium components like cards, dialogs, etc.
    medium = RoundedCornerShape(12.dp),
    
    // For large components like bottom sheets, large cards, etc.
    large = RoundedCornerShape(16.dp),
    
    // For extra large components or featured content
    extraLarge = RoundedCornerShape(24.dp)
)