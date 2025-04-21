
# Architecture Overview

MyMessage uses a layered architecture to ensure maintainability and scalability.

## Overview Diagram

 <p align="center">
  <img src="https://sonofgreatness.github.io/image-holder/images/Architecture.png" width="450">
</p>

## Layers

- **UI Layer (Jetpack Compose)**  
  - Stateless components  
  - Single responsibility per screen

- **ViewModel Layer**  
  - Powered by Hilt for DI  
  - Manages UI state and logic

- **Domain Layer**  
  - Use cases for message handling, theme preference, and update checking

- **Data Layer**  
  - SharedPreferences  
  - Remote GitHub API for release metadata

- **Workers**  
  - WorkManager for background tasks such as SMS monitoring

## Design Principles

- SOLID principles  
- Clean Architecture  
- Testability
