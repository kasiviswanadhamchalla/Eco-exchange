import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { Button } from '../components/ui/Button';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { Input } from '../components/ui/Input';
import { Search, MapPin, Scale, DollarSign, Filter, Eye, AlertTriangle, CheckSquare } from 'lucide-react';

export const Marketplace = () => {
  const {
    listings,
    currentRole,
    setCurrentPage,
    setSelectedListingId,
    moderateListing
  } = useApp();

  const [search, setSearch] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [adminViewStatus, setAdminViewStatus] = useState('all'); // all, active, flagged, deactivated

  const categories = ['All', 'Steel Scrap', 'Fly Ash', 'Plastic Scrap', 'Textile Waste', 'Wood Waste', 'Glass Waste'];

  // Filter listings based on search, category, and role (admins see everything)
  const filteredListings = listings.filter((l) => {
    const matchesSearch = l.title.toLowerCase().includes(search.toLowerCase()) || 
                          l.description.toLowerCase().includes(search.toLowerCase());
    
    const matchesCategory = selectedCategory === 'All' || l.category === selectedCategory;

    // Normal users only see active listings. Admins see moderated items.
    if (currentRole === 'admin') {
      if (adminViewStatus === 'all') return matchesSearch && matchesCategory;
      return matchesSearch && matchesCategory && l.status === adminViewStatus;
    } else {
      return matchesSearch && matchesCategory && l.status === 'active';
    }
  });

  const handleViewDetails = (id) => {
    setSelectedListingId(id);
    setCurrentPage('listing-details');
  };

  return (
    <div className="space-y-6 animate-fade-in-up">
      {/* Search and filter panel */}
      <Card className="border-slate-900 bg-slate-950/20">
        <CardContent className="pt-5 space-y-4">
          <div className="flex flex-col md:flex-row gap-4 items-center">
            {/* Search Input */}
            <div className="relative w-full">
              <Search className="absolute left-3.5 top-3.5 h-4 w-4 text-slate-500" />
              <input
                type="text"
                placeholder="Search raw waste materials (e.g. fly ash, structural steel, cullet)..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="glass-input pl-10 pr-4 py-2.5 w-full rounded-lg text-sm text-slate-100 placeholder-slate-500"
              />
            </div>
            {/* Admin sub-controls */}
            {currentRole === 'admin' && (
              <div className="flex gap-2 w-full md:w-auto shrink-0">
                <select
                  value={adminViewStatus}
                  onChange={(e) => setAdminViewStatus(e.target.value)}
                  className="glass-input px-3.5 py-2.5 rounded-lg text-xs font-semibold text-slate-300"
                >
                  <option value="all">Admin: View All Listings</option>
                  <option value="active">View Active only</option>
                  <option value="flagged">View Flagged only</option>
                  <option value="deactivated">View Deactivated only</option>
                </select>
              </div>
            )}
          </div>

          {/* Categories select row */}
          <div className="flex items-center gap-2 overflow-x-auto pb-1 scrollbar-none">
            <Filter className="w-4 h-4 text-slate-500 shrink-0 mr-1 hidden sm:inline" />
            {categories.map((cat) => (
              <button
                key={cat}
                onClick={() => setSelectedCategory(cat)}
                className={`px-3 py-1.5 rounded-full text-xs font-semibold shrink-0 transition-all ${
                  selectedCategory === cat
                    ? 'bg-emerald-500 text-slate-950 shadow-md shadow-emerald-500/20'
                    : 'bg-slate-900/60 text-slate-400 border border-slate-850 hover:text-slate-200'
                }`}
              >
                {cat}
              </button>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Listings grid */}
      {filteredListings.length === 0 ? (
        <div className="py-20 glass-card rounded-xl text-center flex flex-col items-center justify-center gap-2">
          <Search className="w-8 h-8 text-slate-700" />
          <h3 className="text-sm font-bold text-slate-300">No Industrial Materials Found</h3>
          <p className="text-xs text-slate-500">Refine your search parameters or select a different category filter.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredListings.map((list) => (
            <Card key={list.id} className="border-slate-900 flex flex-col justify-between overflow-hidden relative group">
              

              {/* Card Details */}
              <CardContent className="pt-4 flex-1">
                <h3 className="text-sm font-bold text-slate-100 group-hover:text-emerald-400 transition-colors line-clamp-1">
                  {list.title}
                </h3>
                <p className="text-[11px] text-slate-400 line-clamp-2 mt-1.5 leading-relaxed">
                  {list.description}
                </p>

                <div className="flex items-center gap-1.5 mt-3 pt-3 border-t border-slate-850 text-slate-400 text-xs font-semibold">
                  <MapPin className="w-3.5 h-3.5 text-slate-500" />
                  <span>{list.location}</span>
                </div>
              </CardContent>

              {/* Card Actions */}
              <div className="p-5 pt-0 mt-2 flex gap-2">
                <Button
                  onClick={() => handleViewDetails(list.id)}
                  variant={currentRole === 'buyer' ? 'primary' : 'outline'}
                  className="w-full text-xs font-bold py-2"
                  icon={Eye}
                >
                  View Details
                </Button>
                
                {/* Admin moderation quick button */}
                {currentRole === 'admin' && list.status === 'active' && (
                  <Button
                    onClick={() => moderateListing(list.id, 'flagged')}
                    variant="danger"
                    className="p-2 aspect-square px-2.5 bg-rose-950/20 border-rose-500/30 hover:bg-rose-900/40 text-rose-400 border"
                  >
                    <AlertTriangle className="w-4 h-4" />
                  </Button>
                )}
                {currentRole === 'admin' && list.status === 'flagged' && (
                  <Button
                    onClick={() => moderateListing(list.id, 'active')}
                    variant="primary"
                    className="p-2 aspect-square px-2.5 bg-emerald-950/20 border-emerald-500/30 hover:bg-emerald-900/40 text-emerald-400 border"
                  >
                    <CheckSquare className="w-4 h-4" />
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
