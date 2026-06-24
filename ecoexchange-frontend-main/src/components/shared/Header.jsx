import React, { useState } from 'react';
import { useApp } from '../../context/AppContext';
import { Menu, Bell, User, ChevronDown, Info } from 'lucide-react';
import { Badge } from '../ui/Badge';

export const Header = ({ setSidebarOpen }) => {
  const {
    currentRole,
    currentPage,
    setCurrentPage,
    notifications,
    activeOrg,
    markAllNotificationsRead,
    backendConnected,
    startTour
  } = useApp();

  const [notifOpen, setNotifOpen] = useState(false);
  const [profileOpen, setProfileOpen] = useState(false);

  const pageTitles = {
    'landing': 'Home',
    'login': 'Sign In',
    'register': 'Register Organization',
    'dashboard': 'Dashboard',
    'marketplace': 'Marketplace',
    'listing-details': 'Material Details',
    'create-listing': 'Create Listing',
    'manage-listings': 'My Listings',
    'offers-received': 'Offers Received',
    'offers-sent': 'Offers Sent',
    'orders': 'Orders',
    'shipment-tracking': 'Shipment Tracking',
    'notifications': 'Notifications',
    'analytics': 'Analytics',
    'org-verification': 'Organization Verification',
    'audit-viewer': 'Audit Logs',
  };

  const userNotifs = notifications.filter(
    n => (currentRole === 'admin' ? n.userId === 'admin' : n.userId === activeOrg.id)
  );
  const unreadCount = userNotifs.filter(n => !n.read).length;

  return (
    <header className="h-16 flex items-center justify-between px-5 border-b border-slate-800/80 glass-card sticky top-0 z-30">
      {/* Left: hamburger + page title */}
      <div className="flex items-center gap-3">
        <button
          onClick={() => setSidebarOpen(prev => !prev)}
          className="p-1.5 rounded-lg border border-slate-800 text-slate-400 hover:text-white hover:bg-slate-800/50 lg:hidden"
        >
          <Menu className="w-5 h-5" />
        </button>
        <div>
          <h1 className="text-base font-bold text-slate-100 m-0 leading-none">
            {pageTitles[currentPage] || 'IndustryConnect'}
          </h1>
          <p className="text-[10px] text-slate-500 mt-0.5 hidden sm:block">{activeOrg.name}</p>
        </div>
      </div>

      {/* Right: bell + user */}
      <div className="flex items-center gap-3">
        {/* Connection status badge */}
        <Badge
          variant={backendConnected ? 'success' : 'warning'}
          className="text-[10px] py-1 px-2.5 font-bold uppercase tracking-wider hidden sm:inline-flex"
        >
          {backendConnected ? 'Backend Connected' : 'Simulated Mode'}
        </Badge>

        {/* Notifications bell */}
        <div className="relative">
          <button
            onClick={() => { setNotifOpen(!notifOpen); setProfileOpen(false); }}
            className="relative p-2 rounded-lg border border-slate-800 hover:bg-slate-800/40 text-slate-400 hover:text-white transition-all"
          >
            <Bell className="w-4 h-4" />
            {unreadCount > 0 && (
              <span className="absolute -top-1 -right-1 w-4 h-4 bg-emerald-500 text-[8px] font-bold text-slate-950 rounded-full flex items-center justify-center border border-slate-950">
                {unreadCount}
              </span>
            )}
          </button>

          {notifOpen && (
            <div className="absolute right-0 mt-2 w-72 glass-card rounded-xl shadow-2xl py-2 z-50 border border-slate-800/80 animate-fade-in-up">
              <div className="px-4 py-2 border-b border-slate-800/80 flex items-center justify-between">
                <span className="text-xs font-semibold text-slate-200">Notifications</span>
                {unreadCount > 0 && (
                  <button onClick={() => { markAllNotificationsRead(); setNotifOpen(false); setCurrentPage('notifications'); }}
                    className="text-[10px] text-emerald-400 hover:underline font-semibold">
                    Mark all read
                  </button>
                )}
              </div>
              <div className="max-h-60 overflow-y-auto divide-y divide-slate-850">
                {userNotifs.length === 0 ? (
                  <div className="px-4 py-6 text-center text-xs text-slate-500">
                    No new notifications
                  </div>
                ) : (
                  userNotifs.slice(0, 5).map((n) => (
                    <div key={n.id}
                      onClick={() => { setNotifOpen(false); setCurrentPage('notifications'); }}
                      className={`p-3 cursor-pointer hover:bg-slate-900/60 transition-all duration-300 flex justify-between items-start gap-2 group/notif ${!n.read ? 'bg-emerald-500/5' : ''}`}>
                      <div className="flex-1 min-w-0">
                        <p className="text-xs font-semibold text-slate-200 group-hover/notif:text-emerald-400 transition-colors duration-200">{n.title}</p>
                        <p className="text-[11px] text-slate-400 truncate mt-0.5">{n.message}</p>
                        <span className="text-[9px] text-slate-500">{n.time}</span>
                      </div>
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          setNotifOpen(false);
                          startTour(n);
                        }}
                        className="px-2 py-0.5 rounded bg-emerald-600/20 hover:bg-emerald-600 border border-emerald-500/30 hover:border-emerald-400 text-emerald-400 hover:text-white text-[9px] font-bold transition-all duration-300 hover:scale-105 active:scale-95 shrink-0 mt-0.5 cursor-pointer"
                      >
                        Know More!
                      </button>
                    </div>
                  ))
                )}
              </div>
              <div className="p-2 border-t border-slate-800/80 text-center">
                <button onClick={() => { setNotifOpen(false); setCurrentPage('notifications'); }}
                  className="text-xs font-semibold text-slate-400 hover:text-white">
                  View All
                </button>
              </div>
            </div>
          )}
        </div>

        {/* User menu */}
        <div className="relative">
          <button
            onClick={() => { setProfileOpen(!profileOpen); setNotifOpen(false); }}
            className="flex items-center gap-2 border border-slate-800 hover:bg-slate-800/40 px-3 py-1.5 rounded-lg text-slate-300 hover:text-white transition-all text-xs"
          >
            <div className="w-5 h-5 rounded-full bg-emerald-600 flex items-center justify-center text-[9px] font-bold text-white">
              {activeOrg.name[0]}
            </div>
            <span className="font-semibold hidden sm:inline max-w-[90px] truncate">{activeOrg.name.split(' ')[0]}</span>
            <ChevronDown className="w-3 h-3 text-slate-500" />
          </button>

          {profileOpen && (
            <div className="absolute right-0 mt-2 w-44 glass-card rounded-xl shadow-2xl py-2 z-50 border border-slate-800/80 animate-fade-in-up">
              <div className="px-4 py-2.5 border-b border-slate-800/80">
                <p className="text-xs font-bold text-slate-200 truncate">{activeOrg.name}</p>
                <Badge variant={currentRole === 'admin' ? 'danger' : currentRole === 'logistics' ? 'cyan' : 'success'} className="mt-1 text-[9px]">
                  {currentRole.toUpperCase()}
                </Badge>
              </div>
              <button onClick={() => { setProfileOpen(false); setCurrentPage('landing'); }}
                className="w-full text-left px-4 py-2 text-xs text-slate-400 hover:text-white hover:bg-slate-900/50 transition-all">
                Go to Home
              </button>
              <button onClick={() => { setProfileOpen(false); setCurrentPage('login'); }}
                className="w-full text-left px-4 py-2 text-xs text-slate-400 hover:text-white hover:bg-slate-900/50 transition-all border-t border-slate-850">
                Sign Out
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};
