export default function AdminDashboard() {
  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold">Admin Overview</h1>
      
      <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
        <div className="p-6 bg-zinc-900 rounded-2xl border border-zinc-800">
          <p className="text-sm font-medium text-zinc-400">Total Users</p>
          <p className="mt-2 text-4xl font-semibold text-white">1,248</p>
        </div>
        <div className="p-6 bg-zinc-900 rounded-2xl border border-zinc-800">
          <p className="text-sm font-medium text-zinc-400">Total Transactions (24h)</p>
          <p className="mt-2 text-4xl font-semibold text-white">3,492</p>
        </div>
        <div className="p-6 bg-zinc-900 rounded-2xl border border-emerald-900/50">
          <p className="text-sm font-medium text-emerald-500">System Status</p>
          <p className="mt-2 text-2xl font-semibold text-emerald-400">E2EE Active</p>
        </div>
      </div>
    </div>
  );
}
