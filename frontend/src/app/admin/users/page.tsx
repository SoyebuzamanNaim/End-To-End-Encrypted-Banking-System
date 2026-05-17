'use client';

import { useEffect, useState } from 'react';

type User = {
  id: string;
  fullName: string;
  email: string;
  role: string;
};

export default function AdminUsersPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // In production, this fetch would pass the Admin JWT to the backend.
    // The backend uses Application-Level Encryption to decrypt these values for the Admin.
    // For demonstration, we simulate the decrypted response from the backend.
    setTimeout(() => {
      setUsers([
        { id: 'uuid-1', fullName: 'Alice Johnson', email: 'alice@example.com', role: 'USER' },
        { id: 'uuid-2', fullName: 'Bob Smith', email: 'bob@example.com', role: 'USER' },
        { id: 'uuid-3', fullName: 'Admin System', email: 'admin@vault.com', role: 'ADMIN' },
      ]);
      setLoading(false);
    }, 1000);
  }, []);

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">User Management</h1>
        <span className="px-3 py-1 text-xs font-medium text-emerald-400 bg-emerald-400/10 rounded-full border border-emerald-400/20">
          Decrypted via App-Level Key
        </span>
      </div>

      <div className="overflow-hidden bg-zinc-900 border border-zinc-800 rounded-2xl">
        <table className="min-w-full divide-y divide-zinc-800">
          <thead className="bg-zinc-950">
            <tr>
              <th scope="col" className="px-6 py-4 text-left text-xs font-medium text-zinc-400 uppercase tracking-wider">User ID</th>
              <th scope="col" className="px-6 py-4 text-left text-xs font-medium text-zinc-400 uppercase tracking-wider">Full Name</th>
              <th scope="col" className="px-6 py-4 text-left text-xs font-medium text-zinc-400 uppercase tracking-wider">Email</th>
              <th scope="col" className="px-6 py-4 text-left text-xs font-medium text-zinc-400 uppercase tracking-wider">Role</th>
              <th scope="col" className="px-6 py-4 text-right text-xs font-medium text-zinc-400 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-zinc-800">
            {loading ? (
              <tr>
                <td colSpan={5} className="px-6 py-12 text-center text-zinc-500">Decrypting user data...</td>
              </tr>
            ) : (
              users.map((user) => (
                <tr key={user.id} className="hover:bg-zinc-800/50 transition-colors">
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-zinc-500 font-mono">{user.id.substring(0, 8)}...</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-white">{user.fullName}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-zinc-400">{user.email}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${user.role === 'ADMIN' ? 'bg-indigo-500/10 text-indigo-400 border border-indigo-500/20' : 'bg-zinc-800 text-zinc-400 border border-zinc-700'}`}>
                      {user.role}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button className="text-rose-500 hover:text-rose-400 transition-colors">Suspend</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
