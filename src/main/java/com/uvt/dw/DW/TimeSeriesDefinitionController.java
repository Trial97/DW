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

import com.uvt.dw.Data.TimeSeriesDefinition;

@RestController
public class TimeSeriesDefinitionController {

	@Autowired
	public TimeSeriesDefinitionRepository tsDefinitionRepository;

	@RequestMapping(value = "/tsdefinition", method = RequestMethod.GET)
	@ResponseBody
	public List<TimeSeriesDefinition> getTimeSeriesDefinition() {
		return tsDefinitionRepository.findAll();
	}

	@RequestMapping(value = "/tsdefinition", method = RequestMethod.POST)
	@ResponseBody
	public String saveTimeSeriesDefinition(@RequestBody TimeSeriesDefinition tsdefinition) {
		tsDefinitionRepository.save(tsdefinition);
		return "OK";
	}

	@GetMapping("/tsdefinition/find")
    public Optional<TimeSeriesDefinition> getTimeSeriesDefinition(@RequestParam String id) {
    	MapId o=(MapId) new BasicMapId();
    	o.put("ID", id);
        return tsDefinitionRepository.findById(o);
    }
	
	@RequestMapping(value = "/tsdefinition/findall", method = RequestMethod.GET)
	@ResponseBody
    public List<TimeSeriesDefinition> getTimeSeriesDefinitions(@RequestParam Integer page,@RequestParam(value = "size", defaultValue = "25")  Integer size) {
    	if (page==null) {
    		return tsDefinitionRepository.findAll();
    	}
    	List<TimeSeriesDefinition> assets = new ArrayList<>();
    	int i=0;
    	Slice<TimeSeriesDefinition> casPage= tsDefinitionRepository.findAll(CassandraPageRequest.of(i, size ));
    	if (page==0) {
        	casPage.forEach(e -> assets.add(e));
            return assets;	
    	}
        while (casPage.hasNext()) {
        	i++;
          casPage= tsDefinitionRepository.findAll(casPage.nextPageable());
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
