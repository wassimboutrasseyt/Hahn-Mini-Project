export interface User {
  id: number;
  email: string;
  role: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

export interface Project {
  id: number;
  title: string;
  description?: string;
  totalTasks: number;
  completedTasks: number;
  progressPercentage: number;
}

export interface Task {
  id: number;
  projectId: number;
  title: string;
  description?: string;
  dueDate?: string;
  completed: boolean;
}

export interface CreateProjectRequest {
  title: string;
  description?: string;
}

export interface CreateTaskRequest {
  projectId: number;
  title: string;
  description?: string;
  dueDate?: string;
}

export interface ErrorResponse {
  status: number;
  error: string;
  message: string;
  details?: string[];
  timestamp: string;
}
