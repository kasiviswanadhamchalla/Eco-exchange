import React, { useState } from 'react';
import { AppProvider, useApp } from './context/AppContext';
import { Sidebar } from './components/shared/Sidebar';
import { Header } from './components/shared/Header';
import { LandingPage } from './pages/LandingPage';
import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { Dashboard } from './pages/Dashboard';
import { Marketplace } from './pages/Marketplace';
import { ListingDetails } from './pages/ListingDetails';
import { CreateListing } from './pages/CreateListing';
import { ManageListings } from './pages/ManageListings';
import { OffersReceived } from './pages/OffersReceived';
import { OffersSent } from './pages/OffersSent';
import { Orders } from './pages/Orders';
import { ShipmentTracking } from './pages/ShipmentTracking';
import { Notifications } from './pages/Notifications';
import { Analytics } from './pages/Analytics';
import { OrgVerification } from './pages/OrgVerification';
import { AuditViewer } from './pages/AuditViewer';

function AppContent() {
  const { currentPage } = useApp();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  // Full screen layout pages
  const isFullScreenPage = ['landing', 'login', 'register'].includes(currentPage);

  const renderActivePage = () => {
    switch (currentPage) {
      case 'landing': return <LandingPage />;
      case 'login': return <Login />;
      case 'register': return <Register />;
      case 'dashboard': return <Dashboard />;
      case 'marketplace': return <Marketplace />;
      case 'listing-details': return <ListingDetails />;
      case 'create-listing': return <CreateListing />;
      case 'manage-listings': return <ManageListings />;
      case 'offers-received': return <OffersReceived />;
      case 'offers-sent': return <OffersSent />;
      case 'orders': return <Orders />;
      case 'shipment-tracking': return <ShipmentTracking />;
      case 'notifications': return <Notifications />;
      case 'analytics': return <Analytics />;
      case 'org-verification': return <OrgVerification />;
      case 'audit-viewer': return <AuditViewer />;
      default: return <LandingPage />;
    }
  };

  if (isFullScreenPage) {
    return (
      <main className="w-full min-h-screen bg-[#070B14]">
        {renderActivePage()}
      </main>
    );
  }

  return (
    <div className="min-h-screen bg-[#070B14] flex">
      {/* Collapsible Sidebar */}
      <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} />

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col min-h-screen lg:pl-64 overflow-x-hidden">
        <Header setSidebarOpen={setSidebarOpen} />
        
        <main className="flex-1 p-6 lg:p-8 max-w-7xl w-full mx-auto">
          {renderActivePage()}
        </main>
      </div>
    </div>
  );
}

function App() {
  return (
    <AppProvider>
      <AppContent />
    </AppProvider>
  );
}

export default App;
