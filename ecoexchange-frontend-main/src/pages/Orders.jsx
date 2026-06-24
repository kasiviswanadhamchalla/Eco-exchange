import React from 'react';
import { useApp } from '../context/AppContext';
import { Badge } from '../components/ui/Badge';
import { Button } from '../components/ui/Button';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { FileText, Calendar, DollarSign, Scale, User, ShieldAlert } from 'lucide-react';

export const Orders = () => {
  const { orders, currentRole, activeOrg } = useApp();

  // Filter orders based on active workspace role (Seller or Buyer)
  const roleOrders = orders.filter((o) => {
    if (currentRole === 'seller') return o.sellerId === activeOrg.id;
    if (currentRole === 'buyer') return o.buyerId === activeOrg.id;
    return true; // Admin sees everything
  });

  return (
    <div className="space-y-6 animate-fade-in-up">
      <div>
        <h2 className="text-lg font-bold text-slate-100 m-0">Transactions Ledger</h2>
        <p className="text-xs text-slate-400 mt-1">
          {currentRole === 'seller'
            ? 'Monitor incoming sales transactions and audit certificate downloads'
            : currentRole === 'buyer'
            ? 'Inspect raw resources procurement ledger and retrieve quality validations'
            : 'Review all transactional flows registered on the platform'}
        </p>
      </div>

      {roleOrders.length === 0 ? (
        <div className="py-20 glass-card rounded-xl text-center flex flex-col items-center justify-center gap-2">
          <FileText className="w-8 h-8 text-slate-700" />
          <h3 className="text-sm font-bold text-slate-300">No Orders Registered</h3>
          <p className="text-xs text-slate-500">There are no operational transactions logged in this ledger workspace.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 gap-4">
          {roleOrders.map((ord) => (
            <Card key={ord.id} className="border-slate-900 bg-slate-950/20">
              <div className="p-1.5 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                
                {/* Details */}
                <div className="space-y-1">
                  <div className="flex items-center gap-2">
                    <span className="text-xs font-bold text-emerald-400">Order ID: {ord.id}</span>
                    <Badge
                      variant={
                        ord.status === 'completed' || ord.status === 'delivered'
                          ? 'success'
                          : ord.status === 'in_transit'
                          ? 'cyan'
                          : 'warning'
                      }
                      className="capitalize text-[9px]"
                    >
                      {ord.status.replace('_', ' ')}
                    </Badge>
                  </div>
                  <h3 className="text-sm font-extrabold text-slate-200 mt-1">{ord.listingTitle}</h3>
                  <div className="flex flex-wrap gap-4 text-[10px] text-slate-500 mt-1.5 font-semibold">
                    <span className="flex items-center gap-1">
                      <User className="w-3.5 h-3.5 text-slate-600" />
                      {currentRole === 'seller' ? `Buyer: ${ord.buyerName}` : `Seller: ${ord.sellerName}`}
                    </span>
                    <span className="flex items-center gap-1">
                      <Scale className="w-3.5 h-3.5 text-slate-600" />
                      Volume: {ord.quantity} Tons
                    </span>
                    <span className="flex items-center gap-1">
                      <Calendar className="w-3.5 h-3.5 text-slate-600" />
                      Ordered: {ord.orderedAt}
                    </span>
                  </div>
                </div>

                {/* Ledger calculations */}
                <div className="flex flex-row sm:flex-col items-end justify-between w-full sm:w-auto shrink-0 border-t border-slate-900 sm:border-0 pt-3 sm:pt-0">
                  <div className="text-left sm:text-right">
                    <span className="text-[9px] uppercase text-slate-500 block font-semibold">Total cost</span>
                    <span className="text-base font-bold text-emerald-400">${ord.totalCost.toLocaleString()}</span>
                    <span className="text-[10px] text-slate-500 block mt-0.5">${ord.finalPrice}/ton final price</span>
                  </div>

                </div>

              </div>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};
