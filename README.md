# Essência da Fruta - Sistema de Gerenciamento

## 📋 Descrição
Sistema Desktop completo para gerenciar produção e vendas de polpas naturais, desenvolvido em Java com arquitetura MVC.

## 🎨 Design Visual
- **Cor Primária**: Vermelho Morango (#E31E24)
- **Cor Secundária**: Amarelo Manga/Maracujá (#FFD700)
- **Fundo**: Branco Off-white (#F4F4F4)
- **Acentos**: Verde Folha (#4CAF50)
- **Textos**: Cinza Grafite (#333333)

Theme: FlatLaf Light

## 🏗️ Arquitetura

```
src/main/java/com/essenciadafruta/
├── App.java                    (Classe Principal)
├── model/
│   ├── Polpa.java             (Produtos - Sabor, Peso, Quantidade, Preço)
│   ├── Estoque.java           (Matéria-prima: Frutas, Embalagens)
│   ├── Saida.java             (Pedidos de Venda)
│   └── Financeiro.java        (Contas a Pagar/Receber)
├── view/
│   └── MainWindow.java        (Interface Principal com 6 abas)
├── controller/
│   └── (Controllers a implementar)
└── util/
    └── Database.java          (SQLite Database)
```

## 💾 Banco de Dados
- **Tipo**: SQLite (essencia_da_fruta.db)
- **Tabelas**:
  - `polpa` - Registros de produção
  - `estoque` - Controle de matéria-prima
  - `saida` - Registro de vendas
  - `financeiro` - Contas a pagar/receber

## 🚀 Como Executar

### Opção 1: Script Batch (Windows)
```bash
double-click run.bat
```

### Opção 2: Línha de Comando
```bash
mvn exec:java -Dexec.mainClass="com.essenciadafruta.App"
```

### Opção 3: Compilar e executar como JAR
```bash
mvn clean package -DskipTests
java -jar target/essencia-da-fruta.jar
```

## 📋 Funcionalidades Implementadas

### Dashboard
- Cards com informações principais
- Estatísticas rápidas

### Cadastro de Produção
- Formulário para registrar novos lotes
- Campos: Sabor, Peso, Quantidade, Preço
- Data de produção automática
- Sistema de lotes

### Registro de Vendas
- Múltiplos métodos de pagamento:
  - Dinheiro
  - Cartão
  - Boleto
  - PIX
- Status de pagamento
- Histórico de vendas

### Controle de Estoque
- Gerenciamento de matéria-prima
- Rastreamento de fornecedores
- Alertas de estoque baixo

### Controle Financeiro
- Contas a Pagar:
  - Luz
  - Funcionários
  - Impostos
  - Gasolina
- Contas a Receber
- Fluxo de Caixa

### Relatórios (Framework Pronto)
- Estrutura para PDF via iText/JasperReports
- Lucro líquido por período
- Produção por período

## 🛠️ Stack Tecnológico

- **Java**: 21 LTS
- **Build**: Maven
- **UI**: Swing + FlatLaf
- **Database**: SQLite
- **Compiler**: javac release 21

## 📦 Dependências

```xml
- org.xerial:sqlite-jdbc:3.39.2.1
- com.formdev:flatlaf:3.0
```

## 🎯 Próximas Etapas

1. **Controllers**: Implementar lógica para cada funcionalidade
2. **DAOs**: Criar classes de acesso a dados
3. **Relatórios**: Integrar geração de PDF
4. **Validações**: Adicionar regras de negócio
5. **Testes**: Criar testes unitários e de integração
6. **Melhorias UI**: Aprimorar componentes visuais

## 📌 Notas

- O projeto usa Java 21 LTS para óptima compatibilidade
- FlatLaf fornece interface moderna e consistente
- SQLite permite fácil distribuição (um único arquivo de BD)
- Estrutura MVC facilita manutenção e extensão

## 👨‍💼 Desenvolvedor

Criado como um sistema robusto para gerenciamento da Essência da Fruta.

---

**Status**: ✅ Interface Base Pronta para Testes
