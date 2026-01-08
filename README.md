# CarWashApp

# Car Wash Booking System

**Platform:** Android (Kotlin) + ASP.NET Web API + SQL Server  
**Project Type:** Mobile App with Web API Backend  

---

## Overview
The Car Wash Booking System is an Android application designed to simplify booking car wash services.  
It allows customers to book a service at a specific time slot directly from their phone, while staff and admins can manage and update bookings efficiently.  

---

## Features

### Customer Module
- User Registration and Secure Login  
- Book services (Quick Wash, Polish, etc.) by selecting date and time  
- View booking history with status (Pending, Completed, Cancelled)  

### Staff/Admin Module
- View all bookings from customers  
- Access customer details (Name, Email)  
- Update booking status (Completed, Cancelled)  

---

## Technologies Used

**Front-End (Mobile App)**
- Language: Kotlin  
- IDE: Android Studio  
- UI Design: XML, RelativeLayout, CardView, RecyclerView  
- Networking: Retrofit (API calls)  

**Back-End (Server)**
- Framework: ASP.NET Web API (.NET Framework)  
- Language: C#  
- Database: SQL Server  
- ORM: Entity Framework  

---

## Database Design
- **Users Table:** Stores UserId, Name, Email, Password, and Role (Customer or Staff)  
- **Bookings Table:** Stores BookingId, ServiceType, TimeSlot, Date, TotalAmount, Status; linked to Users Table via UserId  

---

## Screenshots
*(Add screenshots from your Android app here for better visualization)*

---

## Installation & Setup
1. Clone this repository:  
   ```bash
   git clone https://github.com/Asad4625/CarWashApp.git

   
## Possible Enhancements

Add push notifications for booking reminders

Enable payment integration

Add filtering and search for available slots

Implement persistent local caching
