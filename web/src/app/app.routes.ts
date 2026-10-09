import { Routes } from '@angular/router';
import { TodoList } from './todo-list/todo-list';

export const routes: Routes = [
  { path: 'todo-list', component: TodoList },
  { path: '', redirectTo: 'todo-list', pathMatch: 'full' },
];
