# Projeto_Integrador - Requisito individual
API REST desenvolvida pelo grupo Beta Campers para o Projeto Integrador feito durante o IT Bootcamp Backend Java (wave 6).
<br><br> O requisito individual acrescenta funcionalidades para que instituições sociais (como as que abrigam pessoas em vulnerabilidade) possam consultar produtos disponíveis para doação e realizar a reserva destes para retirada em um centro de distribuição.

# Sumário

- Documentos
  - [User Story](User%20story.pdf)
  - <a href="">Diagrama UML </a>
  - [DER](DER.png)
  - [Swagger]()
  - [Postman collection]()
  - [Swagger]()
- [Funcionalidades](#funcionalidades)
  - [GET - Exibe os produtos disponíveis para doação](#get)
  - [POST - Reserva um produto](#post)

# Funcionalidades

`GET /api/v1/foundations?city=[city_name]` <br name="get">
Exibe os produtos disponíveis para doação na cidade pesquisada.
<pre><code><b>Response example:</b>

</code></pre>
 
 `POST /api/v1/foundations` <br name="post">
Realiza a reserva de um produto para retirada no centro de distribuição.
<pre><code><b>Payload Example:</b>


<b>Response:</b>
</code></pre>
