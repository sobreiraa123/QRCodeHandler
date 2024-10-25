# QR Code Handler

QR Code Handler é uma app Java que lê informações de códigos QR em faturas comerciais com ajuda da biblioteca ZXing, guarda os dados numa base de dados PostgreSQL e exporta os dados para um arquivo CSV. A interface gráfica do utilizador foi construída utilizando Java Swing.

## Funcionalidades

  - Leitura de códigos QR de imagens.
  - Extração e parsing de dados extraidos da imagem.
  - Armazenamento dos dados numa base de dados PostgreSQL.
  - Exportação dos dados para arquivos CSV.
  - Introdução do custo e tipo de despesa após a leitura do QR Code.
  - Visualização das despesas e total gasto, categorizadas por tipo de despesa.

## Pré-requisitos

  - Java JDK 8 ou superior
  - PostgreSQL (com uma base de dados e tabela configuradas conforme as instruções)
  - Bibliotecas ZXing (core.jar e javase.jar)
  - Biblioteca JDBC do PostgreSQL (postgresql-<version>.jar)

## Configuração

  **Clone o Repositório:**

    git clone https://github.com/seu-usuario/qr-code-handler.git
    cd qr-code-handler

## Configure a base de dados em PostgreSQL:

**sql**

    CREATE TABLE invoices (
      id SERIAL PRIMARY KEY,
      nif_emitente VARCHAR(255),
      nif_adquirente VARCHAR(255),
      pais_adquirente VARCHAR(255),
      tipo_documento VARCHAR(255),
      estado_documento VARCHAR(255),
      data_documento DATE,
      identificacao_documento VARCHAR(255),
      atcud VARCHAR(255),
      espaco_fiscal VARCHAR(255),
      base_tributavel_iva_taxa_normal DECIMAL,
      total_iva_taxa_normal DECIMAL,
      total_impostos DECIMAL,
      total_documento DECIMAL,
      hash VARCHAR(255),
      numero_certificado VARCHAR(255),
      outras_informacoes TEXT,
      custo VARCHAR(255),
      tipo_despesa VARCHAR(255)
    );

    
    
Abra o projeto numa IDE (Integrated Development Environment) e adicione as bibliotecas necessárias.

Execute a classe MainApp para iniciar a aplicação.



## Utilização

  Submeter dados de código QR na base de dados: Clique em "Load QR Code" e selecione uma imagem com um código QR. Introduza o total do documento e selecione ou adicione uma categoria de despesa.
  Visualizar Despesas: Clique em "Show Expenses" para ver a lista de despesas, incluindo o tipo de despesa e o total gasto
