INSERT INTO NOTES
(
	author, email, date_created, content
)
VALUES
(
	'Adam',	'adam.arczynski@gmail.com',	'2008-10-05 00:00:00', 'moja notatka ble ble ble'
),
(
	'Tomek', 'tt.t@t.t', '2014-03-04 00:00:00',	'moja notatka ble ble ble'
),
(
	'Adam',	'adam.arczynski@gmail.com',	'2011-05-25 00:00:00', 'moja notatka 2 bla bla'
),
(
	'Anka',	'ania@mail.com', '1997-01-01 00:00:00', 'wpis anki'
),
(
	'Anka',	'ania@mail.com', '1998-10-12 00:00:00', 'wpis anki 2'
),
(
	'Rey',	'reyZ@r.r2', '2013-08-22 00:00:00', 'rey rey rey rey rey'
),
(
	'Andrzej',	'and.and@op.pl', '2013-04-17 00:00:00', 'and and and and and and'
),
(
	'Ilona',	'Il1999@mail.com', '2007-02-04 00:00:00', 'ilona ilona ilona ilona ilona'
),
(
	'Jola',	'jj@j.pl', '2011-10-30 00:00:00', 'jjjj jjjjjjjjjjjjj jjj jjjjjjjjjj j jj'
),
(
	'Maciej',	'maciej.mac@op.pl', '2005-04-18 00:00:00', 'Maciek Maciek Maciek Maciek Maciek'
),
(
	'ReRe',	'rere@wp.pl', '2000-01-01 00:00:00', 'rererererer erererere ererere'
);

INSERT INTO USERS
(
	nick, email, date_registered, password_hash, password_encoding, password_salt
)
VALUES
(
	'Adam',	'adam.arczynski@gmail.com',	'1990-01-01 00:00:00', 'hash', 'sha-256', 'salt'
)