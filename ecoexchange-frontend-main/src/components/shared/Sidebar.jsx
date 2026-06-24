import React from 'react';
import { useApp } from '../../context/AppContext';
import {
  LayoutDashboard,
  ShoppingBag,
  PlusCircle,
  List,
  Inbox,
  Send,
  FileText,
  Truck,
  Bell,
  BarChart3,
  Shield,
  FileClock,
  Building,
  Home
} from 'lucide-react';

export const Sidebar = ({ sidebarOpen, setSidebarOpen }) => {
  const { currentRole, setCurrentRole, currentPage, setCurrentPage, notifications, activeOrg } = useApp();

  const unreadNotificationsCount = notifications.filter(
    n => (currentRole === 'admin' ? n.userId === 'admin' : n.userId === activeOrg.id) && !n.read
  ).length;

  // Base navigation configuration for roles
  const getNavItems = () => {
    switch (currentRole) {
      case 'seller':
        return [
          { name: 'Dashboard', page: 'dashboard', icon: LayoutDashboard },
          { name: 'Marketplace', page: 'marketplace', icon: ShoppingBag },
          { name: 'Create Listing', page: 'create-listing', icon: PlusCircle },
          { name: 'Manage Listings', page: 'manage-listings', icon: List },
          { name: 'Offers Received', page: 'offers-received', icon: Inbox },
          { name: 'Orders', page: 'orders', icon: FileText },
          { name: 'Analytics', page: 'analytics', icon: BarChart3 },
          { name: 'Notifications', page: 'notifications', icon: Bell, badge: unreadNotificationsCount },
        ];
      case 'buyer':
        return [
          { name: 'Dashboard', page: 'dashboard', icon: LayoutDashboard },
          { name: 'Marketplace', page: 'marketplace', icon: ShoppingBag },
          { name: 'Offers Sent', page: 'offers-sent', icon: Send },
          { name: 'Orders', page: 'orders', icon: FileText },
          { name: 'Track Shipments', page: 'shipment-tracking', icon: Truck },
          { name: 'Analytics', page: 'analytics', icon: BarChart3 },
          { name: 'Notifications', page: 'notifications', icon: Bell, badge: unreadNotificationsCount },
        ];
      case 'logistics':
        return [
          { name: 'Dashboard', page: 'dashboard', icon: LayoutDashboard },
          { name: 'Shipments Queue', page: 'shipment-tracking', icon: Truck },
          { name: 'System Notifications', page: 'notifications', icon: Bell, badge: unreadNotificationsCount },
        ];
      case 'admin':
        return [
          { name: 'Dashboard', page: 'dashboard', icon: LayoutDashboard },
          { name: 'Marketplace Vetting', page: 'marketplace', icon: ShoppingBag },
          { name: 'Verify Organizations', page: 'org-verification', icon: Building },
          { name: 'Security Audit logs', page: 'audit-viewer', icon: FileClock },
          { name: 'System Notifications', page: 'notifications', icon: Bell, badge: unreadNotificationsCount },
        ];
      default:
        return [];
    }
  };

  const navItems = getNavItems();

  const handleNavClick = (page) => {
    setCurrentPage(page);
    setSidebarOpen(false); // Close sidebar on mobile
  };

  return (
    <>
      {/* Mobile sidebar backdrop */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 z-40 bg-slate-950/60 backdrop-blur-sm lg:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      <aside
        className={`fixed top-0 bottom-0 left-0 z-45 w-64 glass-card border-r border-slate-800/80 flex flex-col justify-between transition-transform duration-300 ease-in-out lg:translate-x-0 ${
          sidebarOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <div className="flex flex-col h-full">
          {/* Sidebar Header Brand */}
          <div className="h-16 flex items-center px-6 border-b border-slate-800/80 gap-3">
            <div className="w-8 h-8 rounded-lg bg-emerald-500 flex items-center justify-center text-slate-950 font-bold shadow-md shadow-emerald-500/20">
              IC
            </div>
            <div>
              <span className="font-semibold text-slate-100 block leading-tight">IndustryConnect</span>
              <span className="text-[10px] text-emerald-400 uppercase tracking-widest font-bold">B2B Circular Economy</span>
            </div>
          </div>

          {/* Navigation Links */}
          <nav className="flex-1 px-4 py-6 space-y-1.5 overflow-y-auto">
            <button
              onClick={() => handleNavClick('landing')}
              className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm transition-all duration-200 ${
                currentPage === 'landing'
                  ? 'bg-slate-800 text-white font-medium shadow-inner'
                  : 'text-slate-400 hover:bg-slate-800/40 hover:text-slate-200'
              }`}
            >
              <Home className="w-4 h-4" />
              <span>Portal Landing</span>
            </button>

            <div className="h-px bg-slate-850 my-4" />

            <span className="px-4 text-[10px] font-bold text-slate-500 uppercase tracking-widest block mb-2">
              {currentRole} WORKSPACE
            </span>

            {navItems.map((item) => {
              const Icon = item.icon;
              const isActive = currentPage === item.page;
              return (
                <button
                  key={item.name}
                  onClick={() => handleNavClick(item.page)}
                  className={`w-full flex items-center justify-between px-4 py-3 rounded-lg text-sm transition-all duration-200 ${
                    isActive
                      ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 font-semibold text-glow-emerald'
                      : 'text-slate-400 hover:bg-slate-800/30 hover:text-slate-200 border border-transparent'
                  }`}
                >
                  <div className="flex items-center gap-3">
                    <Icon className={`w-4.5 h-4.5 ${isActive ? 'text-emerald-400' : 'text-slate-400'}`} />
                    <span>{item.name}</span>
                  </div>
                  {item.badge !== undefined && item.badge > 0 && (
                    <span className="bg-emerald-500 text-slate-950 font-bold text-[10px] px-2 py-0.5 rounded-full">
                      {item.badge}
                    </span>
                  )}
                </button>
              );
            })}
          </nav>
        </div>

        {/* Footer Identity + Role Switcher */}
        <div className="p-4 border-t border-slate-800/80 bg-slate-950/20 space-y-3">
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-emerald-600 flex items-center justify-center font-bold text-white text-sm shrink-0">
              {activeOrg.name[0]}
            </div>
            <div className="overflow-hidden flex-1">
              <span className="text-xs font-semibold text-slate-200 block truncate">{activeOrg.name}</span>
              <span className="text-[10px] text-slate-400 capitalize block">{currentRole} account</span>
            </div>
          </div>
          {/* Switch role */}
          <select
            value={currentRole}
            onChange={(e) => setCurrentRole(e.target.value)}
            className="w-full glass-input text-[10px] px-2.5 py-1.5 rounded-lg text-slate-300 font-semibold cursor-pointer"
          >
            <option value="seller">Switch to: Seller View</option>
            <option value="buyer">Switch to: Buyer View</option>
            <option value="logistics">Switch to: Logistics View</option>
            <option value="admin">Switch to: Admin View</option>
          </select>
        </div>
      </aside>
    </>
  );
};
