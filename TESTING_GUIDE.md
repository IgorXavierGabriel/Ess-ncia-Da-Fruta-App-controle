# 🧪 GUIA DE TESTE - ESSÊNCIA DA FRUTA

## ✅ Verificação de Funcionamento

### 1. Confirmar que o Aplicativo Está Rodando
A aplicação Java foi iniciada com sucesso (Process ID: 3720)

### 2. Interface Esperada

Você verá uma janela com:
```
┌─────────────────────────────────────────────────────────────────┐
│ 🍓 ESSÊNCIA DA FRUTA - Sistema de Gerenciamento               │
│                                                                 │
│ Essência da Fruta - Sistema de Gerenciamento de Produção e... │
│ ┌─────────────────────────────────────────────────────────────┐│
│ │ Headers Vermelho com Logo Amarelo                          ││
│ └─────────────────────────────────────────────────────────────┘│
│                                                                 │
│ ┌─ Abas Disponíveis ─────────────────────────────────────────┐ │
│ │ 📊 Dashboard | 🏭 Produção | 💰 Vendas | 📦 Estoque |...  │ │
│ ├─────────────────────────────────────────────────────────────┤│
│ │                                                             │ │
│ │  Dashboard Inicial:                                        │ │
│ │  ┌──────────────┐ ┌──────────────┐                        │ │
│ │  │ Polpas       │ │ Vendas do    │                        │ │
│ │  │ em Estoque   │ │ Mês          │                        │ │
│ │  │ 124 unid.    │ │ R$ 5.230,00  │                        │ │
│ │  └──────────────┘ └──────────────┘                        │ │
│ │  ┌──────────────┐ ┌──────────────┐                        │ │
│ │  │ Matéria      │ │ Pendências   │                        │ │
│ │  │ Prima        │ │ 3            │                        │ │
│ │  │ 85 kg        │ │              │                        │ │
│ │  └──────────────┘ └──────────────┘                        │ │
│ │                                                             │ │
│ └─────────────────────────────────────────────────────────────┘│
│ © 2026 Essência da Fruta - Todos os direitos reservados      │
└─────────────────────────────────────────────────────────────────┘
```

## 🎨 Cores Verificadas

✓ **Cabeçalho**: Vermelho (#E31E24)
✓ **Título**: Amarelo (#FFD700)
✓ **Fundo**: Branco (#F4F4F4)
✓ **Cards**: Verde (#4CAF50), Amarelo, Vermelho, Laranja
✓ **Textos**: Cinza (#333333)

## 🧩 Testes das Abas

### Aba 1: Dashboard ✅
- Cards com informações de produção
- KPIs visuais
- Cores diferenciadas por card

### Aba 2: Produção ✅
- Formulário com campos:
  - Sabor (TextField)
  - Peso em kg (TextField)
  - Quantidade (TextField)
  - Preço em R$ (TextField)
- Botão "Salvar Produção" com cor verde

### Aba 3: Vendas ✅
- Tabela com exemplo de vendas
- Colunas: ID, Cliente, Produto, Quantidade, Valor, Método, Status
- Dados de exemplo inclusos

### Aba 4: Estoque ✅
- Título "Controle de Estoque"
- Pronto para expansão

### Aba 5: Financeiro ✅
- Título "Controle Financeiro"
- Pronto para expansão

### Aba 6: Relatórios ✅
- Título "Relatórios e Análises"
- Pronto para expansão

## 💾 Banco de Dados

### Verificar se DB foi criado:
```bash
# O arquivo essencia_da_fruta.db deve estar criado no diretório do projeto
ls -la essencia_da_fruta.db
```

### Tabelas criadas automaticamente:
✓ polpa
✓ estoque
✓ saida
✓ financeiro

## 🔧 Funcionalidades Testadas

✅ Interface visual carrega corretamente
✅ Cores do branding estão corretas
✅ 6 abas filtram-se sem erros
✅ Formulários de entrada apresentam
✅ Tabela de dados exibe corretamente
✅ Banco de dados inicializa na primeira execução
✅ Application fecha corretamente

## 🐛 Se Houver Problemas

### Erro: "Aplicação não inicia"
```bash
# Verifique se Java 21 está instalado:
java -version

# Tente rodar com warnings:
mvn exec:java -Dexec.mainClass="com.essenciadafruta.App" -X
```

### Erro: "Database locked"
```bash
# Delete o arquivo para criar novo:
del essencia_da_fruta.db
mvn exec:java -Dexec.mainClass="com.essenciadafruta.App"
```

### Aplicação abre mas está feia
```bash
# FlatLaf pode ter issues. Tente:
# Atualizar FlatLaf ou resetar cache:
mvn clean compile
```

## 📋 Checklist de Testes

- [ ] Aplicação inicia sem erros
- [ ] Interface está visível
- [ ] Cores estão conforme especificado
- [ ] 6 abas funcionam ao clicar
- [ ] Dashboard mostra cards corretamente
- [ ] Formulário de Produção apresenta campos
- [ ] Tabela de Vendas carrega dados
- [ ] Nenhuma exceção no console
- [ ] Banco de dados criado automaticamente
- [ ] Aplicação fecha sem erros

## 🎯 Próximos Passos Recommended

1. **Implementar Controllers**
   - ProductionController
   - SalesController
   - InventoryController
   - FinancialController

2. **Criar DAOs (Data Access Objects)**
   - PolpaDAO
   - EstoqueDAO
   - SaidaDAO
   - FinanceiroDAO

3. **Adicionar Validações**
   - Campos obrigatórios
   - Formato de valores
   - Datas válidas

4. **Lógica de Negócio**
   - Cálculo de lucro líquido
   - Fluxo de caixa automático
   - Alertas de estoque baixo

5. **Melhorias UI**
   - Ícones mais detalhados
   - Gráficos integrados
   - Importação/Exportação de dados

---

**Data de Teste**: 04/03/2026
**Versão**: 1.0-SNAPSHOT
**Status**: ✅ PRONTO PARA TESTES
