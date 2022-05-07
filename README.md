# restaurant-booking

# URLs

- **Netflix Eureka naming server**: http://localhost:8761
- **API Gateway**: http://localhost:8765
- **User service (gateway)**:
- User login: http://localhost:8765/api/v1/auth/login
- User registration: http://localhost:8765/api/v1/auth/register
- Get all users and update user: http://localhost:8765/api/v1/users
- Get user by email: http://localhost:8765/api/v1/users/{email}
- Update user's password: http://localhost:8765/api/v1/users/change-password
- Delete user: http://localhost:8765/api/v1/users/delete/{email}
- **Restaurant service (gateway)**:
  - Restaurant registration: http://localhost:8765/restaurants/register

- **User service**:
  - User login: http://localhost:8000/api/v1/auth/login
  - User registration: http://localhost:8000/api/v1/auth/register
  - Get all users and update user: http://localhost:8000/api/v1/users
  - Get user by email: http://localhost:8000/api/v1/users/{email}
  - Update user's password: http://localhost:8000/api/v1/users/change-password
  - Delete user: http://localhost:8000/api/v1/users/delete/{email}
- **Restaurant service**:
  - Restaurant registration: http://localhost:8100/restaurants/register