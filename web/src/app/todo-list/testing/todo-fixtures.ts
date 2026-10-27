import { of } from 'rxjs';
import { TodoApi } from '../todo-api';
import { Category, CategoryColor, TodoItem, TodoState } from '../todo-types';

export function makeCategory(overrides: Partial<Category> = {}): Category {
  return {
    id: 1,
    name: '仕事',
    slug: 'work',
    colorClass: 'category--blue',
    ...overrides,
  };
}

export function makeTodo(overrides: Partial<TodoItem> = {}): TodoItem {
  return {
    id: 1,
    categoryId: 1,
    title: 'テストTodo',
    body: '本文',
    state: TodoState.NotBegin,
    categoryName: '仕事',
    color: CategoryColor.Blue,
    ...overrides,
  };
}

export function provideFakeTodoApi(overrides: Partial<TodoApi> = {}) {
  return {
    provide: TodoApi,
    useValue: {
      list: () => of([makeTodo()]),
      categories: () => of([makeCategory()]),
      create: () => of(undefined),
      ...overrides,
    } satisfies Partial<TodoApi>,
  };
}
