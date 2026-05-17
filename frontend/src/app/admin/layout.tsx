export default function AdminLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="flex h-screen bg-black text-white">
      {/* Admin Sidebar */}
      <aside className="w-64 bg-zinc-900 border-r border-zinc-800 flex flex-col">
        <div className="p-6">
          <h2 className="text-xl font-bold tracking-tight text-emerald-500">
            Vault Admin
          </h2>
        </div>
        <nav className="flex-1 px-4 space-y-2">
          <a
            href="/admin"
            className="flex items-center px-4 py-3 text-sm font-medium rounded-xl bg-zinc-800 text-white"
          >
            Dashboard
          </a>
          <a
            href="/admin/users"
            className="flex items-center px-4 py-3 text-sm font-medium rounded-xl text-zinc-400 hover:bg-zinc-800 hover:text-white transition-colors"
          >
            User Management
          </a>
        </nav>
      </aside>

      {/* Main Content */}
      <main className="flex-1 overflow-y-auto">
        <div className="p-10">{children}</div>
      </main>
    </div>
  );
}
