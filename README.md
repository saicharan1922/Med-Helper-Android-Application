# 📱 Medicine Reminder OCR

Medicine Reminder OCR is an Android application designed to help users manage their medication schedule efficiently. It combines **Optical Character Recognition (OCR)** with reminders and alarms to ensure users never miss their medicines.

---

## ✨ Features

- **OCR Integration** – Capture and extract text from medicine strips or prescriptions for quick entry.
- **Add Pill Reminders** – Set alarms with medicine details and dosage.
- **Full Screen Alarm Notifications** – Ensures users never miss a dose.
- **History & Details View** – Manage and review medicine schedules.
- **Login & Register Module** – User authentication for data privacy.
- **Material UI Design** – Clean, responsive, and intuitive interface.

---

## 🚀 Tech Stack

- **Language:** Java
- **Platform:** Android
- **Libraries & Tools:**
  - Google ML Kit (for OCR)
  - AndroidX
  - Gradle

---

## 🔧 Project Structure

```
MedicineReminder/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/lamdah/medicinereminder/   # Activities, Receivers, Managers
│   │   │   ├── res/                               # Layouts, Drawables, Values
│   │   │   └── AndroidManifest.xml
│   │   └── test/                                  # Unit tests
│   └── build.gradle
│
├── gradle/
│
├── gradlew
├── gradlew.bat
├── build.gradle
└── settings.gradle
```

---

## 📝 Setup Instructions

1. **Clone the repository:**

```bash
git clone https://github.com/yourusername/MedicineReminderOCR.git
```

2. Open the project in **Android Studio**.
3. Ensure Google ML Kit dependencies are configured.
4. Run on your Android device or emulator with camera permission granted.

---

## 🎯 Usage

1. **Register/Login** to the app.
2. Tap **Add Pill** to scan medicine using OCR or enter manually.
3. Set the reminder time and save.
4. Receive full-screen alarms at scheduled timings.

---

## 💡 Future Improvements

- Cloud backup integration
- Push notification enhancements
- User profile health analytics dashboard
- Improved OCR accuracy with custom ML models

---

## 🤝 Contributing

Contributions are welcome! Please open an issue first to discuss your ideas before creating a pull request.

---

## 📄 License

This project is open-source under the [MIT License](LICENSE).