import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProjectService } from '../../services/project.service';
import { TaskService } from '../../services/task.service';
import { Project, Task } from '../../models';

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="project-detail-container">
      @if (project()) {
        <header class="header">
          <button class="btn btn-back" (click)="goBack()">← Back to Projects</button>
          <h1>{{ project()!.title }}</h1>
        </header>

        @if (project()!.description) {
          <p class="project-description">{{ project()!.description }}</p>
        }

        <div class="project-progress">
          <div class="progress-stats">
            <div class="stat">
              <span class="stat-value">{{ project()!.totalTasks }}</span>
              <span class="stat-label">Total Tasks</span>
            </div>
            <div class="stat">
              <span class="stat-value">{{ project()!.completedTasks }}</span>
              <span class="stat-label">Completed</span>
            </div>
            <div class="stat">
              <span class="stat-value">{{ project()!.progressPercentage }}%</span>
              <span class="stat-label">Progress</span>
            </div>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" [style.width.%]="project()!.progressPercentage"></div>
          </div>
        </div>

        <div class="create-task-section">
          <h3>Add New Task</h3>
          <form (ngSubmit)="createTask()" class="create-form">
            <input
              type="text"
              [(ngModel)]="newTask.title"
              name="title"
              placeholder="Task title"
              class="form-control"
              required
            />
            <input
              type="text"
              [(ngModel)]="newTask.description"
              name="description"
              placeholder="Description (optional)"
              class="form-control"
            />
            <input
              type="date"
              [(ngModel)]="newTask.dueDate"
              name="dueDate"
              class="form-control"
            />
            <button type="submit" class="btn btn-primary" [disabled]="!newTask.title">
              Add Task
            </button>
          </form>
        </div>

        @if (errorMessage()) {
          <div class="alert alert-danger">{{ errorMessage() }}</div>
        }

        <div class="tasks-section">
          <h3>Tasks</h3>
          @if (loading()) {
            <div class="loading">Loading tasks...</div>
          } @else {
            <div class="tasks-list">
              @for (task of tasks(); track task.id) {
                <div class="task-card" [class.completed]="task.completed">
                  <div class="task-checkbox">
                    <input
                      type="checkbox"
                      [checked]="task.completed"
                      (change)="toggleTask(task)"
                      [id]="'task-' + task.id"
                    />
                  </div>
                  <div class="task-content">
                    <label [for]="'task-' + task.id" class="task-title">
                      {{ task.title }}
                    </label>
                    @if (task.description) {
                      <p class="task-description">{{ task.description }}</p>
                    }
                    @if (task.dueDate) {
                      <span class="task-due-date">Due: {{ task.dueDate | date:'short' }}</span>
                    }
                  </div>
                  <button
                    class="btn-delete"
                    (click)="deleteTask(task.id)"
                    title="Delete task"
                  >
                    ×
                  </button>
                </div>
              } @empty {
                <div class="empty-state">
                  <p>No tasks yet. Add your first task!</p>
                </div>
              }
            </div>
          }
        </div>
      } @else if (loading()) {
        <div class="loading">Loading project...</div>
      }
    </div>
  `,
  styles: [`
    .project-detail-container {
      max-width: 900px;
      margin: 0 auto;
      padding: 2rem;
    }

    .header {
      display: flex;
      align-items: center;
      gap: 1rem;
      margin-bottom: 1rem;
    }

    .btn-back {
      background: #6c757d;
      color: white;
      padding: 0.5rem 1rem;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      font-size: 0.9rem;
    }

    .btn-back:hover {
      background: #5a6268;
    }

    h1 {
      margin: 0;
      color: #333;
    }

    .project-description {
      color: #666;
      margin-bottom: 2rem;
    }

    .project-progress {
      background: white;
      padding: 1.5rem;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      margin-bottom: 2rem;
    }

    .progress-stats {
      display: flex;
      justify-content: space-around;
      margin-bottom: 1rem;
    }

    .stat {
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .stat-value {
      font-size: 2rem;
      font-weight: bold;
      color: #667eea;
    }

    .stat-label {
      font-size: 0.9rem;
      color: #888;
    }

    .progress-bar {
      height: 15px;
      background: #e9ecef;
      border-radius: 7px;
      overflow: hidden;
    }

    .progress-fill {
      height: 100%;
      background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
      transition: width 0.3s;
    }

    .create-task-section {
      background: white;
      padding: 1.5rem;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      margin-bottom: 2rem;
    }

    .create-form {
      display: grid;
      grid-template-columns: 1fr 1fr 150px auto;
      gap: 1rem;
      margin-top: 1rem;
    }

    @media (max-width: 768px) {
      .create-form {
        grid-template-columns: 1fr;
      }
    }

    .form-control {
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

    .btn:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }

    .tasks-section {
      background: white;
      padding: 1.5rem;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }

    .tasks-list {
      display: flex;
      flex-direction: column;
      gap: 1rem;
      margin-top: 1rem;
    }

    .task-card {
      display: flex;
      align-items: flex-start;
      gap: 1rem;
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 5px;
      border: 2px solid transparent;
      transition: all 0.3s;
    }

    .task-card:hover {
      border-color: #667eea;
    }

    .task-card.completed {
      opacity: 0.6;
    }

    .task-card.completed .task-title {
      text-decoration: line-through;
    }

    .task-checkbox input[type="checkbox"] {
      width: 20px;
      height: 20px;
      cursor: pointer;
    }

    .task-content {
      flex: 1;
    }

    .task-title {
      font-size: 1.1rem;
      font-weight: 500;
      color: #333;
      cursor: pointer;
      display: block;
      margin-bottom: 0.5rem;
    }

    .task-description {
      color: #666;
      font-size: 0.9rem;
      margin: 0.5rem 0;
    }

    .task-due-date {
      font-size: 0.85rem;
      color: #888;
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
      flex-shrink: 0;
    }

    .btn-delete:hover {
      background: #c82333;
    }

    .loading, .empty-state {
      text-align: center;
      padding: 2rem;
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
export class ProjectDetailComponent implements OnInit {
  private projectService = inject(ProjectService);
  private taskService = inject(TaskService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  project = signal<Project | null>(null);
  tasks = signal<Task[]>([]);
  loading = signal(false);
  errorMessage = signal('');

  newTask = {
    title: '',
    description: '',
    dueDate: ''
  };

  projectId!: number;

  ngOnInit() {
    this.projectId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadProject();
    this.loadTasks();
  }

  loadProject() {
    this.loading.set(true);
    this.projectService.getProject(this.projectId).subscribe({
      next: (project) => {
        this.project.set(project);
        this.loading.set(false);
      },
      error: (error) => {
        this.errorMessage.set('Failed to load project');
        this.loading.set(false);
      }
    });
  }

  loadTasks() {
    this.taskService.getTasks(this.projectId).subscribe({
      next: (tasks) => {
        this.tasks.set(tasks);
      },
      error: (error) => {
        this.errorMessage.set('Failed to load tasks');
      }
    });
  }

  createTask() {
    if (!this.newTask.title.trim()) return;

    this.taskService.createTask({
      projectId: this.projectId,
      ...this.newTask
    }).subscribe({
      next: () => {
        this.newTask = { title: '', description: '', dueDate: '' };
        this.loadTasks();
        this.loadProject(); // Refresh progress
      },
      error: (error) => {
        this.errorMessage.set('Failed to create task');
      }
    });
  }

  toggleTask(task: Task) {
    this.taskService.toggleCompleted(task.id).subscribe({
      next: () => {
        this.loadTasks();
        this.loadProject(); // Refresh progress
      },
      error: (error) => {
        this.errorMessage.set('Failed to update task');
      }
    });
  }

  deleteTask(id: number) {
    if (confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          this.loadTasks();
          this.loadProject(); // Refresh progress
        },
        error: (error) => {
          this.errorMessage.set('Failed to delete task');
        }
      });
    }
  }

  goBack() {
    this.router.navigate(['/projects']);
  }
}
