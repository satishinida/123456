package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentora.data.RentoraRepository
import com.example.rentora.model.Property
import com.example.rentora.model.RentalMode
import com.example.rentora.model.UserRole
import com.example.rentora.ui.components.RentoraHeader
import com.example.rentora.ui.screens.*
import com.example.ui.theme.*

enum class MainTab(val label: String, val icon: ImageVector, val unselectedIcon: ImageVector) {
    SEARCH("Search", Icons.Filled.Search, Icons.Outlined.Search),
    WISHLISTS("Wishlists", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    TRIPS("Trips", Icons.Filled.Luggage, Icons.Outlined.Luggage),
    TENANT("Tenant", Icons.Filled.Key, Icons.Outlined.Key),
    HOST("Hosting", Icons.Filled.Roofing, Icons.Outlined.Roofing),
    INBOX("Inbox", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
    ADMIN("Admin", Icons.Filled.AdminPanelSettings, Icons.Outlined.AdminPanelSettings),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.PersonOutline)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dark status bar and navigation bar icons on light warm off-white background
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            RentoraTheme {
                RentoraAppRoot()
            }
        }
    }
}

@Composable
fun RentoraAppRoot() {
    val currentUser by RentoraRepository.currentUser.collectAsState()
    var currentTab by remember { mutableStateOf(MainTab.SEARCH) }

    // Sub-screen navigation states
    var selectedPropertyForDetail by remember { mutableStateOf<Property?>(null) }
    var selectedPropertyForBooking by remember { mutableStateOf<Property?>(null) }

    // Handle back button for sub-screens
    BackHandler(enabled = selectedPropertyForBooking != null || selectedPropertyForDetail != null) {
        if (selectedPropertyForBooking != null) {
            selectedPropertyForBooking = null
        } else if (selectedPropertyForDetail != null) {
            selectedPropertyForDetail = null
        }
    }

    // Determine bottom navigation tabs based on active role
    val visibleTabs = remember(currentUser.role) {
        when (currentUser.role) {
            UserRole.GUEST -> listOf(
                MainTab.SEARCH,
                MainTab.WISHLISTS,
                MainTab.TRIPS,
                MainTab.INBOX,
                MainTab.PROFILE
            )
            UserRole.HOST -> listOf(
                MainTab.HOST,
                MainTab.SEARCH,
                MainTab.TRIPS,
                MainTab.INBOX,
                MainTab.PROFILE
            )
            UserRole.TENANT -> listOf(
                MainTab.TENANT,
                MainTab.SEARCH,
                MainTab.TRIPS,
                MainTab.INBOX,
                MainTab.PROFILE
            )
            UserRole.ADMIN -> listOf(
                MainTab.ADMIN,
                MainTab.SEARCH,
                MainTab.HOST,
                MainTab.INBOX,
                MainTab.PROFILE
            )
        }
    }

    // Keep currentTab valid if role changes
    LaunchedEffect(currentUser.role) {
        if (!visibleTabs.contains(currentTab)) {
            currentTab = visibleTabs.first()
        }
    }

    Scaffold(
        containerColor = RentoraBackground,
        topBar = {
            if (selectedPropertyForBooking == null && selectedPropertyForDetail == null) {
                RentoraHeader(
                    currentRole = currentUser.role,
                    onRoleSelected = { role ->
                        RentoraRepository.switchRole(role)
                        currentTab = when (role) {
                            UserRole.GUEST -> MainTab.SEARCH
                            UserRole.HOST -> MainTab.HOST
                            UserRole.TENANT -> MainTab.TENANT
                            UserRole.ADMIN -> MainTab.ADMIN
                        }
                    },
                    onNotificationClick = {
                        currentTab = MainTab.INBOX
                    }
                )
            }
        },
        bottomBar = {
            if (selectedPropertyForBooking == null && selectedPropertyForDetail == null) {
                Surface(
                    color = Color.White,
                    shadowElevation = 2.dp,
                    border = BorderStroke(1.dp, RentoraBorder)
                ) {
                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 0.dp,
                        windowInsets = WindowInsets.navigationBars
                    ) {
                        visibleTabs.forEach { tab ->
                            val isSelected = currentTab == tab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { currentTab = tab },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) tab.icon else tab.unselectedIcon,
                                        contentDescription = tab.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = RentoraCoral,
                                    selectedTextColor = RentoraCoral,
                                    unselectedIconColor = RentoraNavInactive,
                                    unselectedTextColor = RentoraNavInactive,
                                    indicatorColor = RentoraCoralLight
                                ),
                                modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // Booking Checkout Flow
                selectedPropertyForBooking != null -> {
                    BookingFlowScreen(
                        property = selectedPropertyForBooking!!,
                        onBackClick = { selectedPropertyForBooking = null },
                        onBookingCompleted = {
                            selectedPropertyForBooking = null
                            selectedPropertyForDetail = null
                            currentTab = MainTab.TRIPS
                        }
                    )
                }

                // Property Detail View
                selectedPropertyForDetail != null -> {
                    PropertyDetailScreen(
                        property = selectedPropertyForDetail!!,
                        onBackClick = { selectedPropertyForDetail = null },
                        onReserveClick = { prop, mode ->
                            if (mode == RentalMode.SHORT_TERM) {
                                selectedPropertyForBooking = prop
                            }
                        }
                    )
                }

                // Main Tab Navigation
                else -> {
                    when (currentTab) {
                        MainTab.SEARCH -> {
                            GuestExploreScreen(
                                onPropertySelected = { prop ->
                                    selectedPropertyForDetail = prop
                                }
                            )
                        }
                        MainTab.WISHLISTS -> {
                            WishlistsScreen(
                                onPropertySelected = { prop ->
                                    selectedPropertyForDetail = prop
                                }
                            )
                        }
                        MainTab.TRIPS -> {
                            TripsScreen(
                                onExploreClick = { currentTab = MainTab.SEARCH }
                            )
                        }
                        MainTab.TENANT -> {
                            TenantHubScreen()
                        }
                        MainTab.HOST -> {
                            HostHubScreen()
                        }
                        MainTab.INBOX -> {
                            InboxScreen()
                        }
                        MainTab.ADMIN -> {
                            AdminDashboardScreen()
                        }
                        MainTab.PROFILE -> {
                            ProfileScreen(
                                onRoleSelected = { role ->
                                    RentoraRepository.switchRole(role)
                                    currentTab = when (role) {
                                        UserRole.GUEST -> MainTab.SEARCH
                                        UserRole.HOST -> MainTab.HOST
                                        UserRole.TENANT -> MainTab.TENANT
                                        UserRole.ADMIN -> MainTab.ADMIN
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
