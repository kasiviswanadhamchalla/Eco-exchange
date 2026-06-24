import React from 'react';

export const Card = ({ children, className = '', hoverable = false, ...props }) => {
  return (
    <div
      className={`glass-card rounded-xl p-5 ${
        hoverable ? 'glass-card-hover cursor-pointer' : ''
      } ${className}`}
      {...props}
    >
      {children}
    </div>
  );
};

export const CardHeader = ({ children, className = '' }) => {
  return (
    <div className={`border-b border-slate-800/80 pb-4 mb-4 flex items-center justify-between ${className}`}>
      {children}
    </div>
  );
};

export const CardTitle = ({ children, className = '' }) => {
  return (
    <h3 className={`text-lg font-semibold text-slate-100 tracking-tight ${className}`}>
      {children}
    </h3>
  );
};

export const CardContent = ({ children, className = '' }) => {
  return <div className={className}>{children}</div>;
};

export const CardFooter = ({ children, className = '' }) => {
  return (
    <div className={`border-t border-slate-800/80 pt-4 mt-4 flex items-center justify-end gap-2 ${className}`}>
      {children}
    </div>
  );
};
