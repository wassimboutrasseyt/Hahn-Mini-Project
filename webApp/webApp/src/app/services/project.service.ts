import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Project, CreateProjectRequest } from '../models';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/projects';

  getProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.apiUrl);
  }

  getProject(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.apiUrl}/${id}`);
  }

  createProject(request: CreateProjectRequest): Observable<Project> {
    console.log(request);
    return this.http.post<Project>(this.apiUrl, request);
  }

  deleteProject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
