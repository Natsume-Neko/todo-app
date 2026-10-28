export enum TodoState {
  NotBegin = 0,
  Doing = 1,
  Done = 2,
}

export enum CategoryColor {
  Blue = 0,
  Green = 1,
  Red = 2,
  Yellow = 3,
}

export const STATE_LABELS: Record<TodoState, string> = {
  [TodoState.NotBegin]: 'TODO',
  [TodoState.Doing]: '進行中',
  [TodoState.Done]: '完了',
};

export const COLOR_CLASSES: Record<CategoryColor, string> = {
  [CategoryColor.Blue]: 'category--blue',
  [CategoryColor.Green]: 'category--green',
  [CategoryColor.Red]: 'category--red',
  [CategoryColor.Yellow]: 'category--yellow',
};

export interface TodoItem {
  id: number;
  categoryId: number;
  title: string;
  body: string;
  state: TodoState;
  categoryName: string;
  color: CategoryColor;
}
