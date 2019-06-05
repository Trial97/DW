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

import com.uvt.dw.Data.Asset;

@RestController
public class AssetController {

	@Autowired
	public AssetRepository assetRepository;

	@RequestMapping(value = "/asset", method = RequestMethod.GET)
	@ResponseBody
	public List<Asset> getAsset() {
		return assetRepository.findAll();
	}

	@RequestMapping(value = "/asset", method = RequestMethod.POST)
	@ResponseBody
	public String saveAsset(@RequestBody Asset aset) {
		aset.SetDefaults();
		assetRepository.save(aset);
		return "OK";
	}

	@GetMapping("/asset/find")
	public Optional<Asset> getAsset(@RequestParam String id) {
		MapId o = (MapId) new BasicMapId();
		o.put("ID", id);
		return assetRepository.findById(o);
	}

	@RequestMapping(value = "/asset/findall", method = RequestMethod.GET)
	@ResponseBody
	public List<Asset> getAssets(@RequestParam Integer page,
			@RequestParam(value = "size", defaultValue = "25") Integer size) {
		if (page == null) {
			return assetRepository.findAll();
		}
		List<Asset> assets = new ArrayList<>();
		int i = 0;
		Slice<Asset> casPage = assetRepository.findAll(CassandraPageRequest.of(i, size));
		if (page == 0) {
			casPage.forEach(e -> assets.add(e));
			return assets;
		}
		while (casPage.hasNext()) {
			i++;
			casPage = assetRepository.findAll(casPage.nextPageable());
			if (i == page) {
				break;
			}
		}
		if (i != page) {
			return assets;
		}
		casPage.forEach(e -> assets.add(e));
		return assets;
	}
}
