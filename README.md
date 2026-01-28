# carpooling_app

A Java-based desktop carpooling application built with Swing and MySQL. 

![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/5.png)

More images can be found at the end of this README file, scroll down !!!
## Description

CoCar is a comprehensive Java-based carpooling platform designed to connect drivers with available seats to passengers traveling in the same direction. The application facilitates ride-sharing to reduce transportation costs, alleviate traffic congestion, and promote environmental sustainability through efficient vehicle utilization.

## Features

- User authentication (Login/Registration)
- Two user roles: Driver and Passenger
- Trip creation and management
- Trip search and filtering
- Booking and reservation system
- Payment processing
- User profiles and trip history

## Prerequisites

- Java JDK 8 or higher
- MySQL Server
- Apache Ant (for building)

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd carpooling-app
   ```

2. **Set up the database**
   - Start MySQL server
   - Create a database named `covoiturage_db`
   - Run the `database/schema.sql` script

3. **Configure database connection**
   - Update `DatabaseConnection.java` with your MySQL credentials
   - Modify host, port, username, and password as needed

4. **Build the project**
   ```bash
   ant build
   ```

5. **Run the application**
   ```bash
   ant run
   ```
   or
   ```bash
   java -cp "bin:lib/*" com.covoiturage.Main
   ```

## Usage

1. **Registration**: Create a new account as either a Driver or Passenger
2. **Login**: Use your credentials to access the application
3. **As a Driver**: 
   - Create trips by specifying route, date, time, price, and available seats
   - View and manage your published trips
   - Accept/reject booking requests
4. **As a Passenger**:
   - Search for available trips using filters
   - Book seats on desired trips
   - View your reservations and trip history
   - Make payments for booked trips

## Database Schema

The application uses the following main tables:
- `Utilisateur` - User accounts and credentials
- `Conducteur` - Driver-specific information
- `Passager` - Passenger-specific information
- `Trajet` - Trip details
- `Reservation` - Booking records
- `Paiement` - Payment transactions
- `Avis` - User ratings and reviews

## Dependencies

- MySQL Connector/J (for database connectivity)
- Java Swing (included in Java SE)

## Building with Ant

The project includes a `build.xml` file for Apache Ant. Use the following commands:

- `ant compile` - Compile the Java source files
- `ant build` - Build the project (compile and create JAR)
- `ant run` - Run the application
- `ant clean` - Clean build artifacts

## Troubleshooting

1. **Database connection issues**: Verify MySQL is running and credentials are correct
2. **Class not found errors**: Ensure all dependencies are in the classpath
3. **UI rendering issues**: Check Java version compatibility

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is open source and available under the MIT License.

## Images
![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/0.png)
![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/1.png)
![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/2.png)
![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/3.png)
![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/4.png)
![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/5.png)
![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/6.png)
![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/7.png)
![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/8.png)
![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/9.png)
![image alt](https://github.com/amineaoudia/carpooling_app/blob/2b816b596e70079c161248ae266fa25315de6c95/Images/10.png)


