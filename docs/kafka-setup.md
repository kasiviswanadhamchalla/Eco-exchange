# Kafka Setup Guide with KRaft

This document outlines the instructions to install, run, and configure Apache Kafka using **KRaft (Kafka Raft)** metadata mode (Zookeeper-less) and create the necessary topics for the **Eco-Exchange** platform.

This guide refers to the topic details in [kafka-topics.txt](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/docs/kafka-topics.txt) and the event definitions in [events.md](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/docs/events.md).

---

## 1. Prerequisites & Download

1. Download the latest binary release of Apache Kafka from the official website (Scala 2.13 version is recommended):
   [https://kafka.apache.org/downloads](https://kafka.apache.org/downloads)
2. Extract the archive file to a directory of your choice (e.g., `C:\kafka` on Windows or `~/kafka` on Unix).
3. Open a terminal / command prompt and navigate to your Kafka root installation directory.

---

## 2. Start Kafka with KRaft

KRaft is the modern consensus protocol that replaces ZooKeeper. Follow these steps to configure and start your local Kafka node:

### Step A: Generate a Cluster UUID
Generate a unique identifier for your new Kafka cluster.

**On Windows (Command Prompt / PowerShell):**
```cmd
bin\windows\kafka-storage.bat random-uuid
```

**On Unix / macOS:**
```bash
bin/kafka-storage.sh random-uuid
```

*This will output a UUID, e.g., `4L62Cmq2TNyx1Y3_12345g`.*

### Step B: Format the Log Directories
Format the storage directory using the UUID generated in the previous step.

**On Windows:**
```cmd
bin\windows\kafka-storage.bat format -t <YOUR_GENERATED_UUID> -c config\kraft\server.properties
```

**On Unix / macOS:**
```bash
bin/kafka-storage.sh format -t <YOUR_GENERATED_UUID> -c config/kraft/server.properties
```

### Step C: Start the Kafka Server
Start the broker service.

**On Windows:**
```cmd
bin\windows\kafka-server-start.bat config\kraft\server.properties
```

**On Unix / macOS:**
```bash
bin/kafka-server-start.sh config/kraft/server.properties
```

The broker will start on `localhost:9092` by default. Keep this terminal window open.

---

## 3. Create Eco-Exchange Kafka Topics

In a new terminal window, navigate to your Kafka root folder and execute the commands below to create the topics required by the microservices.

### Required Topics
Based on the platform design, we need to create the following topics:
1. `listing-events` (Published by Marketplace Service)
2. `offer-events` (Published by Marketplace Service)
3. `offer-events-retry` (For retry logic on offer events)
4. `offer-events-dlq` (Dead Letter Queue for failed offer events)
5. `order-events` (Published by Order Service)
6. `shipment-events` (Published by Logistics/Shipment flow)
7. `notification-events` (Published to trigger Notification Service)
8. `audit-events` (Published to track global platform audit trails)

### Topic Creation Commands

**On Windows:**
```cmd
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic listing-events
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic offer-events
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic offer-events-retry
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic offer-events-dlq
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic order-events
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic shipment-events
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic notification-events
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic audit-events
```

**On Unix / macOS:**
```bash
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic listing-events
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic offer-events
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic offer-events-retry
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic offer-events-dlq
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic order-events
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic shipment-events
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic notification-events
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 --topic audit-events
```

---

## 4. Operational Utility Commands

Here are some helpful commands for verifying and managing your topics during development.

### List Active Topics
Verify that all topics were successfully created.

**On Windows:**
```cmd
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
```

**On Unix / macOS:**
```bash
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

### Describe a Topic
View partition and replication configurations for a specific topic (e.g., `offer-events`).

**On Windows:**
```cmd
bin\windows\kafka-topics.bat --describe --topic offer-events --bootstrap-server localhost:9092
```

**On Unix / macOS:**
```bash
bin/kafka-topics.sh --describe --topic offer-events --bootstrap-server localhost:9092
```

### Consume Messages (Console Consumer)
Monitor events flowing through a topic in real-time.

**On Windows:**
```cmd
bin\windows\kafka-console-consumer.bat --topic offer-events --bootstrap-server localhost:9092 --from-beginning
```

**On Unix / macOS:**
```bash
bin/kafka-console-consumer.sh --topic offer-events --bootstrap-server localhost:9092 --from-beginning
```
