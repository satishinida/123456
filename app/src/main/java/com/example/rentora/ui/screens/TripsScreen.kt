package com.example.rentora.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.rentora.model.Booking
import com.example.rentora.model.BookingStatus
import com.example.ui.theme.*

@Composable
fun TripsScreen(
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookings by RentoraRepository.bookings.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0: Upcoming, 1: Past
    var activeReceiptBooking by remember { mutableStateOf<Booking?>(null) }
    var reviewBookingTarget by remember { mutableStateOf<Booking?>(null) }
    var ratingStars by remember { mutableStateOf(5) }
    var reviewComment by remember { mutableStateOf("") }
    var reviewSubmitted by remember { mutableStateOf(false) }

    val upcomingBookings = bookings.filter { it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.ACTIVE }
    val pastBookings = bookings.filter { it.status == BookingStatus.COMPLETED || it.status == BookingStatus.CANCELLED }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Trips & Reservations",
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tabs (Upcoming vs Past)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = RentoraCoral,
                divider = { Divider(color = RentoraBorder) }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Upcoming (${upcomingBookings.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) RentoraCoral else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Past / History (${pastBookings.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) RentoraCoral else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val currentList = if (selectedTab == 0) upcomingBookings else pastBookings

            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.Luggage,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (selectedTab == 0) "No upcoming trips booked yet" else "No past trips",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Explore luxury villas, lofts and cabins across the world.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onExploreClick,
                            colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Start Searching", color = Color.White)
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(currentList, key = { it.id }) { booking ->
                        TripCard(
                            booking = booking,
                            onViewReceipt = { activeReceiptBooking = booking },
                            onCancel = { RentoraRepository.cancelBooking(booking.id) },
                            onLeaveReview = { reviewBookingTarget = booking }
                        )
                    }
                }
            }
        }

        // Receipt Modal Dialog
        activeReceiptBooking?.let { bkg ->
            AlertDialog(
                onDismissRequest = { activeReceiptBooking = null },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Receipt, contentDescription = null, tint = RentoraCoral)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Official Rentora Invoice", fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "Reference: ${bkg.confirmationCode}", fontWeight = FontWeight.SemiBold, color = RentoraCoral)
                        Text(text = "Property: ${bkg.propertyTitle}", fontSize = 14.sp)
                        Text(text = "Location: ${bkg.location}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "Dates: ${bkg.checkInDate} – ${bkg.checkOutDate} (${bkg.nights} nights)", fontSize = 13.sp)
                        Text(text = "Guests: ${bkg.guestsCount}", fontSize = 13.sp)
                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = RentoraBorder)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Paid:", fontWeight = FontWeight.Bold)
                            Text(RentoraRepository.formatPrice(bkg.totalAmount), fontWeight = FontWeight.Black, color = RentoraCoral)
                        }
                        Text(
                            text = "Payment verified via Stripe Token. Tax invoice available for export.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { activeReceiptBooking = null },
                        colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral)
                    ) {
                        Text("Close", color = Color.White)
                    }
                }
            )
        }

        // Leave Review Modal
        reviewBookingTarget?.let { bkg ->
            AlertDialog(
                onDismissRequest = {
                    reviewBookingTarget = null
                    reviewSubmitted = false
                },
                title = { Text(if (reviewSubmitted) "Review Published!" else "Review your stay") },
                text = {
                    if (reviewSubmitted) {
                        Text("Thank you! Your feedback helps other guests and rewards exceptional hosts.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(text = bkg.propertyTitle, fontWeight = FontWeight.Bold)
                            Text("Rate your experience:", fontSize = 13.sp)
                            Row {
                                (1..5).forEach { star ->
                                    IconButton(onClick = { ratingStars = star }) {
                                        Icon(
                                            imageVector = if (star <= ratingStars) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                            contentDescription = null,
                                            tint = RentoraGold
                                        )
                                    }
                                }
                            }
                            OutlinedTextField(
                                value = reviewComment,
                                onValueChange = { reviewComment = it },
                                placeholder = { Text("What did you love about the property?") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3
                            )
                        }
                    }
                },
                confirmButton = {
                    if (reviewSubmitted) {
                        Button(
                            onClick = {
                                reviewBookingTarget = null
                                reviewSubmitted = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral)
                        ) {
                            Text("Done", color = Color.White)
                        }
                    } else {
                        Button(
                            onClick = { reviewSubmitted = true },
                            colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral)
                        ) {
                            Text("Post Review", color = Color.White)
                        }
                    }
                },
                dismissButton = {
                    if (!reviewSubmitted) {
                        TextButton(onClick = { reviewBookingTarget = null }) {
                            Text("Cancel")
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun TripCard(
    booking: Booking,
    onViewReceipt: () -> Unit,
    onCancel: () -> Unit,
    onLeaveReview: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RentoraCard),
        border = BorderStroke(1.dp, RentoraBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().testTag("trip_card_${booking.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (booking.status) {
                        BookingStatus.CONFIRMED -> RentoraGreen.copy(alpha = 0.15f)
                        BookingStatus.ACTIVE -> RentoraCoralLight
                        BookingStatus.COMPLETED -> RentoraSurface
                        BookingStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
                        else -> RentoraSurface
                    }
                ) {
                    Text(
                        text = booking.status.label.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = when (booking.status) {
                            BookingStatus.CONFIRMED -> RentoraGreen
                            BookingStatus.ACTIVE -> RentoraCoralDark
                            BookingStatus.CANCELLED -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = booking.confirmationCode,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = booking.propertyTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = booking.location,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Check-in", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = booking.checkInDate, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Column {
                    Text(text = "Checkout", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = booking.checkOutDate, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Column {
                    Text(text = "Guests", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "${booking.guestsCount} guests", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            // Keyless Access Info for Active/Confirmed
            if (booking.status == BookingStatus.CONFIRMED || booking.status == BookingStatus.ACTIVE) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = RentoraSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.VpnKey, contentDescription = null, tint = RentoraCoral, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Smart Lock Code: ${booking.doorCode}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Wifi, contentDescription = null, tint = RentoraTeal, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "WiFi: ${booking.wifiName} (${booking.wifiPass})", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewReceipt,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Receipt", fontSize = 12.sp)
                }

                if (booking.status == BookingStatus.CONFIRMED) {
                    OutlinedButton(
                        onClick = onCancel,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", fontSize = 12.sp)
                    }
                }

                if (booking.status == BookingStatus.COMPLETED) {
                    Button(
                        onClick = onLeaveReview,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Review", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
