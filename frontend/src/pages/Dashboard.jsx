import { useNavigate } from 'react-router-dom'
import AnalysisReport from '../components/AnalysisReport';
import { useState, useEffect } from "react";
import { getMyProjects, deleteProject } from '../services/projectService';

function Dashboard() {
    const navigate = useNavigate()
    const [selectedProjectId, setSelectedProjectId] = useState(null);
    const [projects, setProjects] = useState([]);

    useEffect(() => {
        const fetchProjects = async () => {
            try {
                const data = await getMyProjects();
                setProjects(data);
            } catch (err) {
                console.error('Failed to load projects', err);
            }
        };
        fetchProjects();
    }, []);

    const handleDelete = async (e, projectId) => {
        e.stopPropagation(); // prevent triggering the button's onClick for selecting the project
        if (!window.confirm('Delete this project? This cannot be undone.')) return;

        try {
            await deleteProject(projectId);
            setProjects((prev) => prev.filter((p) => p.id !== projectId));
            if (selectedProjectId === projectId) setSelectedProjectId(null);
        } catch (err) {
            console.error('Failed to delete project', err);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token')
        navigate('/')
    }

    return (
        <div className="min-h-screen bg-charcoal text-slate-200 font-sans">
            <nav className="bg-panel border-b border-slate-800 px-6 py-4 flex justify-between items-center">
                <h1 className="text-xl font-bold text-accent font-mono">AI Code Review Assistant</h1>
                <button
                    onClick={handleLogout}
                    className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                >
                    Logout
                </button>
            </nav>

            <main className="p-6">
                <h2 className="text-2xl font-semibold mb-4 text-slate-100">Welcome to your Dashboard</h2>
                <div className="bg-panel border border-slate-800 rounded p-6">
                    <p className="text-slate-400 mb-2 font-mono text-sm uppercase tracking-wide">Your Projects:</p>
                    <div className="space-y-2 mb-4">
                        {projects.length === 0 ? (
                            <p className="text-slate-500">No projects uploaded yet.</p>
                        ) : (
                            projects.map((project) => (
                                <div
                                    key={project.id}
                                    onClick={() => setSelectedProjectId(project.id)}
                                    className={`flex items-center justify-between w-full px-4 py-3 rounded border font-mono text-sm transition-colors cursor-pointer ${
                                        selectedProjectId === project.id
                                            ? 'bg-accent/10 border-accent text-accent'
                                            : 'bg-charcoal border-slate-800 text-slate-300 hover:border-slate-600'
                                    }`}
                                >
        <span>
            {project.projectName} <span className="text-slate-500">(ID: {project.id})</span>
        </span>
                                    <button
                                        onClick={(e) => handleDelete(e, project.id)}
                                        className="text-slate-500 hover:text-err-spot text-xs border border-slate-700 hover:border-err-spot rounded px-2 py-1 transition-colors"
                                    >
                                        Delete
                                    </button>
                                </div>
                            ))
                        )}
                    </div>
                    {selectedProjectId && <AnalysisReport projectId={selectedProjectId} />}
                </div>
            </main>
        </div>
    )
}

export default Dashboard