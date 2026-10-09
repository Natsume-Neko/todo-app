import { Component, inject, signal } from '@angular/core';
import { TodoApi } from './todo-api';

@Component({
  imports: [],
  selector: 'app-todo-list',
  styleUrl: './todo-list.scss',
  templateUrl: './todo-list.html',
})
export class TodoList {
  private todoApi = inject(TodoApi);
  todos = signal<TodoItem[]>([]);

  constructor() {
    this.todoApi.list().subscribe(todos => this.todos.set(todos));
  }
}

export interface TodoItem {
  id: number;
  categoryId: number;
  title: string;
  body: string;
  stateLabel: string;
  categoryName: string;
  colorClass: string;
}