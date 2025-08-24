# Client Application Management Feature Guide

## Overview
This document outlines the implementation of client application management features in the LanceHub platform. Clients can now view, manage, and update the status of applications submitted to their projects.

## Backend Implementation

### 1. ApplicationStatus Enum
Created `ApplicationStatus.java` with three states:
- `PENDING`: Default status when an application is submitted
- `ACCEPTED`: When client accepts the application
- `REJECTED`: When client rejects the application

### 2. ApplicationResponse DTO
Created `ApplicationResponse.java` to provide a clean response format for client-facing endpoints, including:
- Application details
- Project information
- Freelancer information
- Status and timestamps

### 3. Service Layer Updates
**ApplicationService.java**:
- `getApplicationsForClientProjects()`: Retrieves paginated applications for client's projects
- `updateApplicationStatus()`: Updates application status with client ownership validation
- `convertToResponse()`: Converts Application entity to Response DTO

**ProjectService.java**:
- `isProjectOwner()`: Validates if a client owns a specific project

### 4. Repository Updates
**ApplicationRepo.java**:
- Added `findApplicationsForClientProjects()`: Custom query to find applications for client projects with pagination

### 5. Controller Endpoints
**ApplicationController.java**:
- `GET /api/applications/client`: Get applications for client's projects (paginated)
- `PUT /api/applications/{id}/status`: Update application status (ACCEPTED/REJECTED)

## Frontend Implementation

### 1. ClientApplications Component
Created `frontend/src/pages/client/ClientApplications.js` with:
- Paginated list of applications
- Status badges with color coding
- Accept/Reject buttons for pending applications
- Modal for detailed view
- Responsive design with Tailwind CSS

### 2. Route Configuration
Added route in `App.js`:
```javascript
<Route path="/client/applications" element={<ProtectedRoute><ClientApplications /></ProtectedRoute>} />
```

## API Endpoints

### 1. Get Client Applications
**Endpoint**: `GET /api/applications/client`
**Query Parameters**:
- `page` (default: 0)
- `size` (default: 10)
- `sortBy` (default: createdAt)
- `sortDir` (default: desc)

**Response**: Paginated list of ApplicationResponse objects

### 2. Update Application Status
**Endpoint**: `PUT /api/applications/{id}/status?status={status}`
**Path Variable**: `id` - Application ID
**Query Parameter**: `status` - ACCEPTED or REJECTED

**Response**: Updated ApplicationResponse object

## Security Features

1. **Role-based Access**: Only users with CLIENT role can access these endpoints
2. **Ownership Validation**: Clients can only update status for applications to their own projects
3. **Authentication Required**: All endpoints require valid JWT token

## Testing Instructions

### Backend Testing
1. Start the backend server:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. Test endpoints using curl or Postman:

**Get Client Applications**:
```bash
curl -X GET "http://localhost:8080/api/applications/client?page=0&size=10" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Update Application Status**:
```bash
curl -X PUT "http://localhost:8080/api/applications/1/status?status=ACCEPTED" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### Frontend Testing
1. Start the frontend:
   ```bash
   cd frontend
   npm start
   ```

2. Navigate to `/client/applications` when logged in as a client
3. Test the following features:
   - View applications list
   - Accept pending applications
   - Reject pending applications
   - View application details in modal
   - Pagination navigation

## Error Handling

### Backend Errors
- `403 Forbidden`: When non-client users try to access endpoints
- `404 Not Found`: When application doesn't exist
- `400 Bad Request`: When client doesn't own the project

### Frontend Errors
- Toast notifications for API errors
- Loading states during API calls
- Access control for non-client users

## Database Schema Impact

The implementation adds a `status` column to the `applications` table with the following values:
- PENDING (default)
- ACCEPTED
- REJECTED

## Dependencies

### Backend
- Spring Data JPA (for pagination)
- Spring Security (for role-based access)

### Frontend
- React Router (for routing)
- Axios (for API calls)
- React Toastify (for notifications)

## Future Enhancements

1. **Email Notifications**: Send emails to freelancers when application status changes
2. **Real-time Updates**: WebSocket integration for real-time status updates
3. **Advanced Filtering**: Filter applications by project, status, or date range
4. **Bulk Actions**: Accept/reject multiple applications at once
5. **Application History**: Track status changes over time

## Files Modified

### Backend
- `src/main/java/com/launcehub/Model/ApplicationStatus.java` (NEW)
- `src/main/java/com/launcehub/dto/ApplicationResponse.java` (NEW)
- `src/main/java/com/launcehub/service/ApplicationService.java` (UPDATED)
- `src/main/java/com/launcehub/service/ProjectService.java` (UPDATED)
- `src/main/java/com/launcehub/repository/ApplicationRepo.java` (UPDATED)
- `src/main/java/com/launcehub/controller/ApplicationController.java` (UPDATED)

### Frontend
- `src/pages/client/ClientApplications.js` (NEW)
- `src/App.js` (UPDATED)

## Migration Notes

If deploying to production, ensure to:
1. Run database migrations to add the `status` column to applications table
2. Update existing applications to have PENDING status
3. Test with existing data to ensure compatibility
