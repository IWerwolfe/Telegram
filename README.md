# Telegram Bot for Technical Support

## Description
The bot was initially created as an extension to the 1C CRM to address certain business tasks. As it evolved, it became an independent service with its own API and database, capable of interacting with any external CRM system.   
The need for it arose when the volume of support requests increased, and problems with lost orders started to emerge. Managers couldn't keep up with entering all the data, clients were dissatisfied with the turnaround times, and technicians might close tickets without completing them properly.
This bot addresses some of these issues by providing the following capabilities for clients:   

Submit support requests at any time of day
  - View active tickets and their status
  - Cancel or edit tickets via the bot, enhancing control for companies with multiple branches
  - Make payments, replenish balances, saving time
  - Receive notifications about new requests within the organization and changes in ticket status
  - Increase transparency: see which technicians are handling tickets, which employees made the requests, and identify common issues
  - No need to explain details (cash register model, address, company name, etc.) as everything is stored in the database

For technical support, the bot enables:
  - Logging tickets, even outside of working hours
  - Reducing the workload on managers
  - Reducing the number of lost tickets, thereby improving service quality and increasing profits
  - Reducing duplicate tickets when the same issue is entered multiple times
  - Increasing transparency for administrators, showing who did what and for how long
  - Automating the creation of records upon first contact, eliminating the need to manually enter client details
  - Setting up notifications to be sent to a general work group
    
An extension configuration for UNF 1.6.50 has been written for 1C, allowing interaction with the bot and customer tickets:
  - User-friendly interface for interaction
  - Ticket management capabilities
  - Reports and statistics on companies, technicians, and ticket types
  - Efficiency rating system for technicians
  - API services for interaction with websites, Telegram bots via HTTP
  - Automatic generation and sending of invoices for payment at the end of each month

## Working Version
https://t.me/KassaKomTestBot   
Launched for demonstration purposes and testing.   
You can perform all operations; they don't commit you to anything, and your personal data isn't saved. Payment is enabled through a test environment.

## Workflow Scheme   
Two workflow schemes have been implemented: with registration and without. Registration simplifies ticket submission, as it eliminates the need to enter a phone number and name each time.    
When you click the "Register" button, your phone number is sent to 1C, where it is checked. If the phone number is associated with a contact, all the information about the client is sent to the bot. Depending on the automatically determined role (USER, ADMINISTRATOR, DIRECTOR), access to bot information and capabilities is granted.

Role Division   

For UNAUTHORIZED users: (automatically assigned upon initial bot use)  
  - Create tickets
  - Replenish balance
  - Check balance
  - Edit own tickets
  - Close own tickets

For USERs: (if a phone number is specified for a contact person of the client)
  - All capabilities for UNAUTHORIZED users
  - Submit tickets on behalf of the client
  - Receive notifications about new tickets from the client (depends on settings)

For ADMINISTRATORs: (if a phone number is specified as a contact for the client, or for accountants, administrators)
  - All capabilities for UNAUTHORIZED and USER roles
  - Access to all active tickets from the client
  - Editing and closing others' tickets from the client
  - Access to the client's balance
  - Ability to replenish the client's balance

For DIRECTORs: (if a phone number is specified as a contact for the director, or for individual entrepreneurs)
  - Full access to all functions

If nothing is found by phone number, then a small questionnaire will be required. The bot will ask for your full name, organization's TIN, and your position. After that, data will be requested based on the TIN, a new client will be created, and data will be synchronized with 1C. In this case, the USER role is always assigned.    
Bidirectional data exchange with 1C has been implemented. Anything created or edited in the Telegram bot is automatically sent to 1C, and vice versa. In case of exchange errors, data is logged, and there are plans to implement a scheduled task to automatically perform data exchange in such cases.
Client data is represented in the following format:  

  - Partner
    - Department
      - Contact person
     
Tickets can be submitted with or without linking to a storefront.    
The situation is considered where one person has multiple organizations, in which case the bot will offer a choice from the list. The same process occurs when an organization has multiple storefronts.

## Settings File Description
The application-production.yml file contains configurable parameters for our application:

```
#profile name production
server.port=8181 - application port
spring.main.banner-mode=off - disable the SPRING banner on startup

#Logging Configuration
logging.level=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=ERROR
logging.file.name=LOGS/prod_app.log - path to the log file
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

#Bot Data
bot.name=[ bot name ]
bot.token=[ token (can be obtained via @BotFather) ]
bot.payment=[ payment token (can be obtained via @BotFather) ]

#Database Settings
spring.datasource.url=jdbc:sqlserver:[ database path ]
spring.datasource.username=[ username ]
spring.datasource.password=[ password ]
spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver - driver, depends on the type of database
spring.jpa.show-sql=false - show SQL queries in logs
spring.jpa.hibernate.dialect=org.hibernate.dialect.SQLServer2012Dialect - dialect, depends on the type of database
spring.jpa.hibernate.ddl-auto=update

#DaData Service Configuration (for getting data by TIN)
dadata.api.key=[ token ]

#Settings for Access to 1C
Connector1C.url=[ API path ]
Connector1C.token=[ Token, Configuration UID ]
Connector1C.login=[ 1C database username ]
Connector1C.password= [ 1C database password ]

#Payment Settings
pay.sbpStatic=[ link for payment through SBP ]
pay.isUse=true - enable payment through the bot
pay.isBalanceVisibly=true - display balance
pay.isAddBalance=false - allow balance replenishment
pay.isCard=true - enable card payment
pay.isBank=false - enable invoice issuance (not implemented)
pay.isSBPStatic=true - enable payment through SBP
pay.isSBP=false - enable SBP API payment (not implemented)
pay.isCrypto=false - enable cryptocurrency payment (not implemented)

#General Notification Settings
system.isUse=true - enable sending notifications
system.idToWorkGroup=[ telegram id of the work group for sending notifications ]
system.isSendCreateNewTask=true - notify about a new ticket
system.isSendEditTask=false - notify about ticket editing
system.isClosedTask=false - notify about ticket closure
system.isUserClosedTask=true - notify about ticket closure by the client

#Client Notification Settings
user.isUse=true - enable sending notifications to the client
user.isSendCreateNewTask=true - notify about a new ticket
user.isSendEditTask=false - notify about ticket editing
user.isClosedTask=true - notify about ticket closure
user.balanceIsReplenished=false - balance replenishment required
user.needToPayTask=false - ticket payment required
user.taskIsPaid=false - payment received for the ticket (sent only for office or invoice payments)

#Error Notification Settings
bug.isUse=true - enable sending error notifications
bug.idTelegramUser=[ telegram id of the user for sending notifications ]
```

## Development
Everything in this project was implemented by me, including the code for 1C.   

Language: Java 17   
Used: Spring Boot, Spring Framework, Lombok, MSSQL, REST API, JUnit, Mockito, Telegram API, 1C   
Project build system: Maven   
