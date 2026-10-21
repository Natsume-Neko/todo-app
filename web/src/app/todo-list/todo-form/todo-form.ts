import { Component, inject, output, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Category, TodoApi, TodoCreateInput } from '../todo-api';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-todo-form',
  styleUrl: './todo-form.scss',
  templateUrl: './todo-form.html',
})
export class TodoForm {
  private fb = inject(NonNullableFormBuilder);
  private todoApi = inject(TodoApi);

  created = output<void>();
  categories = signal<Category[]>([]);
  submitting = signal(false);
  serverErrors = signal<Record<string, string[]>>({});

  form = this.fb.group({
    title:      ['', [Validators.required, Validators.maxLength(255)]],
    body:       ['', [Validators.maxLength(21845)]],
    categoryId: this.fb.control<number | null>(null, Validators.required),
  })

  constructor() {
    this.todoApi.categories().subscribe(categories => this.categories.set(categories));
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.serverErrors.set({});

    this.todoApi.create(this.form.getRawValue() as TodoCreateInput).subscribe({
      next: () => {
        this.submitting.set(false);
        this.form.reset();
        this.created.emit();
      },
      error: (err: HttpErrorResponse) => {
        this.submitting.set(false);
        if (err.status === 400) {
          this.serverErrors.set(err.error);
        }
      },
    })
  }
}
