package com.example.rentora.model

enum class UserRole(val label: String) {
    GUEST("Guest"),
    HOST("Host"),
    TENANT("Tenant"),
    ADMIN("Admin")
}

enum class RentalMode(val label: String) {
    SHORT_TERM("Short Stay"),
    LONG_TERM("Long-Term Lease"),
    BOTH("Any Duration")
}

enum class PropertyCategory(val label: String, val iconName: String) {
    HOUSE("House", "ic_house"),
    VILLA("Villa", "ic_villa"),
    APARTMENT("Apartment", "ic_apt"),
    BUNGALOW("Bungalow", "ic_bungalow"),
    FARMHOUSE("Farmhouse", "ic_farmhouse"),
    ALL("All", "ic_all")
}

enum class BookingStatus(val label: String) {
    PENDING("Pending Host"),
    CONFIRMED("Confirmed"),
    ACTIVE("Current Stay"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

enum class ApplicationStatus(val label: String) {
    SUBMITTED("Submitted"),
    UNDER_REVIEW("Under Review"),
    APPROVED("Approved"),
    LEASE_OFFERED("Lease Offered"),
    SIGNED("Lease Signed")
}

enum class MaintenancePriority(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    EMERGENCY("Emergency")
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole = UserRole.GUEST,
    val isVerified: Boolean = true,
    val avatarUrl: String = "",
    val memberSince: String = "March 2023",
    val phone: String = "+1 (555) 019-2834"
)

data class Property(
    val id: String,
    val title: String,
    val description: String,
    val category: PropertyCategory,
    val rentalMode: RentalMode = RentalMode.BOTH,
    val city: String,
    val state: String,
    val country: String,
    val nightlyPrice: Int,
    val monthlyPrice: Int,
    val cleaningFee: Int = 85,
    val serviceFeeRate: Double = 0.12,
    val maxGuests: Int = 4,
    val bedrooms: Int = 2,
    val beds: Int = 2,
    val bathrooms: Double = 2.0,
    val squareFeet: Int = 1100,
    val rating: Double = 4.95,
    val reviewCount: Int = 64,
    val isSuperhost: Boolean = true,
    val instantBookable: Boolean = true,
    val amenities: List<String> = listOf("High-Speed WiFi", "Infinity Pool", "Air Conditioning", "Full Kitchen", "EV Charger", "Dedicated Workspace"),
    val houseRules: List<String> = listOf("Check-in: 3:00 PM – 9:00 PM", "Checkout: 11:00 AM", "No parties or unauthorized events", "Quiet hours after 10:00 PM"),
    val cancellationPolicy: String = "Free cancellation up to 48 hours before check-in.",
    val latitude: Double,
    val longitude: Double,
    val hostName: String = "Elena Vance",
    val hostRating: Double = 4.98,
    val hostResponseTime: String = "within 15 minutes",
    val wifiNetwork: String = "Rentora_HighSpeed_Guest",
    val wifiPassword: String = "sunset-villa-2026",
    val doorCode: String = "*4928#",
    val isFavorite: Boolean = false,
    val addressLine: String = "",
    val brokerPhone: String = "18392719103",
    val imageRes: Int = 0 // Optional local drawable or colored palette fallback
)

data class Booking(
    val id: String,
    val propertyId: String,
    val propertyTitle: String,
    val location: String,
    val checkInDate: String,
    val checkOutDate: String,
    val nights: Int,
    val guestsCount: Int,
    val totalAmount: Double,
    val status: BookingStatus = BookingStatus.CONFIRMED,
    val confirmationCode: String,
    val wifiName: String = "Rentora_HighSpeed_Guest",
    val wifiPass: String = "sunset-villa-2026",
    val doorCode: String = "*4928#",
    val checkInInstructions: String = "Smart keypad on the front entrance. Tap wake, enter code, and turn handle."
)

data class RentalApplication(
    val id: String,
    val propertyId: String,
    val propertyTitle: String,
    val applicantName: String,
    val monthlyIncome: Double,
    val employer: String,
    val moveInDate: String,
    val leaseTermMonths: Int,
    val status: ApplicationStatus = ApplicationStatus.SUBMITTED,
    val submittedDate: String
)

data class Lease(
    val id: String,
    val propertyId: String,
    val propertyTitle: String,
    val address: String,
    val monthlyRent: Int,
    val depositAmount: Int,
    val startDate: String,
    val endDate: String,
    val isSigned: Boolean = false,
    val nextRentDueDate: String = "Nov 1, 2026",
    val rentPaidThisMonth: Boolean = false
)

data class MaintenanceTicket(
    val id: String,
    val propertyTitle: String,
    val category: String,
    val priority: MaintenancePriority,
    val description: String,
    val status: String = "In Review",
    val createdAt: String
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val messageText: String,
    val timestamp: String,
    val isFromMe: Boolean
)

data class Conversation(
    val id: String,
    val otherUserName: String,
    val propertyTitle: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val messages: List<ChatMessage> = emptyList()
)

data class NotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val timestamp: String,
    val isRead: Boolean = false
)

data class DisputeItem(
    val id: String,
    val propertyTitle: String,
    val guestName: String,
    val reason: String,
    val requestedAmount: Double,
    val status: String = "Open"
)
