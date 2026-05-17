'use client';

import { useEffect, useState } from 'react';
import { fetchPublicKey, secureFetch } from '@/lib/secureFetch';

export default function Dashboard() {
  const [balance, setBalance] = useState<string | null>(null);
  const [publicKey, setPublicKey] = useState<string | null>(null);

  useEffect(() => {
    // Phase 1: Fetch the Public RSA Key on mount
    fetchPublicKey()
      .then(key => {
        setPublicKey(key);
      })
      .catch(err => console.error("Failed to fetch public key", err));
  }, []);

  const handleSecureTransfer = async () => {
    if (!publicKey) return alert("Public Key not loaded yet!");

    try {
      const payload = { toAccount: "987654321", amount: "500.00" };
      const jwtToken = "mock.jwt.token"; // In reality, get this from auth context

      // Perform secure encrypted POST
      const res = await secureFetch('http://localhost:8080/api/v1/account/transfer', payload, publicKey, jwtToken);
      const data = await res.json();
      
      console.log("Transfer successful", data);
      alert(data.message);
    } catch (err) {
      console.error("Transfer failed", err);
    }
  };

  const fetchBalance = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/v1/account/balance');
      const data = await res.json();
      setBalance(data.balance);
    } catch (err) {
      console.error("Failed to fetch balance", err);
    }
  };

  useEffect(() => {
    fetchBalance();
  }, []);

  return (
    <main className="min-h-screen bg-gray-900 text-white p-8 font-sans">
      <div className="max-w-4xl mx-auto space-y-8">
        <header className="flex justify-between items-center pb-6 border-b border-gray-800">
          <h1 className="text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-emerald-400">
            Secure Banking Dashboard
          </h1>
          <div className="text-sm text-gray-400 flex items-center">
            E2EE Active <span className="inline-block w-2 h-2 rounded-full bg-emerald-500 ml-2 animate-pulse"></span>
          </div>
        </header>

        <section className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Balance Card */}
          <div className="bg-gray-800 p-6 rounded-2xl border border-gray-700 shadow-xl relative overflow-hidden group">
            <div className="absolute inset-0 bg-gradient-to-br from-blue-500/10 to-emerald-500/10 opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
            <h2 className="text-gray-400 text-sm uppercase tracking-wider mb-2">Available Balance</h2>
            <div className="text-5xl font-light mb-6">
              ${balance !== null ? balance : "---"}
            </div>
            <div className="text-xs text-gray-500">
              Data encrypted at rest via AES-256-GCM
            </div>
          </div>

          {/* Transfer Card */}
          <div className="bg-gray-800 p-6 rounded-2xl border border-gray-700 shadow-xl">
            <h2 className="text-gray-400 text-sm uppercase tracking-wider mb-4">Quick Transfer</h2>
            <div className="space-y-4">
              <div>
                <label className="block text-xs text-gray-500 mb-1">To Account</label>
                <input type="text" value="987654321" readOnly className="w-full bg-gray-900 border border-gray-700 rounded-lg p-3 text-white focus:outline-none focus:border-blue-500" />
              </div>
              <div>
                <label className="block text-xs text-gray-500 mb-1">Amount</label>
                <input type="text" value="$ 500.00" readOnly className="w-full bg-gray-900 border border-gray-700 rounded-lg p-3 text-white focus:outline-none focus:border-blue-500" />
              </div>
              <button 
                onClick={handleSecureTransfer}
                disabled={!publicKey}
                className="w-full py-3 px-4 bg-blue-600 hover:bg-blue-500 text-white font-medium rounded-lg transition-colors shadow-lg shadow-blue-500/25 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" /></svg>
                {publicKey ? 'Send Securely (JWE)' : 'Loading Key...'}
              </button>
            </div>
          </div>
        </section>
      </div>
    </main>
  );
}
