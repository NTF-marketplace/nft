CREATE TABLE IF NOT EXISTS collection (
    name varchar(500) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS nft (
    id SERIAL PRIMARY KEY,
    token_id VARCHAR(255) NOT NULL,
    token_address VARCHAR(255) NOT NULL,
    chain_type varchar(100) NOT NULL,
    nft_name varchar(255) NOT NULL,
    owner_of varchar(255),
    token_hash varchar(300),
    amount INT,
    collection_name varchar(500) REFERENCES collection(name)
);


CREATE TABLE IF NOT EXISTS metadata (
    id SERIAL PRIMARY KEY,
    nft_id BIGINT REFERENCES nft(id),
    description TEXT,
    animation_url TEXT,
    image TEXT
);






