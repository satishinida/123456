package com.example.rentora.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentora.data.RentoraRepository
import com.example.rentora.model.UserRole
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    onRoleSelected: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by RentoraRepository.currentUser.collectAsState()
    val selectedCurrency by RentoraRepository.selectedCurrency.collectAsState()
    var showTermsDialog by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .padding(bottom = 96.dp)
        ) {
            Text(
                text = "Profile & Account",
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Info Header Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RentoraCard),
                border = BorderStroke(1.dp, RentoraBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(RentoraCoralLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.name.take(1),
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp,
                            color = RentoraCoralDark
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentUser.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            if (currentUser.isVerified) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified Identity",
                                    tint = RentoraTeal,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(
                            text = currentUser.email,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Member since ${currentUser.memberSince}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Switch Persona / Role Section
            Text(
                text = "Switch Account Mode",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UserRole.values().forEach { role ->
                    val isSel = currentUser.role == role
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSel) RentoraCoral else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onRoleSelected(role) }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = when (role) {
                                    UserRole.GUEST -> Icons.Default.Luggage
                                    UserRole.HOST -> Icons.Default.Roofing
                                    UserRole.TENANT -> Icons.Default.Key
                                    UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                },
                                contentDescription = null,
                                tint = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = role.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Account Settings Items
            Text(
                text = "Preferences & Security",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RentoraCard),
                border = BorderStroke(1.dp, RentoraBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    SettingsRow(
                        icon = Icons.Outlined.Paid,
                        title = "Currency",
                        subtitle = selectedCurrency,
                        onClick = { showCurrencyDialog = true }
                    )
                    Divider(color = RentoraBorder)
                    SettingsRow(
                        icon = Icons.Outlined.CreditCard,
                        title = "Payment Methods",
                        subtitle = "Visa ending in 4242 (Default)",
                        onClick = {}
                    )
                    Divider(color = RentoraBorder)
                    SettingsRow(
                        icon = Icons.Outlined.Shield,
                        title = "Two-Factor Authentication",
                        subtitle = "Enabled via Authenticator",
                        onClick = {}
                    )
                    Divider(color = RentoraBorder)
                    SettingsRow(
                        icon = Icons.Outlined.Description,
                        title = "Terms of Service & Privacy",
                        subtitle = "Standard marketplace agreement",
                        onClick = { showTermsDialog = true }
                    )
                }
            }
        }

        // Currency Picker Dialog
        if (showCurrencyDialog) {
            AlertDialog(
                onDismissRequest = { showCurrencyDialog = false },
                title = { Text("Select Currency", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        listOf("INR", "USD", "EUR", "GBP", "JPY", "CAD").forEach { curr ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        RentoraRepository.setCurrency(curr)
                                        showCurrencyDialog = false
                                    }
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = curr, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                if (curr == selectedCurrency) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = RentoraCoral)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCurrencyDialog = false }) {
                        Text("Done")
                    }
                }
            )
        }

        // Terms Dialog
        if (showTermsDialog) {
            AlertDialog(
                onDismissRequest = { showTermsDialog = false },
                title = { Text("Rentora Legal & Safety", fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(
                            text = "Rentora is a global marketplace platform facilitating short-term vacation stays and formal long-term residential leases between independent hosts/property managers and guests/tenants.\n\n" +
                                    "1. Identity & Screening: All guests and long-term applicants must complete identity verification. Background check authorizations adhere strictly to FCRA and GDPR regulations.\n\n" +
                                    "2. Double-Booking Guarantee: All booking dates are locked with atomic transaction consistency to prevent simultaneous reservations.\n\n" +
                                    "3. Payment Escrow: Guest payments and security deposits are secured via PCI-DSS compliant providers.",
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showTermsDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = RentoraCoral)
                    ) {
                        Text("Agree & Close", color = Color.White)
                    }
                }
            )
        }
    }
}

@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RentoraCoral,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
