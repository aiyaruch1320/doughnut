package com.odde.doughnut.services.wikidataApis;

import com.fasterxml.jackson.databind.JsonNode;
import com.odde.doughnut.entities.Coordinate;
import com.odde.doughnut.services.wikidataApis.thirdPartyEntities.WikidataEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WikidataEntityModelOfProperties {
  private final WikidataEntity entity;

  public WikidataEntityModelOfProperties(WikidataEntity entity) {
    this.entity = entity;
  }

  public Optional<Coordinate> getGeographicCoordinate() {
    return entity.getFirstClaimValue("P625").map(WikidataValue::getCoordinate);
  }

  public Optional<WikidataId> getInstanceOf() {
    return entity.getFirstClaimValue("P31").map(WikidataValue::toWikiClass);
  }

  public Optional<Coordinate> getCoordinate() {
    return entity.getFirstClaimValue("P625").map(WikidataValue::getCoordinate);
  }

  public Optional<WikidataValue> getBirthdayValue() {
    return entity.getFirstClaimValue("P569");
  }

  public Optional<WikidataId> getCountryOfOriginValue() {
    return entity.getFirstClaimValue("P27").map(WikidataValue::toWikiClass);
  }

  public List<WikidataId> getAuthors() {
    var myList = new ArrayList<WikidataId>();
    if (entity.getClaimValues("P50").isEmpty()) return myList;

    JsonNode node = entity.getClaimValues("P50").get(0).datavalue().getValue();

    for (JsonNode jsonNode : node) {
      myList.add(new WikidataId(jsonNode.get("id").textValue()));
    }
    return myList;
  }
}
