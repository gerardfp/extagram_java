CREATE TABLE IF NOT EXISTS post (
    postid uuid NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    content text,
    photourl text);
