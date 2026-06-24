import React, { createContext, useContext, useState, useEffect } from 'react';

const AppContext = createContext();

// Mock Initial Data
const initialOrganizations = [
  { id: 'org-1', name: 'EcoSteel Recycling', type: 'Seller', wasteFocus: ['Steel Scrap', 'Plastic Scrap'], status: 'verified', docUrl: 'cert-steel-1.pdf', registeredAt: '2026-05-10' },
  { id: 'org-2', name: 'Apex Cement Industries', type: 'Buyer', wasteFocus: ['Fly Ash', 'Wood Waste'], status: 'verified', docUrl: 'cert-apex.pdf', registeredAt: '2026-05-15' },
  { id: 'org-3', name: 'GreenBuild Construction', type: 'Buyer', wasteFocus: ['Glass Waste', 'Fly Ash'], status: 'verified', docUrl: 'cert-green.pdf', registeredAt: '2026-05-20' },
  { id: 'org-4', name: 'Polymers & Co.', type: 'Seller', wasteFocus: ['Plastic Scrap', 'Glass Waste'], status: 'pending', docUrl: 'cert-poly.pdf', registeredAt: '2026-06-20' },
  { id: 'org-5', name: 'ThreadLoop Textiles', type: 'Seller', wasteFocus: ['Textile Waste'], status: 'verified', docUrl: 'cert-textile.pdf', registeredAt: '2026-06-21' },
  { id: 'org-6', name: 'BioFuel & Lumber Corp', type: 'Seller', wasteFocus: ['Wood Waste'], status: 'suspended', docUrl: 'cert-lumber.pdf', registeredAt: '2026-04-12' },
];

const initialListings = [
  {
    id: 'list-1',
    title: 'Industrial Heavy Melting Steel Scrap',
    description: 'High-quality grade 1 and grade 2 heavy melting steel scrap. Sourced from industrial automotive manufacturing demolition. Free from toxic contaminants.',
    category: 'Steel Scrap',
    quantity: 45.5, // tons
    unit: 'Tons',
    price: 320, // USD per ton
    sellerId: 'org-1',
    sellerName: 'EcoSteel Recycling',
    location: 'Detroit, MI',
    image: 'https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=600&q=80',
    certificateUrl: 'quality-steel-cert-v4.pdf',
    certificateName: 'ASTM E1019 Grade Validation',
    status: 'active', // active, sold, suspended, moderated
    createdAt: '2026-06-18'
  },
  {
    id: 'list-2',
    title: 'Class F Dry Fly Ash (Bulk)',
    description: 'Dry fly ash suitable for high-durability concrete mixtures and cement replacement. Tested for pozzolanic activity index (exceeds 85%). Packed in 1-ton supersacks.',
    category: 'Fly Ash',
    quantity: 120.0,
    unit: 'Tons',
    price: 45,
    sellerId: 'org-6', // BioFuel & Lumber (represented as Fly Ash seller for demo)
    sellerName: 'CoalTech Power & Materials',
    location: 'Gary, IN',
    image: 'https://images.unsplash.com/photo-1589939705384-5185137a7f0f?auto=format&fit=crop&w=600&q=80',
    certificateUrl: 'fly-ash-spec-sheet.pdf',
    certificateName: 'ASTM C618 Compliance Certificate',
    status: 'active',
    createdAt: '2026-06-19'
  },
  {
    id: 'list-3',
    title: 'Post-Consumer High-Density Polyethylene (HDPE) Flakes',
    description: 'Sorted and washed blue/red HDPE flakes from bottle caps and detergent containers. Melt flow index: 2.1 g/10min. Moisture content < 0.8%. Ready for compounding.',
    category: 'Plastic Scrap',
    quantity: 18.0,
    unit: 'Tons',
    price: 850,
    sellerId: 'org-1',
    sellerName: 'EcoSteel Recycling',
    location: 'Toledo, OH',
    image: 'https://images.unsplash.com/photo-1605600690076-ac2493ba450f?auto=format&fit=crop&w=600&q=80',
    certificateUrl: 'hdpe-purity-report.pdf',
    certificateName: 'SGS Purity & Contaminants Assay',
    status: 'active',
    createdAt: '2026-06-20'
  },
  {
    id: 'list-4',
    title: 'Shredded Cotton & Denim Waste Blend',
    description: 'Post-industrial clean cotton scraps and denim fiber. Ideal for sound insulation, mattress padding, or paper manufacturing. Baled and wire-wrapped.',
    category: 'Textile Waste',
    quantity: 25.0,
    unit: 'Tons',
    price: 180,
    sellerId: 'org-5',
    sellerName: 'ThreadLoop Textiles',
    location: 'Charlotte, NC',
    image: 'https://images.unsplash.com/photo-1558769132-cb1aea458c5e?auto=format&fit=crop&w=600&q=80',
    certificateUrl: 'textile-safety-msds.pdf',
    certificateName: 'OEKO-TEX Standard Eco-Report',
    status: 'active',
    createdAt: '2026-06-21'
  },
  {
    id: 'list-5',
    title: 'Untreated Kiln-Dried Pine Wood Scraps',
    description: 'Pine off-cuts and shavings from furniture mill. No chemical treatments, paints, or laminates. Average moisture: 12%. Perfect for pellet production or engineered board core.',
    category: 'Wood Waste',
    quantity: 35.0,
    unit: 'Tons',
    price: 65,
    sellerId: 'org-6',
    sellerName: 'BioFuel & Lumber Corp',
    location: 'Grand Rapids, MI',
    image: 'https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=600&q=80',
    certificateUrl: 'wood-clean-analysis.pdf',
    certificateName: 'Phytosanitary Log-Inspection Report',
    status: 'active',
    createdAt: '2026-06-22'
  },
  {
    id: 'list-6',
    title: 'Cullet Glass Waste (Clear/Flint)',
    description: 'Crushed furnace-ready clear soda-lime glass cullet. Color sorted, magnetic separation done to remove caps/metal tags. Average size: 10-25mm.',
    category: 'Glass Waste',
    quantity: 60.0,
    unit: 'Tons',
    price: 110,
    sellerId: 'org-4',
    sellerName: 'Polymers & Co.',
    location: 'Cleveland, OH',
    image: 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=600&q=80',
    certificateUrl: 'glass-cullet-purity.pdf',
    certificateName: 'Cullet Fusion and Purity Certificate',
    status: 'active',
    createdAt: '2026-06-23'
  }
];

const initialOffers = [
  {
    id: 'off-1',
    listingId: 'list-1',
    listingTitle: 'Industrial Heavy Melting Steel Scrap',
    sellerId: 'org-1',
    sellerName: 'EcoSteel Recycling',
    buyerId: 'org-2',
    buyerName: 'Apex Cement Industries',
    offeredPrice: 300, // custom bid
    quantity: 40.0,
    status: 'pending', // pending, accepted, rejected, cancelled
    deliveryDate: '2026-07-05',
    createdAt: '2026-06-20'
  },
  {
    id: 'off-2',
    listingId: 'list-2',
    listingTitle: 'Class F Dry Fly Ash (Bulk)',
    sellerId: 'org-6',
    sellerName: 'CoalTech Power & Materials',
    buyerId: 'org-3',
    buyerName: 'GreenBuild Construction',
    offeredPrice: 45, // direct buy match
    quantity: 100.0,
    status: 'accepted',
    deliveryDate: '2026-06-30',
    createdAt: '2026-06-21'
  },
  {
    id: 'off-3',
    listingId: 'list-4',
    listingTitle: 'Shredded Cotton & Denim Waste Blend',
    sellerId: 'org-5',
    sellerName: 'ThreadLoop Textiles',
    buyerId: 'org-2',
    buyerName: 'Apex Cement Industries',
    offeredPrice: 160,
    quantity: 25.0,
    status: 'rejected',
    deliveryDate: '2026-07-10',
    createdAt: '2026-06-22'
  }
];

const initialOrders = [
  {
    id: 'ord-1',
    offerId: 'off-2',
    listingId: 'list-2',
    listingTitle: 'Class F Dry Fly Ash (Bulk)',
    sellerId: 'org-6',
    sellerName: 'CoalTech Power & Materials',
    buyerId: 'org-3',
    buyerName: 'GreenBuild Construction',
    finalPrice: 45,
    quantity: 100.0,
    totalCost: 4500,
    status: 'in_transit', // processing, in_transit, delivered, completed
    certificateUrl: 'fly-ash-spec-sheet.pdf',
    certificateName: 'ASTM C618 Compliance Certificate',
    orderedAt: '2026-06-21'
  }
];

const initialShipments = [
  {
    id: 'shp-1',
    orderId: 'ord-1',
    listingTitle: 'Class F Dry Fly Ash (Bulk)',
    sellerName: 'CoalTech Power & Materials',
    buyerName: 'GreenBuild Construction',
    origin: 'Gary, IN',
    destination: 'Chicago, IL',
    logisticsPartner: 'EcoTrans Logistics Ltd',
    carrierStatus: 'accepted', // pending, accepted, picked_up, in_transit, out_for_delivery, delivered
    currentMilestone: 2, // 0: Assigned, 1: Picked Up, 2: In Transit, 3: Out for Delivery, 4: Delivered
    milestones: [
      { title: 'Shipment Assigned', time: '2026-06-21 09:30', description: 'Assigned to EcoTrans Logistics Ltd' },
      { title: 'Cargo Loaded & Inspected', time: '2026-06-22 14:15', description: 'Picked up from CoalTech Gary silos' },
      { title: 'In Transit', time: '2026-06-23 08:00', description: 'En route via I-90 West highway' },
      { title: 'Out for Delivery', time: null, description: 'Approaching GreenBuild storage yard' },
      { title: 'Delivered', time: null, description: 'Delivered, sign-off pending' }
    ],
    updatedAt: '2026-06-23 08:00'
  }
];

const initialNotifications = [
  { id: 'not-1', userId: 'org-1', title: 'New Offer Received', message: 'Apex Cement placed a $300/ton offer for Steel Scrap.', read: false, type: 'offer', time: '2 hours ago' },
  { id: 'not-2', userId: 'org-3', title: 'Shipment Dispatched', message: 'Your fly ash shipment is now en route from Gary, IN.', read: false, type: 'shipping', time: '5 hours ago' },
  { id: 'not-3', userId: 'org-5', title: 'Offer Decision', message: 'Your offer on Pine Wood Scraps was declined.', read: true, type: 'offer', time: '1 day ago' },
  { id: 'not-4', userId: 'admin', title: 'Verification Requested', message: 'Polymers & Co. uploaded registration docs for vetting.', read: false, type: 'admin', time: '3 hours ago' }
];

const initialAuditLogs = [
  { id: 'aud-101', timestamp: '2026-06-21 09:12:45', action: 'ORGANIZATION_REGISTER', user: 'Polymers & Co.', details: 'Registered seller organization org-4 with Plastic Scrap focus.' },
  { id: 'aud-102', timestamp: '2026-06-21 09:30:12', action: 'OFFER_ACCEPT', user: 'CoalTech Power', details: 'Accepted offer off-2 from GreenBuild. Created order ord-1.' },
  { id: 'aud-103', timestamp: '2026-06-21 09:31:00', action: 'SHIPMENT_CREATE', user: 'System Automated', details: 'Created shipment shp-1 for order ord-1. Assigned to EcoTrans.' },
  { id: 'aud-104', timestamp: '2026-06-22 14:15:30', action: 'SHIPMENT_UPDATE', user: 'EcoTrans Logistics', details: 'Updated shipment shp-1 status to PICKED_UP.' },
  { id: 'aud-105', timestamp: '2026-06-23 08:00:15', action: 'SHIPMENT_UPDATE', user: 'EcoTrans Logistics', details: 'Updated shipment shp-1 status to IN_TRANSIT.' },
  { id: 'aud-106', timestamp: '2026-06-23 11:24:50', action: 'LISTING_CREATE', user: 'Polymers & Co.', details: 'Created listing list-6: Cullet Glass Waste.' }
];

export const AppProvider = ({ children }) => {
  const [organizations, setOrganizations] = useState(initialOrganizations);
  const [listings, setListings] = useState(initialListings);
  const [offers, setOffers] = useState(initialOffers);
  const [orders, setOrders] = useState(initialOrders);
  const [shipments, setShipments] = useState(initialShipments);
  const [notifications, setNotifications] = useState(initialNotifications);
  const [auditLogs, setAuditLogs] = useState(initialAuditLogs);

  // Simulation Role States:
  // 'seller' (org-1: EcoSteel), 'buyer' (org-3: GreenBuild), 'logistics' (Swift Logistics), 'admin'
  const [currentRole, setCurrentRole] = useState('seller');
  
  // Custom router state inside App state
  const [currentPage, setCurrentPage] = useState('landing');
  const [selectedListingId, setSelectedListingId] = useState(null);
  const [selectedShipmentId, setSelectedShipmentId] = useState(null);

  // Connection State
  const [backendConnected, setBackendConnected] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);

  // Fetch API helper
  const apiCall = async (endpoint, method = 'GET', body = null, requiresAuth = true) => {
    const headers = {
      'Content-Type': 'application/json',
    };
    if (requiresAuth) {
      const token = localStorage.getItem('token');
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }
    }
    const config = {
      method,
      headers,
    };
    if (body) {
      config.body = JSON.stringify(body);
    }
    try {
      const response = await fetch(endpoint, config);
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `HTTP error! status: ${response.status}`);
      }
      if (response.status === 204) {
        return null;
      }
      return await response.json();
    } catch (err) {
      console.error(`API Call failed to ${endpoint}:`, err);
      throw err;
    }
  };

  // Get active organization based on simulated role
  const getActiveOrg = () => {
    if (backendConnected && currentUser && currentUser.organizationId) {
      const activeOrgLocal = localStorage.getItem('activeOrg');
      if (activeOrgLocal) {
        try {
          return JSON.parse(activeOrgLocal);
        } catch (e) {
          // fallback
        }
      }
    }
    if (currentRole === 'seller') return { id: 'org-1', name: 'EcoSteel Recycling', type: 'Seller' };
    if (currentRole === 'buyer') return { id: 'org-3', name: 'GreenBuild Construction', type: 'Buyer' };
    if (currentRole === 'logistics') return { id: 'org-log', name: 'EcoTrans Logistics Ltd', type: 'Logistics' };
    return { id: 'admin', name: 'System Administrator', type: 'Admin' };
  };

  // Synchronize data from backend
  const refreshData = async () => {
    const isConnected = await fetch('/api/search/categories')
      .then(res => res.ok)
      .catch(() => false);
    
    setBackendConnected(isConnected);
    if (!isConnected) return;

    // 1. Fetch Listings
    try {
      const listingData = await apiCall('/api/listings', 'GET', null, false);
      if (listingData && Array.isArray(listingData)) {
        const mappedListings = listingData.map(l => ({
          id: `list-${l.id}`,
          title: l.title,
          description: l.description,
          category: l.categoryName || 'General',
          quantity: l.quantity,
          unit: l.unit || 'Tons',
          price: l.pricePerUnit,
          sellerId: l.sellerOrgId ? `org-${l.sellerOrgId}` : 'org-unknown',
          sellerName: l.sellerName || `Organization ${l.sellerOrgId}`,
          location: l.location || 'Unknown',
          image: (l.imageUrls && l.imageUrls.length > 0) ? l.imageUrls[0] : 'https://images.unsplash.com/photo-1533090161767-e6ffed986c88?auto=format&fit=crop&w=600&q=80',
          certificateUrl: (l.certificates && l.certificates.length > 0) ? l.certificates[0].certificateUrl : 'quality-cert.pdf',
          certificateName: (l.certificates && l.certificates.length > 0) ? l.certificates[0].certificateName : 'Standard Analysis Sheet',
          status: l.status.toLowerCase(),
          createdAt: l.createdAt ? l.createdAt.split('T')[0] : new Date().toISOString().split('T')[0]
        }));
        setListings(mappedListings);
      }
    } catch (err) {
      console.warn("Could not fetch listings", err);
    }

    const token = localStorage.getItem('token');
    if (!token) return;

    // 2. Fetch Orders
    try {
      const orderData = await apiCall('/api/orders/my', 'GET', null, true);
      if (orderData && Array.isArray(orderData)) {
        const mappedOrders = orderData.map(o => ({
          id: `ord-${o.id}`,
          offerId: o.offerId ? `off-${o.offerId}` : null,
          listingId: `list-${o.listingId}`,
          listingTitle: o.listingTitle || `Listing #${o.listingId}`,
          sellerId: `org-${o.sellerOrgId}`,
          sellerName: o.sellerName || `Organization ${o.sellerOrgId}`,
          buyerId: `org-${o.buyerOrgId}`,
          buyerName: o.buyerName || `Organization ${o.buyerOrgId}`,
          finalPrice: o.totalAmount / (o.quantity || 1),
          quantity: o.quantity || 1,
          totalCost: o.totalAmount,
          status: o.status.toLowerCase(),
          orderedAt: o.createdAt ? o.createdAt.split('T')[0] : new Date().toISOString().split('T')[0]
        }));
        setOrders(mappedOrders);
      }
    } catch (err) {
      console.warn("Could not fetch orders", err);
    }

    // 3. Fetch Offers Sent & Received
    try {
      const sentOffers = await apiCall('/api/offers/sent', 'GET', null, true);
      const receivedOffers = await apiCall('/api/offers/received', 'GET', null, true);
      const combinedOffers = [];
      const offerIdsSeen = new Set();

      const mapOffer = (o) => ({
        id: `off-${o.id}`,
        listingId: `list-${o.listingId}`,
        listingTitle: o.listingTitle || `Listing #${o.listingId}`,
        sellerId: `org-${o.sellerOrgId}`,
        sellerName: o.sellerName || `Organization ${o.sellerOrgId}`,
        buyerId: `org-${o.buyerOrgId}`,
        buyerName: o.buyerName || `Organization ${o.buyerOrgId}`,
        offeredPrice: o.offeredPrice,
        quantity: o.quantity,
        status: o.status.toLowerCase(),
        deliveryDate: o.deliveryDate || new Date(Date.now() + 7 * 86400000).toISOString().split('T')[0],
        createdAt: o.createdAt ? o.createdAt.split('T')[0] : new Date().toISOString().split('T')[0]
      });

      if (Array.isArray(sentOffers)) {
        sentOffers.forEach(o => {
          combinedOffers.push(mapOffer(o));
          offerIdsSeen.add(o.id);
        });
      }
      if (Array.isArray(receivedOffers)) {
        receivedOffers.forEach(o => {
          if (!offerIdsSeen.has(o.id)) {
            combinedOffers.push(mapOffer(o));
            offerIdsSeen.add(o.id);
          }
        });
      }
      if (combinedOffers.length > 0) {
        setOffers(combinedOffers);
      }
    } catch (err) {
      console.warn("Could not fetch offers", err);
    }

    // 4. Fetch Shipments
    try {
      const shipmentData = await apiCall('/api/shipments', 'GET', null, true);
      if (shipmentData && Array.isArray(shipmentData)) {
        const mappedShipments = shipmentData.map(s => {
          const milestones = [
            { title: 'Shipment Assigned', time: 'Just now', description: 'Assigned to EcoTrans Logistics Ltd' },
            { title: 'Cargo Loaded & Inspected', time: s.status === 'PICKED_UP' || s.status === 'DELIVERED' ? 'Recently' : null, description: 'Pickup check from seller site' },
            { title: 'In Transit', time: s.status === 'PICKED_UP' || s.status === 'DELIVERED' ? 'Recently' : null, description: 'Cargo on highway route' },
            { title: 'Out for Delivery', time: s.status === 'DELIVERED' ? 'Recently' : null, description: 'Arriving at buyer storage terminal' },
            { title: 'Delivered', time: s.status === 'DELIVERED' ? 'Recently' : null, description: 'Delivered and signed off' }
          ];

          let currentMilestone = 0;
          if (s.status === 'ASSIGNED') currentMilestone = 0;
          else if (s.status === 'PICKED_UP') currentMilestone = 2;
          else if (s.status === 'DELIVERED') currentMilestone = 4;

          return {
            id: `shp-${s.id}`,
            orderId: `ord-${s.orderId}`,
            listingTitle: s.listingTitle || `Order #${s.orderId} Delivery`,
            sellerName: s.sellerName || 'Seller',
            buyerName: s.buyerName || 'Buyer',
            origin: 'Detroit, MI',
            destination: 'Chicago, IL',
            logisticsPartner: s.partnerId ? `Partner #${s.partnerId}` : 'EcoTrans Logistics Ltd',
            carrierStatus: s.status.toLowerCase(),
            currentMilestone,
            milestones,
            updatedAt: 'Recently'
          };
        });
        setShipments(mappedShipments);
      }
    } catch (err) {
      console.warn("Could not fetch shipments", err);
    }

    // 5. Fetch Notifications
    try {
      const user = JSON.parse(localStorage.getItem('user'));
      let notifData;
      if (user.role === 'PLATFORM_ADMIN') {
        notifData = await apiCall('/api/notifications', 'GET', null, true);
      } else {
        notifData = await apiCall(`/api/notifications/user/${user.userId}`, 'GET', null, true);
      }
      if (notifData && Array.isArray(notifData)) {
        const mappedNotifs = notifData.map(n => ({
          id: `not-${n.id}`,
          userId: n.userId === 1 ? 'admin' : `org-${n.userId}`,
          title: n.title,
          message: n.message,
          read: n.status === 'READ',
          type: n.title.toLowerCase().includes('offer') ? 'offer' : n.title.toLowerCase().includes('shipment') ? 'shipping' : 'admin',
          time: 'Recently'
        }));
        setNotifications(mappedNotifs);
      }
    } catch (err) {
      console.warn("Could not fetch notifications", err);
    }

    // 6. Fetch Organizations (if Admin)
    try {
      const user = JSON.parse(localStorage.getItem('user'));
      if (user && user.role === 'PLATFORM_ADMIN') {
        const orgData = await apiCall('/api/organizations', 'GET', null, true);
        if (orgData && Array.isArray(orgData)) {
          const mappedOrgs = orgData.map(o => ({
            id: `org-${o.id}`,
            name: o.name,
            type: o.industryType,
            wasteFocus: ['Steel Scrap', 'Plastic Scrap'],
            status: o.verificationStatus.toLowerCase(),
            docUrl: 'uploaded-doc.pdf',
            registeredAt: o.createdAt ? o.createdAt.split('T')[0] : new Date().toISOString().split('T')[0]
          }));
          setOrganizations(mappedOrgs);
        }

        const auditData = await apiCall('/api/analytics/audits', 'GET', null, true);
        if (auditData && Array.isArray(auditData)) {
          const mappedAudits = auditData.map(a => ({
            id: `aud-${a.id}`,
            timestamp: a.timestamp ? a.timestamp.replace('T', ' ').substring(0, 19) : new Date().toISOString().replace('T', ' ').substring(0, 19),
            action: a.action,
            user: a.actorId ? `User #${a.actorId}` : 'System',
            details: JSON.stringify(a.payload || {})
          }));
          setAuditLogs(mappedAudits);
        }
      }
    } catch (err) {
      console.warn("Could not fetch admin data", err);
    }
  };

  // Check backend availability on boot & set interval
  useEffect(() => {
    const checkBackend = async () => {
      try {
        const response = await fetch('/api/search/categories');
        if (response.ok) {
          setBackendConnected(true);
          console.log("Connected to EcoExchange Backend Microservices via API Gateway!");
        } else {
          setBackendConnected(false);
        }
      } catch (err) {
        setBackendConnected(false);
      }
    };
    checkBackend();
  }, []);

  // Poll for new data if backend is connected
  useEffect(() => {
    if (backendConnected) {
      refreshData();
      const interval = setInterval(refreshData, 5000);
      return () => clearInterval(interval);
    }
  }, [backendConnected]);

  // Auth local storage reload
  useEffect(() => {
    const token = localStorage.getItem('token');
    const userJson = localStorage.getItem('user');
    if (token && userJson) {
      try {
        const parsedUser = JSON.parse(userJson);
        setCurrentUser(parsedUser);
        if (parsedUser.role === 'PLATFORM_ADMIN') {
          setCurrentRole('admin');
        } else {
          const activeOrgLocal = localStorage.getItem('activeOrg');
          if (activeOrgLocal) {
            const org = JSON.parse(activeOrgLocal);
            if (org.type === 'Seller') setCurrentRole('seller');
            else if (org.type === 'Buyer') setCurrentRole('buyer');
            else if (org.type === 'Logistics') setCurrentRole('logistics');
          }
        }
      } catch (e) {
        console.error("Failed to restore user from storage", e);
      }
    }
  }, []);

  // Login handler
  const loginUser = async (email, password) => {
    if (!backendConnected) {
      // Offline fallback: simulate credentials
      if (email.includes('admin')) {
        setCurrentRole('admin');
      } else if (email.includes('buyer')) {
        setCurrentRole('buyer');
      } else if (email.includes('logistics')) {
        setCurrentRole('logistics');
      } else {
        setCurrentRole('seller');
      }
      setCurrentPage('dashboard');
      return true;
    }
    
    try {
      const data = await apiCall('/api/auth/login', 'POST', { email, password }, false);
      if (data && data.token) {
        localStorage.setItem('token', data.token);
        localStorage.setItem('user', JSON.stringify(data));
        setCurrentUser(data);
        
        if (data.organizationId) {
          try {
            const org = await apiCall(`/api/organizations/${data.organizationId}`, 'GET', null, true);
            if (org) {
              const formattedOrg = {
                id: `org-${org.id}`,
                name: org.name,
                type: org.industryType
              };
              localStorage.setItem('activeOrg', JSON.stringify(formattedOrg));
              
              if (org.industryType === 'Seller') setCurrentRole('seller');
              else if (org.industryType === 'Buyer') setCurrentRole('buyer');
              else if (org.industryType === 'Logistics') setCurrentRole('logistics');
            }
          } catch (orgErr) {
            console.error("Failed to fetch org details", orgErr);
          }
        } else if (data.role === 'PLATFORM_ADMIN') {
          setCurrentRole('admin');
        }
        
        setCurrentPage('dashboard');
        await refreshData();
        return true;
      }
    } catch (err) {
      console.error("Backend login failed", err);
      alert("Login failed: " + err.message);
      throw err;
    }
  };

  const logoutUser = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('activeOrg');
    setCurrentUser(null);
    setCurrentPage('landing');
  };

  // Add Log helper
  const triggerAuditLog = (action, details, userOverride = null) => {
    const user = userOverride || getActiveOrg().name;
    const now = new Date();
    const timestamp = now.toISOString().replace('T', ' ').substring(0, 19);
    const newLog = {
      id: `aud-${Math.floor(Math.random() * 90000) + 10000}`,
      timestamp,
      action,
      user,
      details
    };
    setAuditLogs(prev => [newLog, ...prev]);
  };

  // Notification Helper
  const triggerNotification = (userId, title, message, type) => {
    const newNotif = {
      id: `not-${Math.floor(Math.random() * 90000) + 10000}`,
      userId,
      title,
      message,
      read: false,
      type,
      time: 'Just now'
    };
    setNotifications(prev => [newNotif, ...prev]);
  };

  // 1. Seller Actions
  const registerOrganization = async (orgForm) => {
    if (backendConnected) {
      try {
        const payload = {
          name: orgForm.name,
          industryType: orgForm.type,
          gstNumber: orgForm.gstNumber || `GST-${Math.floor(100000 + Math.random() * 900000)}`,
          city: orgForm.city || 'Detroit',
          state: orgForm.state || 'MI',
          country: orgForm.country || 'USA',
          contactName: orgForm.contactName || 'Manager',
          email: orgForm.email || `${orgForm.name.toLowerCase().replace(/[^a-z0-9]/g, '')}@company.com`,
          password: orgForm.password || 'Password123!'
        };
        await apiCall('/api/auth/register', 'POST', payload, false);
        await refreshData();
      } catch (err) {
        console.error("Backend registration failed", err);
        throw err;
      }
    } else {
      const newOrg = {
        id: `org-${organizations.length + 1}`,
        name: orgForm.name,
        type: orgForm.type,
        wasteFocus: orgForm.wasteFocus || [],
        status: 'pending',
        docUrl: orgForm.docUrl || 'uploaded-doc.pdf',
        registeredAt: new Date().toISOString().split('T')[0]
      };
      setOrganizations(prev => [...prev, newOrg]);
      triggerAuditLog('ORGANIZATION_REGISTER', `Organization ${newOrg.name} (${newOrg.type}) registered. Status: PENDING validation.`, newOrg.name);
      triggerNotification('admin', 'New Organization Review', `${newOrg.name} has requested seller verification.`, 'admin');
    }
  };

  const createListing = async (listingForm) => {
    if (backendConnected) {
      try {
        const categoriesMap = {
          'Steel Scrap': 1,
          'Fly Ash': 2,
          'Plastic Scrap': 3,
          'Textile Waste': 4,
          'Wood Waste': 5,
          'Glass Waste': 6
        };
        const categoryId = categoriesMap[listingForm.category] || 1;
        
        const payload = {
          categoryId,
          title: listingForm.title,
          description: listingForm.description,
          quantity: parseFloat(listingForm.quantity),
          unit: listingForm.unit || 'Tons',
          pricePerUnit: parseFloat(listingForm.price),
          location: listingForm.location || 'Detroit, MI',
          imageUrls: [listingForm.image || 'https://images.unsplash.com/photo-1533090161767-e6ffed986c88?auto=format&fit=crop&w=600&q=80'],
          certificates: [
            {
              certificateName: listingForm.certificateName || 'Standard Analysis Sheet',
              certificateUrl: listingForm.certificateUrl || 'quality-cert.pdf'
            }
          ]
        };
        await apiCall('/api/listings', 'POST', payload, true);
        await refreshData();
      } catch (err) {
        console.error("Backend listing create failed", err);
        alert("Failed to create listing: " + err.message);
      }
    } else {
      const activeOrg = getActiveOrg();
      const newListing = {
        id: `list-${listings.length + 1}`,
        title: listingForm.title,
        description: listingForm.description,
        category: listingForm.category,
        quantity: parseFloat(listingForm.quantity),
        unit: listingForm.unit || 'Tons',
        price: parseFloat(listingForm.price),
        sellerId: activeOrg.id,
        sellerName: activeOrg.name,
        location: listingForm.location || 'Detroit, MI',
        image: listingForm.image || 'https://images.unsplash.com/photo-1533090161767-e6ffed986c88?auto=format&fit=crop&w=600&q=80',
        certificateUrl: listingForm.certificateUrl || 'quality-cert.pdf',
        certificateName: listingForm.certificateName || 'Standard Analysis Sheet',
        status: 'active',
        createdAt: new Date().toISOString().split('T')[0]
      };
      setListings(prev => [newListing, ...prev]);
      triggerAuditLog('LISTING_CREATE', `Created listing ${newListing.id}: "${newListing.title}" at $${newListing.price}/ton.`);
    }
  };

  const deleteListing = async (id) => {
    if (backendConnected) {
      try {
        const cleanId = id.replace('list-', '');
        await apiCall(`/api/listings/${cleanId}`, 'DELETE', null, true);
        await refreshData();
      } catch (err) {
        console.error("Backend listing delete failed", err);
      }
    } else {
      setListings(prev => prev.map(l => l.id === id ? { ...l, status: 'deactivated' } : l));
      triggerAuditLog('LISTING_DELETE', `Deactivated listing ${id}.`);
    }
  };

  const handleOfferDecision = async (offerId, approve) => {
    if (backendConnected) {
      try {
        const cleanOfferId = offerId.replace('off-', '');
        const endpoint = `/api/offers/${cleanOfferId}/${approve ? 'accept' : 'reject'}`;
        await apiCall(endpoint, 'PUT', null, true);
        
        // Fetch matching order details & create shipment if approved
        if (approve) {
          setTimeout(async () => {
            await refreshData();
            const orderForOffer = orders.find(o => o.offerId === offerId);
            if (!orderForOffer) {
              const offer = offers.find(o => o.id === offerId);
              if (offer) {
                try {
                  const user = JSON.parse(localStorage.getItem('user'));
                  const orderPayload = {
                    listingId: parseInt(offer.listingId.replace('list-', '')),
                    buyerOrgId: parseInt(offer.buyerId.replace('org-', '')),
                    sellerOrgId: user.organizationId,
                    offerId: parseInt(cleanOfferId),
                    totalAmount: offer.offeredPrice * offer.quantity
                  };
                  const orderResponse = await apiCall('/api/orders', 'POST', orderPayload, true);
                  if (orderResponse && orderResponse.id) {
                    await apiCall(`/api/orders/${orderResponse.id}/shipment`, 'POST', { partnerId: 1, trackingNumber: `TRK-${Math.floor(Math.random() * 900000)}` }, true);
                  }
                } catch (orderErr) {
                  console.warn("Failed REST order fallback creation", orderErr);
                }
              }
            }
          }, 1000);
        }
        await refreshData();
      } catch (err) {
        console.error("Backend offer decision failed", err);
      }
    } else {
      const decision = approve ? 'accepted' : 'rejected';
      setOffers(prev => prev.map(o => o.id === offerId ? { ...o, status: decision } : o));
      const offer = offers.find(o => o.id === offerId);
      if (!offer) return;

      triggerAuditLog('OFFER_DECISION', `Offer ${offerId} on listing "${offer.listingTitle}" was ${decision.toUpperCase()}.`);
      triggerNotification(offer.buyerId, `Offer ${approve ? 'Accepted' : 'Rejected'}`, `Your bid for ${offer.listingTitle} has been ${decision}.`, 'offer');

      if (approve) {
        setListings(prev => prev.map(l => l.id === offer.listingId ? { ...l, status: 'sold' } : l));

        const newOrder = {
          id: `ord-${orders.length + 1}`,
          offerId: offer.id,
          listingId: offer.listingId,
          listingTitle: offer.listingTitle,
          sellerId: offer.sellerId,
          sellerName: offer.sellerName,
          buyerId: offer.buyerId,
          buyerName: offer.buyerName,
          finalPrice: offer.offeredPrice,
          quantity: offer.quantity,
          totalCost: offer.offeredPrice * offer.quantity,
          status: 'processing',
          certificateUrl: 'quality-cert.pdf',
          certificateName: 'Quality Conformity Statement',
          orderedAt: new Date().toISOString().split('T')[0]
        };
        setOrders(prev => [newOrder, ...prev]);
        triggerAuditLog('ORDER_CREATE', `Order ${newOrder.id} created from accepted offer ${offerId}.`);

        const newShipment = {
          id: `shp-${shipments.length + 1}`,
          orderId: newOrder.id,
          listingTitle: offer.listingTitle,
          sellerName: offer.sellerName,
          buyerName: offer.buyerName,
          origin: 'Detroit, MI',
          destination: 'Chicago, IL',
          logisticsPartner: 'EcoTrans Logistics Ltd',
          carrierStatus: 'pending',
          currentMilestone: 0,
          milestones: [
            { title: 'Shipment Assigned', time: new Date().toISOString().substring(0, 16).replace('T', ' '), description: 'Assigned to EcoTrans Logistics Ltd' },
            { title: 'Cargo Loaded & Inspected', time: null, description: 'Pickup check from seller site' },
            { title: 'In Transit', time: null, description: 'Cargo on highway route' },
            { title: 'Out for Delivery', time: null, description: 'Arriving at buyer storage terminal' },
            { title: 'Delivered', time: null, description: 'Delivered and signed off' }
          ],
          updatedAt: new Date().toISOString().substring(0, 16).replace('T', ' ')
        };
        setShipments(prev => [newShipment, ...prev]);
        triggerAuditLog('SHIPMENT_CREATE', `Shipment ${newShipment.id} initialized for Order ${newOrder.id}. Assigned to EcoTrans.`);
        triggerNotification('org-log', 'New Shipment Assigned', `Shipment ${newShipment.id} is ready to be claimed.`, 'shipping');
      }
    }
  };

  // 2. Buyer Actions
  const placeOffer = async (offerForm) => {
    if (backendConnected) {
      try {
        const cleanListingId = offerForm.listingId.replace('list-', '');
        const payload = {
          quantity: parseFloat(offerForm.quantity),
          offeredPrice: parseFloat(offerForm.offeredPrice)
        };
        await apiCall(`/api/listings/${cleanListingId}/offers`, 'POST', payload, true);
        await refreshData();
      } catch (err) {
        console.error("Backend place offer failed", err);
        alert("Failed to place offer: " + err.message);
      }
    } else {
      const activeOrg = getActiveOrg();
      const listing = listings.find(l => l.id === offerForm.listingId);
      if (!listing) return;

      const newOffer = {
        id: `off-${offers.length + 1}`,
        listingId: offerForm.listingId,
        listingTitle: listing.title,
        sellerId: listing.sellerId,
        sellerName: listing.sellerName,
        buyerId: activeOrg.id,
        buyerName: activeOrg.name,
        offeredPrice: parseFloat(offerForm.offeredPrice),
        quantity: parseFloat(offerForm.quantity),
        status: 'pending',
        deliveryDate: offerForm.deliveryDate || '2026-07-15',
        createdAt: new Date().toISOString().split('T')[0]
      };
      setOffers(prev => [newOffer, ...prev]);
      triggerAuditLog('OFFER_PLACE', `Placed offer ${newOffer.id} on "${listing.title}" for $${newOffer.offeredPrice}/ton.`);
      triggerNotification(listing.sellerId, 'New Offer Placed', `${activeOrg.name} placed a bid on "${listing.title}".`, 'offer');
    }
  };

  const buyDirectly = async (listingId, quantityNeeded) => {
    if (backendConnected) {
      try {
        const cleanListingId = listingId.replace('list-', '');
        const listing = listings.find(l => l.id === listingId);
        if (!listing) return;

        // 1. Create immediate offer
        const payload = {
          quantity: parseFloat(quantityNeeded),
          offeredPrice: parseFloat(listing.price)
        };
        const offerResponse = await apiCall(`/api/listings/${cleanListingId}/offers`, 'POST', payload, true);
        
        if (offerResponse && offerResponse.id) {
          // 2. Accept offer immediately since it is direct buy
          try {
            await apiCall(`/api/offers/${offerResponse.id}/accept`, 'PUT', null, true);
          } catch (acceptErr) {
            console.warn("Could not auto-accept offer in backend", acceptErr);
          }

          // 3. Create order in order-service
          const user = JSON.parse(localStorage.getItem('user'));
          const orderPayload = {
            listingId: parseInt(cleanListingId),
            buyerOrgId: user.organizationId,
            sellerOrgId: parseInt(listing.sellerId.replace('org-', '')),
            offerId: offerResponse.id,
            totalAmount: parseFloat(listing.price) * parseFloat(quantityNeeded)
          };
          const orderResponse = await apiCall('/api/orders', 'POST', orderPayload, true);

          if (orderResponse && orderResponse.id) {
            // 4. Create shipment
            const shipmentPayload = {
              partnerId: 1, // EcoTrans default
              trackingNumber: `TRK-${Math.floor(100000 + Math.random() * 900000)}`
            };
            await apiCall(`/api/orders/${orderResponse.id}/shipment`, 'POST', shipmentPayload, true);
          }
        }
        await refreshData();
      } catch (err) {
        console.error("Backend direct buy failed", err);
        alert("Failed direct buy: " + err.message);
      }
    } else {
      const activeOrg = getActiveOrg();
      const listing = listings.find(l => l.id === listingId);
      if (!listing) return;

      const newOffer = {
        id: `off-${offers.length + 1}`,
        listingId,
        listingTitle: listing.title,
        sellerId: listing.sellerId,
        sellerName: listing.sellerName,
        buyerId: activeOrg.id,
        buyerName: activeOrg.name,
        offeredPrice: listing.price,
        quantity: quantityNeeded,
        status: 'accepted',
        deliveryDate: new Date(Date.now() + 10 * 86400000).toISOString().split('T')[0],
        createdAt: new Date().toISOString().split('T')[0]
      };

      setOffers(prev => [newOffer, ...prev]);
      setListings(prev => prev.map(l => l.id === listingId ? { ...l, status: 'sold' } : l));

      const newOrder = {
        id: `ord-${orders.length + 1}`,
        offerId: newOffer.id,
        listingId: listingId,
        listingTitle: listing.title,
        sellerId: listing.sellerId,
        sellerName: listing.sellerName,
        buyerId: activeOrg.id,
        buyerName: activeOrg.name,
        finalPrice: listing.price,
        quantity: quantityNeeded,
        totalCost: listing.price * quantityNeeded,
        status: 'processing',
        certificateUrl: listing.certificateUrl,
        certificateName: listing.certificateName,
        orderedAt: new Date().toISOString().split('T')[0]
      };
      setOrders(prev => [newOrder, ...prev]);

      const newShipment = {
        id: `shp-${shipments.length + 1}`,
        orderId: newOrder.id,
        listingTitle: listing.title,
        sellerName: listing.sellerName,
        buyerName: activeOrg.name,
        origin: listing.location,
        destination: 'Cleveland, OH',
        logisticsPartner: 'EcoTrans Logistics Ltd',
        carrierStatus: 'pending',
        currentMilestone: 0,
        milestones: [
          { title: 'Shipment Assigned', time: new Date().toISOString().substring(0, 16).replace('T', ' '), description: 'Assigned to EcoTrans Logistics Ltd' },
          { title: 'Cargo Loaded & Inspected', time: null, description: 'Pickup check from seller site' },
          { title: 'In Transit', time: null, description: 'Cargo on highway route' },
          { title: 'Out for Delivery', time: null, description: 'Arriving at buyer storage terminal' },
          { title: 'Delivered', time: null, description: 'Delivered and signed off' }
        ],
        updatedAt: new Date().toISOString().substring(0, 16).replace('T', ' ')
      };
      setShipments(prev => [newShipment, ...prev]);

      triggerAuditLog('DIRECT_PURCHASE', `Direct purchase on "${listing.title}" for $${newOrder.totalCost}. Order ${newOrder.id} and Shipment ${newShipment.id} created.`);
      triggerNotification(listing.sellerId, 'Material Sold Directly', `Your material "${listing.title}" has been purchased directly by ${activeOrg.name}.`, 'offer');
      triggerNotification('org-log', 'New Shipment Assigned', `Shipment ${newShipment.id} is ready for pick up.`, 'shipping');
    }
  };

  // 3. Logistics Actions
  const updateShipment = async (shipmentId, milestoneIndex) => {
    if (backendConnected) {
      try {
        const cleanShipmentId = shipmentId.replace('shp-', '');
        let endpoint = `/api/shipments/${cleanShipmentId}/pickup`;
        if (milestoneIndex === 4) {
          endpoint = `/api/shipments/${cleanShipmentId}/deliver`;
        }
        await apiCall(endpoint, 'PUT', null, true);
        await refreshData();
      } catch (err) {
        console.error("Backend shipment update failed", err);
      }
    } else {
      const nowStr = new Date().toISOString().substring(0, 16).replace('T', ' ');
      setShipments(prev => prev.map(s => {
        if (s.id === shipmentId) {
          const newMilestones = s.milestones.map((m, idx) => {
            if (idx === milestoneIndex) {
              return { ...m, time: nowStr };
            }
            return m;
          });

          let carrierStatus = 'accepted';
          let orderStatus = 'processing';

          if (milestoneIndex === 1) {
            carrierStatus = 'picked_up';
            orderStatus = 'in_transit';
          } else if (milestoneIndex === 2) {
            carrierStatus = 'in_transit';
            orderStatus = 'in_transit';
          } else if (milestoneIndex === 3) {
            carrierStatus = 'out_for_delivery';
            orderStatus = 'in_transit';
          } else if (milestoneIndex === 4) {
            carrierStatus = 'delivered';
            orderStatus = 'delivered';
          }

          setOrders(oPrev => oPrev.map(o => o.id === s.orderId ? { ...o, status: orderStatus } : o));
          triggerAuditLog('SHIPMENT_UPDATE', `Shipment ${shipmentId} updated to milestone: "${s.milestones[milestoneIndex].title}".`);
          
          const order = orders.find(o => o.id === s.orderId);
          if (order) {
            triggerNotification(order.buyerId, `Shipment Status: ${s.milestones[milestoneIndex].title}`, `Shipment for order ${order.id} is now: ${s.milestones[milestoneIndex].title}`, 'shipping');
            triggerNotification(order.sellerId, `Shipment Status: ${s.milestones[milestoneIndex].title}`, `Shipment for order ${order.id} is now: ${s.milestones[milestoneIndex].title}`, 'shipping');
          }

          return {
            ...s,
            carrierStatus,
            currentMilestone: milestoneIndex,
            milestones: newMilestones,
            updatedAt: nowStr
          };
        }
        return s;
      }));
    }
  };

  const acceptShipment = async (shipmentId) => {
    if (backendConnected) {
      try {
        const cleanShipmentId = shipmentId.replace('shp-', '');
        const user = JSON.parse(localStorage.getItem('user'));
        await apiCall(`/api/shipments/${cleanShipmentId}/assign`, 'PUT', { partnerId: user.organizationId || 1 }, true);
        await refreshData();
      } catch (err) {
        console.error("Backend shipment accept failed", err);
      }
    } else {
      setShipments(prev => prev.map(s => {
        if (s.id === shipmentId) {
          triggerAuditLog('SHIPMENT_ACCEPT', `EcoTrans claimed and accepted transport of shipment ${shipmentId}.`);
          return { ...s, carrierStatus: 'accepted' };
        }
        return s;
      }));
    }
  };

  // 4. Admin Actions
  const moderateListing = (listingId, status) => {
    setListings(prev => prev.map(l => l.id === listingId ? { ...l, status } : l));
    triggerAuditLog('LISTING_MODERATE', `Admin set status of listing ${listingId} to "${status.toUpperCase()}".`);
  };

  const verifyOrganization = async (orgId, approved) => {
    if (backendConnected) {
      try {
        const cleanOrgId = orgId.replace('org-', '');
        const action = approved ? 'verify' : 'suspend';
        await apiCall(`/api/organizations/${cleanOrgId}/${action}`, 'PUT', null, true);
        await refreshData();
      } catch (err) {
        console.error("Backend organization verification update failed", err);
      }
    } else {
      const status = approved ? 'verified' : 'suspended';
      setOrganizations(prev => prev.map(o => o.id === orgId ? { ...o, status } : o));
      const org = organizations.find(o => o.id === orgId);
      if (org) {
        triggerAuditLog('ORGANIZATION_VERIFY', `Admin set organization ${org.name} status to ${status.toUpperCase()}.`);
        triggerNotification(org.id, 'Account Verification Update', `Your organization status is updated to: ${status.toUpperCase()}`, 'admin');
      }
    }
  };

  const markAllNotificationsRead = async () => {
    if (backendConnected) {
      try {
        const unread = notifications.filter(n => !n.read);
        for (const notif of unread) {
          const cleanId = notif.id.replace('not-', '');
          await apiCall(`/api/notifications/${cleanId}/read`, 'PUT', null, true);
        }
        await refreshData();
      } catch (err) {
        console.error("Backend read notifications update failed", err);
      }
    } else {
      const activeOrg = getActiveOrg();
      const userId = currentRole === 'admin' ? 'admin' : activeOrg.id;
      setNotifications(prev => prev.map(n => n.userId === userId ? { ...n, read: true } : n));
    }
  };

  // Sync role shifts with route redirections to avoid dead ends
  useEffect(() => {
    if (currentPage === 'landing' || currentPage === 'login' || currentPage === 'register') {
      // Allow general authentication pages
    } else {
      // Set default landing dashboards on role switch
      setCurrentPage('dashboard');
    }
  }, [currentRole]);

  return (
    <AppContext.Provider value={{
      organizations,
      listings,
      offers,
      orders,
      shipments,
      notifications,
      auditLogs,
      currentRole,
      setCurrentRole,
      currentPage,
      setCurrentPage,
      selectedListingId,
      setSelectedListingId,
      selectedShipmentId,
      setSelectedShipmentId,
      activeOrg: getActiveOrg(),
      registerOrganization,
      createListing,
      deleteListing,
      handleOfferDecision,
      placeOffer,
      buyDirectly,
      updateShipment,
      acceptShipment,
      moderateListing,
      verifyOrganization,
      markAllNotificationsRead,
      triggerAuditLog,
      // Backend status variables
      backendConnected,
      loginUser,
      logoutUser,
      currentUser
    }}>
      {children}
    </AppContext.Provider>
  );
};

export const useApp = () => {
  const context = useContext(AppContext);
  if (!context) throw new Error('useApp must be used within an AppProvider');
  return context;
};
