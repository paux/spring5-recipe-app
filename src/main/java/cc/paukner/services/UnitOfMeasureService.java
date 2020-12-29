package cc.paukner.services;

import cc.paukner.dtos.UnitOfMeasureDto;

import java.util.Set;

public interface UnitOfMeasureService {

    Set<UnitOfMeasureDto> listAll();
}
