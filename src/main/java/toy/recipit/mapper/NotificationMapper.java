package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.NotificationVo;

import java.util.List;

@Mapper
public interface NotificationMapper {
    List<NotificationVo> getNotifications(@Param("userNo") String userNo);
}
