# Database — StockFlowJDBC

Scripts SQL organizados por responsabilidade, executados em ordem:

| Arquivo | Conteúdo |
|---|---|
| `01_tables.sql` | Tabelas principais (produtos, movimentacoes, auditoria_produtos, historico_dashboard) |
| `02_indexes.sql` | Índices para otimização de consultas |
| `03_views.sql` | Views para relatórios e dashboard |
| `04_functions.sql` | Functions reutilizáveis (cálculos) |
| `05_procedures.sql` | Stored Procedures (rotinas de negócio) |
| `06_triggers.sql` | Triggers de integridade e auditoria automática |
| `07_events.sql` | Agendamento automático de manutenção/snapshot |

Para recriar o banco do zero, execute os arquivos na ordem numérica acima.

## Visão geral da arquitetura

- **Integridade reativa:** Triggers `BEFORE INSERT/UPDATE` garantem que o status do produto sempre reflita sua quantidade em estoque, independente de qual caminho da aplicação originou a alteração.
- **Auditoria automática:** Triggers `AFTER INSERT/UPDATE/DELETE` registram todo histórico de mudanças em `auditoria_produtos`, incluindo o usuário do banco responsável.
- **Automação agendada:** Events cuidam de manutenção periódica (limpeza de auditoria antiga) e snapshots históricos do estoque para análise de tendência.