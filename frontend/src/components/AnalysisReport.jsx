import { useState, useEffect } from 'react';
import { getFullAnalysis } from '../services/analysisService';
import { Bar } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Tooltip,
    Legend,
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Tooltip, Legend);

const severityColors = {
    yellow: 'border-ok-check',
    orange: 'border-warn-pmd',
    red: 'border-err-spot',
};

const ViolationSection = ({ title, items, color }) => (
    <div className="mb-6">
        <h3 className="font-sans font-semibold mb-2 text-slate-300 text-sm uppercase tracking-wide">
            {title} <span className="text-slate-500">({items.length})</span>
        </h3>
        {items.length === 0 ? (
            <p className="text-slate-600 text-sm italic">No issues found.</p>
        ) : (
            <ul className="space-y-1">
                {items.map((item, idx) => (
                    <li
                        key={idx}
                        className={`border-l-2 ${severityColors[color]} pl-3 py-1 text-sm text-slate-300`}
                    >
                        {item}
                    </li>
                ))}
            </ul>
        )}
    </div>
);

const AnalysisReport = ({ projectId }) => {
    const [report, setReport] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchReport = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await getFullAnalysis(projectId);
            setReport(data);
        } catch (err) {
            setError('Failed to load analysis report.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (projectId) {
            fetchReport();
        }
    }, [projectId]);
    if (loading) {
        return (
            <div className="p-6 border border-slate-800 rounded-lg bg-charcoal mt-4 flex items-center gap-3">
                <div className="w-4 h-4 border-2 border-accent border-t-transparent rounded-full animate-spin"></div>
                <p className="text-slate-400 font-mono text-sm">Analyzing project...</p>
            </div>
        );
    }
    if (error) {
        return (
            <div className="p-6 border border-err-spot/40 rounded-lg bg-charcoal mt-4">
                <p className="text-err-spot font-mono text-sm">{error}</p>
            </div>
        );
    }
    if (!report) return null;
    const score = Math.max(
        100 -
        report.checkstyleViolations.length * 2 -
        report.pmdViolations.length * 3 -
        report.spotbugsViolations.length * 5,
        0
    );

    const chartData = {
        labels: ['Checkstyle', 'PMD', 'SpotBugs'],
        datasets: [
            {
                label: 'Violations',
                data: [
                    report.checkstyleViolations.length,
                    report.pmdViolations.length,
                    report.spotbugsViolations.length,
                ],
                backgroundColor: ['#facc15', '#fb923c', '#ef4444'],
            },
        ],
    };

    return (
        <div className="p-6 border border-slate-800 rounded-lg bg-charcoal mt-4 font-mono">
            <div className="flex items-center justify-between mb-2">
                <h2 className="text-lg font-semibold text-slate-300 truncate">
                    <span className="text-slate-500">// </span>{report.fileName}
                </h2>
                <button
                    onClick={fetchReport}
                    className="text-xs font-mono text-accent border border-accent/40 rounded px-3 py-1 hover:bg-accent/10 transition-colors whitespace-nowrap ml-3"
                >
                    Re-run analysis
                </button>
            </div>
            <p className="text-2xl font-bold mb-4 font-sans">
                Quality Score:{' '}
                <span className={score >= 70 ? 'text-ok-check' : 'text-err-spot'}>{score}/100</span>
            </p>

            <div className="mb-6 max-w-md">
                <Bar data={chartData} />
            </div>

            <ViolationSection title="Checkstyle Violations" items={report.checkstyleViolations} color="yellow" />
            <ViolationSection title="PMD Violations" items={report.pmdViolations} color="orange" />
            <ViolationSection title="SpotBugs Violations" items={report.spotbugsViolations} color="red" />
        </div>
    );

    return (
        <div className="p-4 border rounded-lg shadow-sm bg-white mt-4">
            <h2 className="text-xl font-semibold mb-4">Analysis Report: {report.fileName}</h2>

            <ViolationSection title="Checkstyle Violations" items={report.checkstyleViolations} color="yellow" />
            <ViolationSection title="PMD Violations" items={report.pmdViolations} color="orange" />
            <ViolationSection title="SpotBugs Violations" items={report.spotbugsViolations} color="red" />
        </div>
    );
};

export default AnalysisReport;