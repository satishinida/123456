package com.example.rentora.data

import com.example.R
import com.example.rentora.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object RentoraRepository {

    private val _currentUser = MutableStateFlow(
        User(
            id = "usr_9918",
            name = "Alex Morgan",
            email = "alex.morgan@rentora.com",
            role = UserRole.GUEST,
            isVerified = true,
            memberSince = "March 2024",
            phone = "+1 (555) 234-5678"
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _selectedCurrency = MutableStateFlow("INR")
    val selectedCurrency: StateFlow<String> = _selectedCurrency.asStateFlow()

    private val _currentLocation = MutableStateFlow("Lahore, Pakistan")
    val currentLocation: StateFlow<String> = _currentLocation.asStateFlow()

    fun setLocation(loc: String) {
        _currentLocation.value = loc
    }

    private val initialProperties = listOf(
        Property(
            id = "prop_mark",
            title = "Mark Willson Property",
            description = "Stunning contemporary estate in the premier DHA district. Featuring sweeping curved glass balconies, warm ambient architectural lighting, high-spec modular chef kitchen, grand foyer, and serene landscaped courtyard.",
            category = PropertyCategory.VILLA,
            rentalMode = RentalMode.BOTH,
            city = "Lahore",
            state = "Punjab",
            country = "Pakistan",
            addressLine = "DHA, North Nazimabad, Bahria Town, Lahore, Pakistan",
            nightlyPrice = 4500,
            monthlyPrice = 95000,
            cleaningFee = 600,
            maxGuests = 6,
            bedrooms = 3,
            beds = 3,
            bathrooms = 2.0,
            squareFeet = 2567,
            rating = 4.96,
            reviewCount = 48,
            isSuperhost = true,
            instantBookable = true,
            amenities = listOf("High-Speed WiFi", "24/7 Security", "Private Garage", "Standby Generator", "Air Conditioning", "Balcony Terraces", "Lush Lawn"),
            latitude = 31.5204,
            longitude = 74.3587,
            hostName = "Muhammad Farhan",
            brokerPhone = "18392719103",
            hostRating = 4.97,
            hostResponseTime = "within 10 minutes",
            isFavorite = true,
            imageRes = R.drawable.img_prop_villa
        ),
        Property(
            id = "prop_eleanor",
            title = "Eleanor Pena Property",
            description = "Charming light-filled bungalow residence with red gabled roofing, covered wrap-around porch, sunny garden breakfast nook, and updated modern interior.",
            category = PropertyCategory.BUNGALOW,
            rentalMode = RentalMode.BOTH,
            city = "Shilo",
            state = "South Dakota",
            country = "United States",
            addressLine = "1901 Thornridge Cir. Shilo 810",
            nightlyPrice = 2800,
            monthlyPrice = 65000,
            cleaningFee = 450,
            maxGuests = 4,
            bedrooms = 2,
            beds = 2,
            bathrooms = 1.0,
            squareFeet = 1450,
            rating = 4.92,
            reviewCount = 36,
            isSuperhost = true,
            instantBookable = true,
            amenities = listOf("Fast WiFi", "Lawn Garden", "Washer/Dryer", "Central Heating", "Covered Porch", "Pet Friendly"),
            latitude = 44.3668,
            longitude = -100.3538,
            hostName = "Eleanor Pena",
            brokerPhone = "18392719104",
            hostRating = 4.94,
            isFavorite = false,
            imageRes = R.drawable.img_prop_house
        ),
        Property(
            id = "prop_bessie",
            title = "Bessie Cooper Property",
            description = "Sleek architectural minimalist villa with resort-style swimming pool, palm landscaping, double-height floor-to-ceiling glass, and designer lighting.",
            category = PropertyCategory.VILLA,
            rentalMode = RentalMode.BOTH,
            city = "Inglewood",
            state = "California",
            country = "United States",
            addressLine = "8502 Preston Rd. Inglewood, CA",
            nightlyPrice = 3500,
            monthlyPrice = 78000,
            cleaningFee = 500,
            maxGuests = 4,
            bedrooms = 2,
            beds = 2,
            bathrooms = 2.0,
            squareFeet = 1800,
            rating = 4.89,
            reviewCount = 28,
            isSuperhost = false,
            instantBookable = true,
            amenities = listOf("Swimming Pool", "Patio Deck", "Central AC", "EV Charger", "Smart Keyless Lock"),
            latitude = 33.9617,
            longitude = -118.3531,
            hostName = "Bessie Cooper",
            brokerPhone = "18392719105",
            hostRating = 4.90,
            isFavorite = false,
            imageRes = R.drawable.img_rentora_hero
        ),
        Property(
            id = "prop_darrell",
            title = "Darrell Steward Property",
            description = "Quiet and scenic farmhouse retreat nestled among rolling hills. Features exposed timber beams, expansive private garden, and modern country kitchen.",
            category = PropertyCategory.FARMHOUSE,
            rentalMode = RentalMode.BOTH,
            city = "Manchester",
            state = "Connecticut",
            country = "United States",
            addressLine = "Connecticut 35624",
            nightlyPrice = 2200,
            monthlyPrice = 52000,
            cleaningFee = 400,
            maxGuests = 6,
            bedrooms = 3,
            beds = 4,
            bathrooms = 2.0,
            squareFeet = 2100,
            rating = 4.95,
            reviewCount = 42,
            isSuperhost = true,
            instantBookable = true,
            amenities = listOf("Fireplace", "Country Kitchen", "Mountain Views", "High-speed Starlink WiFi", "Outdoor BBQ"),
            latitude = 41.7759,
            longitude = -72.5215,
            hostName = "Darrell Steward",
            brokerPhone = "18392719106",
            hostRating = 4.93,
            isFavorite = false,
            imageRes = R.drawable.img_prop_house
        ),
        Property(
            id = "prop_1",
            title = "Azure Horizons Cliffside Villa",
            description = "Perched on the dramatic cliffs of Maui, this architectural masterpiece features an infinity pool overlooking panoramic Pacific sunsets, private beach access, chef's kitchen, and expansive outdoor entertaining decks.",
            category = PropertyCategory.VILLA,
            rentalMode = RentalMode.BOTH,
            city = "Wailea, Maui",
            state = "Hawaii",
            country = "United States",
            addressLine = "Dr. San Jose, South Dakota 83475",
            nightlyPrice = 14500,
            monthlyPrice = 290000,
            cleaningFee = 1500,
            maxGuests = 8,
            bedrooms = 4,
            beds = 5,
            bathrooms = 4.5,
            squareFeet = 3800,
            rating = 4.98,
            reviewCount = 112,
            isSuperhost = true,
            instantBookable = true,
            amenities = listOf("Infinity Edge Pool", "Private Beach Path", "Oceanfront Terrace", "EV Fast Charger", "SubZero Chef Kitchen", "Sonos Multi-Room Sound", "Fast Fiber WiFi"),
            latitude = 20.6899,
            longitude = -156.4422,
            hostName = "Elena Vance",
            brokerPhone = "18392719103",
            hostRating = 4.99,
            isFavorite = true,
            imageRes = R.drawable.img_rentora_hero
        ),
        Property(
            id = "prop_2",
            title = "Tribeca Cast-Iron Designer Loft",
            description = "Sun-drenched architectural loft in historic Tribeca. Featuring soaring 14-foot timbered ceilings, original Corinthian columns, custom Italian marble kitchen, and private elevator landing.",
            category = PropertyCategory.APARTMENT,
            rentalMode = RentalMode.BOTH,
            city = "Tribeca, Manhattan",
            state = "New York",
            country = "United States",
            addressLine = "142 Franklin St #4A, New York, NY",
            nightlyPrice = 8900,
            monthlyPrice = 195000,
            cleaningFee = 1000,
            maxGuests = 4,
            bedrooms = 2,
            beds = 2,
            bathrooms = 2.0,
            squareFeet = 2100,
            rating = 4.94,
            reviewCount = 78,
            isSuperhost = true,
            instantBookable = true,
            amenities = listOf("Keyed Elevator", "Espresso Bar", "Soaking Tub", "Miele Appliances", "Dedicated Office", "Doorman", "Central HVAC"),
            latitude = 40.7163,
            longitude = -74.0086,
            hostName = "Julian Thorne",
            hostRating = 4.92,
            isFavorite = false,
            imageRes = R.drawable.img_prop_house
        ),
        Property(
            id = "prop_3",
            title = "Aspen Alpine Sanctuary & Spa",
            description = "Ski-in/ski-out luxury timber sanctuary surrounded by towering pines. Features heated stone patio with cedar hot tub, wood-burning stone fireplace, private sauna, and mountain views.",
            category = PropertyCategory.BUNGALOW,
            rentalMode = RentalMode.SHORT_TERM,
            city = "Aspen",
            state = "Colorado",
            country = "United States",
            addressLine = "Aspen Mountain Rd, Aspen, CO",
            nightlyPrice = 16500,
            monthlyPrice = 350000,
            cleaningFee = 1800,
            maxGuests = 10,
            bedrooms = 5,
            beds = 6,
            bathrooms = 5.0,
            squareFeet = 4600,
            rating = 4.99,
            reviewCount = 94,
            isSuperhost = true,
            instantBookable = true,
            amenities = listOf("Ski-in/Ski-out", "Cedar Hot Tub", "Finnish Dry Sauna", "Heated Driveway", "Wine Cellar", "Boot Warmers", "Game Room"),
            latitude = 39.1911,
            longitude = -106.8175,
            hostName = "Marcus Brody",
            hostRating = 4.97,
            isFavorite = true,
            imageRes = R.drawable.img_prop_villa
        ),
        Property(
            id = "prop_4",
            title = "Positano Cliffside Terraces",
            description = "Breathtaking multi-level residence hanging over the Tyrrhenian Sea. Pergola draped in fragrant wisteria, handcrafted Vietri ceramic floors, and panoramic vistas of Positano harbor.",
            category = PropertyCategory.VILLA,
            rentalMode = RentalMode.BOTH,
            city = "Positano",
            state = "Amalfi Coast",
            country = "Italy",
            addressLine = "Via Cristoforo Colombo 42, Positano",
            nightlyPrice = 12800,
            monthlyPrice = 280000,
            cleaningFee = 1400,
            maxGuests = 6,
            bedrooms = 3,
            beds = 4,
            bathrooms = 3.0,
            squareFeet = 2400,
            rating = 4.97,
            reviewCount = 145,
            isSuperhost = true,
            instantBookable = false,
            amenities = listOf("Panoramic Sea Terrace", "Luggage Porter Included", "Direct Harbor Steps", "Wood-fired Pizza Oven", "Air Conditioning", "Daily Housekeeping"),
            latitude = 40.6281,
            longitude = 14.4850,
            hostName = "Matteo Rossi",
            hostRating = 4.98,
            isFavorite = false,
            imageRes = R.drawable.img_rentora_hero
        ),
        Property(
            id = "prop_5",
            title = "Kyoto Zen Bamboo Pavilion",
            description = "Tranquil traditional Machiya reimagined with modern Scandinavian-Japanese minimalism. Featuring tatami tea room, internal moss rock garden, Hinoki cedar soaking bath, and sliding shoji screens.",
            category = PropertyCategory.HOUSE,
            rentalMode = RentalMode.BOTH,
            city = "Kyoto",
            state = "Kyoto Prefecture",
            country = "Japan",
            addressLine = "Higashiyama-ku, Kyoto",
            nightlyPrice = 6500,
            monthlyPrice = 140000,
            cleaningFee = 800,
            maxGuests = 4,
            bedrooms = 2,
            beds = 3,
            bathrooms = 1.5,
            squareFeet = 1350,
            rating = 4.96,
            reviewCount = 89,
            isSuperhost = true,
            instantBookable = true,
            amenities = listOf("Hinoki Cypress Bath", "Zen Moss Courtyard", "Ceremonial Tea Set", "Underfloor Heating", "High-speed Pocket Wifi", "Bicycle Fleet"),
            latitude = 34.9984,
            longitude = 135.7800,
            hostName = "Kenji Sato",
            hostRating = 4.95,
            isFavorite = true,
            imageRes = R.drawable.img_prop_house
        ),
        Property(
            id = "prop_6",
            title = "Saint-Germain Haussmannian Manor",
            description = "Historic Parisian elegance with chevron parquet floors, ornate gilded ceiling moldings, marble fireplaces, wrought-iron balconies overlooking boulevard cafes, and bespoke French furnishings.",
            category = PropertyCategory.VILLA,
            rentalMode = RentalMode.BOTH,
            city = "Paris",
            state = "Île-de-France",
            country = "France",
            addressLine = "Boulevard Saint-Germain, Paris",
            nightlyPrice = 11200,
            monthlyPrice = 240000,
            cleaningFee = 1200,
            maxGuests = 6,
            bedrooms = 3,
            beds = 3,
            bathrooms = 2.5,
            squareFeet = 2600,
            rating = 4.93,
            reviewCount = 52,
            isSuperhost = false,
            instantBookable = true,
            amenities = listOf("Boulevard Balcony", "Antique Fireplaces", "Wine Cooler", "Designer Linens", "Concierge Service", "Quiet Courtyard"),
            latitude = 48.8539,
            longitude = 2.3330,
            hostName = "Camille Laurent",
            hostRating = 4.90,
            isFavorite = false,
            imageRes = R.drawable.img_rentora_hero
        )
    )

    private val _properties = MutableStateFlow(initialProperties)
    val properties: StateFlow<List<Property>> = _properties.asStateFlow()

    private val _bookings = MutableStateFlow(
        listOf(
            Booking(
                id = "bkg_7129",
                propertyId = "prop_1",
                propertyTitle = "Azure Horizons Cliffside Villa",
                location = "Wailea, Maui, Hawaii",
                checkInDate = "Nov 14, 2026",
                checkOutDate = "Nov 19, 2026",
                nights = 5,
                guestsCount = 4,
                totalAmount = 82880.0,
                status = BookingStatus.CONFIRMED,
                confirmationCode = "RENT-7829-AZ",
                doorCode = "*4928#",
                checkInInstructions = "Smart keypad on the main glass entry gate. Tap to illuminate, enter *4928# and push handle. Garage opener is on the foyer credenza."
            ),
            Booking(
                id = "bkg_4402",
                propertyId = "prop_3",
                propertyTitle = "Aspen Alpine Sanctuary & Spa",
                location = "Aspen, Colorado",
                checkInDate = "Dec 22, 2026",
                checkOutDate = "Dec 28, 2026",
                nights = 6,
                guestsCount = 6,
                totalAmount = 112896.0,
                status = BookingStatus.CONFIRMED,
                confirmationCode = "RENT-9941-CO",
                doorCode = "8201#",
                checkInInstructions = "Code 8201# unlocks the heated ski-locker mudroom entrance. Host Marcus will meet you upon arrival."
            )
        )
    )
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    private val _leases = MutableStateFlow(
        listOf(
            Lease(
                id = "lease_309",
                propertyId = "prop_2",
                propertyTitle = "Tribeca Cast-Iron Designer Loft",
                address = "142 Franklin St #4A, New York, NY",
                monthlyRent = 8500,
                depositAmount = 8500,
                startDate = "Dec 1, 2026",
                endDate = "Nov 30, 2027",
                isSigned = true,
                nextRentDueDate = "Dec 1, 2026",
                rentPaidThisMonth = false
            )
        )
    )
    val leases: StateFlow<List<Lease>> = _leases.asStateFlow()

    private val _applications = MutableStateFlow(
        listOf(
            RentalApplication(
                id = "app_881",
                propertyId = "prop_2",
                propertyTitle = "Tribeca Cast-Iron Designer Loft",
                applicantName = "Alex Morgan",
                monthlyIncome = 28500.0,
                employer = "Horizon Systems Inc.",
                moveInDate = "Dec 1, 2026",
                leaseTermMonths = 12,
                status = ApplicationStatus.LEASE_OFFERED,
                submittedDate = "Oct 20, 2026"
            )
        )
    )
    val applications: StateFlow<List<RentalApplication>> = _applications.asStateFlow()

    private val _maintenanceTickets = MutableStateFlow(
        listOf(
            MaintenanceTicket(
                id = "maint_101",
                propertyTitle = "Tribeca Cast-Iron Designer Loft",
                category = "Plumbing",
                priority = MaintenancePriority.MEDIUM,
                description = "Master bath soaking tub faucet has a minor drip after usage.",
                status = "Scheduled (Tech Assigned)",
                createdAt = "Oct 24, 2026"
            )
        )
    )
    val maintenanceTickets: StateFlow<List<MaintenanceTicket>> = _maintenanceTickets.asStateFlow()

    private val _conversations = MutableStateFlow(
        listOf(
            Conversation(
                id = "conv_1",
                otherUserName = "Elena Vance (Host)",
                propertyTitle = "Azure Horizons Cliffside Villa",
                lastMessage = "Looking forward to hosting you in Maui! The infinity pool heater is set to 84°F.",
                timestamp = "2:45 PM",
                unreadCount = 1,
                messages = listOf(
                    ChatMessage("m1", "Elena Vance", "Aloha Alex! Thank you for reserving Azure Horizons.", "10:15 AM", false),
                    ChatMessage("m2", "Alex Morgan", "Hi Elena, so excited! Could we request a late checkout on the 19th?", "11:20 AM", true),
                    ChatMessage("m3", "Elena Vance", "Looking forward to hosting you in Maui! The infinity pool heater is set to 84°F. Late checkout at 1:00 PM is approved!", "2:45 PM", false)
                )
            ),
            Conversation(
                id = "conv_2",
                otherUserName = "Marcus Brody (Host)",
                propertyTitle = "Aspen Alpine Sanctuary",
                lastMessage = "Let me know your flight details and I'll arrange the airport private shuttle.",
                timestamp = "Yesterday",
                unreadCount = 0,
                messages = listOf(
                    ChatMessage("m4", "Marcus Brody", "Welcome to Aspen Alpine Sanctuary. Let me know your flight details and I'll arrange the airport private shuttle.", "Yesterday", false)
                )
            ),
            Conversation(
                id = "conv_3",
                otherUserName = "Rentora Concierge Support",
                propertyTitle = "24/7 Guest & Host Assurance",
                lastMessage = "Your identity verification has been confirmed! Enjoy your verified perks.",
                timestamp = "Oct 22",
                unreadCount = 0,
                messages = listOf(
                    ChatMessage("m5", "Rentora Support", "Your identity verification has been confirmed! Enjoy your verified perks.", "Oct 22", false)
                )
            )
        )
    )
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem("n1", "Booking Confirmed", "Your stay at Azure Horizons Villa is locked in.", "10m ago", false),
            NotificationItem("n2", "Lease Ready for Signature", "Landlord issued lease for Tribeca Loft.", "2h ago", false),
            NotificationItem("n3", "Upcoming Trip Reminder", "Maui trip is coming up in 19 days.", "1d ago", true)
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _blockedDates = MutableStateFlow(setOf("2026-11-20", "2026-11-21", "2026-11-22"))
    val blockedDates: StateFlow<Set<String>> = _blockedDates.asStateFlow()

    private val _disputes = MutableStateFlow(
        listOf(
            DisputeItem("dsp_1", "Positano Cliffside Terraces", "David Miller", "Keypad lock delay at check-in (reimbursement requested)", 80.0, "Open"),
            DisputeItem("dsp_2", "Saint-Germain Haussmannian", "Sarah Jenkins", "Damage deposit inquiry", 150.0, "Under Review")
        )
    )
    val disputes: StateFlow<List<DisputeItem>> = _disputes.asStateFlow()

    fun switchRole(newRole: UserRole) {
        _currentUser.value = _currentUser.value.copy(role = newRole)
    }

    fun setCurrency(currency: String) {
        _selectedCurrency.value = currency
    }

    fun formatPrice(amount: Double): String {
        return when (_selectedCurrency.value) {
            "INR" -> "₹${String.format("%,.0f", amount)}"
            "USD" -> "$${String.format("%,.0f", amount)}"
            "EUR" -> "€${String.format("%,.0f", amount * 0.92)}"
            "GBP" -> "£${String.format("%,.0f", amount * 0.78)}"
            "JPY" -> "¥${String.format("%,.0f", amount * 153.0)}"
            "CAD" -> "C$${String.format("%,.0f", amount * 1.38)}"
            else -> "₹${String.format("%,.0f", amount)}"
        }
    }

    fun toggleFavorite(propertyId: String) {
        _properties.value = _properties.value.map {
            if (it.id == propertyId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun createBooking(
        property: Property,
        checkIn: String,
        checkOut: String,
        nights: Int,
        guests: Int,
        extrasTotal: Double,
        discount: Double
    ): Booking {
        val baseAmount = (property.nightlyPrice * nights) + property.cleaningFee
        val serviceFee = baseAmount * property.serviceFeeRate
        val total = baseAmount + serviceFee + extrasTotal - discount
        val code = "RENT-${(1000..9999).random()}-${property.city.take(2).uppercase()}"

        val newBooking = Booking(
            id = "bkg_" + UUID.randomUUID().toString().take(8),
            propertyId = property.id,
            propertyTitle = property.title,
            location = "${property.city}, ${property.country}",
            checkInDate = checkIn,
            checkOutDate = checkOut,
            nights = nights,
            guestsCount = guests,
            totalAmount = total,
            status = BookingStatus.CONFIRMED,
            confirmationCode = code,
            doorCode = property.doorCode,
            checkInInstructions = property.houseRules.firstOrNull() ?: "Self check-in keypad"
        )

        _bookings.value = listOf(newBooking) + _bookings.value

        // Also add conversation
        val newConv = Conversation(
            id = "conv_" + UUID.randomUUID().toString().take(6),
            otherUserName = "${property.hostName} (Host)",
            propertyTitle = property.title,
            lastMessage = "Booking confirmed! We look forward to your arrival on $checkIn.",
            timestamp = "Just now",
            unreadCount = 1,
            messages = listOf(
                ChatMessage("msg_init", property.hostName, "Hi Alex! Your reservation for ${property.title} is confirmed. Let me know if you need any recommendations!", "Just now", false)
            )
        )
        _conversations.value = listOf(newConv) + _conversations.value

        return newBooking
    }

    fun cancelBooking(bookingId: String) {
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) it.copy(status = BookingStatus.CANCELLED) else it
        }
    }

    fun submitRentalApplication(
        property: Property,
        applicantName: String,
        monthlyIncome: Double,
        employer: String,
        termMonths: Int,
        moveInDate: String
    ): RentalApplication {
        val newApp = RentalApplication(
            id = "app_" + UUID.randomUUID().toString().take(6),
            propertyId = property.id,
            propertyTitle = property.title,
            applicantName = applicantName,
            monthlyIncome = monthlyIncome,
            employer = employer,
            moveInDate = moveInDate,
            leaseTermMonths = termMonths,
            status = ApplicationStatus.SUBMITTED,
            submittedDate = "Just now"
        )
        _applications.value = listOf(newApp) + _applications.value
        return newApp
    }

    fun signLease(leaseId: String) {
        _leases.value = _leases.value.map {
            if (it.id == leaseId) it.copy(isSigned = true) else it
        }
    }

    fun payRent(leaseId: String) {
        _leases.value = _leases.value.map {
            if (it.id == leaseId) it.copy(rentPaidThisMonth = true) else it
        }
    }

    fun submitMaintenanceTicket(propertyTitle: String, category: String, priority: MaintenancePriority, desc: String) {
        val ticket = MaintenanceTicket(
            id = "maint_" + (100..999).random(),
            propertyTitle = propertyTitle,
            category = category,
            priority = priority,
            description = desc,
            status = "Open (Dispatched to Super)",
            createdAt = "Today"
        )
        _maintenanceTickets.value = listOf(ticket) + _maintenanceTickets.value
    }

    fun sendMessage(conversationId: String, text: String) {
        _conversations.value = _conversations.value.map { conv ->
            if (conv.id == conversationId) {
                val newMsg = ChatMessage(
                    id = "msg_" + UUID.randomUUID().toString().take(6),
                    senderName = _currentUser.value.name,
                    messageText = text,
                    timestamp = "Just now",
                    isFromMe = true
                )
                conv.copy(
                    lastMessage = text,
                    timestamp = "Just now",
                    messages = conv.messages + newMsg
                )
            } else conv
        }
    }

    fun toggleBlockDate(date: String) {
        val current = _blockedDates.value.toMutableSet()
        if (current.contains(date)) current.remove(date) else current.add(date)
        _blockedDates.value = current
    }

    fun addHostListing(
        title: String,
        description: String,
        category: PropertyCategory,
        city: String,
        state: String,
        country: String,
        nightlyPrice: Int,
        monthlyPrice: Int,
        bedrooms: Int,
        beds: Int,
        bathrooms: Double,
        amenities: List<String>
    ) {
        val newProp = Property(
            id = "prop_" + UUID.randomUUID().toString().take(6),
            title = title,
            description = description,
            category = category,
            rentalMode = RentalMode.BOTH,
            city = city,
            state = state,
            country = country,
            nightlyPrice = nightlyPrice,
            monthlyPrice = monthlyPrice,
            bedrooms = bedrooms,
            beds = beds,
            bathrooms = bathrooms,
            amenities = amenities,
            latitude = 37.7749,
            longitude = -122.4194,
            hostName = _currentUser.value.name,
            imageRes = R.drawable.img_rentora_hero
        )
        _properties.value = listOf(newProp) + _properties.value
    }

    fun approveListing(propertyId: String) {
        // Moderator action
    }

    fun resolveDispute(disputeId: String) {
        _disputes.value = _disputes.value.map {
            if (it.id == disputeId) it.copy(status = "Resolved") else it
        }
    }
}
