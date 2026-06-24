import React, { useState } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { Leaf, Award, Recycle, Building, Activity, ShieldCheck, Download } from 'lucide-react';
import { Button } from '../components/ui/Button';

export const Analytics = () => {
  const [activeTab, setActiveTab] = useState('carbon');

  // Month-by-month recycled tonnage data
  const monthlyData = [
    { month: 'Jan', tonnage: 34.2, carbon: 12.3, savings: 9400 },
    { month: 'Feb', tonnage: 45.6, carbon: 16.4, savings: 12500 },
    { month: 'Mar', tonnage: 52.1, carbon: 18.7, savings: 14300 },
    { month: 'Apr', tonnage: 61.9, carbon: 22.2, savings: 17000 },
    { month: 'May', tonnage: 78.4, carbon: 28.2, savings: 21500 },
    { month: 'Jun', tonnage: 92.5, carbon: 33.3, savings: 25400 }
  ];

  // Material split data
  const materialSplit = [
    { name: 'Steel Scrap', tonnage: 142.5, percentage: 38, color: 'bg-indigo-500' },
    { name: 'Fly Ash', tonnage: 120.0, percentage: 32, color: 'bg-emerald-500' },
    { name: 'Plastic Scrap', tonnage: 54.2, percentage: 14, color: 'bg-cyan-500' },
    { name: 'Glass Waste', tonnage: 32.1, percentage: 9, color: 'bg-purple-550' },
    { name: 'Other Scraps', tonnage: 25.0, percentage: 7, color: 'bg-slate-600' }
  ];

  const maxTonnage = Math.max(...monthlyData.map(d => d.tonnage));
  const maxCarbon = Math.max(...monthlyData.map(d => d.carbon));
  const maxSavings = Math.max(...monthlyData.map(d => d.savings));

  const handleExport = () => {
    alert('Generating ESG PDF Report...\nCircular Audit Trails: Verified.\nDownloaded.');
  };

  return (
    <div className="space-y-6 animate-fade-in-up">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-lg font-bold text-slate-100 m-0">Green Savings & Impact Analytics</h2>
          <p className="text-xs text-slate-400 mt-1">Audit-vetted ecological metrics and resource reallocation ledger</p>
        </div>
        <Button onClick={handleExport} variant="primary" icon={Download}>
          Export ESG Report
        </Button>
      </div>

      {/* Highlights Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="border-emerald-500/20 bg-emerald-950/5 relative overflow-hidden">
          <div className="absolute top-0 right-0 w-24 h-24 bg-emerald-500/5 rounded-full blur-xl" />
          <CardContent className="pt-5 flex items-start gap-4">
            <div className="p-3 bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 rounded-xl">
              <Leaf className="w-5 h-5" />
            </div>
            <div>
              <span className="text-[10px] uppercase font-bold tracking-wider text-slate-400">Net CO2 Footprint Offset</span>
              <h3 className="text-2xl font-black text-emerald-400 mt-1 leading-none">131.1 MT</h3>
              <p className="text-[10px] text-slate-400 mt-2 font-semibold">Equivalent to planting 2,160 saplings</p>
            </div>
          </CardContent>
        </Card>

        <Card className="border-indigo-500/20 bg-indigo-950/5 relative overflow-hidden">
          <div className="absolute top-0 right-0 w-24 h-24 bg-indigo-500/5 rounded-full blur-xl" />
          <CardContent className="pt-5 flex items-start gap-4">
            <div className="p-3 bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 rounded-xl">
              <Recycle className="w-5 h-5" />
            </div>
            <div>
              <span className="text-[10px] uppercase font-bold tracking-wider text-slate-400">Total Material Diverted</span>
              <h3 className="text-2xl font-black text-indigo-400 mt-1 leading-none">364.7 Tons</h3>
              <p className="text-[10px] text-slate-400 mt-2 font-semibold">Steel, Fly Ash, and Plastics repurposed</p>
            </div>
          </CardContent>
        </Card>

        <Card className="border-cyan-500/20 bg-cyan-950/5 relative overflow-hidden">
          <div className="absolute top-0 right-0 w-24 h-24 bg-cyan-500/5 rounded-full blur-xl" />
          <CardContent className="pt-5 flex items-start gap-4">
            <div className="p-3 bg-cyan-500/10 border border-cyan-500/20 text-cyan-400 rounded-xl">
              <Award className="w-5 h-5" />
            </div>
            <div>
              <span className="text-[10px] uppercase font-bold tracking-wider text-slate-400">Disposal cost savings</span>
              <h3 className="text-2xl font-black text-cyan-400 mt-1 leading-none">$96,700</h3>
              <p className="text-[10px] text-slate-400 mt-2 font-semibold">Reduced corporate landfill fees</p>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Monthly Bar charts and Material Split layout */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        
        {/* Monthly Chart (Left - 2 Cols) */}
        <Card className="col-span-1 lg:col-span-2 border-slate-900">
          <CardHeader className="flex justify-between items-center">
            <CardTitle className="text-xs uppercase font-bold text-slate-400 tracking-wider">
              ESG Progression Tracking (2026)
            </CardTitle>
            <div className="flex gap-1.5 bg-slate-950/60 p-1 border border-slate-850 rounded-lg">
              <button
                onClick={() => setActiveTab('tonnage')}
                className={`text-[10px] px-2.5 py-1 rounded font-semibold ${
                  activeTab === 'tonnage' ? 'bg-emerald-500 text-slate-950 font-bold' : 'text-slate-400 hover:text-slate-200'
                }`}
              >
                Tonnage
              </button>
              <button
                onClick={() => setActiveTab('carbon')}
                className={`text-[10px] px-2.5 py-1 rounded font-semibold ${
                  activeTab === 'carbon' ? 'bg-emerald-500 text-slate-950 font-bold' : 'text-slate-400 hover:text-slate-200'
                }`}
              >
                CO2 Offset
              </button>
              <button
                onClick={() => setActiveTab('savings')}
                className={`text-[10px] px-2.5 py-1 rounded font-semibold ${
                  activeTab === 'savings' ? 'bg-emerald-500 text-slate-950 font-bold' : 'text-slate-400 hover:text-slate-200'
                }`}
              >
                Savings
              </button>
            </div>
          </CardHeader>
          
          <CardContent className="pt-6">
            <div className="h-64 flex items-end justify-between gap-3 pt-6 px-4">
              {monthlyData.map((d, idx) => {
                let heightPercent = 0;
                let displayVal = '';
                if (activeTab === 'tonnage') {
                  heightPercent = (d.tonnage / maxTonnage) * 100;
                  displayVal = `${d.tonnage}T`;
                } else if (activeTab === 'carbon') {
                  heightPercent = (d.carbon / maxCarbon) * 100;
                  displayVal = `${d.carbon}MT`;
                } else {
                  heightPercent = (d.savings / maxSavings) * 100;
                  displayVal = `$${(d.savings / 1000).toFixed(1)}k`;
                }

                return (
                  <div key={idx} className="flex-1 flex flex-col items-center gap-2 group relative">
                    <span className="text-[9px] font-bold text-emerald-400 opacity-0 group-hover:opacity-100 transition-opacity absolute -top-5">
                      {displayVal}
                    </span>
                    <div
                      className="w-full bg-gradient-to-t from-emerald-600/40 to-emerald-400/90 rounded-t-md hover:to-teal-300 transition-all duration-500 shadow-md shadow-emerald-500/10 cursor-pointer"
                      style={{ height: `${heightPercent}%`, minHeight: '8px' }}
                    />
                    <span className="text-[10px] font-bold text-slate-500">{d.month}</span>
                  </div>
                );
              })}
            </div>
          </CardContent>
        </Card>

        {/* Categories breakdown (Right - 1 Col) */}
        <Card className="col-span-1 border-slate-900 bg-slate-950/20">
          <CardHeader>
            <CardTitle className="text-xs uppercase font-bold text-slate-400 tracking-wider">
              Material Split Breakdown
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {materialSplit.map((mat, idx) => (
              <div key={idx} className="space-y-1.5 text-left font-semibold">
                <div className="flex items-center justify-between text-xs">
                  <span className="text-slate-300">{mat.name}</span>
                  <span className="text-slate-400 text-[10px]">{mat.tonnage} Tons ({mat.percentage}%)</span>
                </div>
                <div className="w-full bg-slate-900 h-2.5 rounded-full overflow-hidden border border-slate-850">
                  <div
                    className={`${mat.color} h-full rounded-full`}
                    style={{ width: `${mat.percentage}%` }}
                  />
                </div>
              </div>
            ))}
          </CardContent>
        </Card>

      </div>
    </div>
  );
};
