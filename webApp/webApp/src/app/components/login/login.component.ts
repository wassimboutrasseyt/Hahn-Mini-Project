import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <h2>Project Tasks Manager</h2>
        <form (ngSubmit)="onSubmit()" #loginForm="ngForm">
          <div class="form-group">
            <label for="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              [(ngModel)]="credentials.email"
              required
              email
              class="form-control"
              placeholder="user@example.com"
            />
          </div>
          
          <div class="form-group">
            <label for="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              [(ngModel)]="credentials.password"
              required
              class="form-control"
              placeholder="password"
            />
          </div>

          @if (errorMessage()) {
            <div class="alert alert-danger">{{ errorMessage() }}</div>
          }

          <button 
            type="submit" 
            [disabled]="!loginForm.form.valid || loading()"
            class="btn btn-primary btn-block"
          >
            {{ loading() ? 'Logging in...' : 'Login' }}
          </button>
        </form>

        <div class="demo-credentials">
          <small>Demo: user@example.com / password</small>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 20px;
    }

    .login-card {
      background: white;
      padding: 2rem;
      border-radius: 10px;
      box-shadow: 0 10px 40px rgba(0,0,0,0.2);
      width: 100%;
      max-width: 400px;
    }

    h2 {
      text-align: center;
      color: #333;
      margin-bottom: 2rem;
    }

    .form-group {
      margin-bottom: 1.5rem;
    }

    label {
      display: block;
      margin-bottom: 0.5rem;
      color: #555;
      font-weight: 500;
    }

    .form-control {
      width: 100%;
      padding: 0.75rem;
      border: 1px solid #ddd;
      border-radius: 5px;
      font-size: 1rem;
      transition: border-color 0.3s;
    }

    .form-control:focus {
      outline: none;
      border-color: #667eea;
    }

    .btn {
      padding: 0.75rem;
      border: none;
      border-radius: 5px;
      font-size: 1rem;
      cursor: pointer;
      transition: all 0.3s;
    }

    .btn-primary {
      background: #667eea;
      color: white;
      width: 100%;
    }

    .btn-primary:hover:not(:disabled) {
      background: #5568d3;
    }

    .btn:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }

    .alert {
      padding: 0.75rem;
      border-radius: 5px;
      margin-bottom: 1rem;
    }

    .alert-danger {
      background: #fee;
      color: #c33;
      border: 1px solid #fcc;
    }

    .demo-credentials {
      margin-top: 1rem;
      text-align: center;
      color: #888;
    }
  `]
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  credentials = {
    email: '',
    password: ''
  };

  loading = signal(false);
  errorMessage = signal('');

  onSubmit() {
    this.loading.set(true);
    this.errorMessage.set('');

    this.authService.login(this.credentials).subscribe({
      next: () => {
        this.router.navigate(['/projects']);
      },
      error: (error) => {
        this.loading.set(false);
        this.errorMessage.set(error.error?.message || 'Login failed. Please try again.');
      }
    });
  }
}
