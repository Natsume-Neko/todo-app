import { Component, inject, signal } from '@angular/core';
import { TodoApi } from './todo-api';
import { COLOR_CLASSES, STATE_LABELS, TodoState, TodoItem } from './todo-types';
import { TodoForm } from './todo-form/todo-form';
import { TodoEditRow } from './todo-edit-row/todo-edit-row';

@Component({
  imports: [TodoForm, TodoEditRow],
  selector: 'app-todo-list',
  styleUrl: './todo-list.scss',
  templateUrl: './todo-list.html',
})
export class TodoList {
  private todoApi = inject(TodoApi);
  todos = signal<TodoItem[]>([]);
  editingId = signal<number | null>(null);

  protected readonly TodoState = TodoState;
  protected readonly stateLabels = STATE_LABELS;
  protected readonly colorClasses = COLOR_CLASSES;

  constructor() {
    this.load();
  }

  load() {
    this.todoApi.list().subscribe((todos) => this.todos.set(todos));
  }

  remove(todo: TodoItem) {
    if (!confirm(`「${todo.title}」を削除しますか？`)) {
      return;
    }
    this.todoApi.delete(todo.id).subscribe({
      next: () => this.load(),
      error: () => alert('削除に失敗しました'),
    });
  }

  startEdit(todo: TodoItem) {
    this.editingId.set(todo.id);
  }

  cancelEdit() {
    this.editingId.set(null);
  }

  onSaved() {
    this.editingId.set(null);
    this.load();
  }
}
