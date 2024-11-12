# YouTube Trender

## Table of Contents
1. [Introduction](#introduction)
2. [System Requirements](#system-requirements)
3. [Installation](#installation)
4. [How to Use](#how-to-use)
5. [Key Features](#key-features)
6. [Project Structure](#project-structure)
7. [Notes](#notes)

---

## Introduction
**YouTube Trender** is a Java-based application with a graphical user interface (GUI) that helps users analyze YouTube video data. This application can:
- Load and parse data from a JSON file.
- Display a list of YouTube videos.
- Sort videos by various criteria such as **channel**, **publish date**, **view count**, and **description length**.
- Display detailed information about a selected video.

---

## System Requirements
- Java Development Kit (JDK) version 8 or higher.
- Integrated Development Environment (IDE): Recommended options are IntelliJ IDEA, Eclipse, or NetBeans.
- JSON library (`org.json`) for parsing JSON files.
- Operating System: Windows, macOS, or Linux.

---

## Installation
1. **Clone the project** to your local machine:
    ```bash
    git clone https:https://github.com/HarryLe-Theanalyst/YoutubeTrender
    cd YoutubeTrender
    ```
2. **Install the JSON library** if not already included:
   - Download `org.json` from [Maven Central](https://mvnrepository.com/artifact/org.json/json).
   - Add `json.jar` to your project's classpath.
   
3. **Open the project** in your preferred IDE and ensure it is configured with the appropriate JDK version.

---

## How to Use
1. **Run the application**:
    - Open `YouTubeTrenderFrame.java` and run the `main` method.
2. **Load data from a JSON file**:
    - Enter the path to the JSON file in the **Data File** text field (default: `data/youtubedata_15_50.json`).
    - Click the **Load** button to parse and display the data.
3. **Sort videos**:
    - Select a sorting criterion from the **Sort Criteria** section (Channel, Date, Views, or Description).
    - Click the **Sort** button to reorder the video list.
4. **View video details**:
    - Click on any video in the list to see its details, including the channel, publish date, title, view count, and description.

---

## Key Features
### 1. Load and Parse Data
- Uses `YouTubeDataParser` to read and convert JSON data into a list of `YouTubeVideo` objects.

### 2. Display Video List
- Parsed data is displayed in a `JList`, allowing users to select a video and view its details.

### 3. Sort Videos by Criteria
- Available sorting options:
  - **Channel**: Sort by chann
