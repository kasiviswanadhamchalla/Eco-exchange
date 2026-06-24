import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { FileClock, Search, ShieldCheck, Database, Calendar } from 'lucide-react';

export const AuditViewer = () => {
  const { auditLogs } = useApp();
  const [searchTerm, setSearchTerm] = useState('');
  const [actionFilter, setActionFilter] = useState('ALL');

  const actions = ['ALL', 'ORGANIZATION_REGISTER', 'ORGANIZATION_VERIFY', 'LISTING_CREATE', 'LISTING_DELETE', 'LISTING_MODERATE', 'OFFER_PLACE', 'OFFER_DECISION', 'DIRECT_PURCHASE', 'SHIPMENT_CREATE', 'SHIPMENT_UPDATE', 'SHIPMENT_ACCEPT'];

  const filteredLogs = auditLogs.filter(log => {
    const matchesSearch = log.details.toLowerCase().includes(searchTerm.toLowerCase()) ||
                          log.user.toLowerCase().includes(searchTerm.toLowerCase());
    
    const matchesAction = actionFilter === 'ALL' || log.action === actionFilter;

    return matchesSearch && matchesAction;
  });

  return (
    <div className="space-y-6 animate-fade-in-up">
      {/* Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-lg font-bold text-slate-100 m-0">Platform Security Audit Trail</h2>
          <p className="text-xs text-slate-400 mt-1">Platform activity log tracking all transactions and moderation actions</p>
        </div>
        <Badge variant="cyan" className="py-1 px-3 flex items-center gap-1">
          <Database className="w-3.5 h-3.5" /> Event-Driven Ledger
        </Badge>
      </div>

      {/* Filter Options */}
      <Card className="border-slate-900 bg-slate-950/20">
        <CardContent className="pt-5 flex flex-col sm:flex-row gap-4 items-center">
          <div className="relative w-full">
            <Search className="absolute left-3 top-3 h-4 w-4 text-slate-500" />
            <input
              type="text"
              placeholder="Search logs by keyword or organization name..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="glass-input pl-9 pr-4 py-2 w-full rounded-lg text-xs text-slate-100 placeholder-slate-500"
            />
          </div>
          
          <select
            value={actionFilter}
            onChange={(e) => setActionFilter(e.target.value)}
            className="glass-input px-3 py-2 w-full sm:w-64 rounded-lg text-xs font-semibold text-slate-300 cursor-pointer"
          >
            {actions.map((act) => (
              <option key={act} value={act}>
                {act.replace('_', ' ')}
              </option>
            ))}
          </select>
        </CardContent>
      </Card>

      {/* Logs List */}
      <div className="space-y-3">
        {filteredLogs.length === 0 ? (
          <div className="py-20 glass-card rounded-xl text-center flex flex-col items-center justify-center gap-2">
            <FileClock className="w-8 h-8 text-slate-700" />
            <h3 className="text-sm font-bold text-slate-300">No Logs Found</h3>
            <p className="text-xs text-slate-500">Refine search criteria or clear filters.</p>
          </div>
        ) : (
          filteredLogs.map((log) => (
            <Card key={log.id} className="border-slate-900/60 bg-slate-950/10 hover:bg-slate-900/10 transition-all">
              <CardContent className="p-4 flex flex-col sm:flex-row sm:items-center justify-between gap-3 text-left">
                
                {/* Log Header details */}
                <div className="space-y-1">
                  <div className="flex items-center gap-2 flex-wrap">
                    <span className="text-[10px] font-bold text-slate-400 font-mono">[{log.timestamp}]</span>
                    <Badge variant={log.action.includes('REGISTER') || log.action.includes('CREATE') ? 'success' : log.action.includes('VERIFY') || log.action.includes('DECISION') ? 'purple' : 'cyan'} className="text-[8px] font-mono tracking-tight font-bold">
                      {log.action}
                    </Badge>
                    <span className="text-[9px] bg-slate-900 px-1.5 py-0.5 rounded border border-slate-850 text-slate-500">
                      ID: {log.id}
                    </span>
                  </div>
                  <p className="text-xs font-semibold text-slate-200 mt-1 leading-relaxed">
                    {log.details}
                  </p>
                </div>

                {/* Actor */}
                <div className="shrink-0 flex items-center gap-1.5 text-xs text-slate-400 border-t border-slate-900 sm:border-0 pt-2 sm:pt-0">
                  <div className="w-2 h-2 rounded-full bg-indigo-500" />
                  <span>Actor: <strong className="text-slate-300 font-bold">{log.user}</strong></span>
                </div>

              </CardContent>
            </Card>
          ))
        )}
      </div>
    </div>
  );
};
