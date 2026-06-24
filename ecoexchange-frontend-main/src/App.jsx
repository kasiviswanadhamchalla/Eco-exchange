import React, { useState } from 'react';
import { AppProvider, useApp } from './context/AppContext';
import { X } from 'lucide-react';
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
  const { currentPage, activeTour, tourStepIndex, nextTourStep, prevTourStep, skipTour } = useApp();
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
    <div className="min-h-screen bg-[#070B14] flex relative">
      {/* Collapsible Sidebar */}
      <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} />

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col min-h-screen lg:pl-64 overflow-x-hidden">
        <Header setSidebarOpen={setSidebarOpen} />
        
        <main className="flex-1 p-6 lg:p-8 max-w-7xl w-full mx-auto">
          {renderActivePage()}
        </main>
      </div>

      {/* Global Interactive Walkthrough Tour Guide Modal */}
      {activeTour && (
        <div className="fixed bottom-6 right-6 left-6 sm:left-auto z-[999] sm:w-[440px] md:w-[480px] p-6 glass-card border border-emerald-500/40 hover:border-emerald-500/80 rounded-2xl shadow-2xl shadow-emerald-500/10 hover:shadow-emerald-500/20 transition-all duration-300 animate-fade-in-up group/tour">
          <div className="flex justify-between items-center">
            <div className="flex items-center gap-2.5 text-emerald-400">
              <div className="w-6 h-6 rounded-full bg-emerald-500/20 flex items-center justify-center text-xs font-bold animate-pulse">
                i
              </div>
              <span className="text-xs font-bold uppercase tracking-widest">
                {activeTour.length > 1 ? "Interactive Walkthrough" : "Notification Details"}
              </span>
            </div>
            <button
              onClick={skipTour}
              className="text-slate-500 hover:text-emerald-400 hover:bg-slate-900/60 p-1.5 rounded-lg hover:rotate-90 transition-all duration-300 cursor-pointer"
            >
              <X className="w-4 h-4" />
            </button>
          </div>

          <div className="mt-4 space-y-2 text-left">
            {activeTour.length > 1 && (
              <div className="text-xs font-bold text-slate-500 uppercase tracking-wide">
                Step {tourStepIndex + 1} of {activeTour.length}
              </div>
            )}
            <h4 className="text-base sm:text-lg font-extrabold text-slate-100 group-hover/tour:text-emerald-400 transition-colors duration-300">
              {activeTour[tourStepIndex].title}
            </h4>
            <p className="text-sm sm:text-base text-slate-350 leading-relaxed mt-2 font-medium">
              {activeTour[tourStepIndex].content}
            </p>
          </div>

          <div className="flex items-center justify-between mt-6 pt-4 border-t border-slate-850">
            {activeTour.length > 1 ? (
              <button
                onClick={skipTour}
                className="text-xs sm:text-sm font-bold text-slate-500 hover:text-slate-350 hover:underline transition-all duration-200 cursor-pointer"
              >
                Skip Tour
              </button>
            ) : (
              <div />
            )}

            <div className="flex gap-2">
              {tourStepIndex > 0 && (
                <button
                  onClick={prevTourStep}
                  className="px-3 py-1.5 rounded border border-slate-800 text-xs sm:text-sm font-bold text-slate-400 hover:text-white hover:bg-slate-900/60 hover:border-slate-700 hover:scale-105 active:scale-95 transition-all duration-200 cursor-pointer"
                >
                  Back
                </button>
              )}

              <button
                onClick={tourStepIndex === activeTour.length - 1 ? skipTour : nextTourStep}
                className="px-4 py-2 rounded bg-emerald-600 border border-emerald-500 text-xs sm:text-sm font-bold text-white hover:bg-emerald-500 hover:shadow-lg hover:shadow-emerald-500/25 hover:scale-105 active:scale-95 transition-all duration-300 cursor-pointer"
              >
                {tourStepIndex === activeTour.length - 1 ? (activeTour.length > 1 ? "Finish Tour" : "Got It") : "Next Step"}
              </button>
            </div>
          </div>
        </div>
      )}
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
