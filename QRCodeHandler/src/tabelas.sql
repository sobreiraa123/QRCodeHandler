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