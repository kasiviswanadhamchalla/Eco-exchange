import React from 'react';
import { useApp } from '../context/AppContext';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { Inbox, Check, X, ShieldAlert, Calendar, DollarSign } from 'lucide-react';

export const OffersReceived = () => {
  const { offers, activeOrg, handleOfferDecision } = useApp();

  // Filter offers sent to listings owned by this seller
  const sellerOffers = offers.filter(o => o.sellerId === activeOrg.id);

  return (
    <div className="space-y-6 animate-fade-in-up">
      <div>
        <h2 className="text-lg font-bold text-slate-100 m-0">Incoming Purchase Offers</h2>
        <p className="text-xs text-slate-400 mt-1">Review, approve, or reject bids placed by secondary waste resource buyers</p>
      </div>

      {sellerOffers.length === 0 ? (
        <div className="py-20 glass-card rounded-xl text-center flex flex-col items-center justify-center gap-2">
          <Inbox className="w-8 h-8 text-slate-700" />
          <h3 className="text-sm font-bold text-slate-300">No Offers Received</h3>
          <p className="text-xs text-slate-500">No active bids have been submitted for your inventory stock yet.</p>
        </div>
      ) : (
        <div className="glass-card rounded-xl overflow-hidden border border-slate-900">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs border-collapse">
              <thead>
                <tr className="bg-slate-950/60 border-b border-slate-850 text-slate-400 uppercase font-bold tracking-wider">
                  <th className="p-4">Material Listing</th>
                  <th className="p-4">Buyer Organization</th>
                  <th className="p-4 text-center">Bid price</th>
                  <th className="p-4 text-center">Required volume</th>
                  <th className="p-4 text-center">Estimated delivery</th>
                  <th className="p-4 text-center">Status</th>
                  <th className="p-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-850 bg-slate-950/10 font-semibold text-slate-300">
                {sellerOffers.map((off) => (
                  <tr key={off.id} className="hover:bg-slate-900/30 transition-all">
                    <td className="p-4">
                      <span className="text-slate-100 font-bold block">{off.listingTitle}</span>
                      <span className="text-[10px] text-slate-500 font-medium">Offer ID: {off.id}</span>
                    </td>
                    <td className="p-4 text-slate-200">
                      {off.buyerName}
                    </td>
                    <td className="p-4 text-center text-emerald-400">
                      ${off.offeredPrice}/Ton
                    </td>
                    <td className="p-4 text-center text-slate-200">
                      {off.quantity} Tons
                    </td>
                    <td className="p-4 text-center text-slate-400">
                      <span className="inline-flex items-center gap-1">
                        <Calendar className="w-3.5 h-3.5 text-slate-600" />
                        {off.deliveryDate}
                      </span>
                    </td>
                    <td className="p-4 text-center">
                      <Badge
                        variant={
                          off.status === 'accepted' ? 'success' : off.status === 'pending' ? 'warning' : 'danger'
                        }
                        className="capitalize"
                      >
                        {off.status}
                      </Badge>
                    </td>
                    <td className="p-4 text-right">
                      {off.status === 'pending' ? (
                        <div className="inline-flex gap-2">
                          <Button
                            onClick={() => handleOfferDecision(off.id, true)}
                            variant="primary"
                            className="py-1 px-2.5 text-xs bg-emerald-600/20 border border-emerald-500/30 text-emerald-400 hover:bg-emerald-600 hover:text-white"
                          >
                            <Check className="w-3.5 h-3.5" /> Accept
                          </Button>
                          <Button
                            onClick={() => handleOfferDecision(off.id, false)}
                            variant="danger"
                            className="py-1 px-2.5 text-xs bg-rose-600/20 border border-rose-500/30 text-rose-400 hover:bg-rose-600 hover:text-white"
                          >
                            <X className="w-3.5 h-3.5" /> Reject
                          </Button>
                        </div>
                      ) : (
                        <span className="text-[10px] text-slate-500 uppercase tracking-widest font-bold">
                          Decision logged
                        </span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};
