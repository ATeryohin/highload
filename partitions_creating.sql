DROP TABLE IF EXISTS test; 

CREATE TABLE test (
    id         int not null,
    logdate         date not null,
    peaktemp        int,
    unitsales       int
) PARTITION BY RANGE (logdate);

CREATE TABLE test_y2006m02 PARTITION OF test
    FOR VALUES FROM ('2006-02-01') TO ('2006-03-01');

CREATE TABLE test_y2006m03 PARTITION OF test
    FOR VALUES FROM ('2006-03-01') TO ('2006-04-01');

CREATE TABLE test_y2007m11 PARTITION OF test
    FOR VALUES FROM ('2007-11-01') TO ('2007-12-01');

CREATE INDEX ON test (logdate);

INSERT INTO test (
	id, logdate, peaktemp, unitsales)
	VALUES (0, '2006-03-05', 10, 10);

INSERT INTO test (
	id, logdate, peaktemp, unitsales)
	VALUES (0, '2006-02-10', 10, 10);

