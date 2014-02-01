package com.personalapp.hometeaching.repository;

import java.io.Serializable;

public interface Repository<ENTITY, ID extends Serializable> {

	ENTITY findById(ID id);

	void remove(ENTITY model);

	void save(ENTITY model);

	void save(Iterable<ENTITY> models);

	void update(ENTITY model);

}
