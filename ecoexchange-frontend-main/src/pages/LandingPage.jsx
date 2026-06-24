import React from 'react';
import { useApp } from '../context/AppContext';
import { ArrowRight, Leaf, ShieldCheck, Factory, RotateCcw, TrendingUp, Users } from 'lucide-react';
import { Button } from '../components/ui/Button';

export const LandingPage = () => {
  const { setCurrentPage, setCurrentRole } = useApp();

  const handleRoleStart = (role, nextPage) => {
    setCurrentRole(role);
    setCurrentPage(nextPage);
  };

  return (
    <div className="min-h-screen bg-grid-pattern py-12 px-6 lg:px-12 flex flex-col items-center justify-center relative overflow-hidden animate-fade-in-up">
      {/* Decorative glows */}
      <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-emerald-500/10 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-indigo-500/10 rounded-full blur-3xl pointer-events-none" />

      {/* Hero Header */}
      <div className="max-w-4xl text-center z-10">
        <div className="inline-flex items-center gap-2 bg-emerald-500/10 border border-emerald-500/30 px-3.5 py-1.5 rounded-full text-xs text-emerald-400 font-semibold tracking-wider uppercase mb-6 shadow-md shadow-emerald-500/5">
          <Leaf className="w-3.5 h-3.5" /> Introducing Circular Industrial Economics
        </div>
        
        <h1 className="text-4xl md:text-6xl font-extrabold text-slate-100 tracking-tight leading-tight m-0 mb-6">
          Connecting Industrial Waste with <br />
          <span className="gradient-text-green text-glow-emerald">Secondary Resource Buyers</span>
        </h1>
        
        <p className="text-base md:text-lg text-slate-400 max-w-2xl mx-auto leading-relaxed mb-10">
          IndustryConnect is an event-driven B2B circular marketplace bridging manufacturing waste producers (Sellers) with recyclers, construction firms, and cement manufacturers (Buyers) under verified audit trails.
        </p>

        {/* Call to Actions */}
        <div className="flex flex-col sm:flex-row items-center justify-center gap-4 mb-16">
          <Button
            onClick={() => handleRoleStart('buyer', 'marketplace')}
            variant="primary"
            className="w-full sm:w-auto px-8 py-3.5 text-base"
            icon={ArrowRight}
          >
            Browse Marketplace
          </Button>
          <Button
            onClick={() => handleRoleStart('seller', 'register')}
            variant="outline"
            className="w-full sm:w-auto px-8 py-3.5 text-base border-slate-700 hover:border-slate-500"
          >
            Register Organization
          </Button>
        </div>
      </div>

      {/* Live Platform Stats */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-6 max-w-5xl w-full mb-20 z-10">
        {[
          { label: 'Waste Reused', value: '45,280 Tons', desc: 'Diverted from landfills', icon: RotateCcw, color: 'text-emerald-400' },
          { label: 'CO2 Offset Saved', value: '18,420 MT', desc: 'Verified emission reductions', icon: Leaf, color: 'text-emerald-400' },
          { label: 'Transaction Value', value: '$3.4M+', desc: 'Secured trade operations', icon: TrendingUp, color: 'text-indigo-400' },
          { label: 'Vetted Partners', value: '840+', desc: 'Active verified Orgs', icon: Users, color: 'text-indigo-400' }
        ].map((stat, idx) => (
          <div key={idx} className="glass-card rounded-xl p-5 border border-slate-800/80 hover:border-slate-700 transition-all text-center">
            <div className="flex items-center justify-center mb-3">
              <div className="p-2 rounded-lg bg-slate-900 border border-slate-800">
                <stat.icon className={`w-5 h-5 ${stat.color}`} />
              </div>
            </div>
            <div className="text-xl md:text-2xl font-bold text-slate-100">{stat.value}</div>
            <div className="text-xs font-semibold text-slate-400 mt-1 uppercase tracking-wider">{stat.label}</div>
            <div className="text-[10px] text-slate-500 mt-0.5">{stat.desc}</div>
          </div>
        ))}
      </div>

      {/* Main Waste Focus Category Showcase */}
      <div className="max-w-5xl w-full mb-16 z-10">
        <h2 className="text-xl md:text-2xl font-bold text-slate-200 text-center mb-2 tracking-tight">
          Supported Industrial Waste Streams
        </h2>
        <p className="text-xs md:text-sm text-slate-400 text-center mb-8">
          Seamlessly exchange, purchase, and trace certified raw resources across primary waste sectors.
        </p>

        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
          {[
            { name: 'Steel Scrap', count: '14 active', color: 'border-slate-800' },
            { name: 'Fly Ash', count: '28 active', color: 'border-emerald-500/20' },
            { name: 'Plastic Scrap', count: '19 active', color: 'border-slate-800' },
            { name: 'Textile Waste', count: '8 active', color: 'border-slate-800' },
            { name: 'Wood Waste', count: '11 active', color: 'border-slate-800' },
            { name: 'Glass Waste', count: '15 active', color: 'border-slate-800' }
          ].map((cat, idx) => (
            <div key={idx} className={`glass-card rounded-lg p-4 text-center border ${cat.color}`}>
              <div className="text-xs font-bold text-slate-200">{cat.name}</div>
              <div className="text-[10px] text-emerald-400 mt-1 font-semibold">{cat.count}</div>
            </div>
          ))}
        </div>
      </div>

      {/* Developer Sandbox Panel */}
      {/* Clean get-started card */}
      <div className="max-w-4xl w-full glass-card p-8 border border-slate-800/60 rounded-xl z-10 shadow-xl">
        <div className="flex flex-col md:flex-row items-center justify-between gap-6">
          <div className="text-left max-w-lg">
            <h3 className="text-base font-bold text-slate-200 flex items-center gap-2">
              <ShieldCheck className="w-5 h-5 text-emerald-400" /> Ready to get started?
            </h3>
            <p className="text-xs text-slate-400 mt-2 leading-relaxed">
              Join IndustryConnect as a waste material seller or a secondary raw resource buyer. Gain access to verified listings, secure B2B transactions, and real-time logistics tracking.
            </p>
          </div>
          <div className="flex flex-col sm:flex-row gap-3 w-full md:w-auto shrink-0">
            <Button
              onClick={() => setCurrentPage('register')}
              variant="primary"
              className="px-6 py-3 text-sm font-semibold"
            >
              Register Organization
            </Button>
            <Button
              onClick={() => setCurrentPage('login')}
              variant="outline"
              className="px-6 py-3 text-sm font-semibold border-slate-700"
            >
              Sign In
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};
