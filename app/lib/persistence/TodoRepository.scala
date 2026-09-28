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
class TodoRepository @Inject() (
  @Named("master") master: Database,
  @Named("slave") slave:   Database
)(implicit val ec:         ExecutionContext) extends SlickRepository[Todo.Id, Todo] {
  val todoTable         = TableQuery[TodoTable]
  val todoCategoryTable = TableQuery[TodoCategoryTable]

  def getAllJoined(): Future[Seq[(Todo, TodoCategory)]] =
    slave.run(
      todoTable
        .join(todoCategoryTable)
        .on((l, r) => l.categoryId === r.id)
        .result
    )
}
