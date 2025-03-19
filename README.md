Stock Trading System

The Simple Stock Trading System is a backend project designed to simulate a stock trading platform. Users can buy and sell stocks, manage their portfolios, and track transaction history through a RESTful API. The system also includes email notifications to keep users informed of their trading activities.

Features

User Management:
    User registration and login
    Each user has a portfolio and a balance
    Email notifications for key actions

Stock Management:
    Simulated stock prices with periodic updates
    Randomized stock price fluctuations

Trading System:
    Users can buy stocks using their balance
    Users can sell stocks from their portfolio
    Thread-safe transactions using Java concurrency handling

Portfolio Management:
    View owned stocks and their current value
    View transaction history

RESTful API:
    Endpoints for user management, stock trading, and portfolio tracking


Tech Stack
    Backend: Spring Boot (Java 17)
    Database: PostgreSQL/MySQL/H2 (configurable)

Concurrency Handling: 
    Java synchronized or ReentrantLock

Deployment: 
    AWS

API Testing:
    Postman

Configure Database & Email Settings
    Update application.properties:
      spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
      spring.datasource.username=your_username
      spring.datasource.password=your_password
      # Email SMTP Configuration
      spring.mail.host=smtp.example.com
      spring.mail.port=587
      spring.mail.username=your_email@example.com
      spring.mail.password=your_secure_password

API Endpoints
    User Management
        Register: POST /api/user/signup
        Login: POST /api/user/login        
        Get Balance: GET /api/user/balance        
        Add Balance: POST /api/user/balance?amount=1000        
        Logout: GET /api/user/logout
    Stock Management
        Get All Stocks: GET /stocks
        Add Stock: POST /stocks
    Trading
        Buy Stock: POST /api/trade/buy?stockSymbol=TATA&quantity=2
        Sell Stock: POST /api/trade/sell?stockSymbol=TATA&quantity=1
    Portfolio Management
        View Portfolio: GET /api/portfolio
        View Transaction History: GET /api/transaction
