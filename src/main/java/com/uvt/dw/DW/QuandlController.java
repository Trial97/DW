package com.uvt.dw.DW;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jimmoores.quandl.DataSetRequest;
import com.jimmoores.quandl.Row;
import com.jimmoores.quandl.TabularResult;
import com.jimmoores.quandl.classic.ClassicQuandlSession;
import com.uvt.dw.Data.Asset;
import com.uvt.dw.Data.Attribute;
import com.uvt.dw.Data.TimeSeriesData;
import com.uvt.dw.Data.TimeSeriesDefinition;

@RestController
public class QuandlController {

	@Autowired
	public AssetRepository assetRepository;

	@Autowired
	public AttributeRepository attributeRepository;

	@Autowired
	public TimeSeriesDataRepository tsDataRepository;

	@Autowired
	public TimeSeriesDefinitionRepository tsDefinitionRepository;

	@RequestMapping(value = "/quandl", method = RequestMethod.GET)
	@ResponseBody
	public String importQuandl(@RequestParam String symbol) {
		initTables();
		ClassicQuandlSession session = ClassicQuandlSession.create();
		TabularResult tabularResult = session.getDataSet(DataSetRequest.Builder.of(symbol).build());
//		System.out.println(tabularResult.toPrettyPrintedString());
		Date date = new Date();
		String tsName = symbol;
		String tsDescription = "ts";
		TimeSeriesDefinition tsDef = new TimeSeriesDefinition("tsd_" + symbol, date, tsName, 0, tsDescription);

		Asset a = new Asset(symbol, date);
		assetRepository.save(a);

		for (String columnName : tabularResult.getHeaderDefinition().getColumnNames()) {
			Attribute attribute = new Attribute(columnName, date, columnName, 0);
			attributeRepository.save(attribute);
			tsDef.Attributes.add(columnName);
		}
		tsDefinitionRepository.save(tsDef);

		Date businessTime = null;
		for (final Row row : tabularResult) {
			Map<String, Double> doubleValues = new HashMap<String, Double>();
			Map<String, String> stringValues = new HashMap<String, String>();
			for (String columnName : tabularResult.getHeaderDefinition().getColumnNames()) {
				if (!columnName.equals("Date")) {
					try {
						doubleValues.put(columnName, row.getDouble(columnName));
					} catch (NumberFormatException e) {
						stringValues.put(columnName, row.getString(columnName));
					}
				}
			}
			try {
				businessTime = new SimpleDateFormat("yyyy-MM-dd").parse(row.getLocalDate("Date").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			TimeSeriesData tsData = new TimeSeriesData(symbol, tsDef.ID, businessTime.getYear(), businessTime, date);
			tsData.ValuesDouble = doubleValues;
			tsData.ValuesText = stringValues;
			tsDataRepository.save(tsData);
		}
		return "OK";
	}

	@RequestMapping(value = "/csv", method = RequestMethod.GET)
	@ResponseBody
	public String importCSV(@RequestBody CSVHelper csvPath) {
		initTables();
		if (csvPath.path == null || csvPath.path.isEmpty()) {
			return "Error missing path variable";
		}
		if (csvPath.symbol == null || csvPath.symbol.isEmpty()) {
			return "Error missing symbol variable";
		}
		CSVParser csvParser;
		try {
			csvParser = new CSVParser(Files.newBufferedReader(Paths.get(csvPath.path)),
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
		} catch (IOException e) {
			e.printStackTrace();
			return "Error reading file";
		}

		Date date = new Date();
		String symbol = csvPath.symbol;
		String tsName = symbol;
		String tsDescription = "ts";
		TimeSeriesDefinition tsDef = new TimeSeriesDefinition("tsd_" + symbol, date, tsName, 0, tsDescription);

		Asset a = new Asset(symbol, date);
		assetRepository.save(a);

		for (String columnName : csvParser.getHeaderMap().keySet()) {
			Attribute attribute = new Attribute(columnName, date, columnName, 0);
			attributeRepository.save(attribute);
			tsDef.Attributes.add(columnName);
		}
		tsDefinitionRepository.save(tsDef);

		Date businessTime = null;
		for (final CSVRecord row : csvParser) {
			Map<String, Double> doubleValues = new HashMap<String, Double>();
			Map<String, String> stringValues = new HashMap<String, String>();
			for (String columnName : csvParser.getHeaderMap().keySet()) {
				if (!columnName.equals("Date")) {
					try {
						doubleValues.put(columnName, Double.parseDouble(row.get(columnName)));
					} catch (NumberFormatException e) {
						stringValues.put(columnName, row.get(columnName));
					}
				}
			}
			try {
				businessTime = new SimpleDateFormat("yyyy-MM-dd").parse(row.get("Date"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			TimeSeriesData tsData = new TimeSeriesData(symbol, tsDef.ID, businessTime.getYear(), businessTime, date);
			tsData.ValuesDouble = doubleValues;
			tsData.ValuesText = stringValues;
			tsDataRepository.save(tsData);
		}
		try {
			csvParser.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "Error closing file";
		}
		return "OK";
	}

	@RequestMapping(value = "/init", method = RequestMethod.GET)
	@ResponseBody
	public String initTables() {
		assetRepository.initTable();
		attributeRepository.initTable();
		tsDataRepository.initTable();
		tsDefinitionRepository.initTable();
		return "OK";
	}
}
