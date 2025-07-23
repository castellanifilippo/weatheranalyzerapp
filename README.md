# WeatherAnalyzerApp

WeatherAnalyzerApp is a JavaFX desktop application designed for the analysis and visualization of meteorological data. This project was developed as part of a university assignment and is intended to work in conjunction with the backend [WeatherAnalyzerServer](https://github.com/castellanifilippo/weatheranalyzerserver), which manages data acquisition and processing.

---

## Features

- **GUI**: Built with JavaFX, providing an intuitive user interface.
- **Weather Data Visualization**: Includes tables and dashboard summaries.
- **Station Comparison**: Compare datasets from multiple sources or different time periods.
- **Error Handling and Notifications**: User-friendly alerts for connection problems or data issues.
- **Simple Configuration**: API endpoints and parameters can be set via configuration file.

---

## Requirements

- Java 17 or higher (Java 21 LTS recommended)
- Maven (for build and dependency management)
- A running instance of the [WeatherAnalyzerServer](https://github.com/castellanifilippo/weatheranalyzerserver) backend

---

## Setup & Usage

1. **Clone the repository**
   ```bash
   git clone https://github.com/castellanifilippo/weatheranalyzerapp.git
   cd weatheranalyzerapp
   ```

2. **Configure the application**
   
   Edit the main configuration file at `src/main/resources/config.properties`. Example:
   ```
   api.url=http://localhost:5000
   api.timeout=5000
   ```
   Set `api.url` to the address where your backend is running.

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn javafx:run
   ```
   Or run the generated JAR (found in `target/`):
   ```bash
   java -jar target/weatheranalyzerapp.jar
   ```

---

## Main Workflow

- **Dashboard:** Quickly access the latest weather data and key trends.
- **Filters:** Select time intervals, weather stations, and data types.
- **Charts:** Switch between various visualizations (e.g., temperature over time, cross-station comparisons).
- **Data Export:** Export table data to CSV.
- **Error Handling:** The app will notify you in case of network or data issues.

---

## Backend Integration

WeatherAnalyzerApp communicates with WeatherAnalyzerServer via RESTful HTTP APIs. Make sure the backend is running and accessible at the URL specified in `config.properties`.

Example API call:
```
GET /api/weatherdata?station=XYZ&from=2024-01-01&to=2024-01-31
```
Refer to the backend documentation for a complete list of available endpoints.

---

## Author

Developed by [castellanifilippo](https://github.com/castellanifilippo) as a university project.
