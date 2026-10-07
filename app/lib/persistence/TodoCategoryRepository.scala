package lib.persistence

import scala.concurrent.{ ExecutionContext, Future }
import ixias.model._
import ixias.slick.SlickRepository
import ixias.slick.builder.{ DatabaseBuilder, HikariConfigBuilder }
import ixias.slick.jdbc.MySQLProfile.api._
import ixias.slick.model.DataSourceName
import lib.model.Todo
import lib.persistence.db.TodoTable
import slick.dbio.Effect
import slick.sql.FixedSqlAction
import javax.inject._
import lib.persistence.db.TodoCategoryTable
import lib.model.TodoCategory

@Singleton
class TodoCategoryRepository @Inject() (
  @Named("master") master: Database,
  @Named("slave") slave:   Database
)(implicit val ec:         ExecutionContext) extends SlickRepository[TodoCategory.Id, TodoCategory] {
  val todoTable         = TableQuery[TodoTable]
  val todoCategoryTable = TableQuery[TodoCategoryTable]

  def getAllCategories(): Future[Seq[TodoCategory]] =
    slave.run(todoCategoryTable.result)

  def getCategoryById(categoryId: TodoCategory.Id): Future[Option[TodoCategory]] =
    slave.run(todoCategoryTable.filter(_.id === categoryId).result.headOption)

  def addCategory(category: TodoCategory#WithNoId): Future[TodoCategory.Id] =
    master.run(
      (todoCategoryTable returning todoCategoryTable.map(_.id)) += category.v
    )

  def editCategory(
    id:    TodoCategory.Id,
    name:  String,
    slug:  String,
    color: TodoCategory.Color
  ): Future[Int] = {
    master.run(
      todoCategoryTable
        .filter(_.id === id)
        .map(c => (c.name, c.slug, c.color))
        .update((name, slug, color))
    )
  }

  def deleteCategoryWithTodos(id: TodoCategory.Id): Future[Int] = {
    master.run(
      (for {
        _ <- todoTable.filter(_.categoryId === id).delete
        n <- todoCategoryTable.filter(_.id === id).delete
      } yield n).transactionally
    )
  }
}
