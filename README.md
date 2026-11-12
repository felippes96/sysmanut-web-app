# SysManut Web (Spring Boot MVC + Thymeleaf + JDBC/MySQL)

Reaproveita o `sysmanut-core` (etapas 6 e 7) e as páginas da etapa 8.

## Como rodar
1. MySQL com um banco `sysmanut` (`CREATE DATABASE sysmanut;`). As tabelas `cliente` e `usuario` são criadas por `schema.sql`.
2. Defina as variáveis de ambiente (a senha NÃO fica no código):
   `SYSMANUT_DB_PASSWORD` (obrigatória), `SYSMANUT_DB_USER` e `SYSMANUT_DB_URL` (opcionais), `SYSMANUT_ADMIN_PASSWORD` (cria o usuário `admin` na 1ª execução).
   No NetBeans: *Project Properties > Actions > Run* ou *Run > Set Project Configuration*, ou defina no Windows (Variáveis de ambiente) e reabra o NetBeans.
3. NetBeans: *File > Open Project* nesta pasta (projeto Maven), botão direito > *Run*. Abra http://localhost:8080 (login: `admin`).
4. Testes automatizados: botão direito > *Test* (`mvn test`): 51 testes JUnit da etapa 7.

## Estrutura
`sysmanut.*` = núcleo (model, service, repository, validation...) · `sysmanut.web` = controllers MVC, interceptor de login e ligação dos beans · `resources/templates` = páginas Thymeleaf · `resources/static` = CSS e JS.
