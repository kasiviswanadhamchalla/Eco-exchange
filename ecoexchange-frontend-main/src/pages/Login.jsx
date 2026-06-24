import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from '../components/ui/Card';
import { ArrowRight, Eye, EyeOff } from 'lucide-react';

export const Login = () => {
  const { setCurrentPage, loginUser } = useApp();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await loginUser(email, password);
    } catch (err) {
      // loginUser alerts and falls back if offline, so no crash
    }
  };

  const handleForgotPassword = (e) => {
    e.preventDefault();
    const cleanEmail = email.trim().toLowerCase();
    if (!cleanEmail) {
      alert('Please enter your email address first.');
      return;
    }

    // Build list of valid emails from organizations and standard accounts
    const standardEmails = [
      'seller@industryconnect.com',
      'buyer@industryconnect.com',
      'admin@industryconnect.com',
      'logistics@industryconnect.com',
    ];
    
    // Also derive mock emails from organizations names
    const orgEmails = organizations ? organizations.map(org => {
      const formattedName = org.name.toLowerCase().replace(/[^a-z0-9]/g, '');
      return `${formattedName}@industryconnect.com`;
    }) : [];

    const validEmails = [...standardEmails, ...orgEmails];

    // Check if entered email matches any valid email (either exact match, or username match)
    const isEmailValid = validEmails.includes(cleanEmail) || 
      validEmails.some(valid => {
        const username = valid.split('@')[0];
        return cleanEmail === username || cleanEmail.startsWith(username + '@');
      });

    if (isEmailValid) {
      alert(`A password reset link has been sent to ${email}.`);
    } else {
      alert('Your email is not present in our database.');
    }
  };

  return (
    <div className="min-h-[80vh] flex items-center justify-center p-6 bg-grid-pattern animate-fade-in-up">
      <div className="absolute top-1/3 left-1/2 w-80 h-80 bg-emerald-500/5 rounded-full blur-3xl pointer-events-none -translate-x-1/2" />

      <Card className="max-w-md w-full border border-slate-800/80 shadow-2xl relative z-10">
        <CardHeader className="text-center pb-2">
          <CardTitle className="text-xl font-bold">Sign In</CardTitle>
          <p className="text-xs text-slate-400 mt-1">Access your IndustryConnect account</p>
        </CardHeader>

        <CardContent className="mt-4">
          <form onSubmit={handleLogin} className="space-y-4">
            <Input
              label="Email"
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="name@company.com"
            />

            <div className="relative">
              <Input
                label="Password"
                id="password"
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••••••"
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3.5 bottom-3 text-slate-600 hover:text-slate-400 focus:outline-none focus:ring-0"
              >
                {showPassword ? (
                  <Eye className="w-4 h-4" />
                ) : (
                  <EyeOff className="w-4 h-4" />
                )}
              </button>
            </div>

            <div className="flex items-center justify-between text-xs font-semibold">
              <label className="flex items-center gap-1.5 text-slate-400 cursor-pointer select-none">
                <input type="checkbox" className="rounded border-slate-800 text-emerald-600 bg-slate-950 focus:ring-0" defaultChecked />
                <span>Remember me</span>
              </label>
              <a href="#" onClick={handleForgotPassword} className="text-emerald-400 hover:underline">
                Forgot password?
              </a>
            </div>

            <Button type="submit" variant="primary" className="w-full mt-2" icon={ArrowRight}>
              Sign In
            </Button>
          </form>
        </CardContent>

        <CardFooter className="flex-col gap-2 justify-center border-t border-slate-850 mt-6 pt-4 text-xs font-semibold text-slate-400">
          <div>
            Don't have an account?{' '}
            <button
              onClick={() => setCurrentPage('register')}
              className="text-emerald-400 hover:underline font-bold"
            >
              Register
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
      </Card>
    </div>
  );
};
