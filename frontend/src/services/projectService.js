import axios from './api';

export const getMyProjects = async () => {
    const response = await axios.get('/projects');
    return response.data;
};

export const deleteProject = async (projectId) => {
    await axios.delete(`/projects/${projectId}`);
};