import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProjectService } from '../../services/project.service';
import { AuthService } from '../../services/auth.service';
import { Project } from '../../models';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule,  FormsModule],
  template: `
    <div class="projects-container">
      <header class="header">
        <h1>My Projects</h1>
        <div class="header-actions">
          <span class="user-email">{{ userEmail() }}</span>
          <button class="btn btn-secondary" (click)="logout()">Logout</button>
        </div>
      </header>

      <div class="create-project-section">
        <h3>Create New Project</h3>
        <form (ngSubmit)="createProject()" class="create-form">
          <input
            type="text"
            [(ngModel)]="newProject.title"
            name="title"
            placeholder="Project title"
            class="form-control"
            required
          />
          <input
            type="text"
            [(ngModel)]="newProject.description"
            name="description"
            placeholder="Description (optional)"
            class="form-control"
          />
          <button type="submit" class="btn btn-primary" [disabled]="!newProject.title">
            Create Project
          </button>
        </form>
      </div>

      @if (loading()) {
        <div class="loading">Loading projects...</div>
      }

      @if (errorMessage()) {
        <div class="alert alert-danger">{{ errorMessage() }}</div>
      }

      <div class="projects-grid">
        @for (project of projects(); track project.id) {
          <div class="project-card" (click)="viewProject(project.id)">
            <div class="project-header">
              <h3>{{ project.title }}</h3>
              <button
                class="btn-delete"
                (click)="deleteProject($event, project.id)"
                title="Delete project"
              >
                ×
              </button>
            </div>
            @if (project.description) {
              <p class="project-description">{{ project.description }}</p>
            }
            <div class="project-stats">
              <div class="stat">
                <span class="stat-label">Total Tasks:</span>
                <span class="stat-value">{{ project.totalTasks }}</span>
              </div>
              <div class="stat">
                <span class="stat-label">Completed:</span>
                <span class="stat-value">{{ project.completedTasks }}</span>
              </div>
            </div>
            <div class="progress-bar">
              <div
                class="progress-fill"
                [style.width.%]="project.progressPercentage"
              ></div>
            </div>
            <div class="progress-text">{{ project.progressPercentage }}% Complete</div>
          </div>
        } @empty {
          <div class="empty-state">
            <p>No projects yet. Create your first project!</p>
          </div>
        }
      </div>
    </div>
  `,
  styles: [`
    .projects-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 2rem;
    }

    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 2rem;
    }

    .header-actions {
      display: flex;
      align-items: center;
      gap: 1rem;
    }

    .user-email {
      color: #666;
      font-size: 0.9rem;
    }

    .create-project-section {
      background: white;
      padding: 1.5rem;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      margin-bottom: 2rem;
    }

    .create-form {
      display: flex;
      gap: 1rem;
      margin-top: 1rem;
    }

    .form-control {
      flex: 1;
      padding: 0.75rem;
      border: 1px solid #ddd;
      border-radius: 5px;
      font-size: 1rem;
    }

    .btn {
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      font-size: 1rem;
      transition: all 0.3s;
    }

    .btn-primary {
      background: #667eea;
      color: white;
    }

    .btn-primary:hover:not(:disabled) {
      background: #5568d3;
    }

    .btn-secondary {
      background: #6c757d;
      color: white;
    }

    .btn-secondary:hover {
      background: #5a6268;
    }

    .btn:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }

    .projects-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 1.5rem;
    }

    .project-card {
      background: white;
      padding: 1.5rem;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      cursor: pointer;
      transition: transform 0.2s, box-shadow 0.2s;
    }

    .project-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 5px 20px rgba(0,0,0,0.15);
    }

    .project-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 1rem;
    }

    .project-header h3 {
      margin: 0;
      color: #333;
    }

    .btn-delete {
      background: #dc3545;
      color: white;
      border: none;
      border-radius: 50%;
      width: 30px;
      height: 30px;
      cursor: pointer;
      font-size: 1.5rem;
      line-height: 1;
      transition: background 0.3s;
    }

    .btn-delete:hover {
      background: #c82333;
    }

    .project-description {
      color: #666;
      margin-bottom: 1rem;
    }

    .project-stats {
      display: flex;
      justify-content: space-between;
      margin-bottom: 1rem;
    }

    .stat {
      display: flex;
      flex-direction: column;
    }

    .stat-label {
      font-size: 0.85rem;
      color: #888;
    }

    .stat-value {
      font-size: 1.5rem;
      font-weight: bold;
      color: #667eea;
    }

    .progress-bar {
      height: 10px;
      background: #e9ecef;
      border-radius: 5px;
      overflow: hidden;
      margin-bottom: 0.5rem;
    }

    .progress-fill {
      height: 100%;
      background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
      transition: width 0.3s;
    }

    .progress-text {
      text-align: center;
      font-size: 0.9rem;
      color: #666;
    }

    .loading, .empty-state {
      text-align: center;
      padding: 3rem;
      color: #666;
    }

    .alert {
      padding: 1rem;
      border-radius: 5px;
      margin-bottom: 1rem;
    }

    .alert-danger {
      background: #fee;
      color: #c33;
      border: 1px solid #fcc;
    }
  `]
})
export class ProjectsComponent implements OnInit {
  private projectService = inject(ProjectService);
  private authService = inject(AuthService);
  private router = inject(Router);

  projects = signal<Project[]>([]);
  loading = signal(false);
  errorMessage = signal('');
  
  newProject = {
    title: '',
    description: ''
  };

  userEmail = signal('');

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.userEmail.set(user.email);
      }
    });
    this.loadProjects();
  }

  loadProjects() {
    this.loading.set(true);
    this.errorMessage.set('');

    this.projectService.getProjects().subscribe({
      next: (projects) => {
        this.projects.set(projects);
        this.loading.set(false);
      },
      error: (error) => {
        this.errorMessage.set('Failed to load projects');
        this.loading.set(false);
      }
    });
  }

  createProject() {
    if (!this.newProject.title.trim()) return;

    this.projectService.createProject(this.newProject).subscribe({
      next: () => {
        this.newProject = { title: '', description: '' };
        this.loadProjects();
      },
      error: (error) => {
        this.errorMessage.set('Failed to create project');
      }
    });
  }

  viewProject(id: number) {
    this.router.navigate(['/projects', id]);
  }

  deleteProject(event: Event, id: number) {
    event.stopPropagation();
    if (confirm('Are you sure you want to delete this project?')) {
      this.projectService.deleteProject(id).subscribe({
        next: () => {
          this.loadProjects();
        },
        error: (error) => {
          this.errorMessage.set('Failed to delete project');
        }
      });
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
