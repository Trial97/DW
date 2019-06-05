package com.uvt.dw.DW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.uvt.dw.Data.TimeSeriesData;

@RestController
public class TimeSeriesDataController {

	@Autowired
	public TimeSeriesDataRepository tsDataRepository;

	@RequestMapping(value = "/tsdata", method = RequestMethod.GET)
	@ResponseBody
	public List<TimeSeriesData> getTimeSeriesData() {
		return tsDataRepository.findAll();
	}

	@RequestMapping(value = "/tsdata", method = RequestMethod.POST)
	@ResponseBody
	public String saveTimeSeriesData(@RequestBody TimeSeriesData asset) {
		asset.SetDefaults();
		tsDataRepository.save(asset);
		return "OK";
	}
	
	@GetMapping("/tsdata/find")
    public Optional<TimeSeriesData> getAsset(@RequestParam String id) {
        return tsDataRepository.findByIdFltr(id);
    }
	
	
	@RequestMapping(value = "/tsdata/findall", method = RequestMethod.GET)
	@ResponseBody
    public List<TimeSeriesData> getTimeSeriesData(@RequestParam Integer page,@RequestParam(value = "size", defaultValue = "25")  Integer size) {
    	if (page==null) {
    		return tsDataRepository.findAll();
    	}
    	List<TimeSeriesData> assets = new ArrayList<>();
    	int i=0;
    	Slice<TimeSeriesData> casPage= tsDataRepository.findAll(CassandraPageRequest.of(i, size ));
    	if (page==0) {
        	casPage.forEach(e -> assets.add(e));
            return assets;	
    	}
        while (casPage.hasNext()) {
        	i++;
          casPage= tsDataRepository.findAll(casPage.nextPageable());
          if (i==page) {
        	  break;
          }
        }
        if (i!=page) {
        	return assets;
        }
        casPage.forEach(e -> assets.add(e));
        return assets;	
    }
}
