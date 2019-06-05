package com.uvt.dw.Data;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "asset")
public class Asset {
	public Asset() {
	}

	public Asset(String ID, Date SystemTime) {
		this.ID = ID;
		this.SystemTime = SystemTime;
		SetDefaults();
	}

	@PrimaryKeyColumn(name = "id", ordinal = 1)
	public String ID;

	@PrimaryKeyColumn(name = "system_time", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	public Date SystemTime;

	@Column("values_blob")
	public Map<String, ByteBuffer> ValuesBlob;

	@Column("values_boolean")
	public Map<String, Boolean> ValuesBoolean;

	@Column("values_double")
	public Map<String, Double> ValuesDouble;

	@Column("values_int")
	public Map<String, Integer> ValuesInt;

	@Column("values_text")
	public Map<String, String> ValuesText;

	@Column("values_timestamp")
	public Map<String, Date> ValuesTimestamp;

	public void SetDefaults() {
		if (ValuesBlob == null) {
			ValuesBlob = new HashMap<String, ByteBuffer>();
		}
		if (ValuesBoolean == null) {
			ValuesBoolean = new HashMap<String, Boolean>();
		}
		if (ValuesDouble == null) {
			ValuesDouble = new HashMap<String, Double>();
		}
		if (ValuesInt == null) {
			ValuesInt = new HashMap<String, Integer>();
		}
		if (ValuesText == null) {
			ValuesText = new HashMap<String, String>();
		}
		if (ValuesTimestamp == null) {
			ValuesTimestamp = new HashMap<String, Date>();
		}
	}
}