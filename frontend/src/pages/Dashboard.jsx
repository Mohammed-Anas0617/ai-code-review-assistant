import { useNavigate } from 'react-router-dom'

function Dashboard() {
    const navigate = useNavigate()

    const handleLogout = () => {
        localStorage.removeItem('token')
        navigate('/')
    }

    return (
        <div className="min-h-screen bg-gray-100">
            <nav className="bg-white shadow px-6 py-4 flex justify-between items-center">
                <h1 className="text-xl font-bold text-blue-600">AI Code Review Assistant</h1>
                <button
                    onClick={handleLogout}
                    className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                >
                    Logout
                </button>
            </nav>

            <main className="p-6">
                <h2 className="text-2xl font-semibold mb-4">Welcome to your Dashboard</h2>
                <div className="bg-white rounded shadow p-6">
                    <p className="text-gray-600">
                        Your uploaded projects and review reports will appear here.
                    </p>
                </div>
            </main>
        </div>
    )
}

export default Dashboard