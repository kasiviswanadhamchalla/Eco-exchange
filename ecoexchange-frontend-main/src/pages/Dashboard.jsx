import React from 'react';
import { useApp } from '../context/AppContext';
import { Button } from '../components/ui/Button';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import {
  RotateCcw,
  TrendingUp,
  Inbox,
  AlertCircle,
  TrendingDown,
  Truck,
  Building,
  CheckCircle2,
  ChevronRight,
  ShieldCheck,
  Zap
} from 'lucide-react';

export const Dashboard = () => {
  const {
    currentRole,
    setCurrentPage,
    listings,
    offers,
    orders,
    shipments,
    organizations,
    activeOrg,
    setSelectedShipmentId,
    handleOfferDecision
  } = useApp();

  // Metrics calculators
  const getMetrics = () => {
    switch (currentRole) {
      case 'seller': {
        const sellerListings = listings.filter(l => l.sellerId === activeOrg.id);
        const sellerOffers = offers.filter(o => o.sellerId === activeOrg.id);
        const sellerOrders = orders.filter(o => o.sellerId === activeOrg.id);
        const totalSales = sellerOrders.reduce((sum, o) => sum + o.totalCost, 0);
        const totalWasteDiverted = sellerOrders.reduce((sum, o) => sum + o.quantity, 0);
        return [
          { label: 'Total Revenue', value: `$${totalSales.toLocaleString()}`, change: '+12.4% vs last month', trend: 'up', icon: TrendingUp, color: 'text-emerald-400' },
          { label: 'Waste Diverted', value: `${totalWasteDiverted} Tons`, change: 'Verified circular flow', trend: 'up', icon: RotateCcw, color: 'text-emerald-400' },
          { label: 'Active Listings', value: sellerListings.filter(l => l.status === 'active').length, change: 'Vetted in marketplace', trend: 'up', icon: Zap, color: 'text-indigo-400' },
          { label: 'Pending Offers', value: sellerOffers.filter(o => o.status === 'pending').length, change: 'Awaiting your review', trend: 'neutral', icon: Inbox, color: 'text-indigo-400' },
        ];
      }
      case 'buyer': {
        const buyerOffers = offers.filter(o => o.buyerId === activeOrg.id);
        const buyerOrders = orders.filter(o => o.buyerId === activeOrg.id);
        const totalSpent = buyerOrders.reduce((sum, o) => sum + o.totalCost, 0);
        const wastePurchased = buyerOrders.reduce((sum, o) => sum + o.quantity, 0);
        return [
          { label: 'Total Procured', value: `$${totalSpent.toLocaleString()}`, change: '-5.2% procurement cost', trend: 'down', icon: TrendingDown, color: 'text-emerald-400' },
          { label: 'Waste Sourced', value: `${wastePurchased} Tons`, change: 'Diverted material volume', trend: 'up', icon: RotateCcw, color: 'text-emerald-400' },
          { label: 'Sent Bids Active', value: buyerOffers.filter(o => o.status === 'pending').length, change: 'Pending seller action', trend: 'neutral', icon: Inbox, color: 'text-indigo-400' },
          { label: 'Shipments En Route', value: shipments.filter(s => s.buyerName === activeOrg.name && s.carrierStatus !== 'delivered').length, change: 'Tracked on GPS route', trend: 'up', icon: Truck, color: 'text-indigo-400' },
        ];
      }
      case 'logistics': {
        const activeShipments = shipments.filter(s => s.carrierStatus !== 'delivered');
        const completedCount = shipments.filter(s => s.carrierStatus === 'delivered').length;
        return [
          { label: 'Assigned Cargo', value: activeShipments.length, change: 'Awaiting route update', trend: 'neutral', icon: Truck, color: 'text-cyan-400' },
          { label: 'Delivered Shipments', value: completedCount, change: 'All milestones completed', trend: 'up', icon: CheckCircle2, color: 'text-emerald-400' },
          { label: 'Pending Pickups', value: shipments.filter(s => s.carrierStatus === 'pending').length, change: 'Awaiting claim approval', trend: 'neutral', icon: AlertCircle, color: 'text-amber-400' },
          { label: 'Active Route Logs', value: shipments.filter(s => s.carrierStatus === 'in_transit').length, change: 'Milestones en route', trend: 'up', icon: RotateCcw, color: 'text-cyan-400' }
        ];
      }
      case 'admin': {
        return [
          { label: 'Total Listings', value: listings.length, change: 'All categories included', trend: 'up', icon: Zap, color: 'text-emerald-400' },
          { label: 'Unverified Orgs', value: organizations.filter(o => o.status === 'pending').length, change: 'Needs review', trend: 'neutral', icon: Building, color: 'text-amber-400' },
          { label: 'Secured Trades', value: orders.length, change: 'Smart-flow transactions', trend: 'up', icon: ShieldCheck, color: 'text-indigo-400' },
          { label: 'Diverted Tonnage', value: `${orders.reduce((acc, o) => acc + o.quantity, 0)} Tons`, change: 'CO2 offset tracked', trend: 'up', icon: RotateCcw, color: 'text-emerald-400' }
        ];
      }
      default: return [];
    }
  };

  const metrics = getMetrics();

  // Custom renders for Dashboard list content based on role
  const renderDashboardList = () => {
    switch (currentRole) {
      case 'seller': {
        const pendingOffers = offers.filter(o => o.sellerId === activeOrg.id && o.status === 'pending');
        return (
          <Card className="col-span-1 lg:col-span-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Inbox className="w-5 h-5 text-indigo-400" /> Pending Purchase Offers ({pendingOffers.length})
              </CardTitle>
              <button onClick={() => setCurrentPage('offers-received')} className="text-xs text-emerald-400 font-semibold hover:underline">
                View Workspace
              </button>
            </CardHeader>
            <CardContent>
              {pendingOffers.length === 0 ? (
                <div className="py-8 text-center text-xs text-slate-500">
                  No pending buyer bids on your listings. Create more listings to attract offers!
                </div>
              ) : (
                <div className="space-y-3">
                  {pendingOffers.slice(0, 3).map((off) => (
                    <div key={off.id} className="p-3.5 bg-slate-900/40 border border-slate-800/80 rounded-lg flex items-center justify-between">
                      <div>
                        <span className="text-xs font-semibold text-slate-400">{off.buyerName}</span>
                        <h4 className="text-sm font-bold text-slate-200 mt-0.5">{off.listingTitle}</h4>
                        <div className="flex gap-3 text-[10px] text-slate-500 mt-1">
                          <span>Qty: {off.quantity} Tons</span>
                          <span>Bid Price: ${off.offeredPrice}/Ton</span>
                        </div>
                      </div>
                      <div className="flex gap-2">
                        <Button onClick={() => handleOfferDecision(off.id, true)} variant="primary" className="py-1 px-3 text-xs">
                          Accept
                        </Button>
                        <Button onClick={() => handleOfferDecision(off.id, false)} variant="danger" className="py-1 px-3 text-xs">
                          Reject
                        </Button>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        );
      }
      case 'buyer': {
        const activeShipments = shipments.filter(s => s.buyerName === activeOrg.name && s.carrierStatus !== 'delivered');
        return (
          <Card className="col-span-1 lg:col-span-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Truck className="w-5 h-5 text-emerald-400" /> Track Sourced Shipments ({activeShipments.length})
              </CardTitle>
              <button onClick={() => setCurrentPage('shipment-tracking')} className="text-xs text-emerald-400 font-semibold hover:underline">
                Track Board
              </button>
            </CardHeader>
            <CardContent>
              {activeShipments.length === 0 ? (
                <div className="py-8 text-center text-xs text-slate-500">
                  No active materials en route. Purchase from the marketplace to launch shipments!
                </div>
              ) : (
                <div className="space-y-3">
                  {activeShipments.slice(0, 3).map((ship) => (
                    <div key={ship.id} className="p-3.5 bg-slate-900/40 border border-slate-800/80 rounded-lg flex items-center justify-between">
                      <div>
                        <h4 className="text-sm font-bold text-slate-200">{ship.listingTitle}</h4>
                        <div className="flex gap-2.5 text-[10px] text-slate-500 mt-0.5">
                          <span>Origin: {ship.origin}</span>
                          <span>Dest: {ship.destination}</span>
                        </div>
                        <div className="mt-2.5 flex items-center gap-2">
                          <Badge variant="cyan" className="capitalize text-[9px]">{ship.carrierStatus.replace('_', ' ')}</Badge>
                          <span className="text-[10px] text-slate-400 font-semibold">Milestone {ship.currentMilestone}/4</span>
                        </div>
                      </div>
                      <Button
                        onClick={() => {
                          setSelectedShipmentId(ship.id);
                          setCurrentPage('shipment-tracking');
                        }}
                        variant="outline"
                        className="py-1 px-3 text-xs"
                      >
                        Map View <ChevronRight className="w-3.5 h-3.5" />
                      </Button>
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        );
      }
      case 'logistics': {
        const pendingClaims = shipments.filter(s => s.carrierStatus === 'pending');
        return (
          <Card className="col-span-1 lg:col-span-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Truck className="w-5 h-5 text-indigo-400" /> Open Logistics Dispatch Queue ({pendingClaims.length})
              </CardTitle>
              <button onClick={() => setCurrentPage('shipment-tracking')} className="text-xs text-indigo-400 font-semibold hover:underline">
                Full Dispatch
              </button>
            </CardHeader>
            <CardContent>
              {pendingClaims.length === 0 ? (
                <div className="py-8 text-center text-xs text-slate-500">
                  No shipments requiring assignment. System will assign transport tasks as sales close.
                </div>
              ) : (
                <div className="space-y-3">
                  {pendingClaims.slice(0, 3).map((ship) => (
                    <div key={ship.id} className="p-3.5 bg-slate-900/40 border border-slate-800/80 rounded-lg flex items-center justify-between">
                      <div>
                        <h4 className="text-sm font-bold text-slate-200">{ship.listingTitle}</h4>
                        <div className="text-[10px] text-slate-500 mt-1 space-y-0.5">
                          <div>From: <span className="text-slate-400">{ship.sellerName} ({ship.origin})</span></div>
                          <div>To: <span className="text-slate-400">{ship.buyerName} ({ship.destination})</span></div>
                        </div>
                      </div>
                      <Button
                        onClick={() => {
                          setSelectedShipmentId(ship.id);
                          setCurrentPage('shipment-tracking');
                        }}
                        variant="primary"
                        className="py-1.5 px-3 text-xs"
                      >
                        Claim Shipment
                      </Button>
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        );
      }
      case 'admin': {
        const pendingOrgs = organizations.filter(o => o.status === 'pending');
        return (
          <Card className="col-span-1 lg:col-span-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Building className="w-5 h-5 text-amber-400" /> Pending Organizations verification ({pendingOrgs.length})
              </CardTitle>
              <button onClick={() => setCurrentPage('org-verification')} className="text-xs text-emerald-400 font-semibold hover:underline">
                Verification Desk
              </button>
            </CardHeader>
            <CardContent>
              {pendingOrgs.length === 0 ? (
                <div className="py-8 text-center text-xs text-slate-500">
                  All registered organizations have been validated.
                </div>
              ) : (
                <div className="space-y-3">
                  {pendingOrgs.slice(0, 3).map((org) => (
                    <div key={org.id} className="p-3.5 bg-slate-900/40 border border-slate-800/80 rounded-lg flex items-center justify-between">
                      <div>
                        <h4 className="text-sm font-bold text-slate-200">{org.name}</h4>
                        <div className="flex gap-2.5 text-[10px] text-slate-500 mt-1">
                          <span>Focus: {org.wasteFocus.join(', ')}</span>
                          <span>Registered: {org.registeredAt}</span>
                        </div>
                      </div>
                      <Button onClick={() => setCurrentPage('org-verification')} variant="outline" className="py-1 px-3 text-xs text-emerald-400 hover:text-white">
                        Review Organizations
                      </Button>
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        );
      }
      default: return null;
    }
  };

  return (
    <div className="space-y-6 animate-fade-in-up">
      {/* Greetings / Org Info */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-slate-950/40 p-5 rounded-xl border border-slate-900">
        <div>
          <h2 className="text-lg font-bold text-slate-100 flex items-center gap-2">
            Workspace: {activeOrg.name} <Badge variant="success">Active</Badge>
          </h2>
          <p className="text-xs text-slate-400 mt-1">
            Registered scope: {currentRole === 'admin' ? 'Universal platform governance' : `Trading/Handling waste materials as ${activeOrg.type}.`}
          </p>
        </div>
        <div className="flex gap-2">
          {currentRole === 'seller' && (
            <Button onClick={() => setCurrentPage('create-listing')} variant="primary" icon={Zap}>
              Create New Listing
            </Button>
          )}
          {currentRole === 'buyer' && (
            <Button onClick={() => setCurrentPage('marketplace')} variant="primary" icon={Zap}>
              Search Marketplace
            </Button>
          )}
          {currentRole === 'admin' && (
            <Button onClick={() => setCurrentPage('audit-viewer')} variant="outline" className="border-slate-800">
              View Security Trails
            </Button>
          )}
        </div>
      </div>

      {/* Metrics Row */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {metrics.map((m, idx) => {
          const Icon = m.icon;
          return (
            <Card key={idx} className="border-slate-900">
              <CardContent className="pt-5 flex items-center justify-between">
                <div>
                  <span className="text-[10px] uppercase font-bold tracking-wider text-slate-500">{m.label}</span>
                  <h3 className="text-xl md:text-2xl font-bold text-slate-100 mt-1 leading-tight">{m.value}</h3>
                  <span className="text-[10px] font-semibold text-slate-400 mt-1.5 block">{m.change}</span>
                </div>
                <div className="p-3 bg-slate-900 rounded-lg border border-slate-850">
                  <Icon className={`w-5 h-5 ${m.color}`} />
                </div>
              </CardContent>
            </Card>
          );
        })}
      </div>

      {/* Dynamic Main Workspace lists */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {renderDashboardList()}

        {/* Static Circular economy impact status board */}
        <Card className="col-span-1 border-slate-900 bg-slate-950/40">
          <CardHeader>
            <CardTitle className="text-sm uppercase font-bold text-slate-400 tracking-wider">
              Circular Trade Insights
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="space-y-1.5">
              <div className="flex items-center justify-between text-xs font-semibold">
                <span className="text-slate-400">Target Diversion Goal</span>
                <span className="text-emerald-400">76% reached</span>
              </div>
              <div className="w-full bg-slate-900 h-2.5 rounded-full overflow-hidden border border-slate-850">
                <div className="bg-emerald-500 h-full rounded-full w-[76%] shadow-lg shadow-emerald-500/20" />
              </div>
            </div>

            <div className="space-y-2 pt-2">
              <div className="flex items-center justify-between p-2.5 bg-slate-900/50 rounded-lg text-xs font-semibold">
                <span className="text-slate-400">Carbon saved (CO2)</span>
                <span className="text-emerald-400 font-bold">142,300 lbs</span>
              </div>
              <div className="flex items-center justify-between p-2.5 bg-slate-900/50 rounded-lg text-xs font-semibold">
                <span className="text-slate-400">Disposal savings (B2B)</span>
                <span className="text-emerald-400 font-bold">$124,500 USD</span>
              </div>
              <div className="flex items-center justify-between p-2.5 bg-slate-900/50 rounded-lg text-xs font-semibold">
                <span className="text-slate-400">Smart-ledger security</span>
                <span className="text-indigo-400 font-bold">Audit Logs</span>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};
