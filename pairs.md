[SQL Fiddle][1]

**PostgreSQL 9.6 Schema Setup**:

    create table t (a int);
    
    insert into t values (4);
    insert into t values (-5);
    insert into t values (0);
    insert into t values (2);
    insert into t values (1);
    insert into t values (8);
    
**Query 1**:

    with ranked as (
      select rank() over (order by a) as r, a from t
    )
    select r1.a as one, r2.a as two
    from ranked r1, ranked r2
    where r1.a != r2.a and r2.r - r1.r = 1

**[Results][2]**:

    | one | two |
    |-----|-----|
    |  -5 |   0 |
    |   0 |   1 |
    |   1 |   2 |
    |   2 |   4 |
    |   4 |   8 |
