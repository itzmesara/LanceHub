# ApplicationStatus ClassNotFoundException - Fix Guide

## Problem
The Spring Boot application fails to start with a `ClassNotFoundException` for `ApplicationStatus` during Hibernate entity scanning.

## Root Cause
The `ApplicationStatus` enum class exists in the correct package (`com.launcehub.Model`) but is not being compiled properly, likely due to:
1. Missing from the classpath during compilation
2. Not being included in the Maven build process
3. Potential compilation errors preventing the class from being built

## Solution Steps

### 1. Verify ApplicationStatus.java Location and Content
The file should be located at: `backend/src/main/java/com/launcehub/Model/ApplicationStatus.java`

**Current Content:**
```java
package com.launcehub.Model;

public enum ApplicationStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}
```

### 2. Clean and Rebuild the Project
Run the following Maven commands to ensure proper compilation:

```bash
cd backend
mvn clean compile
```

### 3. Check Target Directory
After compilation, verify that `ApplicationStatus.class` exists in:
`backend/target/classes/com/launcehub/Model/ApplicationStatus.class`

### 4. Verify Application.java Usage
The `Application.java` entity uses `ApplicationStatus` correctly:

```java
@Enumerated(EnumType.STRING)
private ApplicationStatus status = ApplicationStatus.PENDING;
```

Since both classes are in the same package (`com.launcehub.Model`), no import is needed.

### 5. Common Reasons for ClassNotFoundException
- **Missing compilation**: The class wasn't compiled during the build process
- **Package mismatch**: Class is in wrong package or package declaration is incorrect
- **Build tool issues**: Maven/Gradle configuration problems
- **IDE caching**: IDE needs to refresh and rebuild the project

### 6. Troubleshooting Steps

#### Step 1: Force Maven Clean and Compile
```bash
cd backend
mvn clean compile -U
```

#### Step 2: Check for Compilation Errors
Look for any compilation errors in the terminal output that might prevent `ApplicationStatus` from compiling.

#### Step 3: Verify Maven Configuration
Check `pom.xml` for proper Java version and compilation settings.

#### Step 4: IDE-Specific Steps
- **IntelliJ**: File > Invalidate Caches / Restart
- **Eclipse**: Project > Clean
- **VS Code**: Restart the IDE and rebuild the project

#### Step 5: Manual Compilation Check
Try compiling just the ApplicationStatus class:
```bash
cd backend/src/main/java
javac com/launcehub/Model/ApplicationStatus.java
```

### 7. Expected Result
After successful compilation, the following files should exist:
- `backend/target/classes/com/launcehub/Model/ApplicationStatus.class`
- `backend/target/classes/com/launcehub/Model/Application.class` (which references ApplicationStatus)

### 8. Frontend Fix Applied
Also fixed the frontend import issue in `ClientApplications.js`:
- Changed `import { api } from '../../utils/api';` to `import api from '../../utils/api';`
- This resolves the webpack compilation error

## Prevention
1. Always run `mvn clean compile` after adding new classes
2. Verify compilation in the target directory
3. Use proper import statements (though not needed for same-package classes)
4. Regularly clean and rebuild the project to avoid caching issues

## Files Affected
- `backend/src/main/java/com/launcehub/Model/ApplicationStatus.java` (exists, needs compilation)
- `backend/src/main/java/com/launcehub/Model/Application.java` (uses ApplicationStatus)
- `frontend/src/pages/client/ClientApplications.js` (fixed import)

## Next Steps
1. Run `mvn clean compile` in the backend directory
2. Verify that `ApplicationStatus.class` appears in the target directory
3. Start the Spring Boot application to confirm the ClassNotFoundException is resolved
4. Test the client applications functionality
