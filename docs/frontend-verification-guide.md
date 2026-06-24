# 🖥️ EcoExchange Frontend Walkthrough & Verification Guide

This guide provides a step-by-step walkthrough to verify the complete trading, negotiation, logistics, and analytics lifecycles on the **EcoExchange** user interface. 

It explains confusing business terms, provides ready-to-use mock data, and details how to run the verification in both **Offline Simulation Mode** (instant sandbox) and **Online Live Integration Mode** (with backend Java services & Kafka running).

---

## 📖 Key Terms Decoded

Before starting, here is a glossary of the concepts used throughout the platform:

*   **Organization Vetting / Verification**: To maintain safety and prevent toxic material dumps or fraud, B2B marketplaces require companies to be audited before trading. When an organization registers, its status is `PENDING`. An Admin must review its tax documents and "vet" (verify) the business, changing its status to `VERIFIED` to unlock selling and buying capabilities.
*   **GST Number / GSTIN**: Goods and Services Tax Identification Number. It is a 15-character identification code for legal business entities. The system checks this string structure during registration.
*   **Listing / Product**: An advertisement created by a Seller offering a specific quantity of industrial scrap (e.g., steel scrap, fly ash, plastic flakes) located at a particular factory site, backed by quality documents.
*   **Offers & Bidding**: Unlike consumer marketplaces with simple checkout carts, B2B transactions involve custom negotiations. A Buyer submits an "Offer" proposing a specific volume and price per unit. The Seller can then choose to accept or reject this custom bid.
*   **Logistics Milestones**: The shipment tracking flow for transporting heavy industrial waste:
    1.  `REQUESTED`: An order is created; shipment needs transport.
    2.  `ASSIGNED`: Assigned to a freight carrier partner.
    3.  `PICKED_UP`: The truck has loaded the cargo at the Seller's site and is in transit.
    4.  `DELIVERED`: The cargo is successfully dropped off and signed for at the Buyer's receiving bay.
*   **CO2 Saved (Carbon Offsets)**: A metric calculated dynamically when a shipment is delivered. The tonnage recycled is multiplied by category-specific factors (e.g., recycling 1 ton of steel scrap saves 1.5 tons of carbon emissions compared to manufacturing virgin steel).

---

## 🧪 Simulation Sandbox (Offline Verification)

The easiest way to test the platform is through the **Offline Simulation Sandbox**. If the Java backend microservices are not running, the React application automatically runs in simulation mode, using pre-populated local memory.

You can toggle between different roles using the simulated role selector dropdown located at the **bottom of the Sidebar**!

```
[Sidebar Bottom]
Role: [ Seller  v ]  <-- Click this to switch roles instantly!
```

---

### Step-by-Step Flow: Sandbox Verification

Follow these steps in sequence to execute a full transaction:

#### Step 1: Register a New Organization
1. Open the portal page and click **Register Organization** (or navigate to the register page if you are logged out).
2. Enter the following test details:
    *   **Organization Legal Name**: `GreenCycle Polymers`
    *   **Contact Person**: `Sarah Connor`
    *   **GSTIN / Tax ID**: `22AAAAA0000A1Z5`
    *   **Business Email**: `sarah@greencycle.com`
    *   **Password**: `SecurePass123!`
    *   **Primary Role**: `Seller`
    *   **Waste Streams**: Check **Plastic Scrap** and **Glass Waste**.
3. Click **Register Organization**. You will see a success checkmark, and after 3 seconds, you will be redirected to the Login page.

#### Step 2: Vet (Verify) the Organization as an Admin
*Because your new organization is `PENDING`, an Administrator must approve it first.*
1. On the Sidebar, click the simulated role dropdown at the bottom and select **admin**.
2. From the Sidebar, navigate to **Organization Verification** (under the Admin section).
3. Find `GreenCycle Polymers` in the pending applications list.
4. Click **Verify Organization**. The status badge will change to a green **Verified** indicator.
5. *(Optional)* Navigate to **Audit Viewer** to see the system audit event log tracking your approval: `ORGANIZATION_VERIFY`.

#### Step 3: Create a Listing as a Seller
1. Click the simulated role dropdown at the bottom of the Sidebar and select **seller** (or log in with `sarah@greencycle.com` / `SecurePass123!`).
2. Navigate to **Create Listing** from the Sidebar menu.
3. Fill in the listing details:
    *   **Title**: `High-Purity Recycled PET Flakes`
    *   **Category**: `Plastic Scrap`
    *   **Quantity**: `20.0`
    *   **Unit**: `Tons`
    *   **Price per Ton**: `600` (USD)
    *   **Location**: `Columbus, OH`
    *   **Description**: `Pre-washed, sorted post-consumer PET flakes. Moisture content is below 0.5%. Bulk supersack packaging.`
    *   **Certificate Name**: `Purity Assured Certificate`
4. Click **Publish Material Listing**. You will automatically be redirected to the **Manage Listings** screen, where your new listing is visible and marked as **Active**.

#### Step 4: Search & Submit an Offer as a Buyer
1. Switch your simulated role to **buyer** via the sidebar dropdown.
2. Navigate to **Marketplace** from the Sidebar.
3. Use the search input to search for `PET` or filter by the **Plastic Scrap** category.
4. Locate the listing titled `High-Purity Recycled PET Flakes` and click **View Details**.
5. Read the description, verify the quality certificate link, and scroll to the **Negotiate / Place Offer** panel.
6. Enter a custom negotiation bid:
    *   **Quantity**: `15.0` (Tons)
    *   **Offered Price**: `550` (proposing a discount from $600)
    *   **Target Delivery Date**: Choose any date in the calendar.
7. Click **Submit Offer**. You will see a confirmation alert and can view your proposal in the **Offers Sent** screen.

#### Step 5: Accept the Offer as the Seller
1. Switch your simulated role back to **seller**.
2. Check the **Notifications** screen (you will see an alert: *"New Offer Received"*).
3. Navigate to **Offers Received** from the Sidebar.
4. Locate the incoming offer for `High-Purity Recycled PET Flakes` from buyer `GreenBuild Construction` for 15 tons at $550/ton.
5. Click **Accept Offer**. The status of the offer changes to **Accepted**, and the listing inventory is automatically reduced to `5.0 Tons`.

#### Step 6: Accessing Checkout & Order Verification (Two Purchase Routes)
In EcoExchange's B2B model, there are **two ways** a purchase is finalized and checked out:

*   **Route A: The Direct Purchase Route (Immediate Checkout)**
    1. Switch your simulated role to **buyer**.
    2. Go to the **Marketplace** page, click **View Details** on any active listing.
    3. Look at the top right card labeled **"Immediate B2B Purchase"**.
    4. Click the **"Buy Inventory Directly"** button. This acts as an immediate checkout, which instantly generates the Order record and a Cargo Shipment log, bypassing negotiation.
    5. You will be redirected to the **Orders** (Transactions Ledger) page where you can see the new order.

*   **Route B: The Custom Offer Route (Negotiation Checkout)**
    1. If you followed the negotiation path (Step 4 & 5), you do **not** need to click a manual checkout button. The Seller's **"Accept Offer"** click acts as the electronic contract signature, which automatically executes checkout.
    2. Switch your simulated role to **buyer**.
    3. Check your **Notifications** on the header (or the notifications page) to confirm you received: *"Offer Decision: Accepted"*.
    4. Navigate to **Orders** on the Sidebar.
    5. You will see a newly created transaction record for the accepted bid amount, in a **Processing** state. It has already been checked out and queued for cargo transport!

#### Step 7: Progress the Delivery as Logistics
1. Switch your simulated role to **logistics**.
2. Navigate to **Shipment Tracking** on the Sidebar.
3. Locate the shipment representing your `High-Purity Recycled PET Flakes` transport run.
4. Click **Accept Transport Run**. The milestone advances to `Assigned` (Carrier status: `ACCEPTED`).
5. Click **Confirm Cargo Loaded** (represents arriving at the Columbus, OH site and loading). The milestone moves to `Cargo Loaded & Inspected`.
6. Click **Progress to In-Transit**. The milestone changes to `In Transit`.
7. Click **Confirm Delivery** (represents dropping off cargo at the buyer yard). The shipment status changes to **Delivered**, closing the associated parent Order.

#### Step 8: View Environmental Impact Analytics
1. Navigate to **Analytics** from the Sidebar.
2. Check the statistics cards and charts:
    *   **Total Waste Reused (Tons)** has increased by `15.0 Tons`.
    *   **CO2 Saved (Tons)** has increased by `25.5 Tons` (15.0 tons of Plastic Scrap * 1.7 CO2 factor = 25.5).
    *   **Platform Revenue** has increased by the order total (`15.0 * $550 = $8,250`).
3. Switch your role back to **admin** and navigate to **Audit Viewer** to see the chronological actions captured during this lifecycle.

---

## ⚡ Live Integration Mode Verification

To verify the system with real databases, services, and live network communication, follow the deployment steps in the root [README.md](../README.md).

### Connection Check
*   Look at the top header of the frontend application.
*   If the services are running, a green **Backend Status: Connected** badge will appear.
*   If the badge is red, check that your `apigateway` is running on port `8080` and the downstream microservices are running.

### Performing the Verification Live
1.  **Register a New Account**: Go to the registration screen. Since you are running live, this writes to the `ecoexchange_identity` database.
2.  **Verify via Database or Admin Account**:
    *   *Option A*: Log in using the default Platform Admin email `platformadmin@ecoexchange.com` (password: `Password123!`) and navigate to the **Org Verification** dashboard to approve the new organization.
    *   *Option B*: If you haven't seeded the admin, manually update the `verification_status` column in the `organizations` table of the `ecoexchange_identity` database to `VERIFIED`.
3.  **Core Services & Kafka Verifications**:
    *   Create a listing, submit an offer, and accept it.
    *   Verify that listing events propagate to the `Search Service` read-optimized database by checking search results under **Marketplace** (the search results populate asynchronously via Kafka).
    *   Submit a checkout. Ensure email notifications trigger (check the Console logs of the `notification-service` to see the consumed events).
    *   Confirm shipment deliveries and verify that database records inside `ecoexchange_analytics` recalculate the aggregated daily metric values.

---

## 📋 Copy-Paste Testing Data Sheets

Use this copy-pasteable test data to run validation cycles:

### Registration Inputs
| Field | Seller Test Value | Buyer Test Value |
| :--- | :--- | :--- |
| **Organization Name** | `Apex Metal Refiners` | `Constructo Brick Co.` |
| **Contact Person** | `Elena Rostova` | `Marcus Vance` |
| **GSTIN** | `27GVP1234F1Z1` | `07AAA1234B2Z2` |
| **Email** | `elena@apexmetals.com` | `marcus@constructobrick.com` |
| **Password** | `RefineSteel2026!` | `BuildGreen2026!` |
| **Waste Streams** | `Steel Scrap` | `Fly Ash`, `Glass Waste` |

### Listing Inputs
| Field | Steel Listing | Ash Listing |
| :--- | :--- | :--- |
| **Title** | `Industrial Galvanized Steel Scrap` | `Processed Dry Fly Ash` |
| **Category** | `Steel Scrap` | `Fly Ash` |
| **Quantity** | `50` (Tons) | `100` (Tons) |
| **Price** | `350` (USD/Ton) | `50` (USD/Ton) |
| **Location** | `Pittsburgh, PA` | `Cincinnati, OH` |
| **Cert Name** | `Galvanization Spec Assay` | `ASTM Concrete Additive Cert` |
