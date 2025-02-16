# MVVMDI

## Overview

This is an Android application built using Kotlin and follows the MVVM architecture pattern. The app provides various features including:

- Fetching and displaying a paginated list of users from an API (`https://reqres.in/api/users?page={page}`).
- Creating a new user by entering a name and job.
    - If online: Sends a POST request to `https://reqres.in/api/users`.
    - If offline: Stores data in Room Database and syncs with the API using WorkManager when internet is available.
- Fetching and displaying a paginated list of trending movies from The Movie Database (TMDb) API.
- Navigating to a Movie Detail Screen when a movie is selected.

## Technologies Used

- **Programming Language**: Kotlin
- **Architecture**: MVVM
- **Networking**: Retrofit
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Database**: Room
- **Background Work**: WorkManager
- **UI Components**: RecyclerView, ViewModel, LiveData

## Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/android-app.git
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an emulator or physical device.

## API Configuration

To use the APIs, add the following keys to your `local.properties` file:

```properties
TMDB_API_KEY=your_api_key_here
REQRES_BASE_URL=https://reqres.in/api/
TMDB_BASE_URL=https://api.themoviedb.org/3/
```

## Features

### User Management

- Fetch and display a paginated list of users.
- Create users with an online/offline sync mechanism.

### Movie List

- Fetch and display trending movies from TMDb.
- Navigate to detailed movie information.

## Contribution

Feel free to fork the repository and contribute by submitting pull requests.

## License

This project is licensed under the MIT License.

