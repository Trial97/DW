package com.uvt.dw.DW;

import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.uvt.dw.Data.Asset;

public interface AssetRepository extends CassandraRepository<Asset, MapId> {
//    @Query("SELECT*FROM greetings WHERE user=?0 LIMIT ?1")
//    Iterable<Greeting> findByUser(String user,Integer limit);
//    @Query("SELECT*FROM greetings WHERE user=?0 AND id<?1 LIMIT ?2")
//    Iterable<Greeting> findByUserFrom(String user,UUID from,Integer limit);

	@Query("create table if not exists asset\n" + "(\n" + "	id text,\n" + "	system_time timestamp,\n"
			+ "	values_blob map<text, blob>,\n" + "	values_boolean map<text, boolean>,\n"
			+ "	values_double map<text, double>,\n" + "	values_int map<text, int>,\n"
			+ "	values_text map<text, text>,\n" + "	values_timestamp map<text, timestamp>,\n"
			+ "	primary key (id, system_time)\n" + ")\n" + "with clustering order by (system_time desc)\n"
			+ "	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}\n"
			+ "	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}\n"
			+ "	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '64'}\n"
			+ "	and dclocal_read_repair_chance = 0.1;")
	public void initTable();
}