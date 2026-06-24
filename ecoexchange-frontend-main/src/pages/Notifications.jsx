import React, { useEffect } from 'react';
import { useApp } from '../context/AppContext';
import { Badge } from '../components/ui/Badge';
import { Button } from '../components/ui/Button';
import { Bell, Inbox, AlertTriangle, Truck, Info, MailOpen } from 'lucide-react';
import { Card, CardContent } from '../components/ui/Card';

export const Notifications = () => {
  const { notifications, currentRole, activeOrg, markAllNotificationsRead } = useApp();

  // Filter notification updates for this actor
  const userNotifs = notifications.filter(
    n => (currentRole === 'admin' ? n.userId === 'admin' : n.userId === activeOrg.id)
  );

  // Clear unread badge on mounting
  useEffect(() => {
    markAllNotificationsRead();
  }, []);

  const getNotifIcon = (type) => {
    switch (type) {
      case 'offer': return <Inbox className="w-4 h-4 text-indigo-400" />;
      case 'shipping': return <Truck className="w-4 h-4 text-cyan-400" />;
      case 'admin': return <AlertTriangle className="w-4 h-4 text-amber-400" />;
      default: return <Bell className="w-4 h-4 text-emerald-400" />;
    }
  };

  return (
    <div className="space-y-6 max-w-3xl mx-auto animate-fade-in-up">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-lg font-bold text-slate-100 m-0">Platform Activity Alerts</h2>
          <p className="text-xs text-slate-400 mt-1">Shipping events, offers, and transaction updates</p>
        </div>
        <Button
          onClick={markAllNotificationsRead}
          variant="outline"
          className="text-xs px-3 py-1.5 border-slate-800 hover:border-slate-600"
          icon={MailOpen}
        >
          Mark All Read
        </Button>
      </div>

      {userNotifs.length === 0 ? (
        <div className="py-20 glass-card rounded-xl text-center flex flex-col items-center justify-center gap-2">
          <Bell className="w-8 h-8 text-slate-700" />
          <h3 className="text-sm font-bold text-slate-300">Workspace Clear</h3>
          <p className="text-xs text-slate-500">No new platform messages or transaction updates recorded.</p>
        </div>
      ) : (
        <div className="space-y-3">
          {userNotifs.map((notif) => (
            <Card
              key={notif.id}
              className={`border-slate-900/60 relative overflow-hidden transition-all ${
                !notif.read ? 'bg-emerald-500/5 border-emerald-500/20' : 'bg-slate-950/20'
              }`}
            >
              <CardContent className="p-4 flex gap-4 items-start">
                <div className="p-2 bg-slate-900 border border-slate-850 rounded-lg shrink-0 mt-0.5">
                  {getNotifIcon(notif.type)}
                </div>

                <div className="flex-1 space-y-0.5 text-left">
                  <div className="flex justify-between items-center">
                    <span className="text-[10px] uppercase font-bold text-slate-500 tracking-wider">
                      {notif.type} notice
                    </span>
                    <span className="text-[9px] text-slate-500 font-semibold">{notif.time}</span>
                  </div>
                  <h4 className="text-xs font-bold text-slate-200">{notif.title}</h4>
                  <p className="text-xs text-slate-400 leading-relaxed mt-1">{notif.message}</p>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};
