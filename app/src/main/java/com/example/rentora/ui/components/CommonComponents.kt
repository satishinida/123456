package com.example.rentora.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.rentora.data.RentoraRepository
import com.example.rentora.model.Property
import com.example.rentora.model.PropertyCategory
import com.example.rentora.model.UserRole
import com.example.ui.theme.*

@Composable
fun RentoraHeader(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    unreadNotifications: Int = 2,
    onNotificationClick: () -> Unit = {}
) {
    val currentLocation by RentoraRepository.currentLocation.collectAsState()
    var showLocationDialog by remember { mutableStateOf(false) }
    var showRoleMenu by remember { mutableStateOf(false) }

    Surface(
        color = RentoraBackground,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Location Header from Mockup: "Location" caption + "Lahore, Pakistan ˅"
            Column(
                modifier = Modifier
                    .clickable { showLocationDialog = true }
                    .testTag("location_picker_header")
            ) {
                Text(
                    text = "Location",
                    fontSize = 12.sp,
                    color = RentoraMuted,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = RentoraCharcoal,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentLocation,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = RentoraCharcoal
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select Location",
                        tint = RentoraCharcoal,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Right side: Role pill + Notification Bell icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Role Switcher Pill
                Box {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, RentoraBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { showRoleMenu = true }
                            .testTag("role_switcher_pill")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = when (currentRole) {
                                    UserRole.GUEST -> Icons.Default.Luggage
                                    UserRole.HOST -> Icons.Default.Roofing
                                    UserRole.TENANT -> Icons.Default.Key
                                    UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                },
                                contentDescription = null,
                                tint = RentoraCoral,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = currentRole.label,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = RentoraCharcoal
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showRoleMenu,
                        onDismissRequest = { showRoleMenu = false }
                    ) {
                        UserRole.values().forEach { role ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = role.label,
                                            fontWeight = if (role == currentRole) FontWeight.Bold else FontWeight.Normal,
                                            color = if (role == currentRole) RentoraCoral else RentoraCharcoal
                                        )
                                        if (role == currentRole) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = RentoraCoral,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    onRoleSelected(role)
                                    showRoleMenu = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Notification Bell with Circle Border
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, RentoraBorder, CircleShape)
                        .clickable { onNotificationClick() }
                        .testTag("notification_bell"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = RentoraCharcoal,
                        modifier = Modifier.size(20.dp)
                    )
                    if (unreadNotifications > 0) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = (-10).dp, y = 10.dp)
                                .clip(CircleShape)
                                .background(RentoraCoral)
                        )
                    }
                }
            }
        }
    }

    // Location Picker Dialog
    if (showLocationDialog) {
        val locations = listOf(
            "Lahore, Pakistan",
            "Wailea, Maui, Hawaii",
            "Tribeca, New York",
            "Aspen, Colorado",
            "Positano, Italy",
            "Kyoto, Japan"
        )
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            containerColor = Color.White,
            title = { Text("Select Destination", fontWeight = FontWeight.Bold, color = RentoraCharcoal) },
            text = {
                Column {
                    locations.forEach { loc ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    RentoraRepository.setLocation(loc)
                                    showLocationDialog = false
                                }
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = RentoraCoral)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = loc,
                                    fontWeight = if (loc == currentLocation) FontWeight.Bold else FontWeight.Normal,
                                    color = RentoraCharcoal
                                )
                            }
                            if (loc == currentLocation) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = RentoraCoral)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text("Close", color = RentoraCoral)
                }
            }
        )
    }
}

@Composable
fun CategorySelectorRow(
    selectedCategory: PropertyCategory,
    onCategorySelected: (PropertyCategory) -> Unit
) {
    val categories = listOf(
        PropertyCategory.HOUSE,
        PropertyCategory.VILLA,
        PropertyCategory.APARTMENT,
        PropertyCategory.BUNGALOW,
        PropertyCategory.FARMHOUSE
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onCategorySelected(category) }
                    .testTag("category_${category.name.lowercase()}")
            ) {
                // Category Icon Box with #F1F5F9 default background
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) RentoraCoral else RentoraSurface,
                    border = BorderStroke(1.dp, if (isSelected) RentoraCoral else RentoraBorder),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when (category) {
                                PropertyCategory.HOUSE -> Icons.Default.Home
                                PropertyCategory.VILLA -> Icons.Default.Villa
                                PropertyCategory.APARTMENT -> Icons.Default.Apartment
                                PropertyCategory.BUNGALOW -> Icons.Default.HolidayVillage
                                PropertyCategory.FARMHOUSE -> Icons.Default.Agriculture
                                PropertyCategory.ALL -> Icons.Default.Dashboard
                            },
                            contentDescription = category.label,
                            tint = if (isSelected) Color.White else RentoraCharcoal,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = category.label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) RentoraCoral else RentoraMuted
                )
            }
        }
    }
}

// Recommended Property Card (Pure White with Subtle Shadow & Rounded Corners)
@Composable
fun RecommendedPropertyCard(
    property: Property,
    onPropertyClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, RentoraBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .width(230.dp)
            .clickable { onPropertyClick() }
            .testTag("recommended_card_${property.id}")
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Photo with rounded corners and favorite heart overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                Image(
                    painter = painterResource(id = if (property.imageRes != 0) property.imageRes else R.drawable.img_rentora_hero),
                    contentDescription = property.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // White circle Favorite Button on top right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.95f))
                        .clickable { onFavoriteToggle() }
                        .testTag("favorite_btn_${property.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (property.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Save to Wishlist",
                        tint = if (property.isFavorite) RentoraCoral else RentoraCharcoal,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category tag "Home"
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = RentoraSurface
            ) {
                Text(
                    text = "Home",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = RentoraMuted,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Property Title
            Text(
                text = property.title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = RentoraCharcoal
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Address Line with Pin
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = RentoraMuted,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = if (property.addressLine.isNotBlank()) property.addressLine else "${property.city}, ${property.country}",
                    fontSize = 11.sp,
                    color = RentoraMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price Row (Daily rental like Airbnb in INR)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = RentoraRepository.formatPrice(property.nightlyPrice.toDouble()),
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = RentoraCharcoal
                )
                Text(
                    text = " / night",
                    fontSize = 11.sp,
                    color = RentoraMuted,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }
        }
    }
}

// Nearby Property Card (Horizontal List Row)
@Composable
fun NearbyPropertyCard(
    property: Property,
    onPropertyClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, RentoraBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onPropertyClick() }
            .testTag("nearby_card_${property.id}")
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail Image on Left with Heart
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 90.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = if (property.imageRes != 0) property.imageRes else R.drawable.img_rentora_hero),
                    contentDescription = property.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // White circle Favorite Button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.95f))
                        .clickable { onFavoriteToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (property.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Save",
                        tint = if (property.isFavorite) RentoraCoral else RentoraCharcoal,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right details
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = RentoraSurface
                ) {
                    Text(
                        text = "Home",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = RentoraMuted,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = property.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = RentoraCharcoal
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = RentoraMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (property.addressLine.isNotBlank()) property.addressLine else "${property.city}, ${property.country}",
                        fontSize = 11.sp,
                        color = RentoraMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = RentoraRepository.formatPrice(property.nightlyPrice.toDouble()),
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = RentoraCharcoal
                    )
                    Text(
                        text = " / night",
                        fontSize = 11.sp,
                        color = RentoraMuted,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PropertyCard(
    property: Property,
    onPropertyClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, RentoraBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onPropertyClick() }
            .testTag("property_card_${property.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = if (property.imageRes != 0) property.imageRes else R.drawable.img_rentora_hero),
                    contentDescription = property.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.95f))
                        .clickable { onFavoriteToggle() }
                        .testTag("favorite_btn_${property.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (property.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Save to Wishlist",
                        tint = if (property.isFavorite) RentoraCoral else RentoraCharcoal,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (property.addressLine.isNotBlank()) property.addressLine else "${property.city}, ${property.country}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = RentoraCharcoal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(
                    text = property.title,
                    fontSize = 13.sp,
                    color = RentoraMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Text(
                    text = "${property.bedrooms} beds · ${property.bathrooms.toInt()} baths · ${property.squareFeet} sq ft",
                    fontSize = 12.sp,
                    color = RentoraMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = RentoraRepository.formatPrice(property.nightlyPrice.toDouble()),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            color = RentoraCharcoal
                        )
                        Text(
                            text = " / night",
                            fontSize = 12.sp,
                            color = RentoraMuted,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
