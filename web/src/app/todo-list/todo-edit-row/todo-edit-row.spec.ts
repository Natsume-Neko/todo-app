import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TodoEditRow } from './todo-edit-row';
import { makeTodo, provideFakeTodoApi } from '../testing/todo-fixtures';

describe('TodoEditRow', () => {
  let component: TodoEditRow;
  let fixture: ComponentFixture<TodoEditRow>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TodoEditRow],
      providers: [provideFakeTodoApi()],
    }).compileComponents();

    fixture = TestBed.createComponent(TodoEditRow);
    fixture.componentRef.setInput('todo', makeTodo());
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
