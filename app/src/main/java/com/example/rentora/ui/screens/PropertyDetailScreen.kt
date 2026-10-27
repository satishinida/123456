package com.example.rentora.ui.screens

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.rentora.data.RentoraRepository
import com.example.rentora.model.Property
import com.example.rentora.model.RentalMode
import com.example.ui.theme.*

@Composable
fun PropertyDetailScreen(
    property: Property,
    onBackClick: () -> Unit,
    onReserveClick: (Property, RentalMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(property.isFavorite) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    var showContactAlert by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Gallery thumbnails list
    val thumbnails = listOf(
        R.drawable.img_prop_villa,
        R.drawable.img_prop_house,
        R.drawable.img_rentora_hero,
        R.drawable.img_prop_villa
    )

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 90.dp)
        ) {
            // Hero Photo Section with Floating Gallery Strip (Matching Mockup)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                // Hero Photo with rounded bottom corners
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(290.dp)
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                ) {
                    Image(
                        painter = painterResource(id = if (property.imageRes != 0) property.imageRes else R.drawable.img_prop_villa),
                        contentDescription = property.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Top Floating Action Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 18.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circular White Back Button
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { onBackClick() }
                                .testTag("detail_back_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = RentoraCharcoal,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Right: Circular Share & Heart Buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            // Share Button
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .clickable {
                                        val sendIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                "Check out ${property.title} on Rentora: ${property.addressLine.ifBlank { property.city }}!"
                                            )
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, null))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = "Share",
                                    tint = RentoraCharcoal,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Favorite Button
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .clickable {
                                        isFavorite = !isFavorite
                                        RentoraRepository.toggleFavorite(property.id)
                                    }
                                    .testTag("detail_favorite_btn"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) RentoraCoral else RentoraCharcoal,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Floating Thumbnail Gallery Strip at the bottom of hero
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    thumbnails.forEachIndexed { index, thumbRes ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                painter = painterResource(id = thumbRes),
                                contentDescription = "Gallery thumbnail $index",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // +10 overlay on the last thumbnail (matching mockup)
                            if (index == thumbnails.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.5f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "10+",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Main Details Content
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                // Category Tag
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = RentoraSurface
                ) {
                    Text(
                        text = "Home",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = RentoraMuted,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Title & Price Row (Matching Mockup)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = property.title,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = RentoraRepository.formatPrice(property.nightlyPrice.toDouble()),
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = " / night",
                            fontSize = 12.sp,
                            color = RentoraMuted,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Location Line with Pin Icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = RentoraMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (property.addressLine.isNotBlank()) property.addressLine else "${property.city}, ${property.country}",
                        fontSize = 13.sp,
                        color = RentoraMuted,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Specs Container: 3 Bed | 2 Bath | 2,567 Sqrt (Matching Mockup)
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = RentoraCard,
                    border = BorderStroke(1.dp, RentoraBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SpecPill(icon = Icons.Outlined.Bed, label = "${property.bedrooms} Bed")
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(RentoraBorder))
                        SpecPill(icon = Icons.Outlined.Bathtub, label = "${property.bathrooms.toInt()} Bath")
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(RentoraBorder))
                        SpecPill(icon = Icons.Outlined.SquareFoot, label = "${property.squareFeet} Sqrt")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Description Section (Matching Mockup)
                Text(
                    text = "Description",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isDescriptionExpanded) {
                        property.description + " The residence features automated security gates, dedicated dual parking spaces, smart climate controls, and seamless access to commercial avenues."
                    } else {
                        property.description.take(130) + "... "
                    },
                    fontSize = 13.sp,
                    lineHeight = 21.sp,
                    color = RentoraMuted
                )
                Text(
                    text = if (isDescriptionExpanded) "Show Less" else "Read More...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = RentoraCharcoal,
                    modifier = Modifier
                        .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                        .padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Listing Broker Section (Matching Mockup)
                Text(
                    text = "Listing Broker",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Broker Avatar & Info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(RentoraCoralLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = property.hostName.take(1),
                                fontWeight = FontWeight.Black,
                                fontSize = 22.sp,
                                color = RentoraCoralDark
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = property.hostName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = property.brokerPhone,
                                fontSize = 12.sp,
                                color = RentoraMuted
                            )
                        }
                    }

                    // Broker Action Buttons: Chat Message & Phone Call
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Message button
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(RentoraSurface)
                                .border(1.dp, RentoraBorder, CircleShape)
                                .clickable { showContactAlert = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubble,
                                contentDescription = "Message Broker",
                                tint = RentoraCharcoal,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Call button
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(RentoraSurface)
                                .border(1.dp, RentoraBorder, CircleShape)
                                .clickable { showContactAlert = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call Broker",
                                tint = RentoraCharcoal,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Sticky Bottom "Book Now" Button (Matching Mockup)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shadowElevation = 8.dp,
            color = RentoraCard,
            border = BorderStroke(1.dp, RentoraBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = { onReserveClick(property, RentalMode.SHORT_TERM) },
                    colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral),
                    shape = RoundedCornerShape(26.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("book_now_button")
                ) {
                    Text(
                        text = "Book Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }

        // Broker Contact Dialog
        if (showContactAlert) {
            AlertDialog(
                onDismissRequest = { showContactAlert = false },
                title = { Text("Contact ${property.hostName}") },
                text = {
                    Text("Direct hotline: ${property.brokerPhone}\nYou can also message the broker directly from the Inbox tab.")
                },
                confirmButton = {
                    Button(
                        onClick = { showContactAlert = false },
                        colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral)
                    ) {
                        Text("Got it", color = Color.White)
                    }
                }
            )
        }
    }
}

@Composable
private fun SpecPill(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RentoraCharcoal,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = RentoraCharcoal
        )
    }
}
