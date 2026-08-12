package IsaqueZaack.com.github.todolist.data

@Entity(tableName = "tarefas")
data class Terefa (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descricao: String,
    val concluido: Boolean = false,
    val dataCriacao: Long = System.currentTimeMillis()
)