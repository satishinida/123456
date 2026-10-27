package com.example.rentora.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentora.data.RentoraRepository
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    modifier: Modifier = Modifier
) {
    val disputes by RentoraRepository.disputes.collectAsState()
    val properties by RentoraRepository.properties.collectAsState()

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Marketplace Oversight",
                            fontWeight = FontWeight.Black,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Admin & Compliance Operations Center",
                            fontSize = 13.sp,
                            color = RentoraCoral,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RentoraGreen.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "SYSTEM HEALTHY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RentoraGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // KPIs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminMetricCard(
                        title = "Gross Booking Vol",
                        value = RentoraRepository.formatPrice(18402500.0),
                        subtitle = "+24.8% YoY",
                        tint = RentoraGreen,
                        modifier = Modifier.weight(1f)
                    )
                    AdminMetricCard(
                        title = "Marketplace Comm",
                        value = RentoraRepository.formatPrice(1840250.0),
                        subtitle = "10.0% take rate",
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
                    AdminMetricCard(
                        title = "Active Units",
                        value = "${properties.size * 70}",
                        subtitle = "Global inventory",
                        tint = RentoraTeal,
                        modifier = Modifier.weight(1f)
                    )
                    AdminMetricCard(
                        title = "KYC Verification",
                        value = "99.4%",
                        subtitle = "Stripe Identity pass",
                        tint = RentoraCharcoal,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Dispute Resolution Center
            item {
                Text(
                    text = "Dispute & Resolution Center (${disputes.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            items(disputes, key = { it.id }) { dispute ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = RentoraCard),
                    border = BorderStroke(1.dp, RentoraBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dispute.propertyTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (dispute.status == "Resolved") RentoraGreen.copy(alpha = 0.15f) else RentoraCoralLight
                            ) {
                                Text(
                                    text = dispute.status.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (dispute.status == "Resolved") RentoraGreen else RentoraCoralDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Guest: ${dispute.guestName} · Claim: ${RentoraRepository.formatPrice(dispute.requestedAmount)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = dispute.reason,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        if (dispute.status != "Resolved") {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { RentoraRepository.resolveDispute(dispute.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Arbitrate & Resolve Case", fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Infrastructure & Security Status
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = RentoraCard),
                    border = BorderStroke(1.dp, RentoraBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "System Infrastructure Health",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ServiceStatusRow(name = "PostgreSQL 16 + PostGIS Spatial Engine", status = "Connected · 12ms")
                        ServiceStatusRow(name = "Redis 7 + BullMQ Background Queues", status = "Healthy · 0 errors")
                        ServiceStatusRow(name = "Stripe Connect Payment Webhooks", status = "Operational")
                        ServiceStatusRow(name = "S3 Object Storage Asset Pipeline", status = "Online")
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminMetricCard(
    title: String,
    value: String,
    subtitle: String,
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
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontWeight = FontWeight.Black, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 11.sp, color = tint, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ServiceStatusRow(name: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
        Text(text = status, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RentoraGreen)
    }
}
