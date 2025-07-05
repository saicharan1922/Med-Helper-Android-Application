# 📱 Medicine Reminder OCR

### **Overview**
Medicine Reminder OCR is an Android application designed to help users manage their medication schedule efficiently. It allows users to:

- Scan medicine names using **Optical Character Recognition (OCR)**
- Set reminders and alarms for medication timings
- View, edit, or delete scheduled medicines
- Enhance adherence to medical prescriptions for personal health management

---

## ✨ **Features**

✅ **OCR Integration** – Capture and extract text from medicine strips or prescriptions for quick entry  
✅ **Add Pill Reminders** – Set alarms with medicine details and dosage  
✅ **Full Screen Alarm Notifications** – Ensures users never miss a dose  
✅ **History & Details View** – Easily manage and review medicine schedules  
✅ **Login & Register Module** – User authentication for data privacy  
✅ **Material UI Design** – Clean, responsive, and intuitive interface

---

## 🚀 **Tech Stack**

- **Language:** Java
- **Platform:** Android
- **Libraries & Tools:**
  - Google ML Kit (for OCR)
  - AndroidX
  - Gradle

---

## 🔧 **Project Structure**

MedicineReminder/
│
├── app/
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/com/lamdah/medicinereminder/ # Activities, Receivers, Managers
│ │ │ ├── res/ # Layouts, Drawables, Values
│ │ │ └── AndroidManifest.xml
│ │ └── test/ # Unit tests
│ └── build.gradle
│
├── gradle/
│
├── gradlew
├── gradlew.bat
├── build.gradle
└── settings.gradle

yaml
Copy
Edit

---

## 📝 **Setup Instructions**

1. Clone the repository:

```bash
git clone https://github.com/yourusername/MedicineReminderOCR.git
Open the project in Android Studio.

Ensure Google ML Kit dependencies are configured.

Run on your Android device or emulator with camera permission granted.

🎯 Usage
Register/Login to the app.

Tap Add Pill to scan medicine using OCR or enter manually.

Set the reminder time and save.

Receive full-screen alarms at scheduled timings.

💡 Future Improvements
Cloud backup integration

Push notification enhancements

User profile health analytics dashboard

Improved OCR accuracy with custom ML models

🤝 Contributing
Contributions are welcome! Please open an issue first to discuss your ideas before creating a pull request.

📄 License
This project is open-source under the MIT License.
