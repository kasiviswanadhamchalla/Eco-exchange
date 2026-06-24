import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { Button } from '../components/ui/Button';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { Input } from '../components/ui/Input';
import {
  MapPin,
  Scale,
  Calendar,
  Building2,
  DollarSign,
  ArrowLeft,
  CheckCircle,
  Truck,
  ShieldAlert
} from 'lucide-react';

export const ListingDetails = () => {
  const {
    selectedListingId,
    listings,
    currentRole,
    placeOffer,
    buyDirectly,
    setCurrentPage
  } = useApp();

  const listing = listings.find((l) => l.id === selectedListingId);

  // Form states for bidding
  const [bidPrice, setBidPrice] = useState(listing ? listing.price : '');
  const [quantity, setQuantity] = useState(listing ? listing.quantity : '');
  const [deliveryDate, setDeliveryDate] = useState('');
  const [offerSubmitted, setOfferSubmitted] = useState(false);
  const [directPurchased, setDirectPurchased] = useState(false);

  if (!listing) {
    return (
      <div className="py-20 text-center glass-card rounded-xl border-slate-900 animate-fade-in-up">
        <ShieldAlert className="w-8 h-8 text-rose-500 mx-auto mb-2" />
        <h3 className="text-sm font-bold text-slate-200">No Material Selected</h3>
        <button onClick={() => setCurrentPage('marketplace')} className="text-xs text-emerald-400 font-bold hover:underline mt-2">
          Back to Marketplace
        </button>
      </div>
    );
  }

  const handlePlaceOffer = (e) => {
    e.preventDefault();
    if (!bidPrice || !quantity || !deliveryDate) {
      alert('Please fill out all bidding options.');
      return;
    }
    
    placeOffer({
      listingId: listing.id,
      offeredPrice: bidPrice,
      quantity,
      deliveryDate
    });

    setOfferSubmitted(true);
    setTimeout(() => {
      setOfferSubmitted(false);
      setCurrentPage('offers-sent');
    }, 1500);
  };

  const handleBuyDirect = () => {
    buyDirectly(listing.id, listing.quantity);
    setDirectPurchased(true);
    setTimeout(() => {
      setDirectPurchased(false);
      setCurrentPage('orders');
    }, 2500);
  };



  return (
    <div className="space-y-6 animate-fade-in-up">
      {/* Back navigation */}
      <button
        onClick={() => setCurrentPage('marketplace')}
        className="flex items-center gap-1.5 text-xs text-slate-400 hover:text-slate-200 font-semibold"
      >
        <ArrowLeft className="w-4 h-4" /> Back to B2B Catalog
      </button>

      {directPurchased ? (
        <div className="p-8 bg-emerald-500/10 border border-emerald-500/20 rounded-xl text-center space-y-3">
          <div className="w-12 h-12 rounded-full bg-emerald-500/20 border border-emerald-500/40 flex items-center justify-center mx-auto text-emerald-400">
            <CheckCircle className="w-6 h-6 animate-bounce" />
          </div>
          <h3 className="text-base font-bold text-slate-200">B2B Order Completed Successfully!</h3>
          <p className="text-xs text-slate-400 max-w-lg mx-auto leading-relaxed">
            Your direct purchase order is logged on the platform audit ledger. A logistics tracking container has been initialized. Moving to Transactions ledger...
          </p>
        </div>
      ) : offerSubmitted ? (
        <div className="p-8 bg-indigo-500/10 border border-indigo-500/20 rounded-xl text-center space-y-3">
          <div className="w-12 h-12 rounded-full bg-indigo-500/20 border border-indigo-500/40 flex items-center justify-center mx-auto text-indigo-400">
            <CheckCircle className="w-6 h-6 animate-bounce" />
          </div>
          <h3 className="text-base font-bold text-slate-200">Custom Purchase Offer Dispatched!</h3>
          <p className="text-xs text-slate-400 max-w-lg mx-auto leading-relaxed">
            Your bid price of **${bidPrice}/ton** was transmitted to **{listing.sellerName}**. Moving to offers board...
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Main description section */}
          <div className="lg:col-span-2 space-y-6">
            <Card className="border-slate-900">
              

              <CardContent className="pt-5 space-y-4">
                <div>
                  <span className="text-[10px] uppercase font-bold text-emerald-400 tracking-wider">
                    {listing.category} Focus
                  </span>
                  <h2 className="text-lg md:text-xl font-bold text-slate-100 mt-1 m-0">
                    {listing.title}
                  </h2>
                </div>

                <p className="text-xs md:text-sm text-slate-400 leading-relaxed pt-2">
                  {listing.description}
                </p>

                <div className="grid grid-cols-2 md:grid-cols-3 gap-4 pt-4 border-t border-slate-850">
                  <div className="flex items-center gap-2">
                    <Scale className="w-4 h-4 text-slate-500" />
                    <div>
                      <span className="text-[9px] uppercase text-slate-500 block font-semibold leading-none">Available Stock</span>
                      <span className="text-xs font-bold text-slate-300">{listing.quantity} {listing.unit}</span>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <DollarSign className="w-4 h-4 text-slate-500" />
                    <div>
                      <span className="text-[9px] uppercase text-slate-500 block font-semibold leading-none">Base Value</span>
                      <span className="text-xs font-bold text-slate-300">${listing.price}/{listing.unit.substring(0, 3)}</span>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <MapPin className="w-4 h-4 text-slate-500" />
                    <div>
                      <span className="text-[9px] uppercase text-slate-500 block font-semibold leading-none">Origin Hub</span>
                      <span className="text-xs font-bold text-slate-300">{listing.location}</span>
                    </div>
                  </div>
                </div>

                <div className="flex items-center gap-4 pt-4 border-t border-slate-850 text-xs text-slate-400 font-semibold justify-between flex-wrap">
                  <div className="flex items-center gap-1.5">
                    <Building2 className="w-4 h-4 text-slate-500" />
                    <span>Producer: <strong className="text-slate-200">{listing.sellerName}</strong></span>
                  </div>
                  <div className="flex items-center gap-1.5">
                    <Calendar className="w-4 h-4 text-slate-500" />
                    <span>Listed on: {listing.createdAt}</span>
                  </div>
                </div>
              </CardContent>
            </Card>


          </div>

          {/* Checkout & Placing Offer Forms */}
          <div className="space-y-6">
            {currentRole === 'buyer' ? (
              <>
                {/* 1. Direct checkout */}
                <Card className="border-emerald-500/20 bg-emerald-950/5 relative overflow-hidden">
                  <div className="absolute top-0 right-0 w-24 h-24 bg-emerald-500/5 rounded-full blur-xl pointer-events-none" />
                  <CardHeader>
                    <CardTitle className="text-emerald-400 font-bold text-sm uppercase tracking-wider">
                      Immediate B2B Purchase
                    </CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-3 pt-2">
                    <div className="p-3 bg-slate-950/80 rounded-lg border border-slate-850 space-y-1">
                      <div className="flex justify-between text-xs text-slate-400">
                        <span>Total Quantity</span>
                        <span className="font-bold text-slate-200">{listing.quantity} {listing.unit}</span>
                      </div>
                      <div className="flex justify-between text-xs text-slate-400">
                        <span>Standard Price</span>
                        <span className="font-bold text-slate-200">${listing.price} / Ton</span>
                      </div>
                      <div className="h-px bg-slate-850 my-2" />
                      <div className="flex justify-between text-sm font-bold text-slate-100">
                        <span>Total Order Value</span>
                        <span className="text-emerald-400">${(listing.price * listing.quantity).toLocaleString()}</span>
                      </div>
                    </div>
                    
                    <Button
                      onClick={handleBuyDirect}
                      variant="primary"
                      className="w-full text-xs font-bold py-2.5 mt-2 bg-emerald-600 hover:bg-emerald-500 text-white"
                      icon={CheckCircle}
                    >
                      Buy Inventory Directly
                    </Button>
                    <span className="text-[9px] text-slate-500 text-center block">
                      Direct purchases are automatically matched with swift logistics dispatch.
                    </span>
                  </CardContent>
                </Card>

                {/* 2. Place Custom Offer */}
                <Card className="border-slate-900 bg-slate-950/30">
                  <CardHeader>
                    <CardTitle className="text-indigo-400 font-bold text-sm uppercase tracking-wider">
                      Negotiate / Place Bid
                    </CardTitle>
                  </CardHeader>
                  <CardContent className="pt-2">
                    <form onSubmit={handlePlaceOffer} className="space-y-4">
                      <Input
                        label={`Offered Price (per ${listing.unit.substring(0, 3)})`}
                        id="bid"
                        type="number"
                        min="1"
                        value={bidPrice}
                        onChange={(e) => setBidPrice(e.target.value)}
                        placeholder={listing.price}
                        required
                      />

                      <Input
                        label={`Required Volume (${listing.unit})`}
                        id="volume"
                        type="number"
                        step="0.1"
                        min="0.1"
                        max={listing.quantity}
                        value={quantity}
                        onChange={(e) => setQuantity(e.target.value)}
                        placeholder={listing.quantity}
                        required
                      />

                      <Input
                        label="Requested Delivery Date"
                        id="date"
                        type="date"
                        value={deliveryDate}
                        onChange={(e) => setDeliveryDate(e.target.value)}
                        required
                      />

                      <Button
                        type="submit"
                        variant="secondary"
                        className="w-full text-xs font-bold py-2.5 mt-2 bg-indigo-600 hover:bg-indigo-500 text-white"
                        icon={Truck}
                      >
                        Submit Purchase Bid
                      </Button>
                    </form>
                  </CardContent>
                </Card>
              </>
            ) : (
              <Card className="border-slate-850 bg-slate-950/30">
                <CardHeader>
                  <CardTitle className="text-slate-400 font-bold text-sm uppercase tracking-wider">
                    Buyer Interaction Desk
                  </CardTitle>
                </CardHeader>
                <CardContent className="text-center py-6 text-xs text-slate-500 space-y-3">
                  <p>
                    You are viewing this listing in simulated **{currentRole.toUpperCase()}** mode. Switch role simulator to **Buyer** in the header to place purchase offers or check out inventory.
                  </p>
                  <Button
                    onClick={() => setCurrentPage('marketplace')}
                    variant="outline"
                    className="text-xs px-4"
                  >
                    Back to Catalog
                  </Button>
                </CardContent>
              </Card>
            )}
          </div>
        </div>
      )}
    </div>
  );
};
