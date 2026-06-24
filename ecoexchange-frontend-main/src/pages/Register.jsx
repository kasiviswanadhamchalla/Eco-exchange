import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { Button } from '../components/ui/Button';
import { Input, Select } from '../components/ui/Input';
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from '../components/ui/Card';
import { Check } from 'lucide-react';

export const Register = () => {
  const { registerOrganization, setCurrentPage } = useApp();
  
  const [name, setName] = useState('');
  const [type, setType] = useState('Seller');
  const [wasteFocus, setWasteFocus] = useState([]);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [contactName, setContactName] = useState('');
  const [gstNumber, setGstNumber] = useState('');
  const [success, setSuccess] = useState(false);

  const categories = [
    'Steel Scrap', 'Fly Ash', 'Plastic Scrap', 'Textile Waste', 'Wood Waste', 'Glass Waste'
  ];

  const handleCheckboxChange = (cat) => {
    if (wasteFocus.includes(cat)) {
      setWasteFocus(prev => prev.filter(c => c !== cat));
    } else {
      setWasteFocus(prev => [...prev, cat]);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await registerOrganization({
        name,
        type,
        wasteFocus,
        email,
        password,
        contactName,
        gstNumber,
        docUrl: 'uploaded-regulatory-verification.pdf'
      });
      setSuccess(true);
      setTimeout(() => {
        setCurrentPage('login');
      }, 3000);
    } catch (err) {
      alert("Registration failed: " + err.message);
    }
  };

  return (
    <div className="min-h-[85vh] flex items-center justify-center p-6 bg-grid-pattern animate-fade-in-up">
      <div className="absolute top-1/4 left-1/3 w-96 h-96 bg-emerald-500/5 rounded-full blur-3xl pointer-events-none" />

      <Card className="max-w-xl w-full border border-slate-800/80 shadow-2xl relative z-10">
        <CardHeader className="text-center pb-2">
          <CardTitle className="text-xl font-bold">Register Organization</CardTitle>
          <p className="text-xs text-slate-400 mt-1">Request seller/buyer verification to start trading</p>
        </CardHeader>

        <CardContent className="mt-4">
          {success ? (
            <div className="p-6 bg-emerald-500/10 border border-emerald-500/20 rounded-xl text-center space-y-4">
              <div className="relative w-16 h-16 mx-auto flex items-center justify-center">
                {/* Accent theme loading spinner */}
                <div className="absolute inset-0 rounded-full border-4 border-emerald-500/20 border-t-emerald-400 animate-spin" />
                {/* Pulsing check mark icon */}
                <div className="w-10 h-10 rounded-full bg-emerald-500/20 flex items-center justify-center text-emerald-400 animate-pulse">
                  <Check className="w-5 h-5" />
                </div>
              </div>
              <h3 className="text-base font-bold text-slate-200">Registration Request Submitted!</h3>
              <p className="text-xs text-slate-400 leading-relaxed">
                Your organization registration has been submitted successfully. Redirecting to sign in...
              </p>
            </div>
          ) : (
            <form onSubmit={handleSubmit} className="space-y-4">
              <Input
                label="Organization Legal Name"
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="EcoMaterials Supply Inc."
                required
              />

              <div className="grid grid-cols-2 gap-4">
                <Input
                  label="Contact Person Name"
                  id="contactName"
                  value={contactName}
                  onChange={(e) => setContactName(e.target.value)}
                  placeholder="John Doe"
                  required
                />
                <Input
                  label="GSTIN / Tax ID"
                  id="gstNumber"
                  value={gstNumber}
                  onChange={(e) => setGstNumber(e.target.value)}
                  placeholder="22AAAAA0000A1Z5"
                  required
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <Input
                  label="Business Email"
                  id="email"
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="admin@company.com"
                  required
                />
                <Input
                  label="Password"
                  id="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••••••"
                  required
                />
              </div>

              <Select
                label="Primary Marketplace Role"
                id="type"
                value={type}
                onChange={(e) => setType(e.target.value)}
                options={[
                  { label: 'Seller (Waste Material Producer)', value: 'Seller' },
                  { label: 'Buyer (Waste Recycler / Raw Input Consumer)', value: 'Buyer' }
                ]}
              />

              {/* Waste Stream Multi-select */}
              <div className="flex flex-col gap-1.5">
                <label className="text-xs font-semibold text-slate-400">
                  Waste Streams
                </label>
                <div className="grid grid-cols-2 gap-2 mt-1">
                  {categories.map((cat) => (
                    <label key={cat} className="flex items-center gap-2 text-xs text-slate-300 cursor-pointer hover:text-emerald-400 transition-colors duration-200 select-none">
                      <input
                        type="checkbox"
                        checked={wasteFocus.includes(cat)}
                        onChange={() => handleCheckboxChange(cat)}
                        className="rounded border-slate-700 bg-slate-900 text-emerald-500 focus:ring-0 transition-transform duration-200 hover:scale-110 active:scale-90"
                      />
                      {cat}
                    </label>
                  ))}
                </div>
              </div>

              <Button 
                type="submit" 
                variant="primary" 
                className="w-full mt-2 transition-all duration-300 hover:scale-[1.02] active:scale-[0.98] hover:shadow-lg hover:shadow-emerald-500/20 cursor-pointer"
              >
                Register Organization
              </Button>
            </form>
          )}
        </CardContent>

        {!success && (
          <CardFooter className="flex-col gap-2 justify-center border-t border-slate-850 mt-6 pt-4 text-xs font-semibold text-slate-400">
            <div>
              Already registered?{' '}
              <button
                onClick={() => setCurrentPage('login')}
                className="text-emerald-400 hover:underline font-bold"
              >
                Sign In
              </button>
            </div>
            <div>
              <button
                onClick={() => setCurrentPage('landing')}
                className="text-slate-500 hover:text-slate-300 hover:underline font-bold mt-1"
              >
                ← Back to Portal Landing
              </button>
            </div>
          </CardFooter>
        )}
      </Card>
    </div>
  );
};
