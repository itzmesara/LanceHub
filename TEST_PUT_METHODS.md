# Testing the PUT Methods for Client Profiles

## Test Scenarios

### 1. Test PUT /api/client-profiles (Without ID)
**Purpose**: Update the authenticated user's own profile
**Expected**: Should return 200 OK with updated profile

```bash
# Request
PUT http://localhost:8080/api/client-profiles
Content-Type: application/json
Authorization: Bearer <your-jwt-token>

{
  "companyName": "Updated Company Name",
  "contactName": "Updated Contact Person",
  "bio": "This is my updated bio information",
  "profilePictureUri": "https://example.com/updated-profile.jpg"
}

# Expected Response (200 OK)
{
  "id": 123,
  "user": { ... },
  "companyName": "Updated Company Name",
  "contactName": "Updated Contact Person", 
  "bio": "This is my updated bio information",
  "profilePictureUri": "https://example.com/updated-profile.jpg"
}
```

### 2. Test PUT /api/client-profiles/{id} (With ID)
**Purpose**: Update a specific profile by ID (requires ownership)
**Expected**: Should return 200 OK with updated profile

```bash
# Request  
PUT http://localhost:8080/api/client-profiles/123
Content-Type: application/json
Authorization: Bearer <your-jwt-token>

{
  "companyName": "Specific Update Company",
  "contactName": "Specific Contact",
  "bio": "Specific bio update"
}

# Expected Response (200 OK)
{
  "id": 123,
  "user": { ... },
  "companyName": "Specific Update Company",
  "contactName": "Specific Contact",
  "bio": "Specific bio update",
  "profilePictureUri": "https://example.com/updated-profile.jpg"
}
```

### 3. Test Debugging Endpoint
**Purpose**: View all registered Spring MVC mappings
**Expected**: Should return all endpoints including the new PUT methods

```bash
# Request
GET http://localhost:8080/api/test/mappings

# Expected Response (200 OK)
{
  "{PUT [/api/client-profiles]}": HandlerMethod details...,
  "{PUT [/api/client-profiles/{id}]}": HandlerMethod details...,
  ... other mappings
}
```

## Error Scenarios to Test

### 1. Unauthorized Access (No JWT Token)
```bash
PUT http://localhost:8080/api/client-profiles
Content-Type: application/json

{
  "companyName": "Test Company"
}

# Expected: 401 Unauthorized
```

### 2. Invalid Role (Non-CLIENT User)
```bash
# If user doesn't have CLIENT role
PUT http://localhost:8080/api/client-profiles
Content-Type: application/json  
Authorization: Bearer <non-client-jwt-token>

{
  "companyName": "Test Company"
}

# Expected: 403 Forbidden
```

### 3. Missing Required Fields
```bash
PUT http://localhost:8080/api/client-profiles
Content-Type: application/json
Authorization: Bearer <your-jwt-token>

{
  "bio": "Only bio provided"
}

# Expected: 400 Bad Request (validation errors)
```

## Verification Steps

1. **Start the application**: `mvn spring-boot:run`
2. **Authenticate**: Get a JWT token for a CLIENT user
3. **Test PUT endpoints**: Use the above test cases
4. **Verify mappings**: Check `/api/test/mappings` to confirm both PUT methods are registered
5. **Test error cases**: Verify proper error responses

## Expected Results After Fix

- ✅ PUT `/api/client-profiles` should return 200 OK (was previously 405)
- ✅ PUT `/api/client-profiles/{id}` should continue to work
- ✅ Both endpoints should enforce proper authentication and authorization
- ✅ Debug endpoint should show both PUT mappings registered
