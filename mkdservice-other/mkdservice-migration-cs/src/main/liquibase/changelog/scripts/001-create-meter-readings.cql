CREATE TABLE IF NOT EXISTS mkdservice_meter_readings (
    id text PRIMARY KEY,
    amount text,
    unit text,
    date_time text,
    meter_id text,
    apartment_id text,
    lock text
);

CREATE CUSTOM INDEX IF NOT EXISTS ON mkdservice_meter_readings (date_time)
  USING 'org.apache.cassandra.index.sasi.SASIIndex'
  WITH OPTIONS = {
    'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.StandardAnalyzer',
    'tokenization_enable_stemming': 'true',
    'analyzed': 'true',
    'tokenization_normalize_lowercase': 'true',
    'tokenization_locale': 'en'
  };
