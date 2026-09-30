package model

import lib.model.TodoCategory
import lib.model.Todo

object Utils {
  def colorClass(color: TodoCategory.Color): String = color match {
    case TodoCategory.Color.Blue   => "category--blue"
    case TodoCategory.Color.Green  => "category--green"
    case TodoCategory.Color.Red    => "category--red"
    case TodoCategory.Color.Yellow => "category--yellow"
  }

  def stateLabel(state: Todo.State): String = state match {
    case Todo.State.NotBegin => "TODO"
    case Todo.State.Doing    => "進行中"
    case Todo.State.Done     => "完了"
  }
}
