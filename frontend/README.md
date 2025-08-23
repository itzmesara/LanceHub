# Freelancer Marketplace Frontend

A modern, responsive React.js frontend for the Freelancer Marketplace platform built according to the Software Requirements Specification (SRS).

## Features Implemented

### ✅ Authentication & User Management
- User registration with role selection (Freelancer/Client)
- Secure login with JWT token management
- Protected routes based on authentication status
- Automatic token refresh and user session management

### ✅ Profile Management
- Freelancer profile creation and editing
- Client profile management
- Skills and portfolio management
- Profile photo upload capability

### ✅ Project Management
- Project creation for clients
- Project listing and filtering
- Project detail view with full information
- Project status tracking (Open → Assigned → In Progress → Completed/Canceled)
- Project applications and assignment system

### ✅ Search & Discovery
- Freelancer search with filters (skills, hourly rate, location)
- Project search for freelancers
- Paginated results
- Real-time filtering capabilities

### ✅ Communication System
- In-app messaging between clients and freelancers
- Conversation-based messaging interface
- Real-time message updates
- Project-specific chat rooms

### ✅ Admin Dashboard
- User management (view, deactivate users)
- Project management (view, archive projects)
- System statistics and analytics
- Audit logging capabilities

### ✅ Responsive Design
- Mobile-first responsive design
- Tailwind CSS for consistent styling
- Cross-browser compatibility
- WCAG 2.1 AA accessibility compliance

## Technology Stack

- **React.js 18+** - Frontend framework
- **React Router v6** - Client-side routing
- **Axios** - HTTP client for API calls
- **Tailwind CSS** - Utility-first CSS framework
- **Context API** - State management
- **JWT** - Token-based authentication

## Project Structure

```
frontend/
├── public/
├── src/
│   ├── components/
│   │   ├── auth/
│   │   │   └── ProtectedRoute.js
│   │   └── layout/
│   │       └── Navbar.js
│   ├── context/
│   │   └── AuthContext.js
│   ├── pages/
│   │   ├── admin/
│   │   │   └── AdminDashboard.js
│   │   ├── auth/
│   │   │   ├── Login.js
│   │   │   └── Register.js
│   │   ├── freelancers/
│   │   │   └── Freelancers.js
│   │   ├── profiles/
│   │   │   ├── ClientProfile.js
│   │   │   └── FreelancerProfile.js
│   │   ├── projects/
│   │   │   ├── CreateProject.js
│   │   │   ├── ProjectDetail.js
│   │   │   └── Projects.js
│   │   ├── Dashboard.js
│   │   ├── Home.js
│   │   └── Messages.js
│   ├── App.js
│   ├── index.css
│   └── index.js
├── package.json
└── tailwind.config.js
```

## Getting Started

### Prerequisites
- Node.js 16+ and npm
- Backend API running on http://localhost:8080

### Installation

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

The application will open at [http://localhost:3000](http://localhost:3000).

## API Integration

The frontend integrates with the Spring Boot backend through RESTful APIs:

- **Authentication**: `/api/auth/**`
- **Users**: `/api/users/**`
- **Projects**: `/api/projects/**`
- **Applications**: `/api/applications/**`
- **Messages**: `/api/messages/**`
- **Profiles**: `/api/profiles/**`
- **Admin**: `/api/admin/**`

## Environment Configuration

The frontend is configured to work with the backend running on `http://localhost:8080`. Update the base URL in the AuthContext and individual components if needed.

## Features Breakdown

### 1. Home Page (`/`)
- Landing page with feature highlights
- Call-to-action buttons for registration/login
- Responsive hero section

### 2. Authentication
- **Login** (`/login`): Secure login form with validation
- **Register** (`/register`): Role-based registration (Freelancer/Client)

### 3. Dashboard (`/dashboard`)
- Personalized dashboard based on user role
- Quick access to relevant features
- Recent activity overview

### 4. Projects
- **Browse Projects** (`/projects`): List all available projects
- **Project Details** (`/projects/:id`): Detailed project view with application/assignment features
- **Create Project** (`/projects/create`): Multi-step project creation form for clients

### 5. Freelancers
- **Browse Freelancers** (`/freelancers`): Search and filter freelancers
- **Freelancer Profile** (`/freelancers/:id`): Detailed freelancer profile view

### 6. Messaging (`/messages`)
- Real-time messaging interface
- Conversation-based layout
- Project-specific chat rooms

### 7. Admin Dashboard (`/admin`)
- User management interface
- Project oversight capabilities
- System analytics and reporting

## Security Features

- JWT token-based authentication
- Protected routes for authenticated users
- Role-based access control
- Secure API communication
- Input validation and sanitization

## Responsive Design

- Mobile-first approach
- Responsive navigation
- Touch-friendly interfaces
- Optimized for all screen sizes

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Development Commands

```bash
# Start development server
npm start

# Build for production
npm run build

# Run tests
npm test

# Lint code
npm run lint
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is part of the Freelancer Marketplace system and follows the specifications outlined in the SRS document.
