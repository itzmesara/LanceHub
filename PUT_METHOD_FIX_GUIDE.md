# PUT Method Fix Guide for Client Profiles

## Problem Summary
The PUT request to `/api/client-profiles` was returning "405 Method Not Allowed" because:
1. The existing PUT mapping expected `/api/client-profiles/{id}` (with an ID parameter)
2. No PUT method was mapped to handle requests to `/api/client-profiles` (without ID)

## Solution Implemented

### 1. Added New PUT Method in ClientProfileController
- **Endpoint**: `PUT /api/client-profiles` (without ID)
- **Method**: `updateMyClientProfile()`
- **Security**: `@PreAuthorize("hasRole('CLIENT')")`
- **Functionality**: Updates the authenticated user's own client profile using their email

### 2. Added Service Method in ClientProfileService
- **Method**: `updateClientProfileByEmail(String email, ClientProfile clientProfileDetails)`
- **Functionality**: Finds profile by user email and updates the fields

### 3. Added Debugging Endpoint
- **Endpoint**: `GET /api/test/mappings`
- **Functionality**: Returns all registered Spring MVC mappings for debugging

## How to Use

### Update Client Profile (Without ID)
```bash
PUT /api/client-profiles
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "companyName": "Updated Company",
  "contactName": "Updated Contact",
  "bio": "Updated bio",
  "profilePictureUri": "https://example.com/updated.jpg"
}
```

### Update Client Profile (With ID)
```bash
PUT /api/client-profiles/123
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "companyName": "Updated Company",
  "contactName": "Updated Contact",
  "bio": "Updated bio",
  "profilePictureUri": "https://example.com/updated.jpg"
}
```

## Debugging Tools

### View All Registered Mappings
```bash
GET /api/test/mappings
```

This will show all Spring MVC endpoints and their corresponding handler methods, helping to verify that the PUT methods are properly registered.

## Common Solutions for HttpRequestMethodNotSupportedException

1. **Check Endpoint URL**: Ensure you're using the correct URL format
   - With ID: `/api/client-profiles/{id}`
   - Without ID: `/api/client-profiles`

2. **Verify Method Signature**: Ensure the controller method has correct annotations
   - `@PutMapping("/{id}")` for ID-based updates
   - `@PutMapping` for base endpoint updates

3. **Check Security Configuration**: Ensure the user has the required role (`CLIENT`)

4. **Review Request Body**: Ensure the JSON structure matches the `ClientProfile` model

## ClientProfile Model Structure
```java
public class ClientProfile {
    long id;
    Users user;
    String companyName;    // Required
    String contactName;    // Required
    String bio;            // Optional
    String profilePictureUri; // Optional
}
```

## Testing the Fix

1. Start the Spring Boot application
2. Authenticate as a client user
3. Test both PUT endpoints:
   - `PUT /api/client-profiles` (updates authenticated user's profile)
   - `PUT /api/client-profiles/{id}` (updates specific profile by ID, requires ownership)

4. Use the debugging endpoint to verify mappings:
   - `GET /api/test/mappings` should show both PUT methods registered

## Files Modified
- `ClientProfileController.java` - Added `updateMyClientProfile()` method
- `ClientProfileService.java` - Added `updateClientProfileByEmail()` method  
- `TestController.java` - Added `/api/test/mappings` endpoint for debugging
