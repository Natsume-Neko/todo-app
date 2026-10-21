import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TodoItem } from './todo-types';

export interface TodoCreateInput {
  title: string;
  body: string;
  categoryId: number;
}

export interface Category {
  id: number;
  name: string;
  slug: string;
  colorClass: string;
}

@Injectable({ providedIn: 'root' })
export class TodoApi {
  private http = inject(HttpClient);

  list(): Observable<TodoItem[]> {
    return this.http.get<TodoItem[]>('/api/todos');
  }

  create(input: TodoCreateInput): Observable<void> {
    return this.http.post<void>('/api/todos', input);
  }

  categories(): Observable<Category[]> {
    return this.http.get<Category[]>('/api/categories');
  }
}
