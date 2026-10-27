package com.example.rentora.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentora.data.RentoraRepository
import com.example.rentora.model.Property
import com.example.rentora.model.PropertyCategory
import com.example.rentora.model.RentalMode
import com.example.rentora.ui.components.CategorySelectorRow
import com.example.rentora.ui.components.NearbyPropertyCard
import com.example.rentora.ui.components.RecommendedPropertyCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestExploreScreen(
    onPropertySelected: (Property) -> Unit,
    modifier: Modifier = Modifier
) {
    val properties by RentoraRepository.properties.collectAsState()
    var selectedCategory by remember { mutableStateOf(PropertyCategory.HOUSE) }
    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    var isMapMode by remember { mutableStateOf(false) }
    var selectedPropertyForMapPreview by remember { mutableStateOf<Property?>(null) }

    // Filter states
    var maxNightlyPrice by remember { mutableStateOf(25000f) }
    var selectedRentalMode by remember { mutableStateOf(RentalMode.BOTH) }

    // Filter logic
    val filteredProperties = remember(properties, selectedCategory, searchQuery, maxNightlyPrice, selectedRentalMode) {
        properties.filter { p ->
            val matchCategory = selectedCategory == PropertyCategory.ALL || p.category == selectedCategory
            val matchSearch = searchQuery.isBlank() ||
                    p.city.contains(searchQuery, ignoreCase = true) ||
                    p.title.contains(searchQuery, ignoreCase = true) ||
                    p.addressLine.contains(searchQuery, ignoreCase = true) ||
                    p.country.contains(searchQuery, ignoreCase = true)
            val matchPrice = p.nightlyPrice <= maxNightlyPrice.toInt()
            val matchMode = selectedRentalMode == RentalMode.BOTH || p.rentalMode == selectedRentalMode || p.rentalMode == RentalMode.BOTH
            matchCategory && matchSearch && matchPrice && matchMode
        }
    }

    val recommendedList = remember(filteredProperties) {
        if (filteredProperties.isNotEmpty()) filteredProperties else properties.take(3)
    }

    val nearbyList = remember(properties, searchQuery) {
        if (searchQuery.isNotBlank()) {
            properties.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.addressLine.contains(searchQuery, ignoreCase = true) ||
                it.city.contains(searchQuery, ignoreCase = true)
            }
        } else {
            properties.drop(1)
        }
    }

    Box(modifier = modifier.fillMaxSize().background(RentoraBackground)) {
        if (isMapMode) {
            // 100% Free OpenSource Map (OSM-styled vector canvas with interactive pins)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF2EFE9)) // OSM light terrain
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // OpenStreetMap Park layer (soft green)
                    drawRoundRect(
                        color = Color(0xFFD8E8C8),
                        topLeft = Offset(size.width * 0.1f, size.height * 0.08f),
                        size = Size(size.width * 0.45f, size.height * 0.22f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f)
                    )
                    // OpenStreetMap Water body (light blue lake)
                    drawRoundRect(
                        color = Color(0xFFA5BFDD),
                        topLeft = Offset(size.width * 0.4f, size.height * 0.58f),
                        size = Size(size.width * 0.6f, size.height * 0.35f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(32f, 32f)
                    )
                    // OpenStreetMap Roads (white avenues with subtle outlines)
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, size.height * 0.35f),
                        end = Offset(size.width, size.height * 0.42f),
                        strokeWidth = 20f
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(size.width * 0.35f, 0f),
                        end = Offset(size.width * 0.45f, size.height),
                        strokeWidth = 18f
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(size.width * 0.7f, 0f),
                        end = Offset(size.width * 0.8f, size.height),
                        strokeWidth = 14f
                    )
                }

                // Map Pins with price pill markers (OpenSource styling)
                filteredProperties.forEachIndexed { index, prop ->
                    val xOffset = ((index * 68 + 40) % 310).dp
                    val yOffset = ((index * 84 + 80) % 400).dp
                    val isSelected = selectedPropertyForMapPreview?.id == prop.id

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) RentoraCoral else Color.White,
                        border = BorderStroke(1.dp, if (isSelected) RentoraCoral else RentoraBorder),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .offset(x = xOffset, y = yOffset)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { selectedPropertyForMapPreview = prop }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else RentoraCoral,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = RentoraRepository.formatPrice(prop.nightlyPrice.toDouble()),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (isSelected) Color.White else RentoraCharcoal
                            )
                        }
                    }
                }

                // Top Map Controls & Toggle Back to List
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, RentoraBorder),
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "OpenSource Map",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = RentoraCharcoal
                            )
                        }
                    }

                    // Floating List Switcher Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, RentoraBorder),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { isMapMode = false }
                            .testTag("map_toggle_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ViewAgenda, contentDescription = null, tint = RentoraCharcoal, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "List View", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RentoraCharcoal)
                        }
                    }
                }

                // Bottom preview card if a pin is tapped
                selectedPropertyForMapPreview?.let { prop ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        NearbyPropertyCard(
                            property = prop,
                            onPropertyClick = { onPropertySelected(prop) },
                            onFavoriteToggle = { RentoraRepository.toggleFavorite(prop.id) }
                        )
                    }
                }
            }
        } else {
            // List View Mode
            LazyColumn(
                contentPadding = PaddingValues(top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Search Bar & Filter Button (Pure White background #FFFFFF)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pure White Search Input Field
                        Surface(
                            shape = RoundedCornerShape(28.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, RentoraBorder),
                            shadowElevation = 1.dp,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("explore_search_bar")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = RentoraMuted,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = {
                                        Text(
                                            "Search",
                                            fontSize = 14.sp,
                                            color = RentoraMuted,
                                            fontWeight = FontWeight.Normal
                                        )
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Filter Button (Light White surface with soft border)
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(26.dp))
                                .background(Color.White)
                                .border(1.dp, RentoraBorder, RoundedCornerShape(26.dp))
                                .clickable { showFilterSheet = true }
                                .testTag("filter_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Tune,
                                contentDescription = "Filters",
                                tint = RentoraCharcoal,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Map Toggle Button
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(26.dp))
                                .background(Color.White)
                                .border(1.dp, RentoraBorder, RoundedCornerShape(26.dp))
                                .clickable { isMapMode = true }
                                .testTag("map_toggle_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = "OpenSource Map",
                                tint = RentoraCoral,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Categories Selector Row (House, Villa, Apartment, Bungalow, Farmhouse)
                item {
                    CategorySelectorRow(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it }
                    )
                }

                // "Recommended Property" Section Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recommended Property",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = RentoraCharcoal
                        )
                        Text(
                            text = "See all",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RentoraMuted,
                            modifier = Modifier
                                .clickable { selectedCategory = PropertyCategory.ALL }
                                .testTag("see_all_recommended")
                        )
                    }
                }

                // Recommended Properties Horizontal Carousel
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(recommendedList, key = { it.id }) { property ->
                            RecommendedPropertyCard(
                                property = property,
                                onPropertyClick = { onPropertySelected(property) },
                                onFavoriteToggle = { RentoraRepository.toggleFavorite(property.id) }
                            )
                        }
                    }
                }

                // "Nearby Property" Section Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Nearby Property",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = RentoraCharcoal
                        )
                        Text(
                            text = "See all",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RentoraMuted,
                            modifier = Modifier
                                .clickable { }
                                .testTag("see_all_nearby")
                        )
                    }
                }

                // Nearby Property Vertical List
                items(nearbyList, key = { "nearby_${it.id}" }) { property ->
                    Box(modifier = Modifier.padding(horizontal = 18.dp)) {
                        NearbyPropertyCard(
                            property = property,
                            onPropertyClick = { onPropertySelected(property) },
                            onFavoriteToggle = { RentoraRepository.toggleFavorite(property.id) }
                        )
                    }
                }
            }
        }

        // Filter Modal Sheet (Pure White container #FFFFFF)
        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filter Properties",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = RentoraCharcoal
                        )
                        IconButton(onClick = { showFilterSheet = false }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = RentoraCharcoal)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Max Daily Price: ${RentoraRepository.formatPrice(maxNightlyPrice.toDouble())} / night",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = RentoraCharcoal
                    )
                    Slider(
                        value = maxNightlyPrice,
                        onValueChange = { maxNightlyPrice = it },
                        valueRange = 1000f..30000f,
                        steps = 28,
                        colors = SliderDefaults.colors(
                            thumbColor = RentoraCoral,
                            activeTrackColor = RentoraCoral,
                            inactiveTrackColor = RentoraSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Property Category",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = RentoraCharcoal
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(PropertyCategory.ALL, PropertyCategory.HOUSE, PropertyCategory.VILLA, PropertyCategory.APARTMENT).forEach { cat ->
                            val isSel = selectedCategory == cat
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSel) RentoraCoral else RentoraSurface,
                                border = BorderStroke(1.dp, if (isSel) RentoraCoral else RentoraBorder),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { selectedCategory = cat }
                            ) {
                                Text(
                                    text = cat.label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.White else RentoraCharcoal,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { showFilterSheet = false },
                        colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Apply Filters (${filteredProperties.size} places)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
