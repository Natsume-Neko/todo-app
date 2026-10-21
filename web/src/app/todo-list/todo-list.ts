import { Component, inject, signal } from '@angular/core';
import { TodoApi } from './todo-api';
import { COLOR_CLASSES, STATE_LABELS, TodoState, TodoItem } from './todo-types';
import { TodoForm } from './todo-form/todo-form';

@Component({
  imports: [TodoForm],
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
    this.load()
  }

  load() {
    this.todoApi.list().subscribe(todos => this.todos.set(todos));
  }
}
