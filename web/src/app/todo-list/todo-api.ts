import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Category, TodoCreateInput, TodoEditInput, TodoItem } from './todo-types';

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

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`/api/todos/${id}`);
  }

  edit(id: number, input: TodoEditInput): Observable<void> {
    return this.http.put<void>(`/api/todos/${id}`, input);
  }
}
