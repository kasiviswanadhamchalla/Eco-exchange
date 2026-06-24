import React from 'react';

export const Input = ({
  label,
  id,
  type = 'text',
  placeholder,
  value,
  onChange,
  className = '',
  required = false,
  error,
  ...props
}) => {
  return (
    <div className={`flex flex-col gap-1.5 w-full ${className}`}>
      {label && (
        <label htmlFor={id} className="text-xs font-semibold text-slate-400 uppercase tracking-wider">
          {label} {required && <span className="text-rose-500">*</span>}
        </label>
      )}
      <input
        type={type}
        id={id}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        required={required}
        className={`glass-input w-full rounded-lg px-3.5 py-2.5 text-sm text-slate-100 placeholder-slate-500 transition-all ${
          error ? 'border-rose-500/50 focus:border-rose-500/80 focus:ring-rose-500/20' : ''
        }`}
        {...props}
      />
      {error && <span className="text-xs text-rose-400 mt-1">{error}</span>}
    </div>
  );
};

export const TextArea = ({
  label,
  id,
  placeholder,
  value,
  onChange,
  rows = 3,
  className = '',
  required = false,
  error,
  ...props
}) => {
  return (
    <div className={`flex flex-col gap-1.5 w-full ${className}`}>
      {label && (
        <label htmlFor={id} className="text-xs font-semibold text-slate-400 uppercase tracking-wider">
          {label} {required && <span className="text-rose-500">*</span>}
        </label>
      )}
      <textarea
        id={id}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        rows={rows}
        required={required}
        className={`glass-input w-full rounded-lg px-3.5 py-2.5 text-sm text-slate-100 placeholder-slate-500 transition-all ${
          error ? 'border-rose-500/50 focus:border-rose-500/80 focus:ring-rose-500/20' : ''
        }`}
        {...props}
      />
      {error && <span className="text-xs text-rose-400 mt-1">{error}</span>}
    </div>
  );
};

export const Select = ({
  label,
  id,
  value,
  onChange,
  options = [],
  className = '',
  required = false,
  error,
  ...props
}) => {
  return (
    <div className={`flex flex-col gap-1.5 w-full ${className}`}>
      {label && (
        <label htmlFor={id} className="text-xs font-semibold text-slate-400 uppercase tracking-wider">
          {label} {required && <span className="text-rose-500">*</span>}
        </label>
      )}
      <div className="relative">
        <select
          id={id}
          value={value}
          onChange={onChange}
          required={required}
          className={`glass-input w-full rounded-lg px-3.5 py-2.5 text-sm text-slate-100 transition-all appearance-none cursor-pointer pr-10`}
          {...props}
        >
          {options.map((opt) => (
            <option key={opt.value} value={opt.value} className="bg-slate-900 text-slate-200">
              {opt.label}
            </option>
          ))}
        </select>
        <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-3.5 text-slate-400">
          <svg className="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
            <path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z"/>
          </svg>
        </div>
      </div>
      {error && <span className="text-xs text-rose-400 mt-1">{error}</span>}
    </div>
  );
};
