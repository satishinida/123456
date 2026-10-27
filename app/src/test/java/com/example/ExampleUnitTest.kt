package com.example

import com.example.rentora.data.RentoraRepository
import com.example.rentora.model.BookingStatus
import com.example.rentora.model.MaintenancePriority
import com.example.rentora.model.UserRole
import org.junit.Assert.*
import org.junit.Test

class RentoraUnitTest {

    @Test
    fun testInitialSeedProperties() {
        val properties = RentoraRepository.properties.value
        assertTrue("Repository should have seed properties", properties.isNotEmpty())
        val firstProp = properties.first()
        assertNotNull(firstProp.title)
        assertTrue(firstProp.nightlyPrice > 0)
        assertTrue(firstProp.monthlyPrice > 0)
    }

    @Test
    fun testCurrencyFormatting() {
        RentoraRepository.setCurrency("INR")
        assertEquals("₹1,000", RentoraRepository.formatPrice(1000.0))

        RentoraRepository.setCurrency("USD")
        assertEquals("$1,000", RentoraRepository.formatPrice(1000.0))

        RentoraRepository.setCurrency("EUR")
        assertEquals("€920", RentoraRepository.formatPrice(1000.0))

        RentoraRepository.setCurrency("GBP")
        assertEquals("£780", RentoraRepository.formatPrice(1000.0))

        // Reset
        RentoraRepository.setCurrency("INR")
    }

    @Test
    fun testBookingCreationAndCancellation() {
        val prop = RentoraRepository.properties.value.first()
        val booking = RentoraRepository.createBooking(
            property = prop,
            checkIn = "Dec 10, 2026",
            checkOut = "Dec 15, 2026",
            nights = 5,
            guests = 2,
            extrasTotal = 120.0,
            discount = 50.0
        )

        assertEquals(BookingStatus.CONFIRMED, booking.status)
        assertTrue(booking.confirmationCode.startsWith("RENT-"))
        assertNotNull(booking.doorCode)

        // Test cancel booking
        RentoraRepository.cancelBooking(booking.id)
        val cancelled = RentoraRepository.bookings.value.first { it.id == booking.id }
        assertEquals(BookingStatus.CANCELLED, cancelled.status)
    }

    @Test
    fun testRentalApplicationAndLeaseSigning() {
        val prop = RentoraRepository.properties.value.first()
        val app = RentoraRepository.submitRentalApplication(
            property = prop,
            applicantName = "Alex Morgan",
            monthlyIncome = 24000.0,
            employer = "Stripe",
            termMonths = 12,
            moveInDate = "Jan 1, 2027"
        )
        assertNotNull(app.id)

        val activeLease = RentoraRepository.leases.value.first()
        RentoraRepository.signLease(activeLease.id)
        val signedLease = RentoraRepository.leases.value.first { it.id == activeLease.id }
        assertTrue(signedLease.isSigned)

        // Pay rent
        RentoraRepository.payRent(activeLease.id)
        val paidLease = RentoraRepository.leases.value.first { it.id == activeLease.id }
        assertTrue(paidLease.rentPaidThisMonth)
    }

    @Test
    fun testMaintenanceTicketSubmission() {
        RentoraRepository.submitMaintenanceTicket(
            propertyTitle = "Azure Horizons",
            category = "HVAC",
            priority = MaintenancePriority.HIGH,
            desc = "Air conditioning unit needs filter replacement."
        )
        val tickets = RentoraRepository.maintenanceTickets.value
        val submitted = tickets.firstOrNull { it.category == "HVAC" }
        assertNotNull(submitted)
        assertEquals("HVAC", submitted?.category)
    }

    @Test
    fun testRoleSwitching() {
        RentoraRepository.switchRole(UserRole.HOST)
        assertEquals(UserRole.HOST, RentoraRepository.currentUser.value.role)

        RentoraRepository.switchRole(UserRole.TENANT)
        assertEquals(UserRole.TENANT, RentoraRepository.currentUser.value.role)

        RentoraRepository.switchRole(UserRole.GUEST)
        assertEquals(UserRole.GUEST, RentoraRepository.currentUser.value.role)
    }

    @Test
    fun testCalendarDateBlocking() {
        val testDate = "2026-12-25"
        RentoraRepository.toggleBlockDate(testDate)
        assertTrue(RentoraRepository.blockedDates.value.contains(testDate))

        RentoraRepository.toggleBlockDate(testDate)
        assertFalse(RentoraRepository.blockedDates.value.contains(testDate))
    }
}
