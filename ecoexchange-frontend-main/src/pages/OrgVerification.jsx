import React from 'react';
import { useApp } from '../context/AppContext';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { Building, ShieldAlert, Check, X, FileText, Calendar } from 'lucide-react';

export const OrgVerification = () => {
  const { organizations, verifyOrganization } = useApp();

  const handleVerify = (id, approve) => {
    verifyOrganization(id, approve);
  };

  const simulateDocView = (_doc) => {
    // In production, this would open the document in a new tab
  };

  return (
    <div className="space-y-6 animate-fade-in-up">
      <div>
        <h2 className="text-lg font-bold text-slate-100 m-0">Organization Vetting Desk</h2>
        <p className="text-xs text-slate-400 mt-1">Review registered organizations and manage platform access</p>
      </div>

      <div className="glass-card rounded-xl overflow-hidden border border-slate-900">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-slate-950/60 border-b border-slate-850 text-slate-400 uppercase font-bold tracking-wider">
                <th className="p-4">Legal Enterprise Name</th>
                <th className="p-4">Registration Type</th>
                <th className="p-4">Target waste Streams</th>
                <th className="p-4 text-center">Documents</th>
                <th className="p-4 text-center">Registered Date</th>
                <th className="p-4 text-center">Current Status</th>
                <th className="p-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-850 bg-slate-950/10 font-semibold text-slate-300">
              {organizations.map((org) => (
                <tr key={org.id} className="hover:bg-slate-900/30 transition-all">
                  <td className="p-4">
                    <span className="text-slate-100 font-bold block">{org.name}</span>
                    <span className="text-[10px] text-slate-500 font-medium">Org ID: {org.id}</span>
                  </td>
                  <td className="p-4 text-slate-300 font-bold">
                    {org.type}
                  </td>
                  <td className="p-4">
                    <div className="flex flex-wrap gap-1 max-w-[200px]">
                      {org.wasteFocus.map((wf) => (
                        <span key={wf} className="bg-slate-900 text-slate-400 text-[9px] px-1.5 py-0.5 rounded border border-slate-850">
                          {wf}
                        </span>
                      ))}
                    </div>
                  </td>
                  <td className="p-4 text-center">
                    <button
                      onClick={() => simulateDocView(org.docUrl)}
                      className="inline-flex items-center gap-1 text-[11px] text-indigo-400 hover:text-white hover:underline cursor-pointer"
                    >
                      <FileText className="w-3.5 h-3.5" /> View Docs
                    </button>
                  </td>
                  <td className="p-4 text-center text-slate-400">
                    <span className="inline-flex items-center gap-1">
                      <Calendar className="w-3.5 h-3.5 text-slate-600" />
                      {org.registeredAt}
                    </span>
                  </td>
                  <td className="p-4 text-center">
                    <Badge
                      variant={
                        org.status === 'verified'
                          ? 'success'
                          : org.status === 'pending'
                          ? 'warning'
                          : 'danger'
                      }
                      className="capitalize"
                    >
                      {org.status}
                    </Badge>
                  </td>
                  <td className="p-4 text-right">
                    <div className="inline-flex gap-1.5">
                      {org.status !== 'verified' && (
                        <Button
                          onClick={() => handleVerify(org.id, true)}
                          variant="primary"
                          className="py-1 px-2.5 text-[10px] bg-emerald-600/20 border border-emerald-500/30 text-emerald-400 hover:bg-emerald-600 hover:text-white"
                        >
                          <Check className="w-3 h-3" /> Approve
                        </Button>
                      )}
                      {org.status !== 'suspended' && (
                        <Button
                          onClick={() => handleVerify(org.id, false)}
                          variant="danger"
                          className="py-1 px-2.5 text-[10px] bg-rose-600/20 border border-rose-500/30 text-rose-400 hover:bg-rose-600 hover:text-white"
                        >
                          <X className="w-3 h-3" /> Suspend
                        </Button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
