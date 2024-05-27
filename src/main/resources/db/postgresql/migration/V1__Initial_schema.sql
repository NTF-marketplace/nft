CREATE TYPE contract_type AS ENUM(
    'ERC721',
    'ERC1155'
    );

CREATE TYPE  chain_type AS ENUM (
    'ETHEREUM_MAINNET',
    'LINEA_MAINNET',
    'LINEA_SEPOLIA',
    'POLYGON_MAINNET',
    'ETHEREUM_HOLESKY',
    'ETHEREUM_SEPOLIA',
    'POLYGON_AMOY'
    );


CREATE TABLE IF NOT EXISTS collection (
    name varchar(500) PRIMARY KEY,
    logo varchar(500),
    banner_image varchar(500),
    description varchar(1000)
);

CREATE TABLE IF NOT EXISTS nft (
    id SERIAL PRIMARY KEY,
    token_id VARCHAR(255) NOT NULL,
    token_address VARCHAR(255) NOT NULL,
    chain_type chain_type NOT NULL,
    nft_name varchar(255),
    contract_type contract_type NOT NULL,
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


CREATE TABLE IF NOT EXISTS attribute (
    id SERIAL PRIMARY KEY,
    nft_id BIGINT REFERENCES nft(id),
    trait_type varchar(255),
    value varchar(255)
);


CREATE TABLE IF NOT EXISTS transfer (
    id SERIAL PRIMARY KEY,
    nft_id BIGINT REFERENCES nft(id),
    from_address varchar(255) not null,
    to_address varchar(255) not null,
    block_number bigint not null,
    block_timestamp bigint not null
);






