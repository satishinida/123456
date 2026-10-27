# RENTORA System Architecture & Specification

## Executive Overview
RENTORA is a modern, unified rental marketplace connecting guests, tenants, hosts, property managers, and marketplace administrators across short-term vacation stays and long-term residential leases.

---

## 1. Monorepo & System Topology

```
rentora/
├── apps/
│   ├── mobile/         # Android (Kotlin / Jetpack Compose / M3) & Cross-Platform Client
│   ├── admin/          # Responsive Next.js App Router Admin & Moderation Console
│   └── api/            # NestJS Core API Engine with OpenAPI / Swagger
├── packages/
│   ├── ui/             # Rentora Design Tokens (#FF5A5F, #222222, 16dp radii)
│   ├── types/          # Shared DTOs, Zod Schemas & State Interfaces
│   ├── config/         # Environment, Security, Regulatory Policies
│   └── api-client/     # TanStack Query & Type-safe HTTP Client
├── infrastructure/
│   ├── docker/         # Docker Compose (PostgreSQL 16, PostGIS, Redis 7)
│   └── k8s/            # Cloud deployment descriptors & Ingress
└── docs/               # System Architecture, Runbooks & Regulatory Compliance
```

---

## 2. Relational Database Design (Prisma / PostGIS)

```prisma
datasource db {
  provider   = "postgresql"
  url        = env("DATABASE_URL")
  extensions = [postgis]
}

generator client {
  provider        = "prisma-client-js"
  previewFeatures = ["postgresqlExtensions"]
}

enum UserRole {
  GUEST
  TENANT
  HOST
  PROPERTY_MANAGER
  ADMIN
  SUPER_ADMIN
}

enum RentalMode {
  SHORT_TERM
  LONG_TERM
  BOTH
}

enum BookingStatus {
  PENDING
  CONFIRMED
  ACTIVE
  COMPLETED
  CANCELLED
  DISPUTED
}

enum ApplicationStatus {
  DRAFT
  SUBMITTED
  UNDER_REVIEW
  APPROVED
  REJECTED
  LEASE_OFFERED
  LEASE_SIGNED
}

enum MaintenancePriority {
  LOW
  MEDIUM
  HIGH
  EMERGENCY
}

model User {
  id                   String                 @id @default(uuid())
  email                String                 @unique
  phoneNumber          String?                @unique
  passwordHash         String
  fullName             String
  avatarUrl            String?
  role                 UserRole               @default(GUEST)
  isIdentityVerified   Boolean                @default(false)
  twoFactorEnabled     Boolean                @default(false)
  createdAt            DateTime               @default(now())
  updatedAt            DateTime               @updatedAt

  hostProfile          HostProfile?
  properties           Property[]             @relation("HostProperties")
  bookings             Booking[]              @relation("GuestBookings")
  applications         RentalApplication[]
  leases               Lease[]                @relation("TenantLeases")
  reviewsWritten       Review[]               @relation("AuthorReviews")
  sentMessages         Message[]              @relation("SenderMessages")
  wishlists            Wishlist[]
  notifications        Notification[]
}

model HostProfile {
  id                   String                 @id @default(uuid())
  userId               String                 @unique
  user                 User                   @relation(fields: [userId], references: [id])
  bio                  String?
  superhostStatus      Boolean                @default(false)
  responseRate         Float                  @default(0.98)
  responseTimeMinutes  Int                    @default(15)
  stripeAccountId      String?
  payoutsEnabled       Boolean                @default(false)
  totalEarningsMinor   BigInt                 @default(0)
  createdAt            DateTime               @default(now())
}

model Property {
  id                   String                 @id @default(uuid())
  hostId               String
  host                 User                   @relation("HostProperties", fields: [hostId], references: [id])
  title                String
  description          String
  category             String                 // Apartment, Villa, House, Cabin, etc.
  rentalMode           RentalMode             @default(BOTH)
  
  // Location & Spatial
  addressLine1         String
  city                 String
  state                String
  country              String
  postalCode           String
  latitude             Float
  longitude            Float

  // Specifications
  maxGuests            Int                    @default(2)
  bedrooms             Int                    @default(1)
  beds                 Int                    @default(1)
  bathrooms            Float                  @default(1.0)
  squareFeet           Int                    @default(650)

  // Pricing (Minor Units / Cents)
  nightlyPriceMinor    Int
  monthlyPriceMinor    Int
  cleaningFeeMinor     Int                    @default(0)
  securityDepositMinor Int                    @default(0)
  currency             String                 @default("USD")

  // Flags & Amenities
  instantBookable      Boolean                @default(true)
  amenities            String[]
  houseRules           String[]
  photoUrls            String[]
  ratingAverage        Float                  @default(5.0)
  reviewCount          Int                    @default(0)
  isPublished          Boolean                @default(true)
  moderationApproved   Boolean                @default(true)
  createdAt            DateTime               @default(now())
  updatedAt            DateTime               @updatedAt

  bookings             Booking[]
  leases               Lease[]
  applications         RentalApplication[]
  reviews              Review[]
  maintenanceRequests  MaintenanceRequest[]
}

model Booking {
  id                   String                 @id @default(uuid())
  propertyId           String
  property             Property               @relation(fields: [propertyId], references: [id])
  guestId              String
  guest                User                   @relation("GuestBookings", fields: [guestId], references: [id])
  checkIn              DateTime
  checkOut             DateTime
  guestsCount          Int
  
  // Financial breakdown
  totalAmountMinor     Int
  taxAmountMinor       Int
  serviceFeeMinor      Int
  cleaningFeeMinor     Int
  hostPayoutMinor      Int
  currency             String                 @default("USD")
  
  status               BookingStatus          @default(CONFIRMED)
  paymentIntentId      String?
  checkInInstructions  String?
  wifiNetwork          String?
  wifiPassword         String?
  createdAt            DateTime               @default(now())
  updatedAt            DateTime               @updatedAt
}

model Lease {
  id                   String                 @id @default(uuid())
  propertyId           String
  property             Property               @relation(fields: [propertyId], references: [id])
  tenantId             String
  tenant               User                   @relation("TenantLeases", fields: [tenantId], references: [id])
  startDate            DateTime
  endDate              DateTime
  monthlyRentMinor     Int
  depositAmountMinor   Int
  isSigned             Boolean                @default(false)
  signedAt             DateTime?
  documentUrl          String?
  status               String                 @default("ACTIVE")
  createdAt            DateTime               @default(now())

  rentPayments         RentPayment[]
}

model RentPayment {
  id                   String                 @id @default(uuid())
  leaseId              String
  lease                Lease                  @relation(fields: [leaseId], references: [id])
  dueDate              DateTime
  paidDate             DateTime?
  amountMinor          Int
  status               String                 @default("PENDING") // PENDING, PAID, OVERDUE
  transactionReference String?
}

model MaintenanceRequest {
  id                   String                 @id @default(uuid())
  propertyId           String
  property             Property               @relation(fields: [propertyId], references: [id])
  tenantName           String
  title                String
  description          String
  category             String                 // Plumbing, Electrical, HVAC, Appliance, General
  priority             MaintenancePriority    @default(MEDIUM)
  status               String                 @default("OPEN") // OPEN, IN_PROGRESS, RESOLVED
  createdAt            DateTime               @default(now())
  resolvedAt           DateTime?
}
```

---

## 3. Core Business Logic & State Machines

### Short-Term Stay Lifecycle
```
[Select Dates & Extras] ──> [Atomic Availability Lock] ──> [Checkout & Payment Intent] 
  ──> [Instant Confirmation] ──> [Active Stay & Check-in Details] ──> [Checkout & Review]
```

### Long-Term Lease Lifecycle
```
[Schedule Tour / Viewing] ──> [Rental Application & Screening] ──> [Host / Landlord Review]
  ──> [Approve & Issue Lease] ──> [E-Signature & Security Deposit] ──> [Monthly Rent Portal]
```

### Double-Booking Prevention & Concurrency Protection
1. Availability queries check exact intersection with existing confirmed bookings and host blackout dates:
   `NOT EXISTS (SELECT 1 FROM Booking WHERE property_id = :p AND status = 'CONFIRMED' AND check_in < :new_out AND check_out > :new_in)`.
2. PostgreSQL transaction-level locking (`SELECT ... FOR UPDATE` on property calendar partitions) guarantees zero race conditions during peak checkout periods.
