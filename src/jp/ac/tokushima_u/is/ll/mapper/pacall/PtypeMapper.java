package jp.ac.tokushima_u.is.ll.mapper.pacall;

import jp.ac.tokushima_u.is.ll.entity.pacall.Ptype;

public interface PtypeMapper {
	Ptype selectById(String id);
	Ptype selectByName(String name);
}
