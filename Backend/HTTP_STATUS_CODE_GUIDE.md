# HTTP Status Code Best Practices for LanceHub

## Problem Summary
The application was incorrectly returning 403 Forbidden instead of 404 Not Found when a client profile didn't exist for an authenticated user with CLIENT role.

## Root Cause
1. **ResourceNotFoundException** was annotated with `@ResponseStatus(HttpStatus.NOT_FOUND)` which can conflict with Spring Security's exception handling
2. Spring Security was intercepting the exception before it reached the GlobalExceptionHandler
3. Frontend was treating all errors as 403 without proper differentiation

## Solutions Implemented

### Backend Changes

#### 1. Exception Handling (`/exception/` package)
- **ResourceNotFoundException.java**: Removed `@ResponseStatus` annotation to prevent conflicts
- **GlobalExceptionHandler.java**: Enhanced with proper exception handling:
  - `@ExceptionHandler(ResourceNotFoundException.class)` → Returns 404 with proper JSON response
  - `@ExceptionHandler(AccessDeniedException.class)` → Returns 403 for real permission issues
  - `@ExceptionHandler(BadCredentialsException.class)` → Returns 401 for authentication failures
  - `@ExceptionHandler(Exception.class)` → Returns 500 for unexpected errors

#### 2. ClientProfileController Updates
- **Proper status codes**: 
  - 404 when profile doesn't exist (instead of 403)
  - 403 only for real permission violations
- **Enhanced security**: Added method-level security with `@PreAuthorize` annotations
- **Ownership validation**: Added `@clientProfileService.isProfileOwner()` for resource ownership checks

#### 3. ClientProfileService Updates
- **New methods**:
  - `createClientProfileForUser()`: Ensures profile is created for the correct user
  - `updateClientProfile()`: Proper field validation and updates
  - `isProfileOwner()`: Security method for ownership validation

### Frontend Changes

#### 1. API Service (`/utils/api.js`)
- **Enhanced error handling**: Differentiates between 403 (real access denied) and 404 (resource not found)
- **Smart error suppression**: Doesn't show alerts for profile-related 403 errors
- **Proper cleanup**: Removes both token and user data on 401 errors

#### 2. ClientProfile Component
- **404 Handling**: Shows "Create Profile" form when profile doesn't exist (404 response)
- **403 Handling**: Shows access denied message for real permission issues
- **Enhanced UI**: Complete form with all ClientProfile fields (companyName, contactName, bio, profilePictureUri)
- **State management**: Proper loading, editing, and creating states

## HTTP Status Code Best Practices

### 200 OK
- Successful GET, PUT, PATCH, DELETE operations
- Resource exists and user has permission

### 201 Created
- Successful POST operations creating new resources

### 400 Bad Request
- Invalid request body or parameters
- Validation errors

### 401 Unauthorized
- Missing or invalid authentication token
- Token expired

### 403 Forbidden
- **Authenticated user lacks permission** for specific action
- User has valid credentials but not the required role/privileges
- **DO NOT USE for missing resources**

### 404 Not Found
- **Resource doesn't exist** (e.g., profile not found for user)
- Invalid endpoint URL
- **Preferred over 403 for missing resources that user should have access to**

### 500 Internal Server Error
- Unexpected server errors
- Database connection issues

## Key Principles

1. **404 for Absence, 403 for Permission**: 
   - Use 404 when a resource doesn't exist but the user should have access if it did exist
   - Use 403 when the user fundamentally lacks permission to access the resource type

2. **Security Through Obscurity**: 
   - Don't reveal whether a resource exists if the user shouldn't know about it
   - Return 404 instead of 403 for resources that exist but user can't access

3. **Consistent Error Responses**: 
   - All errors return JSON with consistent structure: `{timestamp, message, status, error, details?}`

## Testing Scenarios

### Scenario 1: Client without profile
- **Before**: 403 Forbidden (incorrect)
- **After**: 404 Not Found (correct) → Shows create profile form

### Scenario 2: Non-client accessing client endpoint
- **Before**: Mixed behavior
- **After**: 403 Forbidden (correct) → Shows access denied

### Scenario 3: Client accessing other client's profile
- **Before**: Possibly 403 or incorrect data
- **After**: 403 Forbidden (correct) → Ownership validation

## Files Modified

### Backend
- `exception/ResourceNotFoundException.java`
- `exception/GlobalExceptionHandler.java`
- `controller/ClientProfileController.java`
- `service/ClientProfileService.java`

### Frontend
- `utils/api.js`
- `pages/profiles/ClientProfile.js`

This implementation ensures proper HTTP status codes, better user experience, and maintains security best practices throughout the application.
