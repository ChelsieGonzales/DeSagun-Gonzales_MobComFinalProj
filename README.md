# CICS SmartPass

CICS SmartPass is an Android application designed for fast and secure student attendance tracking at the College of Industrial and Computer Studies (CICS). The app allows students to log in, view their QR codes for attendance, submit excuse letters, and see upcoming events. It leverages Firebase Realtime Database and Firebase Authentication for secure data storage and user management.

## Features

* **User Authentication:** Sign up and login using Firebase Authentication.
* **QR Code Attendance:** Each student has a unique QR code for attendance tracking.
* **Next Event Display:** View details of upcoming events from Firebase Realtime Database.
* **Excuse Letter Submission:** Students can submit medical certificates or excuse letters.
* **Profile Management:** Basic user profile displaying student name and QR code.
* **Logout Functionality:** Securely sign out from the app.

---
## Technologies Used

* **Programming Language:** Kotlin
* **UI Framework:** Jetpack Compose
* **Navigation:** Jetpack Navigation Compose
* **Database:** Firebase Realtime Database
* **Authentication:** Firebase Authentication
* **QR Code:** ZXing Android Embedded
* **Minimum SDK:** 24
* **Target SDK:** 36

---

## Project Structure

```
com.example.mobcomfinal
│
├─ HomeScreen.kt         # Main dashboard with QR code and navigation buttons
├─ NextEventScreen.kt    # Displays next upcoming event
├─ SignUpScreen.kt       # User registration screen
├─ LoginScreen.kt        # User login screen
├─ ExcuseNoteScreen.kt   # Submit medical/excuse letters
├─ QRCodeImage.kt        # Component to generate QR codes
└─ ...
```
---
## Usage

1. **Sign Up:** Create an account with your name, student number, email, and password.
2. **Login:** Access your account using registered credentials.
3. **View QR Code:** Your unique QR code is displayed on the Home Screen for attendance scanning.
4. **Next Event:** Tap "View Next Event" to see upcoming events.
5. **Excuse Letter:** Tap "Submit Excuse Letter" to upload medical certificates or letters.
6. **Logout:** Securely log out from the app using the "Logout" button.

---

## Future Improvements

* **Firebase Storage Integration:** Store uploaded excuse letters securely.
* **Push Notifications:** Notify students of upcoming events.
* **Admin Panel:** Allow admin users to manage events and verify excuse letters.
* **Enhanced QR Code Security:** Implement dynamic QR codes for extra security.


