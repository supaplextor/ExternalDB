# ExternalDB

Simple JDBC powered generic SQL client for Android

* MySQL
* PostgreSQL
* MSSQL

Tested with PostgreSQL

# Tablet View

![Alt text](https://raw.githubusercontent.com/supaplextor/ExternalDB/master/Screenshot_1552430558.png?raw=true "Screenshot")

# SSH / CLI alternative

E.g., not using this app, use ssh + cli

<pre>bitcoin=# \d users_102202 
                        Table &quot;public.users_102202&quot;
   Column   |            Type             | Collation | Nullable | Default 
------------+-----------------------------+-----------+----------+---------
 id         | integer                     |           | not null | 
 reputation | integer                     |           | not null | 
 creation   | timestamp without time zone |           | not null | 
 name       | text                        |           |          | 
 lastaccess | timestamp without time zone |           |          | 
 website    | text                        |           |          | 
 location   | text                        |           |          | 
 aboutme    | text                        |           |          | 
 views      | integer                     |           |          | 
 upvotes    | integer                     |           |          | 
 downvotes  | integer                     |           |          | 
 age        | integer                     |           |          | 
Indexes:
    &quot;users_id_key_102202&quot; UNIQUE CONSTRAINT, btree (id)

bitcoin=# select id,name,views,age from public.users_102202 limit 10;
 id  |         name         | views | age 
-----+----------------------+-------+-----
  31 | Rebecca Chernoff     |     1 |  34
  88 | Phonics The Hedgehog |    74 |    
 119 | Domchi               |    20 |  39
 137 | Crispy               |   597 |    
 145 | osmosis              |   127 |    
 155 | user155              |     0 |    
 255 | Gabriel              |     5 |    
 283 | Dmitry Vyal          |     0 |    
 291 | jamie                |     3 |    
 311 | André Paramés        |     1 |    
(10 rows)

bitcoin=# \q
<font color="#4E9A06"><b>supaplex@compute202</b></font>:<font color="#3465A4"><b>~</b></font>$ psql -U postgres -h compute202 bitcoin

</pre>
