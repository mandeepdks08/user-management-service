CREATE TABLE IF NOT EXISTS userrelation (
    id bigserial PRIMARY KEY,
    userid varchar(256) REFERENCES users(userid) NOT NULL,
    relateduserid varchar(256) REFERENCES users(userid) NOT NULL,
    relationtype varchar(256) NOT NULL,
    createdon timestamp NOT NULL,
    processedon timestamp NOT NULL,
    UNIQUE(userid, relateduserid)
);