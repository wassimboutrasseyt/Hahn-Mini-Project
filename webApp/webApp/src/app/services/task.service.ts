import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task, CreateTaskRequest } from '../models';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/tasks';

  getTasks(projectId: number): Observable<Task[]> {
    const params = new HttpParams().set('projectId', projectId.toString());
    return this.http.get<Task[]>(this.apiUrl, { params });
  }

  createTask(request: CreateTaskRequest): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, request);
  }

  markCompleted(id: number): Observable<Task> {
    return this.http.patch<Task>(`${this.apiUrl}/${id}/complete`, {});
  }

  toggleCompleted(id: number): Observable<Task> {
    return this.http.patch<Task>(`${this.apiUrl}/${id}/toggle`, {});
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
