package lib.persistence.db

import java.time.LocalDateTime
import ixias.slick.jdbc.MySQLProfile.api._
import ixias.slick.builder._
import lib.model.Todo
import lib.model.TodoCategory

case class TodoCategoryTable(tag: Tag) extends Table[TodoCategory](tag, "to_do_category") {
  // Columns
  /* @1 */
  def id        = column[TodoCategory.Id]("id", UInt64, O.PrimaryKey, O.AutoInc)
  /* @2 */
  def name      = column[String]("name", Utf8Char255)
  /* @3 */
  def slug      = column[String]("slug", AsciiChar64)
  /* @4 */
  def color     = column[TodoCategory.Color]("color", UInt8)
  /* @5 */
  def updatedAt = column[LocalDateTime]("updated_at", TsCurrent)
  /* @6 */
  def createdAt = column[LocalDateTime]("created_at", Ts)

  def * = (id.?, name, slug, color, updatedAt, createdAt).<>(
    (TodoCategory.apply _).tupled,
    (TodoCategory.unapply _).andThen(_.map(_.copy(
      _5 = LocalDateTime.now()
    )))
  )
}
