# 🖥️ EcoExchange Frontend Overview & Architecture

Welcome to the documentation for the **EcoExchange** client application interface. The frontend is a modern B2B portal engineered to connect industrial waste producers (Sellers), recycling and construction companies (Buyers), freight carriers (Logistics Partners), and system administrators (Admins).

For the step-by-step verification flows, please refer to the [Frontend Walkthrough & Verification Guide](file:///C:/Users/kasiv/OneDrive/Desktop/ecoexchange/docs/frontend-verification-guide.md).

---

## 🛠️ Technology Stack

The frontend is built on a modern JavaScript client-side architecture focusing on speed, visual excellence, and ease of deployment:

*   **Framework**: [React 19](https://react.dev/) (leveraging modern hooks and components).
*   **Build Tool & Dev Server**: [Vite 8](https://vite.dev/) (delivering sub-second Hot Module Replacement).
*   **Styling System**: [Tailwind CSS v4](https://tailwindcss.com/) (using CSS-first configuration and utilities for sleek styling).
*   **Iconography**: [Lucide React](https://lucide.dev/) (providing sharp, modern SVG-based vector icons).
*   **Routing & State Management**: Custom Context-driven router implemented inside [AppContext.jsx](file:///C:/Users/kasiv/OneDrive/Desktop/ecoexchange/ecoexchange-frontend-main/src/context/AppContext.jsx).
*   **API Client**: Standard `fetch` API wrapped in asynchronous handlers supporting authorization tokens and custom idempotency headers.

---

## 🏗️ Architectural Patterns

### 1. Dual-Mode Resiliency (Offline Sandbox vs. Online Integration)
To enable rapid developer onboarding and offline demonstration capability, the frontend features **Dual-Mode Resiliency**:

> [!NOTE]
> The app detects connection readiness by calling `GET /api/search/categories`. If the microservices are offline, it falls back to the **Offline Simulation Sandbox**.

*   **Offline Simulation Sandbox**: Runs entirely in the browser using pre-populated, reactive mock data stored in local state. Developers can test complex B2B flows (like vetting organizations, submitting/accepting bids, modifying listing inventory, simulating cargo transit milestones, and calculating carbon offsets) without running a single Docker container or Java service.
*   **Online Integration Mode**: When the Java Microservices suite is running, the app automatically transitions to calling live API endpoints. User logins, organizations, listings, bids, shipments, and analytics are synced directly with the SQL and Kafka backends through the [API Gateway](file:///C:/Users/kasiv/OneDrive/Desktop/ecoexchange/apigateway).

### 2. Centralized State Engine (`AppContext.jsx`)
All shared application concerns are organized in [AppContext.jsx](file:///C:/Users/kasiv/OneDrive/Desktop/ecoexchange/ecoexchange-frontend-main/src/context/AppContext.jsx):
*   Tracks current authenticated user session and roles.
*   Manages the simulated role toggling dropdown (allows instant switching between `Seller`, `Buyer`, `Logistics Partner`, and `Admin` in simulation mode).
*   Provides abstract API wrappers (`apiCall`) that inject the active JWT and handle error conditions gracefully.
*   Triggers live notification updates and carbon footprint calculation rules.

### 3. Interactive Walkthrough Tour
In [App.jsx](file:///C:/Users/kasiv/OneDrive/Desktop/ecoexchange/ecoexchange-frontend-main/src/App.jsx), the portal integrates a **guided tour wizard**. This is a step-by-step walkthrough built into the user interface to hand-hold new users through creating organizations, getting vetted, listing scrap, placing bids, checking out, and tracking delivery.

---

## 📂 Core Folder & File Structure

The project follows a standard Vite/React file layout:

```
ecoexchange-frontend-main/
├── src/
│   ├── assets/             # Branding and static images
│   ├── components/
│   │   ├── shared/         # Common layout pieces (Header.jsx, Sidebar.jsx)
│   │   └── ui/             # Reusable low-level widgets (Button.jsx, Input.jsx, Card.jsx, Badge.jsx)
│   ├── context/
│   │   └── AppContext.jsx  # Core state, simulation data, and API synchronization logic
│   ├── pages/              # View components representing full-page routes
│   │   ├── LandingPage.jsx # Public-facing marketing portal and stats
│   │   ├── Dashboard.jsx   # Role-specific analytics panels and actions
│   │   ├── Marketplace.jsx # Listing grid, keyword search, and filters
│   │   ├── ListingDetails.jsx # In-depth view of waste parameters & certificates
│   │   ├── CreateListing.jsx # Form to publish new industrial byproducts
│   │   ├── ManageListings.jsx # Inventory dashboard for waste producers
│   │   ├── OffersReceived.jsx # Negotiation center for Sellers
│   │   ├── OffersSent.jsx  # Active bid negotiation history for Buyers
│   │   ├── Orders.jsx      # B2B checkout, invoice details, and shipment status triggers
│   │   ├── ShipmentTracking.jsx # Milestone progress map for carriers and buyers
│   │   ├── Analytics.jsx   # Sustainability reports and carbon offset metrics
│   │   ├── OrgVerification.jsx # Admin panel to vet and verify business licenses
│   │   └── AuditViewer.jsx # Live audit logs tracker for system events
│   ├── App.jsx             # Entry layout and Tour Guide components
│   ├── index.css           # Global theme styles, custom fonts, and glassmorphic designs
│   └── main.jsx            # React DOM mounting point
├── vite.config.js          # Development server and proxy configurations (/api -> localhost:8080)
└── package.json            # Script commands and dependency manifests
```

---

## 🎨 Visual Design & Theme Principles

The visual layout focuses on a premium, clean B2B look:
*   **Deep Slate/Dark Theme**: Backgrounds rely on `#070B14` and slate variants to represent a modern industrial feel.
*   **Luminous Accents**: Emerald greens (`text-emerald-400` / `bg-emerald-600`) represent environmental sustainability and circular economy metrics.
*   **Glassmorphic Cards**: Content panels use translucent backdrops with subtle borders (`glass-card`) to create visual hierarchy and depth.
*   **Semantic Badges**: High-contrast, glowing status indicators represent listing availability, offer states, and logistics milestones (`REQUESTED`, `ASSIGNED`, `PICKED_UP`, `DELIVERED`).

---

## 🔮 Future Vision & Roadmaps

1.  **Direct Charting Integration**: Integrate dynamic graphing libraries (like *Recharts*) to display complex historical recycling throughput and monthly CO2 offsets in [Analytics.jsx](file:///C:/Users/kasiv/OneDrive/Desktop/ecoexchange/ecoexchange-frontend-main/src/pages/Analytics.jsx).
2.  **WebSocket-Powered Feed**: Replace HTTP-polling with WebSockets for real-time notifications when bids are accepted, or when a truck updates its transit status.
3.  **Mobile App Wrap**: Package the shipment tracking views specifically for mobile browsers using Progressive Web App (PWA) guidelines, optimizing the workflow for drivers operating heavy transport vehicles.
4.  **Advanced Certificate Previews**: Render PDF laboratory files and quality metrics (like ASTM ash standards or plastic purity grades) using in-browser document previews.
