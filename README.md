## Idea

To help me get in the proper mindset, I created a fictitious company called Kara.io that is rolling out glucose meters
paired with a mobile app to contribute to a national database of glucose readings per city. The company is facing
scalability issues with their service layer & datalayer. I have proposed a migration to a new architecture including
Cassandra.

## Project Structure

I have two Rest Api microservices defined, called **order** and **device**. They are both setup as IntelliJ modules so
you can run them both in the same project. I recommend using the Services tab, and looking under Spring Boot.

The **order** service runs on 8082, and uses simple synchronous spring web on Tomcat.
<br/>
The **device** service runs on 8081, and is beginning to utilize an asynchronous webfux model, running under Netty.
<br/>
The **web** service runs on 8080, and is a simple web app using Thymeleaf.

## Data

I'm loading a database of all US cities, along with population, density, and timezone. I also have a test data generator that creates readings from fake devices
out in the field.

## Astra

I started out using Astra and the datastax drivers, using the secure connect bundle. I'm not sure why, but 50% of the
time, authentication would fail (load balancer?) so I switched to a local database. I can work on migrating this back to
Astra if you need.