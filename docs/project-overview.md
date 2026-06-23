# EcoExchange (IndustryConnect) Microservices Platform

Welcome to the **EcoExchange** (formerly *IndustryConnect*) project. This platform is a cloud-native, B2B digital marketplace designed to bridge the gap between waste-producing industrial plants and recycling or manufacturing enterprises that consume waste as production raw materials.

By facilitating trade, tracking shipments, and providing comprehensive analytics on recycled waste volumes and carbon savings, EcoExchange powers the transition towards a sustainable, circular economy.

For additional documentation, refer to:
- [Requirements](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/docs/requirements.txt)
- [Service Boundaries](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/docs/service-boundaries.txt)
- [Database Schema](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/docs/database.md)
- [Kafka Configuration](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/docs/kafka-setup.md)
- [Kafka Topics Details](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/docs/kafka-topics.txt)

---

## 1. The Existing Problem (The Industrial Waste Dilemma)

Modern manufacturing processes generate vast quantities of industrial byproducts and waste materials, including:
- **Steel Scrap & Metal Offcuts**
- **Fly Ash** (from power generation and heavy combustion)
- **Plastic Scrap & Polymers**
- **Textile Waste & Fabric Offcuts**
- **Wood Waste & Sawdust**
- **Glass Waste & Cullet**

### The Challenges:
1. **Inefficient Disposal & High Costs**: Producers often pay high disposal and landfill fees to get rid of scrap material, even though these materials hold residual market value.
2. **Raw Material Sourcing Friction**: Recyclers, cement manufacturers, construction companies, and packaging firms purchase virgin raw inputs at high costs when recycled industrial byproducts could suffice.
3. **Lack of Trust & Quality Assurance**: Industrial buyers require proof of material quality. Without verified quality certificates, compliance tracking, and transparent seller identity, buyers hesitate to use secondary inputs.
4. **Logistics & Coordination Overhead**: Coordinating the pickup and delivery of industrial-scale waste materials requires complex logistics tracking, which is currently fragmented.
5. **No Centralized Sustainability Tracking**: Organizations lack a unified way to measure and report their contribution to landfill diversion, circular economy metrics, and carbon emissions reduction.

---

## 2. How EcoExchange Solves It

EcoExchange offers a secure, decentralized B2B marketplace to streamline waste reuse. The platform solves the existing problems through several key capabilities:

- **Verified Organization Listings**: Only validated organizations (moderated by platform admins) can list materials.
- **Dynamic Marketplace Listings**: Sellers list material types, quantities, pricing, and locations, accompanied by images and official quality certificates.
- **Negotiation Engine (Offers & Bidding)**: Buyers can submit structured offers to buy materials (specifying quantity and price), allowing sellers to accept or reject negotiations.
- **Logistics Integration**: Seamless order fulfillment flows from payment to assigning logistics partners, tracking shipment pickup, transit status, and delivery confirmation.
- **Environmental Impact Analytics**: An automated analytics service calculates waste reused in tons and carbon footprint (CO2) saved.
- **Global Transparency & Safety**: Features global audit logging and idempotent operations (using transaction-specific tokens) to ensure the platform remains secure and robust.

---

## 3. End Users (Target Audience)

The EcoExchange ecosystem serves four distinct types of users:

1. **Sellers (Waste Producers)**:
   - *Who*: Manufacturing factories, chemical plants, demolition contractors, metalworks.
   - *Key Actions*: Register organizations, list surplus/scrap materials, upload lab certificates and images, manage available inventory, and accept/negotiate buyer offers.
2. **Buyers (Waste Consumers)**:
   - *Who*: Recyclers, construction/building developers, cement plants, packaging manufacturers.
   - *Key Actions*: Search and filter material listings by category, location, and price; download certificates; submit buy offers; track orders and shipments.
3. **Logistics Partners**:
   - *Who*: Freight carriers, shipping companies, third-party logistics (3PL) providers.
   - *Key Actions*: Accept transport orders, view pickup/delivery details, and update shipment status (e.g., ASSIGNED, PICKED_UP, DELIVERED).
4. **Platform Administrators (Admins)**:
   - *Who*: EcoExchange platform operations team.
   - *Key Actions*: Verify and onboard organizations, moderate listings for safety compliance, audit transaction history, and view overall platform performance metrics.

---

## 4. Industry & Sustainability Benefits

- **Circular Economy Enablement**: Turns waste from one industry into the valuable raw materials of another, keeping resources in circulation.
- **Cost Savings**: Sellers turn disposal liabilities into revenue streams; buyers purchase raw inputs at competitive rates compared to virgin materials.
- **Carbon Footprint Reduction**: Diverts waste from landfills and reduces energy consumption associated with extracting new raw resources, tracking CO2 savings in real-time.
- **Reduced Sourcing Risks**: Verification of organizations and digital quality certificates guarantee supply chain compliance and quality.
- **Transparency**: Every listing creation, offer validation, order, and shipment updates are logged for absolute auditability.

---

## 5. Technology Stack & Architectural Decisions

EcoExchange is designed as a cloud-native microservices architecture built on the following technologies:

| Category | Technology | Version | Purpose & Rationale |
| :--- | :--- | :--- | :--- |
| **Language** | **Java** | 25 | Utilizes the latest Java features for high performance, virtual thread support, and type safety. |
| **Core Framework** | **Spring Boot** | 4.1.0 | Standardized framework for configuring dependency injection, REST endpoints, and MVC components. |
| **Microservices Suite** | **Spring Cloud** | 2025.1.2 | Facilitates service discovery, gateway routing, and distributed config patterns. |
| **Service Discovery** | **Eureka Server** | (Spring Cloud) | Registers all services dynamically, removing the need to hardcode service endpoints. |
| **Gateway Routing** | **Spring Cloud Gateway** | (Webflux based) | Orchestrates incoming traffic, routes client calls to downstream microservices, and handles authentication. |
| **Configuration** | **Spring Cloud Config** | (Server & Client) | Centralizes all application properties (`application.properties` / `yaml` files) inside a version-controlled git config repository (`config-repo`). |
| **Relational Database** | **MySQL** | (Connector-J) | Stores strongly-typed transactional data (e.g., user profiles, organizations, orders, listings, offers, shipments). |
| **Persistence Layer** | **Spring Data JPA** | (Hibernate) | Simplifies object-relational mapping (ORM) and standardizes CRUD operations across all services. |
| **Message Broker** | **Apache Kafka** | (KRaft Mode) | Event streaming platform running Zookeeper-less (via KRaft consensus). Implements asynchronous event propagation. |
| **Security & Auth** | **Spring Security & JWT** | 0.12.5 (jjwt) | Handles user authentication and authorization. Emits stateless JSON Web Tokens for API requests. |

---

## 6. How Features Map to the Technology Stack

Each business feature is powered by specific architectural components and frameworks:

### A. Authentication & Access Control
- **Feature**: User registration, login, JWT issuance, organization validation.
- **Tech Used**: [Identity Service](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/identity-service) powered by **Spring Security** and **JJWT**.
- **Implementation**: The [Identity Service](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/identity-service) validates user credentials, checks organization GST numbers, and issues secure JWTs. All subsequent API calls go through the [API Gateway](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/apigateway), which validates the signature of the token before forwarding the request.

### B. Marketplace Listings & Offer Negotiations
- **Feature**: Listing industrial waste, uploading quality files, submitting purchase bids, and accepting/rejecting offers.
- **Tech Used**: [Marketplace Service](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/marketplace-service) powered by **Spring Boot**, **Spring Data JPA**, and **MySQL**.
- **Implementation**: Handles core marketplace listings and offer records. Emits Kafka events like `ListingCreatedEvent` and `OfferPlacedEvent` to decouple business flows.

### C. Order Processing & Logistics Delivery
- **Feature**: Converting accepted offers into formal orders, creating shipments, assigning logistics partners, and tracking delivery progress.
- **Tech Used**: [Order Service](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/order-service) using **Spring Boot**, **Spring Data JPA**, **MySQL**, and consuming/producing Kafka events.
- **Implementation**: Listens for accepted offers, generates orders, and schedules shipments. Emits `OrderCreatedEvent` and `ShipmentRequestedEvent` to update inventory and trigger notification streams.

### D. Optimized Real-Time Search & Autocomplete
- **Feature**: Fast searching of scrap material, filtering by category, minimum/maximum price, location, and autocompleting queries under 500ms.
- **Tech Used**: [Search Service](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/search-service) using a denormalized database schema inside **MySQL** via [SearchListingRepository](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/search-service/src/main/java/com/industry_connect/search_service/repository/SearchListingRepository.java) combined with **Apache Kafka**.
- **Implementation**: The [Search Service](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/search-service) maintains a read-optimized copy of listings. It listens to Kafka's `listing-events` topic to update its local schema asynchronously, keeping search execution independent of write-heavy transactional tables.

### E. Real-Time Alerts & Email Notifications
- **Feature**: Alerting buyers when their offers are accepted, notifying logistics partners of assigned shipping runs, and informing sellers when new bids arrive.
- **Tech Used**: [Notification Service](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/notification-service) listening to the `notification-events` Kafka topic.
- **Implementation**: Consumes events from the queue asynchronously. This decouples user actions (like accepting an offer) from long-running notification operations (e.g., SMTP email delivery), improving platform response times.

### F. Sustainability Analytics & Auditing
- **Feature**: Aggregating carbon offset data (CO2 saved), tracking total tons of waste reused on the platform, and capturing audit trails of user operations.
- **Tech Used**: [Analytics Service](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/analytics-service) consuming `order-events` and `audit-events` Kafka topics.
- **Implementation**: Accumulates metrics on successful transactions. Periodically aggregates waste reused (tons) and revenue inside `daily_metrics` database tables to generate reporting dashboards.

---

## 7. Advanced Engineering Patterns

### A. Idempotency Guardrails (`X-Idempotency-Key`)
To prevent duplicate resource creation (e.g. double-submitting an offer, double-paying an order, or creating multiple shipments due to network retries), the platform implements an **Idempotency Strategy**:
- Clients send a unique UUID header: `X-Idempotency-Key`.
- When an API endpoint (e.g. creating listings/offers) receives this key, the service verifies its presence in the `idempotency_keys` table using [IdempotencyService](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/marketplace-service/src/main/java/com/industry_connect/marketplace_service/service/IdempotencyService.java).
- If the key exists, the service bypasses processing and immediately returns the cached response.
- If it does not exist, the request is executed, and its response is serialized and stored in the database for potential subsequent retries.

### B. Asynchronous Resiliency (Kafka Retry & DLQ)
EcoExchange uses robust error handling strategies for distributed message consumption:
- If a consumer (e.g., [Notification Service](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/notification-service)) fails due to temporary outages, the event is redirected to a dedicated retry topic (`offer-events-retry`).
- After a configurable number of retries (e.g., 3 attempts), if the error persists, the message is routed to the Dead Letter Queue (DLQ) topic (`offer-events-dlq`).
- Administrators can monitor the DLQ to resolve system failures without losing transaction event context.

---

## 8. Status of Elasticsearch and Redis

Based on the current codebase and configurations:
- **Elasticsearch**: **Not Currently Used**. While earlier high-level designs (such as [service-boundaries.txt](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/docs/service-boundaries.txt)) anticipated using Elasticsearch indexes for optimized catalog searches, the actual [Search Service](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/search-service) implementation relies on SQL-based `LIKE` pattern matching using [SearchListingRepository](file:///C:/Users/nikhi/Desktop/ExoExchange/Eco-exchange/search-service/src/main/java/com/industry_connect/search_service/repository/SearchListingRepository.java) against a denormalized MySQL database schema.
- **Redis**: **Not Currently Used**. Distributed memory caches (like Redis) are not present in any microservice dependencies. The platform handles user sessions via stateless JWTs and tracks duplicate request attempts via the relational `idempotency_keys` table inside MySQL, omitting the immediate need for a Redis deployment.

