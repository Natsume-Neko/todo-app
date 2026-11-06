import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { TodoApi } from '../todo-api';
import { TodoItem, Category, STATE_LABELS, TodoState } from '../todo-types';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  imports: [ReactiveFormsModule],
  selector: 'tr[app-todo-edit-row]',
  styleUrl: './todo-edit-row.scss',
  templateUrl: './todo-edit-row.html',
  host: {
    '(keydown.escape)': 'cancelled.emit()',
  },
})
export class TodoEditRow implements OnInit {
  private fb = inject(NonNullableFormBuilder);
  private todoApi = inject(TodoApi);

  todo = input.required<TodoItem>();
  saved = output<void>();
  cancelled = output<void>();

  categories = signal<Category[]>([]);
  submitting = signal(false);
  serverErrors = signal<Record<string, string[]>>({});
  generalError = signal<string | null>(null);

  protected readonly stateOptions = [TodoState.NotBegin, TodoState.Doing, TodoState.Done];
  protected readonly stateLabels = STATE_LABELS;

  form = this.fb.group({
    title: ['', [Validators.required, Validators.maxLength(255)]],
    body: ['', [Validators.maxLength(21845)]],
    state: this.fb.control<TodoState>(TodoState.NotBegin),
    categoryId: this.fb.control<number>(0),
  });

  constructor() {
    this.todoApi.categories().subscribe((c) => this.categories.set(c));
  }

  ngOnInit(): void {
    const t = this.todo();
    this.form.setValue({
      title: t.title,
      body: t.body,
      state: t.state,
      categoryId: t.categoryId,
    });
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.serverErrors.set({});
    this.generalError.set(null);

    this.todoApi.edit(this.todo().id, this.form.getRawValue()).subscribe({
      next: () => {
        this.submitting.set(false);
        this.saved.emit();
      },
      error: (err: HttpErrorResponse) => {
        this.submitting.set(false);
        if (err.status === 400) {
          this.serverErrors.set(err.error);
        } else {
          this.generalError.set('編集に失敗しました。時間をおいて再度お試しください。');
        }
      },
    });
  }
}
