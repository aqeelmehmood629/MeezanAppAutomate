# Meezan Bank Mobile App Automation

This project is an automated testing framework for the Meezan Bank mobile application using Appium, Java, Cucumber, and TestNG. It follows a Page Object Model (POM) design pattern and uses Cucumber for BDD-style testing.

## 🚀 Technologies Used
- **Language**: Java 17
- **Automation Tool**: Appium 2.x
- **Testing Framework**: TestNG, Cucumber (BDD)
- **Reporting**: Extent Reports, Cucumber HTML Reports
- **Build Tool**: Maven
- **Other Utilities**: OpenCSV for data-driven testing

## 📂 Project Structure
- `src/test/java/pages`: Contains Page Object classes.
- `src/test/java/steps`: Contains step definition classes for Cucumber scenarios.
- `src/test/java/driver`: Driver factory and initialization logic.
- `src/test/java/utils`: Helper classes and utilities (e.g., CSV reading, Hybrid App stabilization).
- `src/test/resources/features`: BDD feature files.
- `testng.xml`: TestNG suite configuration.
- `pom.xml`: Project dependencies and plugins.

## 🛠️ Prerequisites
- Java JDK 17
- Appium Server installed and running
- Android SDK & Emulator/Real Device
- Maven installed

## 🔧 Setup & Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/aqeelmehmood629/MeezanAppAutomate.git
   ```
2. Navigate to the project directory.
3. Update `DriverFactory.java` with your local device details and ChromeDriver path.
4. Install dependencies:
   ```bash
   mvn clean install
   ```

## 🧪 Running Tests
### Run via Maven:
```bash
mvn test
```

### Run via TestNG:
Right-click on `testng.xml` and select **Run As -> TestNG Suite**.

### Run Specific Features:
Use the `MainTestRunner.java` class to run specific scenarios or features.

## 📊 Reporting
After test execution, reports can be found in the following locations:
- **Cucumber HTML Report**: `target/cucumber-reports/cucumber-pretty.html`
- **Extent Reports**: Check the `reports` directory for detailed graphical analysis.

## 🤝 Contribution
Feel free to fork the project and submit pull requests for any improvements or bug fixes.
