# Multi-Threaded File Downloader
A Java-based multi-threaded file downloader allows users to download files efficiently using multiple threads to speed up the process.

## Features
- Multi-threaded downloading for improved speed
- Error handling and status messages
- AJAX-based communication with the backend

## Technologies Used
### Backend:
- Java (Servlets)
- Multi-threaded file downloading
- HTTP-based file handling

### Frontend:
- HTML, CSS (Bootstrap), JavaScript
- AJAX for seamless user experience

## Project Structure
```
multi-threaded-downloader/
│── downloads/
│── src/main/java/ndys/
│   ├── App.java
│   ├── DownloadServlet.java
│   ├── FileDownloader.java
│   ├── DownloadTask.java
│── src/main/resources/static
│   ├── index.html
│   ├── styles.css
│── pom.xml
```

## Installation & Setup
### Prerequisites:
- Java 17+
- Spring Boot / Apache Tomcat (or any servlet container)
- Maven

### Steps:

1. Clone the repository:
```
git clone https://github.com/ndy-s/java-multi-threaded-downloader.git
cd java-multi-threaded-downloader
```
2. Build and run the project using Maven:
```
mvn spring-boot:run
```
3. Access the application at:
```
http://localhost:8080/
```

## Usage
1. Open the web interface.
2. Enter the file URL you want to download.
3. Click the **Download** button.
4. Wait for the download to complete and view the result.

## Screenshots
![image](https://github.com/user-attachments/assets/0237327b-6167-4741-b3c2-ea06dab78a5a)

## License
MIT
