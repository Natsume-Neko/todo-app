import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TodoForm } from './todo-form';
import { provideFakeTodoApi } from '../testing/todo-fixtures';

describe('TodoForm', () => {
  let component: TodoForm;
  let fixture: ComponentFixture<TodoForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TodoForm],
      providers: [provideFakeTodoApi()],
    }).compileComponents();

    fixture = TestBed.createComponent(TodoForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
