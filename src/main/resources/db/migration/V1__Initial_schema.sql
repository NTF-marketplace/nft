CREATE TABLE IF NOT EXISTS collection (
    name TEXT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS nft (
    id SERIAL PRIMARY KEY,
    token_id VARCHAR(255) NOT NULL,
    token_address VARCHAR(255) NOT NULL,
    chain_type varchar(100) NOT NULL,
    image TEXT NOT NULL,
    nft_name varchar(255) NOT NULL,
    collection_name TEXT REFERENCES collection(name)
);

