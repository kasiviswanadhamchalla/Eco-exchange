import React from 'react';
import { useApp } from '../context/AppContext';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { Scale, DollarSign, Calendar, Eye, Trash2, ShieldAlert, FileEdit } from 'lucide-react';

export const ManageListings = () => {
  const { listings, activeOrg, deleteListing, setCurrentPage, setSelectedListingId } = useApp();

  // Filter listings owned by this seller
  const sellerListings = listings.filter(l => l.sellerId === activeOrg.id && l.status !== 'deactivated');

  const handleDeactivate = (id) => {
    deleteListing(id);
  };

  const handleView = (id) => {
    setSelectedListingId(id);
    setCurrentPage('listing-details');
  };

  return (
    <div className="space-y-6 animate-fade-in-up">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-lg font-bold text-slate-100 m-0">My Stock Inventory</h2>
          <p className="text-xs text-slate-400 mt-1">Manage, edit, or deactivate waste material postings</p>
        </div>
        <Button onClick={() => setCurrentPage('create-listing')} variant="primary">
          List New Material
        </Button>
      </div>

      {sellerListings.length === 0 ? (
        <div className="py-20 glass-card rounded-xl text-center flex flex-col items-center justify-center gap-2">
          <ShieldAlert className="w-8 h-8 text-slate-700" />
          <h3 className="text-sm font-bold text-slate-300">No Inventory Found</h3>
          <p className="text-xs text-slate-500">You haven't listed any materials under {activeOrg.name} yet.</p>
          <Button onClick={() => setCurrentPage('create-listing')} variant="outline" className="mt-2 text-xs">
            Create First Listing
          </Button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {sellerListings.map((list) => (
            <Card key={list.id} className="border-slate-900 flex flex-col justify-between overflow-hidden relative group">
              <div className="h-40 w-full relative overflow-hidden rounded-t-xl bg-slate-950">
                <img
                  src={list.image}
                  alt={list.title}
                  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300 opacity-85"
                />
                <div className="absolute top-3 left-3 bg-slate-900/90 border border-slate-800 text-[10px] text-slate-300 font-bold px-2 py-0.5 rounded">
                  {list.category}
                </div>
                <Badge
                  variant={list.status === 'active' ? 'success' : list.status === 'sold' ? 'purple' : 'danger'}
                  className="absolute top-3 right-3 capitalize"
                >
                  {list.status}
                </Badge>
              </div>

              <CardContent className="pt-4 flex-1">
                <h3 className="text-sm font-bold text-slate-100 line-clamp-1">
                  {list.title}
                </h3>
                
                <div className="grid grid-cols-2 gap-3 mt-4 pt-4 border-t border-slate-850">
                  <div className="flex items-center gap-1.5">
                    <Scale className="w-3.5 h-3.5 text-slate-500" />
                    <div>
                      <span className="text-[9px] uppercase text-slate-500 block font-semibold leading-none">Stock Weight</span>
                      <span className="text-xs font-bold text-slate-300">{list.quantity} {list.unit}</span>
                    </div>
                  </div>
                  <div className="flex items-center gap-1.5">
                    <DollarSign className="w-3.5 h-3.5 text-slate-500" />
                    <div>
                      <span className="text-[9px] uppercase text-slate-500 block font-semibold leading-none">Unit price</span>
                      <span className="text-xs font-bold text-emerald-400">${list.price}/{list.unit.substring(0, 3)}</span>
                    </div>
                  </div>
                </div>

                <div className="flex items-center justify-between mt-3 pt-3 border-t border-slate-850 text-slate-400 text-xs font-semibold">
                  <div className="flex items-center gap-1">
                    <Calendar className="w-3.5 h-3.5 text-slate-500" />
                    <span>Listed: {list.createdAt}</span>
                  </div>
                  <span className="text-[10px] font-bold text-slate-500">ID: {list.id}</span>
                </div>
              </CardContent>

              <div className="p-5 pt-0 mt-2 flex gap-2">
                <Button
                  onClick={() => handleView(list.id)}
                  variant="outline"
                  className="w-full text-xs font-bold py-2"
                  icon={Eye}
                >
                  View Page
                </Button>
                
                {list.status === 'active' && (
                  <Button
                    onClick={() => handleDeactivate(list.id, list.title)}
                    variant="danger"
                    className="p-2 aspect-square px-2.5 bg-rose-950/20 border-rose-500/30 hover:bg-rose-900/40 text-rose-400 border"
                  >
                    <Trash2 className="w-4 h-4" />
                  </Button>
                )}
              </div>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};
