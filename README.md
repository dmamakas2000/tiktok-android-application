# TikTok Video Streaming Platform: A Distributed Broker-Client Android Application :iphone: :video_camera: :family:

<br>

## 📢Note📢
🎯Please, clone this repository before reading the description. Don't forget to like👍and share your thoughts😊.

<br>

## About
This repository contains the second phase of the TikTok Java App project, transitioning from the foundational framework developed in Part 1 to an Android application. The goal is to implement a fully functional Android app that allows users to browse, upload, and play videos in real time while leveraging the framework and algorithms developed earlier. This phase focuses on integrating video streaming and playback functionalities, enabling seamless content delivery from creators to subscribers on Android devices.

## Features
**Real-Time Video Playback:**
- The application streams videos directly from user channels, supporting real-time playback through a custom player built with the Android Framework.

**Channel and Hashtag Browsing:**
- Users can view a list of available channels or search for videos using hashtags.
- If no content is found, the app will display an appropriate message and suggest alternative options.

**Video Uploading:**
- Users can upload videos, either recording them directly through the app or selecting pre-recorded files from their device.

**Broker-Based Communication:**
- The app communicates with a broker node to fetch necessary information when the channel list or search results are initially empty.

**Tabbed Interface:**
- Utilizes Android's Tabbed Activity for seamless navigation between video browsing and uploading sections.

## How It Works
**Main Screen:**
- Displays a list of available channels, allowing users to:
  - Select a channel to watch videos.
  - Upload videos to their channel.
  - Search for videos by hashtags.
- If the list is empty, the app requests information from a broker node.

**Handling Empty Results:**
- If the broker node provides no data after a reasonable wait time, the app displays a "not found" message and suggests alternatives.

**Video Playback:**
- Once the app retrieves video data from the broker, it uses a simple custom player, built with the Android Framework to play the videos.

**Uploading Videos:**
- Users can record videos through the app or select pre-recorded videos for uploading.

## Prerequisites
1. Android Studio: The primary IDE for Android development.
1. Android 5.0 (Lollipop) or later: The minimum required version for running the application.

## Getting Started

1. Clone the repository:
```
git clone https://github.com/dmamakas2000/tiktok-android-app.git
cd tiktok-android-app  
```

1. Open the project in Android Studio.
1. Sync the Gradle files to install all dependencies.
1. Run the application on an emulator or Android device.

## Directory Structure
```
tiktok-android-app/  
│  
├── app/  
│   ├── src/  
│   │   ├── main/  
│   │   │   ├── java/       # Core application logic  
│   │   │   ├── res/        # Layouts, drawables, and other resources  
│   │   │   └── AndroidManifest.xml  # App configuration  
│  
├── README.md               # Project documentation  
└── build/                  # Build outputs  
```

## License
This project is licensed under the MIT License - see the LICENSE file for details.
