import React from 'react';
import { useApp } from '../context/AppContext';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { Button } from '../components/ui/Button';
import { Truck, Play, AlertCircle } from 'lucide-react';

export const ShipmentTracking = () => {
  const {
    shipments,
    currentRole,
    updateShipment,
    acceptShipment,
    selectedShipmentId,
    setSelectedShipmentId
  } = useApp();

  // Pick first shipment as default active if none is selected
  const activeShipment = shipments.find(s => s.id === selectedShipmentId) || shipments[0];

  const handleClaim = (id) => {
    acceptShipment(id);
  };

  const handleNextMilestone = (shipment) => {
    const nextIdx = shipment.currentMilestone + 1;
    if (nextIdx < shipment.milestones.length) {
      updateShipment(shipment.id, nextIdx);
    }
  };

  const getMilestoneStatusColor = (milestoneIdx, currentIdx) => {
    if (milestoneIdx < currentIdx) return 'bg-emerald-500 border-emerald-500 text-slate-950';
    if (milestoneIdx === currentIdx) return 'bg-indigo-600 border-indigo-500 text-white animate-pulse';
    return 'bg-slate-950 border-slate-800 text-slate-600';
  };

  return (
    <div className="space-y-6 animate-fade-in-up">
      <div>
        <h2 className="text-lg font-bold text-slate-100 m-0">Cargo Tracking Desk</h2>
        <p className="text-xs text-slate-400 mt-1">Monitor transit paths, logistics milestones, and secure carrier coordinates</p>
      </div>

      {shipments.length === 0 ? (
        <div className="py-20 glass-card rounded-xl text-center flex flex-col items-center justify-center gap-2">
          <Truck className="w-8 h-8 text-slate-700" />
          <h3 className="text-sm font-bold text-slate-300">No Shipments Initialized</h3>
          <p className="text-xs text-slate-500">Wait for buyers to purchase listed materials to trigger transport queues.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          
          {/* Shipment List (Left Panel) */}
          <div className="space-y-4">
            <h3 className="text-xs font-bold text-slate-500 uppercase tracking-widest px-1">Active Cargo Logs</h3>
            <div className="space-y-3">
              {shipments.map((ship) => {
                const isActive = activeShipment && activeShipment.id === ship.id;
                return (
                  <div
                    key={ship.id}
                    onClick={() => setSelectedShipmentId(ship.id)}
                    className={`p-4 rounded-xl border text-left cursor-pointer transition-all ${
                      isActive
                        ? 'bg-slate-900/60 border-emerald-500/30 shadow-md shadow-emerald-500/5'
                        : 'glass-card border-slate-900 hover:border-slate-800'
                    }`}
                  >
                    <div className="flex justify-between items-start">
                      <span className="text-[10px] font-bold text-emerald-400">ID: {ship.id}</span>
                      <Badge
                        variant={ship.carrierStatus === 'delivered' ? 'success' : 'cyan'}
                        className="text-[9px] capitalize"
                      >
                        {ship.carrierStatus.replace('_', ' ')}
                      </Badge>
                    </div>
                    <h4 className="text-xs font-bold text-slate-200 mt-1.5 line-clamp-1">{ship.listingTitle}</h4>
                    <div className="text-[10px] text-slate-500 mt-1 font-semibold">
                      Route: {ship.origin} ➔ {ship.destination}
                    </div>

                    {/* Quick logs controls */}
                    {currentRole === 'logistics' && ship.carrierStatus === 'pending' && (
                      <Button
                        onClick={(e) => {
                          e.stopPropagation();
                          handleClaim(ship.id);
                        }}
                        variant="primary"
                        className="w-full py-1 text-[10px] mt-3.5"
                      >
                        Claim Shipment Transport
                      </Button>
                    )}
                  </div>
                );
              })}
            </div>
          </div>

          {/* Active Tracking Details (Right Panel - takes 2 cols) */}
          {activeShipment ? (
            <div className="lg:col-span-2 space-y-6">
              


              {/* Progress Milestones Timeline */}
              <Card className="border-slate-900">
                <CardHeader className="flex justify-between items-center">
                  <CardTitle className="text-xs uppercase font-bold text-slate-400 tracking-wider">
                    Logistics Milestones
                  </CardTitle>
                  
                  {/* Milestones Action Controls for Logistics */}
                  {currentRole === 'logistics' && activeShipment.carrierStatus !== 'delivered' && (
                    <Button
                      onClick={() => handleNextMilestone(activeShipment)}
                      variant="primary"
                      className="text-xs py-1 px-3 bg-emerald-600 text-white"
                      icon={Play}
                    >
                      Advance Milestone
                    </Button>
                  )}
                </CardHeader>
                <CardContent className="pt-2">
                  <div className="relative pl-8 space-y-6 before:absolute before:left-3.5 before:top-2 before:bottom-2 before:w-0.5 before:bg-slate-850">
                    {activeShipment.milestones.map((ms, idx) => {
                      const isCompleted = idx <= activeShipment.currentMilestone;
                      const circleColor = getMilestoneStatusColor(idx, activeShipment.currentMilestone);
                      return (
                        <div key={idx} className="relative text-left">
                          <div className={`absolute -left-8 w-6 h-6 rounded-full border-2 text-[10px] font-bold flex items-center justify-center ${circleColor} transition-all duration-300`}>
                            {idx + 1}
                          </div>
                          <div>
                            <div className="flex items-center gap-2">
                              <h4 className={`text-xs font-bold ${isCompleted ? 'text-slate-200' : 'text-slate-500'}`}>
                                {ms.title}
                              </h4>
                              {ms.time && (
                                <span className="text-[9px] text-slate-500 font-semibold bg-slate-900 px-1.5 py-0.5 rounded border border-slate-850">
                                  {ms.time}
                                </span>
                              )}
                            </div>
                            <p className="text-[10px] text-slate-400 mt-0.5">{ms.description}</p>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </CardContent>
              </Card>

            </div>
          ) : (
            <div className="lg:col-span-2 text-center py-20 bg-slate-950/20 border border-slate-900 rounded-xl">
              <AlertCircle className="w-8 h-8 text-slate-700 mx-auto" />
              <p className="text-xs text-slate-500 mt-2 font-semibold">Select a shipment cargo log to view tracing path.</p>
            </div>
          )}
        </div>
      )}
    </div>
  );
};
