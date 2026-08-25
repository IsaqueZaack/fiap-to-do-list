package IsaqueZaack.com.github.todolist.repository

import IsaqueZaack.com.github.todolist.data.Tarefa
import IsaqueZaack.com.github.todolist.data.TarefaDao
import kotlinx.coroutines.flow.Flow

/**
 * Camada de abstração entre a fonte de dados (Room/DAO) e o restante da aplicação.
 * A ViewModel nunca acessa o DAO diretamente: toda operação passa por aqui, o que
 * facilita testes e futuras trocas de fonte de dados (ex.: API remota).
 */
class TarefaRepository(private val dao: TarefaDao) {

    /** Fluxo reativo com a lista de tarefas persistida localmente. */
    val tarefas: Flow<List<Tarefa>> = dao.listarTodas()

    suspend fun inserir(tarefa: Tarefa) = dao.inserir(tarefa)

    suspend fun atualizar(tarefa: Tarefa) = dao.atualizar(tarefa)

    suspend fun deletar(tarefa: Tarefa) = dao.deletar(tarefa)
}
