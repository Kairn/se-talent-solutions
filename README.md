# SE Talent Solutions
[![Apache license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

SE Talent Solutions (SETS) is an online human resources management system which facilitates an ERS (Expense Reimbursement System) for its employees.

**Update: This project has been shut down and the service is no longer available on the web.**

## Build Project
### Prerequisites
* [Eclipse EE](https://www.eclipse.org/downloads/packages/release/kepler/sr2/eclipse-ide-java-ee-developers) or [Spring Tool Suite](https://spring.io/tools3/sts/all)
* Maven
* Oracle Relational Database System
* A Server Runtime (e.g. Oracle WebLogic Server)
* SCSS Compiler

### Install
1. Download this repository and extract the project into an Eclipse Workspace.
2. Import the project as a maven project.
3. Update the project to download all Maven dependencies (OJDBC driver not included).
4. Open Oracle Database and run the SQL scripts in the "database" folder to create a starter database. (All password data are SHA-256 hash strings, raw passwords for each employee have the pattern \<first name initial> + \<full last name> + "123456".)
5. Create a new folder named "properties" and add it to your build path.
6. Add your database "connection.properties" and email server "email.properties" to the "properties" folder.
7. Inside the "WebContent/resources" folder, create a new folder named "libraries" in which you will include the following: **Bootstrap 4**, **jQuery**, **MDB DataTables**, **Font Awesome**. (*All libraries must be referenced by the "index.html" document.*)
8. Compile the "main.scss" file to generate the "main.css" file.
9. Run the JUnit tests to make sure the Java code is functional.
10. Publish the project to your server runtime.

## Use Cases
### Employee Authentication
* Registered employees can login and logout.
* Each login session will be invalidated after the browser has been idle for 10 minutes.
* Employees can obtain a new pair of credentials with a correct username and email address.

### Employee Management
* All employees can update their basic information and change their username/password.
* Managers and executives can register new employees. (A new credentials pair is automatically generated and sent to the employee's email address.)
* Executives can change the roles of other non-executive employees.
* Executives can fire non-executive employees.

### Reimbursement Filing
* All employees can create new expense reimbursement requests.
* Employees can view the status of their previously submitted requests.
* Employees can upload one or more image files which are attached to individual requests to provide extra detail and evidence.
* Employees can recall any of his/her pending requests.
* Employees will be notified by email once one of his/her requests has been resolved.

### Reimbursement Administration
* Managers can view the reimbursement requests submitted by the employees they manage directly.
* Managers can approve or deny the requests they administer.
* Managers can view the full history of all resolved requests from non-executive employees.
* Executives can view all requests regardless of ownership.
* Executives can resolve pending requests submitted by all employees except him/herself.