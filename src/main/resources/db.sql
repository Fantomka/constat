CREATE TABLE contractor_rating(
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    INN varchar(12) NOT NULL UNIQUE,
    rate int
);
INSERT INTO contractor_rating(INN, rate) VALUES ('0262014762', 15);
INSERT INTO contractor_rating(INN, rate) VALUES ('1655325273', 11);
INSERT INTO contractor_rating(INN, rate) VALUES ('3849031759', 17);
INSERT INTO contractor_rating(INN, rate) VALUES ('5015012336', 24);
INSERT INTO contractor_rating(INN, rate) VALUES ('5017050954', 13);
INSERT INTO contractor_rating(INN, rate) VALUES ('2540186843', 16);
INSERT INTO contractor_rating(INN, rate) VALUES ('2465288642', 31);
INSERT INTO contractor_rating(INN, rate) VALUES ('2222044316', 15);
INSERT INTO contractor_rating(INN, rate) VALUES ('4205002373', 22);
INSERT INTO contractor_rating(INN, rate) VALUES ('5013019921', 19);


CREATE TABLE contracts(
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    contractor_id int REFERENCES contractor_rating ON DELETE SET NULL,
    given_at timestamp,
    expires_after int,
    paid_at int
);
