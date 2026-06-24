import React from 'react';

export const Button = ({
  children,
  onClick,
  type = 'button',
  variant = 'primary',
  className = '',
  disabled = false,
  icon: Icon = null,
  ...props
}) => {
  const baseStyle = 'inline-flex items-center justify-center gap-2 font-medium rounded-lg text-sm px-4 py-2.5 transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-[#070B14] disabled:opacity-50 disabled:cursor-not-allowed';
  
  const variants = {
    primary: 'bg-emerald-600 hover:bg-emerald-500 text-white shadow-lg shadow-emerald-950/20 focus:ring-emerald-500 active:scale-[0.98]',
    secondary: 'bg-indigo-600 hover:bg-indigo-500 text-white shadow-lg shadow-indigo-950/20 focus:ring-indigo-500 active:scale-[0.98]',
    outline: 'bg-transparent border border-slate-700 hover:bg-slate-800/50 hover:border-slate-500 text-slate-300 focus:ring-slate-500',
    danger: 'bg-rose-600 hover:bg-rose-500 text-white shadow-lg shadow-rose-950/20 focus:ring-rose-500 active:scale-[0.98]',
    ghost: 'bg-transparent hover:bg-slate-850 text-slate-300 hover:text-white focus:ring-slate-500',
  };

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${baseStyle} ${variants[variant]} ${className}`}
      {...props}
    >
      {Icon && <Icon className="w-4 h-4" />}
      {children}
    </button>
  );
};
