package jp.ac.tokushima_u.is.ll.mapper;

import java.util.List;
import java.util.Map;

public interface ItemMapper {
	List<Map<String, Object>> selectItemWhereHasFile();
}
