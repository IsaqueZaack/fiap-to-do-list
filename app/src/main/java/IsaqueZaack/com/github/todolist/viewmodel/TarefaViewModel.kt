package IsaqueZaack.com.github.todolist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import IsaqueZaack.com.github.todolist.data.Tarefa
import IsaqueZaack.com.github.todolist.data.TarefaDatabase
import IsaqueZaack.com.github.todolist.repository.TarefaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel responsável por expor a lista de tarefas como estado observável
 * (StateFlow) para a UI e por disponibilizar as operações de inserção,
 * atualização e exclusão, delegando a persistência ao [TarefaRepository].
 */
class TarefaViewModel(private val repository: TarefaRepository) : ViewModel() {

    val tarefas: StateFlow<List<Tarefa>> = repository.tarefas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun inserir(tarefa: Tarefa) = viewModelScope.launch { repository.inserir(tarefa) }

    fun atualizar(tarefa: Tarefa) = viewModelScope.launch { repository.atualizar(tarefa) }

    fun deletar(tarefa: Tarefa) = viewModelScope.launch { repository.deletar(tarefa) }

    fun alternarConclusao(tarefa: Tarefa) = atualizar(tarefa.copy(concluida = !tarefa.concluida))

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val dao = TarefaDatabase.getDatabase(context).tarefaDao()
                    return TarefaViewModel(TarefaRepository(dao)) as T
                }
            }
    }
}
