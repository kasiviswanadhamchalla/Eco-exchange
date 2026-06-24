import React from 'react';

export const Badge = ({ children, variant = 'info', className = '', ...props }) => {
  const baseStyle = 'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold tracking-wide border';
  
  const variants = {
    success: 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20',
    info: 'bg-indigo-500/10 text-indigo-400 border-indigo-500/20',
    warning: 'bg-amber-500/10 text-amber-400 border-amber-500/20',
    danger: 'bg-rose-500/10 text-rose-400 border-rose-500/20',
    slate: 'bg-slate-500/10 text-slate-400 border-slate-500/20',
    purple: 'bg-purple-500/10 text-purple-400 border-purple-500/20',
    cyan: 'bg-cyan-500/10 text-cyan-400 border-cyan-500/20',
  };

  return (
    <span className={`${baseStyle} ${variants[variant] || variants.info} ${className}`} {...props}>
      {children}
    </span>
  );
};
