# 📡 Telecom Multi-Asset Distribution System

A comprehensive telecom investment platform that allows users to manage digital wallets and invest in tower infrastructure shares.



## 🛠️ Database Schema
The system runs on a MySQL backend named `telecom_investment`. The schema is designed for data integrity with relational mapping between users and their asset holdings.

### Core Tables:
* **`users`**: Stores profile information,encrypted passwords, and wallet balances.
* **`towers`**: Manages telecom tower inventory and available shares.
* **`user_shares`**: A junction table tracking which user owns what percentage of a specific tower.

## 🚀 Getting Started

### 1. Database Setup
Ensure you have MySQL installed. Run the following script to initialize the environment:

```sql
DROP DATABASE IF EXISTS telecom_investment;
CREATE DATABASE telecom_investment;
USE telecom_investment;

-- Create user profiles
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'ROLE_USER',
    wallet_balance DECIMAL(19, 2) DEFAULT 0.00
);

-- Initialize tower assets
CREATE TABLE towers (
    tower_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tower_name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    total_shares INT DEFAULT 100,
    status VARCHAR(50) DEFAULT 'ACTIVE'
);

-- Track investments
CREATE TABLE user_shares (
    share_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tower_id BIGINT NOT NULL,
    share_percentage DOUBLE NOT NULL,
    purchase_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (tower_id) REFERENCES towers(tower_id)
);
```
2. Backend Configuration
Update your src/main/resources/application.properties to connect to the schema:

```
 spring.datasource.url=jdbc:mysql://localhost:3306/telecom_investment
 spring.datasource.username=YOUR_MYSQL_USERNAME
 spring.datasource.password=YOUR_MYSQL_PASSWORD
 spring.jpa.hibernate.ddl-auto=update
 ```

🎨 UI Features
* Theme: Modern Deep Sea Blue glassmorphism.

* Security: Integrated with Spring Security to authenticate against the users table.

* Dynamic Toasts: Real-time feedback for login errors or successful transactions.

📂 Folder Structure

```
Telecom_MultiAsset_Distribution_System/
├── .mvn/                       # Maven Wrapper files
├── Capstone/                   # Root project folder
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/           # Java Source Code (Spring Boot)
│   │   │   │   └── com/telecom/
│   │   │   │       ├── controller/
│   │   │   │       ├── model/
│   │   │   │       └── service/
│   │   │   └── resources/
│   │   │       ├── static/     # CSS, JS, and Images
│   │   │       └── templates/  # Thymeleaf HTML (Login/Signup)
│   ├── target/                 # Compiled bytecode (ignored by git)
│   └── pom.xml                 # Maven project dependencies
├── .gitignore                  # Files to exclude from Git
├── HELP.md                     # Spring Boot help guide
└── README.md                   # Project documentation
```


