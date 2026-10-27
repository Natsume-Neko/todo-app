import { Component, inject, signal } from '@angular/core';
import { TodoApi } from './todo-api';
import { CategoryColor, COLOR_CLASSES, STATE_LABELS, TodoState } from './todo-codes';

@Component({
  imports: [],
  selector: 'app-todo-list',
  styleUrl: './todo-list.scss',
  templateUrl: './todo-list.html',
})
export class TodoList {
  private todoApi = inject(TodoApi);
  todos = signal<TodoItem[]>([]);

  protected readonly TodoState = TodoState;
  protected readonly stateLabels = STATE_LABELS;
  protected readonly colorClasses = COLOR_CLASSES;

  constructor() {
    this.todoApi.list().subscribe(todos => this.todos.set(todos));
  }
}

export interface TodoItem {
  id: number;
  categoryId: number;
  title: string;
  body: string;
  state: TodoState;
  categoryName: string;
  color: CategoryColor;
}