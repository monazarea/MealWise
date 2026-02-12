# 🥗 MealWise - Android Food Planner App

**MealWise** is a comprehensive Android application designed to help users discover recipes and organize their nutrition through a smart weekly planner. 

---


## 🛠️ Key Features
* **Daily Meal Discovery:** Get random meal inspirations every day.
* **Smart Search:** Filter by Category (Beef, Seafood, etc.), Area (Egyptian, Italian, etc.), or Ingredients.
* **Weekly Planner:** A dedicated calendar to schedule your meals for the entire week.
* **Favorites:** Save your favorite recipes for quick access.
* **Cloud Sync:** Uses Firebase to sync your data so you never lose your favorites or plans.
* **Offline Access:** Powered by Room Database to keep your data available even without internet.

---

## 🏗️ Architecture & Technologies
This project is built using modern Android development practices:

* **Architecture:** MVP (Model-View-Presenter) to ensure clean code and testability.
* **Networking:** **Retrofit** with **RxJava3** for handling API calls from `TheMealDB`.
* **Local Storage:** **Room Database** for caching and offline support.
* **Backend:** **Firebase Authentication** & **Cloud Firestore** for real-time synchronization.
* **Image Loading:** **Glide** for smooth image rendering.
* **Reactive Programming:** **RxJava3** (Flowables, Completable, Singles) to manage data streams efficiently.

---

## 🚀 How to Run
1. Clone the repository.
2. Open the project in **Android Studio (Ladybug or later)**.
3. Make sure to add your own `google-services.json` in the `app/` folder.
4. Build and Run on an emulator or a physical device.

---

## 👨‍💻 Developed By 
* **[Mona Zarea]** - Android Developer

---

## 📄 License
This project was developed as part of a technical training.
