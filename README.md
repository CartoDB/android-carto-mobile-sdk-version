# Android Carto Mobile SDK

This repository contains the Carto Mobile SDK for Android, distributed via GitHub Packages.

Starting in the 5 version. This version add 16Kb page size to the arm64 compilation.

## 📦 Published Package Information

- **Group ID:** `com.carto`
- **Artifact ID:** `carto-mobile-sdk`
- **Version:** `5.0.0`
- **Repository:** [GitHub Packages](https://maven.pkg.github.com/CartoDB/android-carto-mobile-sdk-version)

---

## 🚀 How to Add This Library to Your Project

### **Step 1: Configure GitHub Packages Repository**

#### For Projects Using `settings.gradle` (Gradle 7.0+):

Add the following to your `settings.gradle` file:

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()

        // Add GitHub Packages repository
        maven {
            url = uri("https://maven.pkg.github.com/CartoDB/android-carto-mobile-sdk-version")
            credentials {
                username = providers.gradleProperty("gpr.usr").getOrElse(System.getenv("GPR_USER"))
                password = providers.gradleProperty("gpr.key").getOrElse(System.getenv("GPR_API_KEY"))
            }
        }
    }
}
```

#### For Projects Using Root `build.gradle` (Older Gradle Versions):

Add the following to your root `build.gradle` file:

```groovy
allprojects {
    repositories {
        google()
        mavenCentral()

        // Add GitHub Packages repository
        maven {
            url = uri("https://maven.pkg.github.com/CartoDB/android-carto-mobile-sdk-version")
            credentials {
                username = project.findProperty("gpr.usr") ?: System.getenv("GPR_USER")
                password = project.findProperty("gpr.key") ?: System.getenv("GPR_API_KEY")
            }
        }
    }
}
```

---

### **Step 2: Create GitHub Credentials File**

Create a `github.properties` file in your project's root directory:

```properties
gpr.usr=your-github-username
gpr.key=your-github-personal-access-token
```

**Important:**

- Your GitHub Personal Access Token must have at least `read:packages` permission
- Add `github.properties` to your `.gitignore` file to keep credentials secure

```gitignore
# Add this to your .gitignore
github.properties
```

---

### **Step 3: Add Dependency**

In your app module's `build.gradle` file, add the dependency:

```groovy
dependencies {
    implementation 'com.carto:carto-mobile-sdk:5.0.0'

    // ... other dependencies
}
```

---

### **Step 4: Sync Project**

Sync your project in Android Studio or run:

```bash
./gradlew build
```

---

## 🔐 Alternative: Using Environment Variables

For CI/CD pipelines or if you prefer not to use a properties file, you can set environment variables:

```bash
export GPR_USER=your-github-username
export GPR_API_KEY=your-github-token
```

The repository configuration will automatically fall back to these environment variables if the `github.properties` file is not found.

---

## 📝 Creating a GitHub Personal Access Token

1. Go to [GitHub Settings > Developer Settings > Personal Access Tokens](https://github.com/settings/tokens)
2. Click "Generate new token (classic)"
3. Give it a descriptive name (e.g., "Android Package Access")
4. Select the `read:packages` scope (or `write:packages` if you need to publish)
5. Click "Generate token"
6. Copy the token immediately (you won't be able to see it again)

---
