package com.example.rentora.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.rentora.data.RentoraRepository
import com.example.rentora.model.Booking
import com.example.rentora.model.Property
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun BookingFlowScreen(
    property: Property,
    onBackClick: () -> Unit,
    onBookingCompleted: (Booking) -> Unit,
    modifier: Modifier = Modifier
) {
    var nights by remember { mutableStateOf(5) }
    var guests by remember { mutableStateOf(2) }

    // Extras
    var addAirportShuttle by remember { mutableStateOf(false) }
    var addDailyCleaning by remember { mutableStateOf(false) }
    var addEarlyCheckin by remember { mutableStateOf(false) }

    // Promo code
    var couponCode by remember { mutableStateOf("") }
    var couponApplied by remember { mutableStateOf(false) }
    var couponError by remember { mutableStateOf(false) }

    // Payment method
    var selectedPaymentMethod by remember { mutableStateOf("Apple Pay / Google Pay") }
    var isProcessing by remember { mutableStateOf(false) }
    var confirmedBooking by remember { mutableStateOf<Booking?>(null) }

    val extrasTotal = (if (addAirportShuttle) 1500.0 else 0.0) +
            (if (addDailyCleaning) 600.0 * nights else 0.0) +
            (if (addEarlyCheckin) 750.0 else 0.0)

    val baseTotal = (property.nightlyPrice * nights) + property.cleaningFee
    val serviceFee = baseTotal * property.serviceFeeRate
    val discount = if (couponApplied) (baseTotal * 0.10) else 0.0
    val finalTotal = baseTotal + serviceFee + extrasTotal - discount

    val scrollState = rememberScrollState()

    // Confirmation Screen State
    if (confirmedBooking != null) {
        val booking = confirmedBooking!!
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(RentoraCoralLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = RentoraCoral,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Reservation Confirmed!",
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Confirmation Code: ${booking.confirmationCode}",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = RentoraCoral
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RentoraCard),
                border = BorderStroke(1.dp, RentoraBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = booking.propertyTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${booking.location} · ${booking.checkInDate} to ${booking.checkOutDate}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = RentoraBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Keyless Door Code", fontSize = 13.sp)
                        Text(
                            text = booking.doorCode,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = RentoraCoral
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Charged", fontSize = 13.sp)
                        Text(
                            text = RentoraRepository.formatPrice(booking.totalAmount),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onBookingCompleted(booking) },
                colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("view_trips_button")
            ) {
                Text(
                    text = "View in Trips",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
        return
    }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .statusBarsPadding()
                .padding(bottom = 90.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Confirm & Pay",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            // Property Card Summary
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RentoraCard),
                border = BorderStroke(1.dp, RentoraBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    Image(
                        painter = painterResource(id = if (property.imageRes != 0) property.imageRes else R.drawable.img_rentora_hero),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = property.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 2
                        )
                        Text(
                            text = "${property.city}, ${property.country}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "★ ${property.rating} (${property.reviewCount})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Trip Details
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = "Your Trip", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(modifier = Modifier.height(12.dp))

                // Dates selector row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Dates", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text(
                            text = "Nov 14 – 19, 2026 ($nights nights)",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (nights > 1) nights-- }) {
                            Icon(imageVector = Icons.Default.RemoveCircleOutline, contentDescription = null)
                        }
                        Text(text = "$nights", fontWeight = FontWeight.Bold)
                        IconButton(onClick = { nights++ }) {
                            Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = null)
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = RentoraBorder)

                // Guests row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Guests", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text(
                            text = "$guests guests (max ${property.maxGuests})",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (guests > 1) guests-- }) {
                            Icon(imageVector = Icons.Default.RemoveCircleOutline, contentDescription = null)
                        }
                        Text(text = "$guests", fontWeight = FontWeight.Bold)
                        IconButton(onClick = { if (guests < property.maxGuests) guests++ }) {
                            Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = null)
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = RentoraBorder)

                // Booking Extras
                Text(text = "Optional Extras", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Airport Shuttle Transfer", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text("Private chauffeur from airport", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("+${RentoraRepository.formatPrice(1500.0)}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Checkbox(
                            checked = addAirportShuttle,
                            onCheckedChange = { addAirportShuttle = it },
                            colors = CheckboxDefaults.colors(checkedColor = RentoraCoral)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Daily Linen & Housekeeping", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text("Turn-down service each morning", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("+${RentoraRepository.formatPrice((600 * nights).toDouble())}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Checkbox(
                            checked = addDailyCleaning,
                            onCheckedChange = { addDailyCleaning = it },
                            colors = CheckboxDefaults.colors(checkedColor = RentoraCoral)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Early Check-in (12:00 PM)", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text("Guaranteed early access", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("+${RentoraRepository.formatPrice(750.0)}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Checkbox(
                            checked = addEarlyCheckin,
                            onCheckedChange = { addEarlyCheckin = it },
                            colors = CheckboxDefaults.colors(checkedColor = RentoraCoral)
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = RentoraBorder)

                // Coupon code
                Text(text = "Promo Code", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = couponCode,
                        onValueChange = {
                            couponCode = it
                            couponError = false
                        },
                        placeholder = { Text("Try RENTORA10", fontSize = 13.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (couponCode.trim().equals("RENTORA10", ignoreCase = true)) {
                                couponApplied = true
                                couponError = false
                            } else {
                                couponApplied = false
                                couponError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral)
                    ) {
                        Text(if (couponApplied) "Applied" else "Apply")
                    }
                }
                if (couponApplied) {
                    Text(
                        text = "10% Rentora discount applied!",
                        color = RentoraGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                if (couponError) {
                    Text(
                        text = "Invalid promo code. Use RENTORA10",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp), color = RentoraBorder)

                // Itemized Price Breakdown
                Text(text = "Price Details", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(modifier = Modifier.height(8.dp))

                PriceRow(
                    label = "${RentoraRepository.formatPrice(property.nightlyPrice.toDouble())} × $nights nights",
                    value = RentoraRepository.formatPrice((property.nightlyPrice * nights).toDouble())
                )
                PriceRow(
                    label = "Cleaning fee",
                    value = RentoraRepository.formatPrice(property.cleaningFee.toDouble())
                )
                PriceRow(
                    label = "Rentora service fee (12%)",
                    value = RentoraRepository.formatPrice(serviceFee)
                )
                if (extrasTotal > 0) {
                    PriceRow(
                        label = "Selected extras",
                        value = RentoraRepository.formatPrice(extrasTotal)
                    )
                }
                if (couponApplied) {
                    PriceRow(
                        label = "Promo discount",
                        value = "-${RentoraRepository.formatPrice(discount)}",
                        isHighlight = true
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 10.dp), color = RentoraBorder)

                PriceRow(
                    label = "Total (USD)",
                    value = RentoraRepository.formatPrice(finalTotal),
                    isBold = true
                )

                Divider(modifier = Modifier.padding(vertical = 16.dp), color = RentoraBorder)

                // Payment Method
                Text(text = "Pay With", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(modifier = Modifier.height(8.dp))

                listOf("Google Pay / Apple Pay", "Credit Card (•••• 4242)", "Direct Bank Transfer (ACH)").forEach { method ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPaymentMethod = method }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPaymentMethod == method,
                            onClick = { selectedPaymentMethod = method },
                            colors = RadioButtonDefaults.colors(selectedColor = RentoraCoral)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = method, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        // Sticky Bottom Checkout Button
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
                    onClick = {
                        isProcessing = true
                    },
                    enabled = !isProcessing,
                    colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_and_pay_button")
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Authorize & Pay ${RentoraRepository.formatPrice(finalTotal)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        LaunchedEffect(isProcessing) {
            if (isProcessing) {
                delay(1200)
                val booking = RentoraRepository.createBooking(
                    property = property,
                    checkIn = "Nov 14, 2026",
                    checkOut = "Nov 19, 2026",
                    nights = nights,
                    guests = guests,
                    extrasTotal = extrasTotal,
                    discount = discount
                )
                confirmedBooking = booking
                isProcessing = false
            }
        }
    }
}

@Composable
private fun PriceRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isBold) 16.sp else 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) RentoraGreen else MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            fontSize = if (isBold) 16.sp else 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isHighlight) RentoraGreen else MaterialTheme.colorScheme.onSurface
        )
    }
}
