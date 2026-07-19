import axios from './api'; // your existing configured axios instance with base URL + JWT interceptor

export const getFullAnalysis = async (projectId) => {
    const response = await axios.get(`/analysis/full?projectId=${projectId}`);
    return response.data;
};

