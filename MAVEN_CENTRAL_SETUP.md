# Maven Central Publishing Setup Guide

This guide explains how to set up your credentials for publishing to Maven Central (Sonatype OSSRH).

## Prerequisites

You should have already completed:
- ✅ Created a Sonatype JIRA account at https://issues.sonatype.org/
- ✅ Created an OSSRH ticket to claim the `com.carto` groupId
- ✅ Verified domain ownership and received approval
- ✅ Generated GPG keys for signing

## Step 1: Set Up GPG Keys

### Generate GPG Key (if not already done)

```bash
# Install GPG (macOS)
brew install gnupg

# Generate a new key
gpg --gen-key
# Follow the prompts (use your email associated with the project)

# List your keys to get the key ID
gpg --list-keys
# Output will show something like:
# pub   rsa3072 2024-01-01 [SC]
#       1234567890ABCDEF1234567890ABCDEF12345678
# The last 8 characters (12345678) are your keyId

# Export your secret key (needed for signing.secretKeyRingFile)
gpg --keyring secring.gpg --export-secret-keys > ~/.gnupg/secring.gpg
```

### Publish Your Public Key

Your public key must be published to key servers so Maven Central can verify signatures:

```bash
# Get your key ID
gpg --list-keys

# Publish to key servers (replace KEY_ID with your actual key ID)
gpg --keyserver keyserver.ubuntu.com --send-keys KEY_ID
gpg --keyserver keys.openpgp.org --send-keys KEY_ID
gpg --keyserver pgp.mit.edu --send-keys KEY_ID
```

## Step 2: Create Local Credentials File

1. Copy the template file:
   ```bash
   cp mavencentral.properties.template mavencentral.properties
   ```

2. Edit `mavencentral.properties` with your actual credentials:

### Option A: File-Based GPG Signing (Recommended for Local Development)

```properties
# Sonatype JIRA credentials
ossrhUsername=your_sonatype_username
ossrhPassword=your_sonatype_password_or_token

# GPG Signing - File-based approach
signing.keyId=12345678
signing.password=your_gpg_key_password
signing.secretKeyRingFile=/Users/yourusername/.gnupg/secring.gpg
```

**How to find your values:**
- `ossrhUsername`: Your Sonatype JIRA username
- `ossrhPassword`: Your Sonatype JIRA password (or generate a token at https://s01.oss.sonatype.org/)
- `signing.keyId`: Last 8 characters from `gpg --list-keys`
- `signing.password`: The password you set when creating the GPG key
- `signing.secretKeyRingFile`: Path to your exported secret key ring

### Option B: In-Memory GPG Signing (Recommended for CI/CD)

```properties
# Sonatype JIRA credentials
ossrhUsername=your_sonatype_username
ossrhPassword=your_sonatype_password_or_token

# GPG Signing - In-memory approach
signingKey=-----BEGIN PGP PRIVATE KEY BLOCK-----\n...\n-----END PGP PRIVATE KEY BLOCK-----
signingPassword=your_gpg_key_password
```

**To export your key in ASCII format:**
```bash
gpg --export-secret-keys --armor YOUR_KEY_ID
# Copy the entire output (including BEGIN and END lines)
# Replace newlines with \n in the properties file
```

## Step 3: Verify Credentials File is Ignored

The `mavencentral.properties` file is already added to `.gitignore` and should **NEVER** be committed to version control.

Verify it's ignored:
```bash
git status
# mavencentral.properties should NOT appear in the list
```

## Step 4: Test the Configuration

### Build All Artifacts

```bash
./gradlew clean
./gradlew assembleRelease
./gradlew androidSourcesJar androidJavadocsJar
```

### Test Local Publishing

Publish to your local Maven repository to verify everything works:

```bash
./gradlew publishToMavenLocal
```

Check the output in `~/.m2/repository/com/carto/carto-mobile-sdk/5.0.0/`:
- ✅ `carto-mobile-sdk-5.0.0.aar`
- ✅ `carto-mobile-sdk-5.0.0-sources.jar`
- ✅ `carto-mobile-sdk-5.0.0-javadoc.jar`
- ✅ `carto-mobile-sdk-5.0.0.pom`
- ✅ All `.asc` signature files

## Step 5: Publish to Maven Central

### First Time Publishing

```bash
# Publish to Sonatype staging repository
./gradlew publishToSonatype

# Close and release the staging repository (automatic)
./gradlew closeAndReleaseSonatypeStagingRepository
```

### Manual Release (Alternative)

If you prefer manual control:

1. Publish to staging:
   ```bash
   ./gradlew publishToSonatype
   ```

2. Log in to Sonatype Nexus: https://s01.oss.sonatype.org/

3. Navigate to "Staging Repositories"

4. Find your repository (should be named like `comcarto-XXXX`)

5. Select it and click "Close"
   - Wait for validation to complete
   - Fix any errors if they occur

6. Once closed successfully, click "Release"
   - Artifacts will sync to Maven Central in 15-30 minutes
   - Full propagation to all mirrors takes 2-4 hours

## Step 6: Verify Publication

After 15-30 minutes, check:

1. **Maven Central Repository Browser:**
   https://repo1.maven.org/maven2/com/carto/carto-mobile-sdk/

2. **Maven Central Search:**
   https://search.maven.org/artifact/com.carto/carto-mobile-sdk

3. **Test in a project:**
   ```gradle
   dependencies {
       implementation 'com.carto:carto-mobile-sdk:5.0.0'
   }
   ```

## CI/CD Configuration (GitHub Actions)

For automated publishing, add these secrets to your GitHub repository:

- `OSSRH_USERNAME`: Your Sonatype username
- `OSSRH_PASSWORD`: Your Sonatype password/token
- `SIGNING_KEY`: Your GPG private key in ASCII format (base64 encoded)
- `SIGNING_PASSWORD`: Your GPG key password

Example workflow snippet:
```yaml
- name: Publish to Maven Central
  env:
    OSSRH_USERNAME: ${{ secrets.OSSRH_USERNAME }}
    OSSRH_PASSWORD: ${{ secrets.OSSRH_PASSWORD }}
    SIGNING_KEY: ${{ secrets.SIGNING_KEY }}
    SIGNING_PASSWORD: ${{ secrets.SIGNING_PASSWORD }}
  run: |
    echo "$SIGNING_KEY" | base64 -d > secring.gpg
    ./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository
```

## Troubleshooting

### "No signing key found"
- Verify your GPG key is properly configured
- Check that `secring.gpg` exists at the specified path
- Ensure `signing.keyId` matches your actual key ID

### "401 Unauthorized" from Sonatype
- Verify your Sonatype credentials are correct
- Try generating a new user token at https://s01.oss.sonatype.org/

### "Invalid signature" errors
- Ensure your public key is published to key servers
- Wait a few minutes for key servers to sync
- Verify `signing.password` is correct

### Staging repository validation fails
- Check POM metadata is complete (license, developers, SCM)
- Verify all artifacts have signatures (.asc files)
- Ensure Javadoc and sources JARs are present

## Support

- **Sonatype OSSRH Guide:** https://central.sonatype.org/publish/publish-guide/
- **Maven Central Issues:** Create a ticket at https://issues.sonatype.org/
- **GPG Documentation:** https://www.gnupg.org/documentation/
