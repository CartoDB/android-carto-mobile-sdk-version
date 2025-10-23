# Maven Central Portal Credentials Setup (2025)

## Important: New Central Publishing Portal

The old OSSRH system (oss.sonatype.org / s01.oss.sonatype.org) reached end-of-life on June 30, 2025.

You now need to use the **new Central Publishing Portal**: https://central.sonatype.com

## Step 1: Create Account on New Central Portal

1. Go to https://central.sonatype.com
2. **Sign up** with a new account OR **migrate** your existing OSSRH account
3. Verify your namespace (GitHub organization or custom domain)

## Step 2: Generate User Token

1. Log into https://central.sonatype.com
2. Click your username (top right) → **Account**
3. Click **Generate User Token**
4. Copy the generated username and password

## Step 3: Update gradle.properties

The vanniktech plugin reads credentials from `~/.gradle/gradle.properties` OR project-level `gradle.properties`.

Add to `~/.gradle/gradle.properties`:

```properties
# Maven Central Portal credentials (from https://central.sonatype.com)
mavenCentralUsername=YOUR_GENERATED_USERNAME
mavenCentralPassword=YOUR_GENERATED_PASSWORD

# GPG Signing
signing.keyId=C4AE6526
signing.password=
signing.secretKeyRingFile=/Users/joaquinpbarroso/.gnupg/secring.gpg
```

**OR** add to your local `mavencentral.properties`:

```properties
# Maven Central Portal credentials (from https://central.sonatype.com)
mavenCentralUsername=YOUR_GENERATED_USERNAME
mavenCentralPassword=YOUR_GENERATED_PASSWORD

# GPG Signing
signing.keyId=C4AE6526
signing.password=
signing.secretKeyRingFile=/Users/joaquinpbarroso/.gnupg/secring.gpg
```

## Step 4: Verify Namespace

Before publishing, you must verify ownership of the `com.carto` namespace:

### Option A: GitHub Organization Verification
1. In Central Portal, register namespace: `io.github.CartoDB`
2. Verify by creating a repo named like `OSSRH-12345` in CartoDB organization

### Option B: Custom Domain Verification
1. Register namespace: `com.carto`
2. Add TXT record to DNS: `v=spf1 include:_spf.sonatype.com ~all`
3. Wait for verification (usually within 24 hours)

## Key Differences from Old OSSRH

| Old OSSRH | New Central Portal |
|-----------|-------------------|
| JIRA ticket required | Self-service registration |
| Staging repositories | Direct publishing |
| Manual release process | Automatic after upload |
| oss.sonatype.org | central.sonatype.com |
| Username/password from JIRA | Generated user tokens |

## Publishing Commands with Vanniktech Plugin

```bash
# Publish to Maven Central
./gradlew publishToMavenCentral

# Or with manual credentials
./gradlew publishToMavenCentral \
  -PmavenCentralUsername=YOUR_USERNAME \
  -PmavenCentralPassword=YOUR_PASSWORD
```

## Troubleshooting

### "Namespace not found" error
- Register and verify your namespace at https://central.sonatype.com first

### "403 Forbidden" error
- Make sure you're using credentials from central.sonatype.com (NOT oss.sonatype.org)
- Regenerate user token if needed

### "Unauthorized" error
- Check that credentials are in the correct properties file
- Ensure properties file is not being ignored by .gitignore
