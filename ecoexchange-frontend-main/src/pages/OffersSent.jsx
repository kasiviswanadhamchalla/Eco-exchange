import React from 'react';
import { useApp } from '../context/AppContext';
import { Badge } from '../components/ui/Badge';
import { Send, Calendar } from 'lucide-react';
import { Button } from '../components/ui/Button';

export const OffersSent = () => {
  const { offers, activeOrg, setCurrentPage } = useApp();

  // Filter offers sent by this buyer
  const buyerOffers = offers.filter(o => o.buyerId === activeOrg.id);

  return (
    <div className="space-y-6 animate-fade-in-up">
      <div>
        <h2 className="text-lg font-bold text-slate-100 m-0">Submitted Purchase Bids</h2>
        <p className="text-xs text-slate-400 mt-1">Track custom offers and pricing proposals submitted to waste sellers</p>
      </div>

      {buyerOffers.length === 0 ? (
        <div className="py-20 glass-card rounded-xl text-center flex flex-col items-center justify-center gap-2">
          <Send className="w-8 h-8 text-slate-700" />
          <h3 className="text-sm font-bold text-slate-300">No Offers Sent</h3>
          <p className="text-xs text-slate-500">You haven't placed any bids on marketplace items yet.</p>
          <Button onClick={() => setCurrentPage('marketplace')} variant="outline" className="mt-2 text-xs">
            Browse Marketplace
          </Button>
        </div>
      ) : (
        <div className="glass-card rounded-xl overflow-hidden border border-slate-900">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs border-collapse">
              <thead>
                <tr className="bg-slate-950/60 border-b border-slate-850 text-slate-400 uppercase font-bold tracking-wider">
                  <th className="p-4">Material Listing</th>
                  <th className="p-4">Seller Organization</th>
                  <th className="p-4 text-center">My Bid price</th>
                  <th className="p-4 text-center">Required volume</th>
                  <th className="p-4 text-center">Estimated delivery</th>
                  <th className="p-4 text-center">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-850 bg-slate-950/10 font-semibold text-slate-300">
                {buyerOffers.map((off) => (
                  <tr key={off.id} className="hover:bg-slate-900/30 transition-all">
                    <td className="p-4">
                      <span className="text-slate-100 font-bold block">{off.listingTitle}</span>
                      <span className="text-[10px] text-slate-500 font-medium">Offer ID: {off.id}</span>
                    </td>
                    <td className="p-4 text-slate-200">
                      {off.sellerName}
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
