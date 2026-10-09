import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TodoItem } from './todo-list';

@Injectable({ providedIn: 'root' })
export class TodoApi {
  private http = inject(HttpClient);

  list(): Observable<TodoItem[]> {
    return this.http.get<TodoItem[]>('/api/todos');
  }
}
