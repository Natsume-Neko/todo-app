import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TodoList } from './todo-list';
import { TodoApi } from './todo-api';
import { of } from 'rxjs';

describe('TodoList', () => {
  let component: TodoList;
  let fixture: ComponentFixture<TodoList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TodoList],
      providers: [
        { 
          provide: TodoApi,
          useValue: {
            list: () => of([{
              id: 1,
              categoryId: 1,
              title: 'string',
              body: 'string',
              stateLabel: 'string',
              categoryName: 'string',
              colorClass: 'string',
            }])
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TodoList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
