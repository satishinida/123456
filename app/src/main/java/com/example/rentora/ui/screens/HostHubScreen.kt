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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentora.data.RentoraRepository
import com.example.rentora.model.PropertyCategory
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostHubScreen(
    modifier: Modifier = Modifier
) {
    val blockedDates by RentoraRepository.blockedDates.collectAsState()
    val properties by RentoraRepository.properties.collectAsState()

    var showCreateListingDialog by remember { mutableStateOf(false) }

    // New Listing state
    var newTitle by remember { mutableStateOf("") }
    var newCity by remember { mutableStateOf("") }
    var newCountry by remember { mutableStateOf("") }
    var newNightlyPrice by remember { mutableStateOf("4500") }
    var newMonthlyPrice by remember { mutableStateOf("85000") }
    var newCategory by remember { mutableStateOf(PropertyCategory.VILLA) }
    var newBedrooms by remember { mutableStateOf(3) }

    val hostProperties = properties.take(3)

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header & Create Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Host Command Center",
                            fontWeight = FontWeight.Black,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Elena Vance · Superhost Dashboard",
                            fontSize = 13.sp,
                            color = RentoraCoral,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = { showCreateListingDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("create_listing_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Listing", fontSize = 12.sp, color = Color.White)
                    }
                }
            }

            // Host KPIs Grid (Earnings, Occupancy, Rating, Active Units)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "Monthly Payout",
                        value = RentoraRepository.formatPrice(185000.0),
                        trend = "+18% vs Sept",
                        icon = Icons.Outlined.MonetizationOn,
                        tint = RentoraGreen,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Occupancy Rate",
                        value = "88.4%",
                        trend = "24 booked nights",
                        icon = Icons.Outlined.CalendarToday,
                        tint = RentoraCoral,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "Guest Rating",
                        value = "4.98 ★",
                        trend = "112 5-star reviews",
                        icon = Icons.Outlined.Star,
                        tint = RentoraGold,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Active Units",
                        value = "${hostProperties.size}",
                        trend = "100% compliant",
                        icon = Icons.Outlined.HomeWork,
                        tint = RentoraTeal,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Multi-Property Calendar & Date Blocker
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RentoraCard),
                    border = BorderStroke(1.dp, RentoraBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Availability & Blackout Calendar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Tap a day to block or open for bookings",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Calendar strip for next 10 days
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val dates = listOf(
                                "Nov 15", "Nov 16", "Nov 17", "Nov 18", "Nov 19",
                                "Nov 20", "Nov 21", "Nov 22", "Nov 23", "Nov 24"
                            )
                            items(dates) { dayStr ->
                                val dateKey = "2026-11-${dayStr.takeLast(2)}"
                                val isBlocked = blockedDates.contains(dateKey)
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isBlocked) MaterialTheme.colorScheme.errorContainer else RentoraCoralLight,
                                    border = BorderStroke(1.dp, if (isBlocked) MaterialTheme.colorScheme.error else RentoraCoral),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { RentoraRepository.toggleBlockDate(dateKey) }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = dayStr, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = if (isBlocked) "BLOCKED" else "OPEN",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isBlocked) MaterialTheme.colorScheme.error else RentoraGreen
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Active Listings Roster
            item {
                Text(
                    text = "Your Managed Listings",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            items(hostProperties, key = { it.id }) { prop ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = RentoraCard),
                    border = BorderStroke(1.dp, RentoraBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = prop.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(
                                text = "${prop.city} · ${RentoraRepository.formatPrice(prop.nightlyPrice.toDouble())}/night · ${RentoraRepository.formatPrice(prop.monthlyPrice.toDouble())}/mo",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = RentoraGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "LIVE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = RentoraGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Create Listing Modal Dialog
        if (showCreateListingDialog) {
            AlertDialog(
                onDismissRequest = { showCreateListingDialog = false },
                title = { Text("List a New Property", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Listing Title") },
                            placeholder = { Text("e.g. Modern Sunset Beach Haven") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newCity,
                                onValueChange = { newCity = it },
                                label = { Text("City") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = newCountry,
                                onValueChange = { newCountry = it },
                                label = { Text("Country") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newNightlyPrice,
                                onValueChange = { newNightlyPrice = it },
                                label = { Text("Nightly (₹)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = newMonthlyPrice,
                                onValueChange = { newMonthlyPrice = it },
                                label = { Text("Monthly (₹)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Text("Category", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(PropertyCategory.HOUSE, PropertyCategory.VILLA, PropertyCategory.APARTMENT).forEach { cat ->
                                FilterChip(
                                    selected = newCategory == cat,
                                    onClick = { newCategory = cat },
                                    label = { Text(cat.label, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newTitle.isNotBlank() && newCity.isNotBlank()) {
                                RentoraRepository.addHostListing(
                                    title = newTitle,
                                    description = "A newly published luxury sanctuary hosted on Rentora.",
                                    category = newCategory,
                                    city = newCity,
                                    state = "Region",
                                    country = if (newCountry.isNotBlank()) newCountry else "United States",
                                    nightlyPrice = newNightlyPrice.toIntOrNull() ?: 4500,
                                    monthlyPrice = newMonthlyPrice.toIntOrNull() ?: 85000,
                                    bedrooms = newBedrooms,
                                    beds = newBedrooms + 1,
                                    bathrooms = 2.5,
                                    amenities = listOf("High-Speed WiFi", "Air Conditioning", "Balcony", "Self Check-in")
                                )
                                showCreateListingDialog = false
                                newTitle = ""
                                newCity = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral)
                    ) {
                        Text("Publish Listing", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateListingDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    trend: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = RentoraCard),
        border = BorderStroke(1.dp, RentoraBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontWeight = FontWeight.Black, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = trend, fontSize = 10.sp, color = tint, fontWeight = FontWeight.SemiBold)
        }
    }
}
