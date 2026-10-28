# 🍓 Essência da Fruta

Sistema desktop para gestão de uma produção de **polpas de fruta**: lotes produzidos, estoque, vendas, custos e relatórios em PDF. Foi criado para resolver um problema real: substituir planilhas e anotações soltas por um controle único e confiável.

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven&logoColor=white)
![H2](https://img.shields.io/badge/Database-H2-1021FF)
![License](https://img.shields.io/badge/license-MIT-blue)

## ✨ Funcionalidades

| Aba | O que faz |
|---|---|
| **Dashboard** | Indicadores do mês: polpas em estoque, custos, vendas e lucro |
| **Produção** | Cadastro de lotes (sabor, peso, quantidade, preço, validade) |
| **Estoque** | Consulta do estoque atual por dia, semana, mês ou ano |
| **Custos** | Lançamento de despesas (luz, funcionários, impostos, combustível etc.) |
| **Vendas** | Registro de pedidos com cliente, WhatsApp, endereço, forma de pagamento e data de entrega. A baixa de estoque é automática |
| **Controles** | Relatórios em **PDF** de produção, estoque, custos, vendas e lucro por período |

## 🏗️ Arquitetura

Padrão **MVC** com camada de acesso a dados (DAO):

```
src/main/java/com/essenciadafruta/
├── App.java            Ponto de entrada (tema FlatLaf + inicialização do banco)
├── view/               Interface Swing (MainWindow)
├── controller/         Regras de negócio e validações
├── model/              Entidades (Polpa, Saida, Custo, ...)
│   └── dao/            Acesso ao banco via JDBC (PreparedStatement)
└── util/               Conexão com o banco e geradores de PDF (iText)
```

**Stack:** Java 21 · Swing + FlatLaf · H2 Database (arquivo local) · iText 5 · Maven · JUnit 5

O banco é um arquivo local em `~/essencia_da_fruta`, criado automaticamente na primeira execução. Não é preciso instalar nenhum servidor.

## 🚀 Como executar

Pré-requisitos: **JDK 21** e **Maven 3.9+**.

```bash
git clone https://github.com/IgorXavierGabriel/Ess-ncia-Da-Fruta-App-controle.git
cd Ess-ncia-Da-Fruta-App-controle

# Opção 1: Maven
mvn compile exec:java

# Opção 2: JAR executável
mvn clean package
java -jar target/essencia-da-fruta.jar
```

No Windows, também dá para dar dois cliques em `run.bat`.

## 🧪 Testes

```bash
mvn test
```

Os testes usam um H2 **em memória**, sem tocar no seu banco real. Eles cobrem as consultas por período (dia, semana, mês e ano) de produção, estoque, vendas e custos, além do fluxo de venda (baixa de estoque, estoque insuficiente e consistência).

## 🧩 Dificuldades encontradas e soluções

| Desafio | Solução |
|---|---|
| **Migração de SQLite para H2 deixou consultas quebradas.** Relatórios por mês e por ano, o total de vendas do mês e os filtros de estoque falhavam com `Function "STRFTIME" not found` e `Function "DATE" not found`, porque são funções específicas do SQLite. | Escrevi testes que reproduziam o erro (5 falhas) e troquei as consultas por **comparação de intervalo** (`data >= início AND data < próximo mês`). As datas ficam no formato ISO, então a comparação funciona em qualquer banco e ainda é mais simples. |
| **A venda gravava o pedido e baixava o estoque em duas operações separadas.** Uma falha no meio, ou duas vendas ao mesmo tempo, deixava o estoque inconsistente ou negativo. | A venda agora roda em uma **transação**: `UPDATE ... WHERE quantidade >= ?` (que impede estoque negativo de forma atômica) seguido do `INSERT`, com `commit`/`rollback`. |
| **Conexão única compartilhada sendo fechada a cada consulta.** O `try-with-resources` dos DAOs fechava a conexão `static`, que reconectava e recriava as tabelas toda vez. | `Database.getConnection()` passou a entregar uma conexão nova por chamada, com a criação das tabelas feita **uma única vez**. Isso também permitiu usar transações. |
| **Erros de banco engolidos silenciosamente** (`catch` vazio na migração, erro de criação de tabelas só impresso no console). | Os erros agora são propagados, e a aplicação deixa de falhar em silêncio. |
| **Testar o banco sem sujar os dados reais.** | A URL do banco é configurável com `-Dessencia.db.url`. Os testes usam H2 em memória. |
| **`java -jar` não funcionava.** O `pom.xml` não gerava um JAR executável. | Adicionei o `maven-shade-plugin` com `Main-Class`, e o `target/essencia-da-fruta.jar` embute as dependências. |
| **Repositório com lixo versionado.** Arquivos `.class` do `target/`, imagens duplicadas e documentos desatualizados. | Removi esses arquivos do Git, ajustei o `.gitignore` e reescrevi a documentação. |

## 🔭 Próximos passos

- Guardar valores monetários em `DECIMAL`/`BigDecimal` em vez de `DOUBLE`.
- Usar colunas `DATE` no banco em vez de texto ISO.
- Quebrar o `MainWindow` (mais de 1.600 linhas) em painéis menores, um por aba.
- Salvar os PDFs em uma pasta escolhida pelo usuário.
- Testes automatizados da interface e CI com GitHub Actions.
- Backup e exportação dos dados.

## 📄 Licença

Distribuído sob a licença MIT. Veja [LICENSE](LICENSE).

## 👤 Autor

**Igor Gabriel Xavier Pereira**: [github.com/IgorXavierGabriel](https://github.com/IgorXavierGabriel)
