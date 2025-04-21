# Setup Guide

This guide will help you set up the MyMessage Android app on your local development environment.

## 🛠 Prerequisites

Before you begin, ensure you have the following:

- ✅ Android Studio **Hedgehog** or later
- ✅ **JDK 17** or higher
- ✅ A stable internet connection
- ✅ An Android device or emulator

## 🚀 Getting Started

Clone the project and switch to the working branch:

```bash
git clone https://github.com/sonofgreatness/MyMessage.git
cd MyMessage
git checkout main
```

ℹ️ You’ll be making changes in the main branch locally, and pushing to master for releases.


## ▶️ Run the App
Open the project in Android Studio.

- Let Gradle sync automatically. If not, click File > Sync Project with Gradle Files.
- Connect an Android device or launch an emulator.
- From the toolbar, choose the app configuration and click Run (▶️).


## 📦 Managing App Versions
To ensure your app correctly checks for updates via GitHub:

- Locate your SharedPreferences implementation.
- Set the key app_version to match the latest GitHub release tag.
- This enables version-aware update checks via the GitHub API.


```kotlin
val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
sharedPreferences.edit().putString("app_version", "v1.0.0").apply()
```



For further inquiries:

- 📧 [simphiweiq@gmail.com](mailto:simphiweiq@gmail.com)
- 📞 +268 76911464 / (+268) 79487461
