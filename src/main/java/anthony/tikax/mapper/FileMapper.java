package anthony.tikax.mapper;

import anthony.tikax.domain.model.UploadFileDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

    @Insert("insert into file_upload (" +
            "file_md5, file_name, total_size, file_type, extension, mime_type, status, user_id, created_at) " +
            "values (#{fileMd5}, #{fileName}, #{totalSize}, #{fileType}, #{extension}, #{mimeType}, #{status}, #{userId}, #{createAt})")
    void insert(UploadFileDO uploadFileDO);
}
