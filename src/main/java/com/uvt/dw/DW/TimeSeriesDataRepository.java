package com.uvt.dw.DW;

import java.util.Optional;

import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.uvt.dw.Data.TimeSeriesData;

public interface TimeSeriesDataRepository extends CassandraRepository<TimeSeriesData, MapId> {
//    @Query("SELECT*FROM greetings WHERE user=?0 LIMIT ?1")
//    Iterable<Greeting> findByUser(String user,Integer limit);
//    @Query("SELECT*FROM greetings WHERE user=?0 AND id<?1 LIMIT ?2")
//    Iterable<Greeting> findByUserFrom(String user,UUID from,Integer limit);

	@Query("create table if not exists timeseriesdata\n" + "(\n" + "	asset_id text,\n"
			+ "	time_series_definition_id text,\n" + "	business_date_year int,\n" + "	business_date timestamp,\n"
			+ "	system_time timestamp,\n" + "	values_blob map<text, blob>,\n"
			+ "	values_boolean map<text, boolean>,\n" + "	values_double map<text, double>,\n"
			+ "	values_int map<text, int>,\n" + "	values_text map<text, text>,\n"
			+ "	values_timestamp map<text, timestamp>,\n"
			+ "	primary key ((asset_id, time_series_definition_id, business_date_year), business_date, system_time)\n"
			+ ")\n" + "with clustering order by (business_date desc, system_time desc)\n"
			+ "	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}\n"
			+ "	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}\n"
			+ "	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '64'}\n"
			+ "	and dclocal_read_repair_chance = 0.1;")
	public void initTable();
	
	@AllowFiltering
	@Query("SELECT*FROM timeseriesdata WHERE asset_id=?0 ALLOW FILTERING")
	Optional<TimeSeriesData> findByIdFltr(String id);
}