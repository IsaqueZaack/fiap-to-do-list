package IsaqueZaack.com.github.todolist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import IsaqueZaack.com.github.todolist.data.Tarefa
import IsaqueZaack.com.github.todolist.data.TarefaDatabase
import IsaqueZaack.com.github.todolist.repository.TarefaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel responsável por expor a lista de tarefas como estado observável
 * (StateFlow) para a UI e por disponibilizar as operações de inserção,
 * atualização e exclusão, delegando a persistência ao [TarefaRepository].
 *
 * Também mantém o estado do fluxo de confirmação de exclusão: a tarefa
 * selecionada fica em [tarefaParaExcluir] até que o usuário confirme ou
 * cancele a operação no diálogo exibido sobre a lista.
 */
class TarefaViewModel(private val repository: TarefaRepository) : ViewModel() {

    val tarefas: StateFlow<List<Tarefa>> = repository.tarefas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /** Tarefa aguardando confirmação de exclusão (null = diálogo fechado). */
    private val _tarefaParaExcluir = MutableStateFlow<Tarefa?>(null)
    val tarefaParaExcluir: StateFlow<Tarefa?> = _tarefaParaExcluir.asStateFlow()

    fun inserir(tarefa: Tarefa) = viewModelScope.launch { repository.inserir(tarefa) }

    fun atualizar(tarefa: Tarefa) = viewModelScope.launch { repository.atualizar(tarefa) }

    fun deletar(tarefa: Tarefa) = viewModelScope.launch { repository.deletar(tarefa) }

    fun alternarConclusao(tarefa: Tarefa) = atualizar(tarefa.copy(concluida = !tarefa.concluida))

    /** Abre o diálogo de confirmação para a tarefa selecionada (nada é excluído aqui). */
    fun solicitarExclusao(tarefa: Tarefa) {
        _tarefaParaExcluir.value = tarefa
    }

    /** Fecha o diálogo sem alterar a lista. */
    fun cancelarExclusao() {
        _tarefaParaExcluir.value = null
    }

    /**
     * Exclui somente a tarefa que foi selecionada ao abrir o diálogo
     * e fecha o diálogo em seguida.
     */
    fun confirmarExclusao() {
        val tarefa = _tarefaParaExcluir.value ?: return
        _tarefaParaExcluir.value = null
        deletar(tarefa)
    }

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
