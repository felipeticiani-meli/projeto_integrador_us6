# Projeto_Integrador - Requisito individual
API REST desenvolvida pelo grupo Beta Campers para o Projeto Integrador feito durante o IT Bootcamp Backend Java (wave 6).
<br><br> O requisito individual acrescenta funcionalidades para que instituições sociais (como as que abrigam pessoas em vulnerabilidade) possam consultar produtos disponíveis para doação e realizar a reserva destes para retirada em um centro de distribuição.

# Sumário

- Documentos
  - [User Story](User%20story.pdf)
  - <a href="https://app.diagrams.net/#G1Gj9U0cSE4nMpaeo89gFOF42RJTsVXzGC">Diagrama UML </a>
  - [DER](DER.png)
  - [Swagger]()
  - [Postman collection](Projeto%20integrador.postman_collection.json)
- [Funcionalidades](#funcionalidades)
  - [GET - Exibe os produtos disponíveis para doação](#get)
  - [POST - Reserva um produto](#post)

# Funcionalidades

`GET /api/v1/foundations?state=[state]&city=[city_name]` <br name="get">
Exibe os produtos disponíveis para doação na cidade pesquisada. Necessário 'Foundation-Id' no header.
<pre><code><b>Response example:</b>
{
    "district": "Centro",
    "address": "Rua XYZ, 45",
    "batches": [
        {
            "batchNumber": 2,
            "productName": "Queijo",
            "brand": "Sadia",
            "category": "CHILLED",
            "quantity": 109,
            "dueDate": "2022-09-01"
        },
        {
            "batchNumber": 7,
            "productName": "Queijo",
            "brand": "Sadia",
            "category": "CHILLED",
            "quantity": 58,
            "dueDate": "2022-10-12"
        }
    ]
}
</code></pre>
 
 `POST /api/v1/foundations?batchNumber=[batch_number]` <br name="post">
Realiza a reserva de um produto para retirada no centro de distribuição. Necessário 'Foundation-Id' no header.
<pre><code><b>Response example:</b>
{
    "donationId": "010a6db6-a043-4b5d-adef-d974f9df2630",
    "quantity": 109,
    "date": "2022-08-15"
}
</code></pre>
