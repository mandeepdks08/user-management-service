CREATE TABLE IF NOT EXISTS userrelationrequest (
    id bigserial PRIMARY KEY,
    fromuserid varchar(256) REFERENCES users(userid) NOT NULL,
    touserid varchar(256) REFERENCES users(userid) NOT NULL,
    relationrequesttype varchar(256) NOT NULL,
    state varchar(256) NOT NULL,
    createdon timestamp NOT NULL,
    processedon timestamp NOT NULL
);