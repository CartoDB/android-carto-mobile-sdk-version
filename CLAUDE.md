# Quick Reference: Publishing to Maven Central

This guide contains minimal info needed to publish new versions of Carto Mobile SDK to Maven Central.

## Prerequisites (Already Configured ✅)

- ✅ Maven Central Portal account: https://central.sonatype.com
- ✅ Namespace verified: `com.carto`
- ✅ GPG keys generated and published
- ✅ Credentials stored in `~/.gradle/gradle.properties` and `mavencentral.properties`
- ✅ Gradle configured with vanniktech maven publish plugin

## Publishing a New Version - Quick Steps

### 1. Update Version Number

Edit `carto_mobile_sdk/build.gradle` line 52:

```gradle
mavenPublishing {
    coordinates("com.carto", "carto-mobile-sdk", "5.0.1")  // <- Change version here
    // ...
}
```

### 2. Build Release

```bash
./gradlew clean assembleRelease
```

### 3. Publish to Maven Central

```bash
./gradlew publishAndReleaseToMavenCentral --no-configuration-cache
```

### 4. Monitor Deployment

1. Go to https://central.sonatype.com/publishing/deployments
2. Wait for status to change to **PUBLISHED** (10-30 minutes)
3. Check Maven Central: https://repo1.maven.org/maven2/com/carto/carto-mobile-sdk/

### 5. Verify Publication

After 15-30 minutes, verify at:

- Repository: https://repo1.maven.org/maven2/com/carto/carto-mobile-sdk/[VERSION]/
- Search: https://search.maven.org/artifact/com.carto/carto-mobile-sdk

---

## Credentials Location

Credentials are stored in (gitignored, never commit):

- `~/.gradle/gradle.properties`
- `./mavencentral.properties`

**Required properties:**

```properties
mavenCentralUsername=TOKEN_ID
mavenCentralPassword=TOKEN

```

---

## Current Configuration

**Group ID:** `com.carto`
**Artifact ID:** `carto-mobile-sdk`
**Current Version:** `5.X.X`
**Publishing System:** Maven Central Portal (2025)
**Plugin:** vanniktech/gradle-maven-publish-plugin v0.30.0

---

## Artifacts Published

Each release includes:

- AAR file (main library with native .so files)
- Sources JAR
- JavaDoc JAR (generated with Dokka)
- POM with metadata
- All artifacts GPG signed

---

## Troubleshooting

### Build fails

```bash
./gradlew clean
./gradlew assembleRelease
```

### Publishing fails with 403 Forbidden

- Check credentials in `~/.gradle/gradle.properties`
- Regenerate token at https://central.sonatype.com/account

### Deployment stuck at VALIDATING

- Check https://central.sonatype.com/publishing/deployments
- Click deployment for error details
- Common issues: missing signatures, incomplete POM

### GPG signing fails

```bash
# Verify GPG key exists
gpg --list-keys

# Should show key C4AE6526
# If not, GPG keys need to be reimported
```

---

## Important Files

- `carto_mobile_sdk/build.gradle` - Main publishing configuration
- `build.gradle` - Root project configuration
- `mavencentral.properties` - Credentials (gitignored)
- `~/.gradle/gradle.properties` - System-wide credentials
- `~/.gnupg/secring.gpg` - GPG secret key

---

## Users' Installation

Once published, users add to their `build.gradle`:

```gradle
dependencies {
    implementation 'com.carto:carto-mobile-sdk:5.X.X'
}
```

---

## Additional Documentation

For detailed information, see:

- `CENTRAL_PORTAL_CREDENTIALS.md` - Portal-specific instructions
- `mavencentral.properties.template` - Credentials template

---

## Notes

- The old OSSRH system (oss.sonatype.org) reached EOL June 2025
- Now using Central Portal (central.sonatype.com)
- Publishing is automated - no manual staging/release needed
- Sync to Maven Central takes 10-30 minutes after publishing
- Search indexing may take up to 2-4 hours

---

**Last Published:** 2025-10-23
**Version:** 5.0.0
**Status:** ✅ Successfully published to Maven Central
