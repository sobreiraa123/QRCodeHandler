# QR Code Handler

QR Code Handler é um aplicativo Java que lê informações de códigos QR em faturas comerciais, armazena os dados em um banco de dados PostgreSQL e exporta os dados para um arquivo CSV. A interface gráfica do usuário é construída usando Swing.

## Visão Geral

Este projeto foi desenvolvido para facilitar a leitura e processamento de faturas comerciais através de códigos QR, seguindo a Especificação Técnica da Autoridade Tributária e Aduaneira. O aplicativo extrai informações detalhadas de cada fatura, armazena em um banco de dados PostgreSQL e permite exportar os dados para um formato CSV para análise posterior.

## Funcionalidades

- Leitura de códigos QR de imagens.
- Extração e parsing de dados conforme especificação técnica.
- Armazenamento dos dados em um banco de dados PostgreSQL.
- Exportação dos dados para arquivos CSV.
- Interface gráfica amigável com Java Swing.

## Pré-requisitos

- **Java JDK 8 ou superior**
- **PostgreSQL** (com um banco de dados e tabela configurados conforme as instruções)
- **Bibliotecas ZXing** (`core.jar` e `javase.jar`)
- **Biblioteca JDBC do PostgreSQL** (`postgresql-<version>.jar`)
