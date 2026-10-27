package com.example.rentora.ui.screens

import androidx.compose.animation.*
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
import com.example.rentora.model.MaintenancePriority
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantHubScreen(
    modifier: Modifier = Modifier
) {
    val leases by RentoraRepository.leases.collectAsState()
    val applications by RentoraRepository.applications.collectAsState()
    val tickets by RentoraRepository.maintenanceTickets.collectAsState()

    var showSignLeaseDialog by remember { mutableStateOf(false) }
    var signatureInput by remember { mutableStateOf("") }
    var showMaintenanceDialog by remember { mutableStateOf(false) }

    // Maintenance ticket fields
    var maintCategory by remember { mutableStateOf("Plumbing") }
    var maintPriority by remember { mutableStateOf(MaintenancePriority.MEDIUM) }
    var maintDesc by remember { mutableStateOf("") }

    val activeLease = leases.firstOrNull()

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Tenant Command Hub",
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Manage long-term lease, rent collection, and maintenance",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Active Lease Card
            if (activeLease != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = RentoraCard),
                        border = BorderStroke(1.dp, RentoraBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth().testTag("active_lease_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (activeLease.isSigned) RentoraGreen.copy(alpha = 0.15f) else RentoraCoralLight
                                ) {
                                    Text(
                                        text = if (activeLease.isSigned) "ACTIVE LEASE" else "SIGNATURE REQUIRED",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (activeLease.isSigned) RentoraGreen else RentoraCoralDark,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Text(
                                    text = "${activeLease.startDate} - ${activeLease.endDate}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = activeLease.propertyTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = activeLease.address,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = RentoraBorder)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Monthly Rent", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        text = RentoraRepository.formatPrice(activeLease.monthlyRent.toDouble()),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp,
                                        color = RentoraTeal
                                    )
                                }

                                Column {
                                    Text("Security Deposit", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        text = RentoraRepository.formatPrice(activeLease.depositAmount.toDouble()),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }

                                Column {
                                    Text("Next Due", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        text = activeLease.nextRentDueDate,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Rent Payment & Signature Actions
                            if (!activeLease.isSigned) {
                                Button(
                                    onClick = { showSignLeaseDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(imageVector = Icons.Default.Draw, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Sign Official Lease Agreement", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            } else {
                                if (activeLease.rentPaidThisMonth) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = RentoraGreen.copy(alpha = 0.15f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = RentoraGreen)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Rent Paid for this cycle! Ledger updated.",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = RentoraGreen
                                            )
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = { RentoraRepository.payRent(activeLease.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = RentoraTeal),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth().testTag("pay_rent_button")
                                    ) {
                                        Icon(imageVector = Icons.Default.Payment, contentDescription = null, tint = Color.White)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Pay Rent (${RentoraRepository.formatPrice(activeLease.monthlyRent.toDouble())})",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Maintenance Section Header & Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Maintenance Requests",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Direct dispatch to building management",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { showMaintenanceDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("new_maintenance_button")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Request", fontSize = 12.sp)
                    }
                }
            }

            // Maintenance Tickets List
            items(tickets, key = { it.id }) { ticket ->
                Card(
                    shape = RoundedCornerShape(12.dp),
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
                                text = "${ticket.category} · ${ticket.priority.label} Priority",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (ticket.priority == MaintenancePriority.EMERGENCY) MaterialTheme.colorScheme.error else RentoraCoral
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = RentoraSurface
                            ) {
                                Text(
                                    text = ticket.status,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RentoraTeal,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = ticket.description,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Filed on ${ticket.createdAt}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Applications History
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rental Applications (${applications.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            items(applications, key = { it.id }) { app ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = app.propertyTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = "Move-in: ${app.moveInDate} · ${app.leaseTermMonths} Mo",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = RentoraGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = app.status.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RentoraGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // Electronic Signature Dialog
        if (showSignLeaseDialog && activeLease != null) {
            AlertDialog(
                onDismissRequest = { showSignLeaseDialog = false },
                title = { Text("Sign Tenancy Agreement", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "By signing electronically below, you agree to the residential lease terms for ${activeLease.propertyTitle} at ${RentoraRepository.formatPrice(activeLease.monthlyRent.toDouble())}/month.",
                            fontSize = 13.sp
                        )
                        OutlinedTextField(
                            value = signatureInput,
                            onValueChange = { signatureInput = it },
                            label = { Text("Type Full Legal Name") },
                            placeholder = { Text("e.g. Alex Morgan") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (signatureInput.isNotBlank()) {
                                RentoraRepository.signLease(activeLease.id)
                                showSignLeaseDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral)
                    ) {
                        Text("Sign & Authorize", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSignLeaseDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // New Maintenance Ticket Dialog
        if (showMaintenanceDialog) {
            AlertDialog(
                onDismissRequest = { showMaintenanceDialog = false },
                title = { Text("Submit Maintenance Request", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Category", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Plumbing", "Electrical", "HVAC", "Appliance").forEach { cat ->
                                FilterChip(
                                    selected = maintCategory == cat,
                                    onClick = { maintCategory = cat },
                                    label = { Text(cat, fontSize = 11.sp) }
                                )
                            }
                        }

                        Text("Priority", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            MaintenancePriority.values().forEach { prio ->
                                FilterChip(
                                    selected = maintPriority == prio,
                                    onClick = { maintPriority = prio },
                                    label = { Text(prio.label, fontSize = 11.sp) }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = maintDesc,
                            onValueChange = { maintDesc = it },
                            label = { Text("Describe the issue") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (maintDesc.isNotBlank()) {
                                RentoraRepository.submitMaintenanceTicket(
                                    propertyTitle = activeLease?.propertyTitle ?: "Tribeca Loft",
                                    category = maintCategory,
                                    priority = maintPriority,
                                    desc = maintDesc
                                )
                                showMaintenanceDialog = false
                                maintDesc = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RentoraTeal)
                    ) {
                        Text("Dispatch Request", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showMaintenanceDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
