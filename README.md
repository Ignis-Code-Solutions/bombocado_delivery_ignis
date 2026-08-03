# BOMbocado - Delivery de Alimentos

<br />

<div align="center">    <img src="https://ik.imagekit.io/carlosTeste/BOMbocado.png" title="Logo do projeto BOMbocado" alt="Logo BOMbocado" width="55%" /> </div>

<br /><br />

------



## 1. Descrição

O **BOMbocado** é uma plataforma de delivery de alimentos criada para conectar empresas que possuem produtos próximos da validade ou fora de época a consumidores que buscam alimentos de qualidade por preços mais acessíveis.

Por meio da plataforma, as empresas podem disponibilizar produtos e montar ofertas com descontos atrativos, enquanto os consumidores podem visualizar as opções disponíveis e realizar suas compras de forma prática.

O projeto busca contribuir para a redução do desperdício de alimentos, gerar economia para os consumidores e criar novas oportunidades de receita para empresas parceiras.

> **Conectando quem tem excesso a quem precisa de acesso.**

------



## 2. Sobre esta API

A API do **BOMbocado** foi desenvolvida para gerenciar os principais dados da plataforma, como usuários, categorias e produtos.

O backend disponibiliza recursos para cadastro, consulta, atualização e exclusão de informações, além de organizar o relacionamento entre as entidades do sistema e realizar a persistência dos dados em um banco de dados relacional.

A aplicação foi construída seguindo a arquitetura REST, utilizando Java, Spring Boot, Spring Data JPA e MySQL.

### 2.1. Principais Funcionalidades

1. Cadastro, consulta, atualização e exclusão de usuários.
2. Cadastro e gerenciamento de categorias de produtos.
3. Cadastro, consulta, atualização e exclusão de produtos.
4. Associação de produtos às suas respectivas categorias.
5. Armazenamento de informações como nome, descrição, imagem, preço e tempo estimado de entrega.
6. Organização dos usuários por tipo de atuação na plataforma.
7. Persistência dos dados em banco de dados MySQL.
8. Validação dos dados recebidos pela API.
9. Estrutura preparada para integração com uma aplicação frontend.
10. Base para futuras funcionalidades, como avaliações, cupons, rastreamento de pedidos, notificações e doações.

------



## 3. Diagrama de Classes

O Diagrama de Classes representa a estrutura das principais entidades do backend e seus relacionamentos.

<div align="center">     <img src="https://i.imgur.com/SEU_LINK_DO_DIAGRAMA_DE_CLASSES.png" title="Diagrama de Classes do BOMbocado" alt="Diagrama de Classes" width="85%" /> </div>

------



## 4. Diagrama Entidade-Relacionamento (DER)

O Diagrama Entidade-Relacionamento apresenta a organização das tabelas do banco de dados e os relacionamentos entre usuários, categorias e produtos.

<div align="center">     <img src="https://i.imgur.com/SEU_LINK_DO_DER.png" title="Diagrama Entidade-Relacionamento do BOMbocado" alt="Diagrama Entidade-Relacionamento" width="85%" /> </div>

------



## 5. Tecnologias utilizadas

| Item                         | Descrição                                         |
| ---------------------------- | ------------------------------------------------- |
| **Servidor**                 | Tomcat embutido, disponibilizado pelo Spring Boot |
| **Linguagem de programação** | Java 17                                           |
| **Framework**                | Spring Boot                                       |
| **ORM**                      | Spring Data JPA / Hibernate                       |
| **Banco de dados**           | MySQL 8.0                                         |

------



## 6. Estrutura principal do projeto

```
src
└── main
    ├── java
    │   └── com
    │       └── bombocado
    │           ├── controller
    │           ├── model
    │           ├── repository
    │           └── service
    └── resources
        └── application.properties
```

- **Controller:** recebe as requisições HTTP e disponibiliza os endpoints da API.
- **Model:** representa as entidades e tabelas do sistema.
- **Repository:** realiza a comunicação com o banco de dados.
- **Service:** concentra regras de negócio, quando aplicável.
- **application.properties:** armazena as configurações da aplicação e do banco de dados.

------



## 7. Configuração e Execução

### 7.1. Pré-requisitos

Antes de executar o projeto, instale:

- Java JDK 17
- MySQL 8.0
- Maven
- Git
- Uma IDE, como Spring Tool Suite, IntelliJ IDEA ou Eclipse

### 7.2. Clone o repositório

```
git clone https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git
```

Acesse a pasta do projeto:

```
cd SEU_REPOSITORIO
```

### 7.3. Crie o banco de dados

Acesse o MySQL e execute:

```
CREATE DATABASE db_bombocado;
```

### 7.4. Configure o banco de dados

No arquivo `src/main/resources/application.properties`, configure os dados de acesso ao MySQL:

```
spring.datasource.url=jdbc:mysql://localhost:3306/db_bombocado?createDatabaseIfNotExist=true&serverTimezone=America/Sao_Paulo&useSSl=false
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> Não envie sua senha pessoal do banco de dados para o GitHub. Utilize variáveis de ambiente ou um arquivo de configuração local ignorado pelo Git.

### 7.5. Instale as dependências

```
mvn clean install
```

### 7.6. Execute a aplicação

```
mvn spring-boot:run
```

A API ficará disponível, por padrão, em:

```
http://localhost:8080
```

------



## 8. Equipe

O **BOMbocado** foi desenvolvido pela equipe **Ignis Code Solutions**, formada por estudantes do Bootcamp Java Full Stack da Generation Brasil, com foco na aplicação de boas práticas de desenvolvimento de software, metodologias ágeis e construção de APIs REST utilizando Java e Spring Boot.

### Desenvolvedores

- *André Nunes*
- *Daniel Araújo*
- *Elaine Alves*
- *Jonathan Leão*
- *João Pedro Duo*
- *Luiza Paolinelli*
- *Patrick Carneiro*

------

