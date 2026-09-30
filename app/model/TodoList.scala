package model

import lib.model.Todo
import lib.model.TodoCategory
import play.api.data._
import play.api.data.Forms._

case class ViewValueTodoItem(
  cssSrc:        Seq[String],
  jsSrc:         Seq[String],
  title:         String,
  body:          String,
  state:         Todo.State,
  category_name: String,
  color:         TodoCategory.Color
) extends ViewValueCommon

case class ViewValueTodoList(
  title:      String,
  cssSrc:     Seq[String],
  jsSrc:      Seq[String],
  todos:      Seq[ViewValueTodoItem],
  form:       Form[TodoAddData],
  categories: Seq[(String, String)]
) extends ViewValueCommon
