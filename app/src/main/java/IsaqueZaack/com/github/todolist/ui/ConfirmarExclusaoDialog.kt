package IsaqueZaack.com.github.todolist.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import IsaqueZaack.com.github.todolist.data.Tarefa

/**
 * Diálogo Material 3 que pede confirmação antes de excluir uma tarefa.
 * É exibido sobre a tela da lista (não é uma nova tela/rota).
 *
 * @param tarefa tarefa selecionada para exclusão; seu título é exibido no diálogo.
 * @param onConfirmar remove a tarefa e fecha o diálogo.
 * @param onCancelar fecha o diálogo sem alterar a lista.
 */
@Composable
fun ConfirmarExclusaoDialog(
    tarefa: Tarefa,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        icon = {
            Icon(Icons.Default.Delete, contentDescription = null)
        },
        title = {
            Text("Excluir tarefa?")
        },
        text = {
            Text(
                buildAnnotatedString {
                    append("A tarefa ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("\"${tarefa.titulo}\"")
                    }
                    append(" será excluída permanentemente. Esta ação não pode ser desfeita.")
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Excluir", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true, name = "Diálogo de confirmação de exclusão")
@Composable
private fun ConfirmarExclusaoDialogPreview() {
    ConfirmarExclusaoDialog(
        tarefa = Tarefa(id = 1, titulo = "Estudar Room", descricao = "Revisar anotações e DAO"),
        onConfirmar = {},
        onCancelar = {}
    )
}
