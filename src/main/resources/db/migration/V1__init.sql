-- Users
create table if not exists users (
    id varchar primary key,
    username varchar(32) unique not null,
    password varchar(32),
    created_at bigint not null
    );


-- Follows (grafo social)
create table if not exists follows (
    follower_id varchar not null,
    followee_id varchar not null,
    created_at bigint not null,
    primary key (follower_id, followee_id)
    );
create index if not exists idx_follows_followee on follows(followee_id);
create index if not exists idx_follows_follower on follows(follower_id);


-- Tweets
create table if not exists tweets (
    id varchar primary key,
    author_id varchar not null references users(id),
    text varchar(280) not null,
    created_at bigint not null
    );
create index if not exists idx_tweets_author_created on tweets(author_id, created_at desc);


-- Datos demo opcionales (descomentar para pruebas)
-- insert into users(id, username, created_at) values ('01JZ0DEMOUSER000000000000', 'demo', extract(epoch from now())*1000)
-- on conflict do nothing;
