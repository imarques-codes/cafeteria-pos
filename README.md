# PDV Desktop

Sistema de Ponto de Venda desktop que estou desenvolvendo em Java, utilizando JavaFX, Maven e SQLite.

Minha ideia com este projeto é construir um sistema que vá além de uma interface demonstrativa. Quero desenvolver um fluxo de venda próximo de uma aplicação comercial real, trabalhando autenticação, abertura de caixa, produtos, estoque, clientes, pagamentos, vendas, fechamento de caixa e persistência dos dados.

Durante o desenvolvimento, também estou registrando os problemas que encontro, como identifico a causa e qual solução adoto. Para mim, essa parte é tão importante quanto fazer o sistema funcionar.

---

# Objetivo do projeto

Meu principal objetivo é utilizar este PDV para praticar o desenvolvimento de uma aplicação desktop completa e aplicar conceitos que encontro em sistemas comerciais reais.

Com este projeto estou trabalhando principalmente:

- Java e orientação a objetos
- JavaFX
- FXML
- CSS
- arquitetura em camadas
- regras de negócio
- SQLite
- SQL
- transações no banco de dados
- validações
- controle de estoque
- autenticação
- segurança de senhas
- tratamento de erros
- Git
- GitHub
- organização e evolução de software

Também utilizo este projeto como parte do meu portfólio.

Minha intenção não é mostrar apenas o resultado final, mas também demonstrar como organizei a aplicação, quais regras implementei, quais problemas encontrei e como fui evoluindo o sistema.

---

# Tecnologias utilizadas

## Linguagens

- Java
- SQL
- FXML
- CSS

## Tecnologias principais

- Java 25
- JavaFX
- Maven
- SQLite
- Git
- GitHub

## Bibliotecas

### Password4j

Utilizo o Password4j para geração e validação dos hashes das senhas.

As senhas dos usuários não são armazenadas em texto puro.

Atualmente utilizo Argon2 para geração do hash.

### SQLite JDBC

Utilizo o driver JDBC do SQLite para permitir a comunicação entre a aplicação Java e o banco de dados local.

---

# Ferramentas utilizadas

Durante o desenvolvimento estou utilizando:

- IntelliJ IDEA
- Scene Builder
- PowerShell
- Git CLI
- GitHub

---

# Organização do projeto

Eu organizei o código em pacotes para separar responsabilidades e evitar concentrar toda a lógica dentro dos controllers.

```text
br.com.pdv
├── aplicacao
├── controlador
├── dominio
├── infraestrutura
├── repositorio
├── servico
├── utilitario
└── Main.java
```

## Aplicacao

Uso essa camada para classes relacionadas ao funcionamento geral da aplicação, como sessão do usuário e inicializações.

## Controlador

Aqui ficam os controllers das telas JavaFX.

Eu procuro manter os controllers responsáveis principalmente pela interação entre o usuário e a interface.

## Dominio

Aqui ficam os objetos que representam entidades e conceitos do sistema, como:

- Produto
- Cliente
- Venda
- ItemVenda
- PagamentoVenda
- Usuario
- SessaoCaixa

## Infraestrutura

É onde mantenho a parte relacionada à conexão e inicialização do banco de dados.

## Repositorio

Uso os repositórios para concentrar o SQL e o acesso aos dados.

Minha intenção é evitar consultas SQL espalhadas pelos controllers.

## Servico

Aqui eu concentro regras de negócio que não devem ficar diretamente nas telas.

## Utilitario

Uso esse pacote para funcionalidades reutilizáveis, como:

- formatação de moeda
- formatação de CPF
- formatação de telefone
- validação de CPF
- validação de telefone
- validação de e-mail

---

# Arquitetura utilizada

Estou trabalhando com uma separação simples de responsabilidades.

O fluxo principal segue esta estrutura:

```text
Controller
   ↓
Servico
   ↓
Repositorio
   ↓
Banco de dados
```

A interface não deve acessar diretamente o banco sempre que for possível evitar.

As regras de negócio ficam principalmente na camada de serviço.

O SQL fica concentrado nos repositórios.

---

# Funcionalidades implementadas

## Login

O sistema possui autenticação de usuário.

A senha não é armazenada em texto puro no banco.

Utilizo Password4j com Argon2 para geração e validação do hash.

O administrador inicial pode ser configurado utilizando variáveis de ambiente:

```text
PDV_ADMIN_NOME
PDV_ADMIN_LOGIN
PDV_ADMIN_SENHA
```

Também implementei a opção de mostrar ou ocultar a senha durante o login.

---

# Abertura de caixa

Antes de iniciar as vendas, o sistema verifica se existe uma sessão de caixa aberta.

Quando não existe caixa aberto, o operador é direcionado para a tela de abertura.

Na abertura eu registro o saldo inicial e crio uma nova sessão de caixa no banco.

O fluxo inicial funciona assim:

```text
Login
  ↓
Verificação de caixa
  ↓
Abertura de caixa
  ↓
PDV
```

---

# Tela principal do PDV

A tela principal já possui:

- identificação do operador
- campo para leitura ou digitação do código do produto
- inclusão de produtos
- quantidade
- preço unitário
- total do item
- subtotal da venda
- exclusão de item
- alteração de quantidade
- acesso ao cadastro de produtos
- acesso ao cadastro de clientes
- pagamento
- fechamento de caixa

---

# Produtos

O gerenciamento de produtos possui informações como:

- código
- nome
- descrição
- preço
- controle de estoque
- estoque atual
- situação ativa ou inativa

Também implementei tratamento para código de barras duplicado.

Quando um produto controla estoque, o sistema verifica a quantidade disponível antes de permitir que a venda seja concluída.

---

# Controle de estoque

Uma das regras que considero mais importantes no projeto é manter venda e estoque consistentes.

A baixa de estoque acontece dentro da mesma transação utilizada para registrar a venda.

Durante a finalização utilizo uma atualização semelhante a:

```sql
UPDATE produto
SET estoque_atual = estoque_atual - ?
WHERE id = ?
  AND controla_estoque = 1
  AND estoque_atual >= ?;
```

Se nenhuma linha for alterada, considero que não existe estoque suficiente.

Nesse caso, a operação é interrompida.

Minha intenção é evitar situações em que uma venda seja registrada mesmo quando a baixa de estoque falhou.

---

# Clientes

O sistema possui gerenciamento de clientes.

Atualmente trabalho com:

- nome
- CPF
- telefone
- e-mail
- data de cadastro
- situação ativa ou inativa

Também implementei:

- máscara de CPF
- validação de CPF
- máscara de telefone
- validação de telefone
- validação de e-mail

O CPF pode ser opcional, mas quando informado precisa ser válido.

O mesmo princípio é utilizado para telefone e e-mail.

---

# Pagamentos

A tela de pagamento já trabalha com:

- PIX
- dinheiro
- cartão de crédito
- cartão de débito

No pagamento em dinheiro, o sistema também trabalha com:

- valor recebido
- valor da venda
- cálculo de troco

Os pagamentos são gravados separadamente e relacionados à venda.

---

# Finalização da venda

Antes de gravar uma venda, faço algumas validações.

Entre elas:

- deve existir um usuário autenticado
- deve existir caixa aberto
- a venda deve possuir pelo menos um item
- a quantidade dos itens deve ser maior que zero
- o total da venda deve ser maior que zero
- deve existir pelo menos uma forma de pagamento
- os valores dos pagamentos devem ser válidos
- o total pago não pode ser menor que o total da venda

Depois dessas validações, a venda é enviada ao repositório.

---

# Transação da venda

Eu utilizo transação porque considero que várias operações fazem parte da mesma venda.

O fluxo é aproximadamente:

```text
Iniciar transação
       ↓
Gravar venda
       ↓
Gravar itens
       ↓
Baixar estoque
       ↓
Gravar pagamentos
       ↓
Commit
```

Se ocorrer um erro durante uma etapa importante:

```text
Rollback
```

Dessa forma eu evito deixar informações parcialmente registradas.

---

# Fechamento de caixa

O sistema também possui tela de fechamento de caixa.

O fluxo principal da operação fica:

```text
Login
  ↓
Abertura de caixa
  ↓
PDV
  ↓
Venda
  ↓
Pagamento
  ↓
Fechamento de caixa
```

---

# Histórico de vendas

Esta funcionalidade está em desenvolvimento.

Eu já iniciei a estrutura necessária para consultar vendas registradas anteriormente.

Foram criados:

- ResumoVenda
- consulta no VendaRepositorio
- acesso através do ServicoVenda
- HistoricoVendasController
- historico-vendas.fxml

Minha ideia inicial é exibir:

```text
Venda | Data/Hora | Operador | Total | Status
```

---

# Por que criei ResumoVenda

A classe Venda possui mais informações do que eu preciso para exibir uma tabela simples de histórico.

Ela trabalha com:

- itens
- pagamentos
- sessão de caixa
- usuário
- status

Para listar centenas de vendas eu não preciso necessariamente carregar todos os itens e pagamentos.

Por isso criei a classe:

```text
ResumoVenda
```

Ela representa somente as informações necessárias para a listagem.

Atualmente possui:

- ID
- data e hora
- total
- status
- operador

Essa decisão deixa a consulta mais simples e reduz dados desnecessários durante a listagem.

---

# Banco de dados

O projeto utiliza SQLite como banco local.

Atualmente o sistema trabalha com tabelas como:

```text
usuario
caixa
sessao_caixa
produto
cliente
venda
item_venda
pagamento_venda
```

O arquivo do banco local não é enviado para o GitHub.

Os arquivos `.db` gerados durante os testes ficam ignorados pelo Git.

---

# Principais regras de negócio

Durante o desenvolvimento defini algumas regras importantes.

1. Não permito finalizar venda sem usuário autenticado.

2. Não permito vender sem sessão de caixa aberta.

3. Não permito finalizar venda sem itens.

4. Não permito quantidade menor ou igual a zero.

5. Não permito pagamento menor que o total da venda.

6. Valido o estoque antes da finalização.

7. Faço uma segunda proteção de estoque durante a própria transação.

8. A baixa de estoque acontece dentro da mesma transação da venda.

9. Não armazeno senha em texto puro.

10. CPF pode ser opcional, mas precisa ser válido quando informado.

11. Telefone precisa ser validado quando informado.

12. E-mail precisa ser validado quando informado.

13. Produtos inativos não devem ser considerados disponíveis para venda.

14. Tento manter as regras de negócio fora da interface.

15. Tento manter o SQL concentrado nos repositórios.

---

# Requisitos de desenvolvimento

Durante o projeto fui criando alguns padrões pessoais para manter o código organizado.

## Código em português

Utilizo nomes de variáveis, métodos, classes e conceitos de negócio em português sempre que isso faz sentido.

Exemplo:

```java
Venda venda;
Produto produto;
Cliente cliente;
long totalCentavos;
```

Minha intenção é deixar o código próximo do domínio da aplicação.

---

# Comentários no código

Daqui para frente estou utilizando comentários em primeira pessoa e com escrita natural.

Exemplo:

```java
// Aqui eu verifico se ainda tenho estoque suficiente antes de concluir a venda.
```

Outro exemplo:

```java
// Eu busco somente os dados que preciso para montar a tela de histórico.
```

Evito comentários que simplesmente repetem o código.

Exemplo do que procuro evitar:

```java
// Cria variável
int quantidade = 1;
```

Prefiro comentar regras, decisões ou trechos que possam gerar dúvida no futuro.

---

# Valores monetários

Decidi trabalhar internamente com valores monetários em centavos.

Por exemplo:

```text
R$ 7,00
```

é armazenado como:

```text
700
```

Uso `long` para representar esses valores.

Isso reduz problemas relacionados ao uso de ponto flutuante em cálculos financeiros.

---

# Problemas encontrados durante o desenvolvimento

Durante o projeto encontrei vários problemas.

Decidi documentá-los porque eles fazem parte do aprendizado e mostram como fui investigando os erros.

---

# Malformed POM

## O que aconteceu

Em uma das etapas o Maven apresentou:

```text
Malformed POM
```

## Impacto

O Maven não conseguia interpretar corretamente a configuração do projeto.

Isso afetava dependências e execução.

## Como identifiquei

A própria mensagem do Maven apontava para o `pom.xml`.

## Correção

Revisei a estrutura XML do arquivo e corrigi a organização das tags.

Depois disso o Maven voltou a interpretar o projeto corretamente.

---

# InvocationTargetException

## O que aconteceu

Durante algumas inicializações do JavaFX apareceu:

```text
InvocationTargetException
```

## Impacto

A aplicação não conseguia abrir determinada tela ou concluir a inicialização.

## Como identifiquei

Percebi que `InvocationTargetException` normalmente não era a causa principal.

Passei a analisar as mensagens abaixo dela no stack trace.

## Correção

Fui até a causa raiz indicada pelo JavaFX e corrigi o FXML ou recurso relacionado.

Esse problema me ajudou a entender melhor a importância de analisar o stack trace inteiro.

---

# fechamento-caixa.fxml não encontrado

## O que aconteceu

Ao tentar abrir o fechamento de caixa, o sistema não encontrou:

```text
fechamento-caixa.fxml
```

## Impacto

A tela de fechamento não podia ser carregada.

## Como identifiquei

A mensagem indicava que o recurso solicitado não existia no caminho esperado.

## Correção

Criei o arquivo no diretório correto e mantive o caminho de carregamento compatível com a estrutura de resources.

---

# Campo de senha duplicado

## O que aconteceu

Durante a implementação da visualização de senha, o login acabou ficando com conflito entre campos.

Eu precisava trabalhar com:

```text
PasswordField
```

e:

```text
TextField
```

## Impacto

A senha poderia ficar dessincronizada entre os campos.

## Correção

Passei a manter os dois campos sincronizados.

Quando o usuário escolhe visualizar a senha, o `TextField` assume a visualização.

Quando escolhe ocultar, o `PasswordField` volta a ser utilizado.

---

# Exclusão de item

## O que aconteceu

O botão de exclusão precisava identificar corretamente qual item da venda estava selecionado.

## Impacto

A exclusão poderia ser executada sem um item definido.

## Correção

Passei a utilizar explicitamente o item selecionado pela `TableView`.

---

# FormatadorMoeda no pacote errado

## O que aconteceu

O arquivo:

```text
FormatadorMoeda.java
```

ficou inicialmente em uma pasta incompatível com o package declarado.

## Impacto

Outras classes não conseguiam localizar corretamente o utilitário.

## Como identifiquei

Os imports apresentavam erro mesmo com o arquivo existente.

## Correção

Mudei a classe para:

```text
src/main/java/br/com/pdv/utilitario
```

mantendo diretório e package compatíveis.

---

# Estrutura quebrada no ServicoCliente

## O que aconteceu

Durante algumas alterações, chaves foram posicionadas incorretamente.

O compilador começou a mostrar mensagens semelhantes a:

```text
class, interface, enum, or record expected
```

## Impacto

A classe deixou de compilar.

## Como identifiquei

Esse tipo de erro geralmente indica que um método ficou fora da classe ou que alguma chave fechou um bloco cedo demais.

## Correção

Revisei a estrutura completa da classe e reorganizei os métodos dentro do bloco correto.

---

# Método duplicado no ClienteRepositorio

## O que aconteceu

Durante uma alteração no repositório de clientes, um método acabou ficando duplicado ou posicionado de forma incorreta.

## Impacto

O arquivo deixou de compilar corretamente.

## Correção

Revisei a classe completa e deixei apenas uma implementação válida de cada operação.

---

# Referências antigas de imagens

## O que aconteceu

Durante uma mudança visual no projeto, alguns arquivos de imagem foram removidos.

Porém, alguns FXML ainda continham referências a essas imagens.

## Impacto

Se os arquivos fossem excluídos antes das referências, o JavaFX poderia apresentar erro ao carregar a tela.

## Como identifiquei

Utilizei pesquisa recursiva no código.

Exemplo:

```powershell
Get-ChildItem src -Recurse -File |
    Select-String -Pattern 'logo-le-cafe','logo-datacore'
```

## Correção

Primeiro removi todas as referências.

Depois excluí os arquivos.

Por último executei uma nova busca para confirmar que nenhuma referência havia permanecido.

---

# Diferença entre Admin e admin

## O que aconteceu

Durante uma redefinição temporária de senha, utilizei:

```text
Admin
```

Porém, o login salvo no banco era:

```text
admin
```

## Impacto

O comando de atualização não encontrava o usuário correto.

O acesso continuava sendo negado.

## Como identifiquei

Utilizei o próprio repositório para verificar os dois valores.

O teste retornou:

```text
Existe login Admin? false
Existe login admin? true
```

## Correção

Passei a utilizar exatamente:

```text
admin
```

Esse problema também me mostrou a importância de observar a forma como valores de login são tratados.

---

# Método fora da classe

## O que aconteceu

Durante uma manutenção temporária no `UsuarioRepositorio`, adicionei um método depois da chave que encerrava a classe.

## Impacto

Outra classe não conseguia localizar o método.

## Como identifiquei

O método estava visível no arquivo, mas o compilador dizia que ele não existia para `UsuarioRepositorio`.

Então revisei a posição das chaves.

## Correção

Movi o método para dentro da classe.

---

# Erro prepareSent

## O que aconteceu

Durante uma alteração foi digitado:

```java
conexao.prepareSent(sql);
```

O método correto era:

```java
conexao.prepareStatement(sql);
```

## Impacto

O compilador apresentou:

```text
cannot find symbol
```

## Como identifiquei

Fui diretamente para a linha indicada pelo compilador.

## Correção

Corrigi o nome do método.

Apesar de ser um erro simples de digitação, ele mostrou como a mensagem do compilador pode levar diretamente à causa do problema.

---

# listarVendas dentro de salvar

## O que aconteceu

Quando comecei a desenvolver o histórico de vendas, adicionei:

```java
listarVendas()
```

no `VendaRepositorio`.

Porém, faltava uma chave para finalizar corretamente:

```java
salvar()
```

## Impacto

O Java interpretava `listarVendas()` como estando dentro de outro método.

O arquivo começou a apresentar erros de compilação.

## Como identifiquei

Revisei o trecho entre:

```text
finally
```

e:

```java
listarVendas()
```

Foi possível perceber que o método anterior ainda não havia sido fechado.

## Correção

Adicionei a chave no local correto e deixei os dois métodos separados dentro da classe `VendaRepositorio`.

---

# Como costumo investigar erros

Durante este projeto comecei a seguir uma lógica simples sempre que encontro um problema.

Primeiro tento identificar:

```text
Qual foi o erro?
```

Depois:

```text
Em qual arquivo?
```

Depois:

```text
Em qual linha?
```

Em seguida verifico:

```text
O erro é a causa ou apenas consequência de outro erro?
```

Só então altero o código.

Essa abordagem tem evitado fazer várias alterações ao mesmo tempo sem saber exatamente qual resolveu o problema.

---

# Fluxo atual de venda

Atualmente o fluxo principal funciona aproximadamente assim:

```text
Usuário realiza login
        ↓
Sistema verifica sessão do caixa
        ↓
Caixa é aberto
        ↓
Operador informa produto
        ↓
Produto é localizado
        ↓
Sistema valida produto e estoque
        ↓
Produto entra na venda
        ↓
Operador informa pagamento
        ↓
ServicoVenda valida regras
        ↓
VendaRepositorio inicia transação
        ↓
Venda é gravada
        ↓
Itens são gravados
        ↓
Estoque é atualizado
        ↓
Pagamentos são gravados
        ↓
Commit
```

Caso uma operação de banco falhe:

```text
Rollback
```

---

# Estado atual do projeto

Atualmente já tenho uma base funcional com:

- login
- autenticação
- abertura de caixa
- fechamento de caixa
- tela principal do PDV
- cadastro de produtos
- controle de estoque
- cadastro de clientes
- validação de CPF
- validação de telefone
- validação de e-mail
- pagamento em dinheiro
- PIX
- cartão de crédito
- cartão de débito
- cálculo de troco
- persistência das vendas
- persistência dos itens
- persistência dos pagamentos
- baixa transacional de estoque
- estrutura inicial do histórico de vendas

---

# Histórico de vendas em desenvolvimento

A próxima etapa atual do projeto é concluir a tela de histórico.

Já tenho a estrutura inicial para apresentar:

```text
Venda
Data/Hora
Operador
Total
Status
```

Depois pretendo evoluir essa funcionalidade com:

- detalhes da venda
- itens vendidos
- formas de pagamento
- filtros por data
- pesquisa pelo número da venda

---

# Próximas etapas

Entre as próximas funcionalidades que pretendo desenvolver estão:

- concluir histórico de vendas
- visualizar detalhes da venda
- consultar itens
- consultar pagamentos
- filtros por período
- pesquisa por venda
- cancelamento de venda
- devolução de estoque em cancelamentos
- relatórios gerenciais
- gerenciamento de usuários
- configurações do estabelecimento
- backup do banco
- restauração do banco
- impressão de comprovante
- empacotamento para Windows

---

# Evoluções futuras

Depois que a base do PDV estiver mais completa, pretendo estudar integrações mais próximas de um ambiente comercial real.

Entre elas:

```text
NFC-e
TEF
Impressora térmica
Gaveta de dinheiro
```

Essas integrações ainda não fazem parte da versão atual.

---

# Como executar

## Pré-requisitos

Para executar o projeto é necessário possuir:

- Java 25
- Maven

Com o projeto configurado, posso executar através do IntelliJ ou utilizando Maven.

Exemplo:

```bash
mvn javafx:run
```

O SQLite é utilizado localmente pela aplicação.

---

# Configuração inicial do administrador

A criação inicial do administrador utiliza variáveis de ambiente.

```text
PDV_ADMIN_NOME
PDV_ADMIN_LOGIN
PDV_ADMIN_SENHA
```

A senha é convertida para hash antes de ser armazenada.

Ela não deve ser colocada diretamente no código-fonte.

---

# Controle de versão

Estou utilizando Git durante todo o desenvolvimento.

Costumo trabalhar em pequenas etapas e realizar commits conforme cada funcionalidade evolui.

Exemplos de commits utilizados:

```text
feat: inicia estrutura do historico de vendas
```

```text
chore: ajustes e correcoes no projeto
```

Essa organização me permite acompanhar a evolução do projeto e retornar a versões anteriores caso seja necessário.

---

# Sobre meu processo de desenvolvimento

Estou construindo este projeto de maneira incremental.

Minha intenção não é apenas escrever um código que funcione.

Quero entender:

- por que determinada classe existe
- qual responsabilidade ela possui
- onde uma regra de negócio deve ficar
- como o banco deve ser atualizado
- como uma transação protege os dados
- como identificar a causa de um erro
- como manter o projeto organizado enquanto ele cresce

Quando encontro um erro, procuro evitar simplesmente trocar o código até funcionar.

Primeiro tento entender o que aconteceu.

Depois verifico o impacto.

Em seguida procuro a causa.

Só então faço a correção.

Esse processo faz parte do objetivo deste projeto e também da minha evolução como desenvolvedor.
