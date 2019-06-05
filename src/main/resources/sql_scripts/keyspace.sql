create keyspace if not exists dwkeyspace with durable_writes = true
	and replication = {'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '2'};

create table if not exists asset
(
	id text,
	system_time timestamp,
	values_blob map<text, blob>,
	values_boolean map<text, boolean>,
	values_double map<text, double>,
	values_int map<text, int>,
	values_text map<text, text>,
	values_timestamp map<text, timestamp>,
	primary key (id, system_time)
)
with clustering order by (system_time desc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '64'}
	and dclocal_read_repair_chance = 0.1;

create table if not exists attribute
(
	id text,
	system_time timestamp,
	data_type int,
	description text,
	name text,
	primary key (id, system_time)
)
with clustering order by (system_time desc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '64'}
	and dclocal_read_repair_chance = 0.1;

create table if not exists timeseriesdata
(
	asset_id text,
	time_series_definition_id text,
	business_date_year int,
	business_date timestamp,
	system_time timestamp,
	values_blob map<text, blob>,
	values_boolean map<text, boolean>,
	values_double map<text, double>,
	values_int map<text, int>,
	values_text map<text, text>,
	values_timestamp map<text, timestamp>,
	primary key ((asset_id, time_series_definition_id, business_date_year), business_date, system_time)
)
with clustering order by (business_date desc, system_time desc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '64'}
	and dclocal_read_repair_chance = 0.1;


create table if not exists timeseriesdefinition
(
	id text,
	system_time timestamp,
	attributes set<text>,
	description text,
	name text,
	publisher text,
	type int,
	primary key (id, system_time)
)
with clustering order by (system_time desc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '64'}
	and dclocal_read_repair_chance = 0.1;

