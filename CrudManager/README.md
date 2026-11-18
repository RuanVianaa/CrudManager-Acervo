# AcervoMusic (CRUD Manager)

Projeto da disciplina Tecnologias de Desenvolvimento para Internet II
(IFSULDEMINAS, Campus Machado), baseado no **CRUD Manager** do
Prof. Dr. Emerson A. Carvalho.

## O que veio do projeto base
Cadastros de Usuários, Posts e Empresas, com a estrutura em camadas
(modelo, DAO, controle, visão) e o `DAOFactory`.

## O que eu acrescentei
- **Artistas** (nome, data de nascimento, país, ativo, biografia e
  curador, que é uma FK para Usuário).
- **Álbuns** (título, lançamento, faixas, formato, explícito, descrição
  e artista, que é uma FK para Artista).
- **Login** com `AuthFilter`, `LoginController`, página de login,
  logout e senha guardada como hash SHA-256.
- **Paginação** em todos os cadastros (5 registros por página).
- Links dos novos cadastros no cabeçalho.

## Relacionamentos
Usuário → Artista → Álbum
(`artistas.user_id` com RESTRICT; `albuns.artista_id` com CASCADE).

## Como executar
1. Java 21 e Apache Tomcat 11 (ou 10.1, com Dynamic Web Module 6.0).
2. No MySQL, crie o banco `crud_manager` e rode o script `sql/<nome-do-seu-script>.sql`.
3. Confira a conexão em `MySQLConnectionFactory` (porta, usuário e senha).
4. Importe como "Existing Projects into Workspace" e rode no servidor.
5. Acesse `http://localhost:8080/crud-manager/` e entre com um usuário
   do banco (senha padrão do script: `123456`).

## Estrutura
- `controller/`: servlets, filtro de login e utilitários de paginação
- `model/`: classes de domínio
- `model/dao/`: interfaces, implementações MySQL e `DAOFactory`
- `webapp/`: páginas JSP