package com.odde.doughnut.services;

import com.odde.doughnut.models.CurrentModelVersionResponse;

public class AiModelService {
  public CurrentModelVersionResponse getCurrentModelVersions() {
    return new CurrentModelVersionResponse("gpt-4", "gpt-3.5", null);
  }
}