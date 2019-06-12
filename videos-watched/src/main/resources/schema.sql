CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS video (
    id BIGINT PRIMARY KEY,
    name VARCHAR,
    group_name VARCHAR,
    total_time BIGINT
);

CREATE TABLE IF NOT EXISTS watch_progress (
    video_id BIGINT,
    user_id BIGINT,
    time_played BIGINT,
    last_watch_date TIMESTAMP,
    PRIMARY KEY(user_id, video_id)
);