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

import com.uvt.dw.Data.Attribute;

@RestController
public class AttributeController {

	@Autowired
	public AttributeRepository attributeRepository;

	@RequestMapping(value = "/attribute", method = RequestMethod.GET)
	@ResponseBody
	public List<Attribute> getAttribute() {
		return attributeRepository.findAll();
	}

	@RequestMapping(value = "/attribute", method = RequestMethod.POST)
	@ResponseBody
	public String saveAttribute(@RequestBody Attribute attribute) {
		attributeRepository.save(attribute);
		return "OK";
	}
    
	@GetMapping("/attribute/find")
    public Optional<Attribute> getAttribute(@RequestParam String id) {
    	MapId o=(MapId) new BasicMapId();
    	o.put("ID", id);
        return attributeRepository.findById(o);
    }
	
	@RequestMapping(value = "/attribute/findall", method = RequestMethod.GET)
	@ResponseBody
    public List<Attribute> getAttributes(@RequestParam Integer page,@RequestParam(value = "size", defaultValue = "25")  Integer size) {
    	if (page==null) {
    		return attributeRepository.findAll();
    	}
    	List<Attribute> assets = new ArrayList<>();
    	int i=0;
    	Slice<Attribute> casPage= attributeRepository.findAll(CassandraPageRequest.of(i, size ));
    	if (page==0) {
        	casPage.forEach(e -> assets.add(e));
            return assets;	
    	}
        while (casPage.hasNext()) {
        	i++;
          casPage= attributeRepository.findAll(casPage.nextPageable());
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
